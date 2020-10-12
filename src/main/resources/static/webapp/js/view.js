var form = layui.form,
    layer = layui.layer,
    upload = layui.upload;
var Viewer = window.Viewer;

var documentCategoryData, // 档案类别的数据
    documentCatAbbr, // 档案类别缩写；
    fileCategoryList = [];

var fileListView = $('#fileList'), // 文件列表的展示元素
    uploadListIns; // 文件上传的实例，后期赋值，重载
var uploadedFiles = [],
    choosedFiles = [],
    imgList = [];

$(document).ready(function(){
    // selectInit(documentCategoryData, add=true);
    $('.doc-form').attr('readonly', true).attr('disabled', true).addClass('layui-disabled');
    form.render();
});

var durationDict = {
        '10年': 'D',
        '30年': 'Z',
        '50年': 'C',
        '100年': 'H',
        '永久': 'Y'
    };

var documentRecordId = 3;

// 创建图片预览实例
var viewer = new Viewer(document.getElementById('images'), {
        toolbar: true,
    });

// 获取档案类别，
function getCat() {
    $.ajax({
        url: "../../../DocumentCategory/FindAllDocumentCategory", // 获取档案类别
        type: "get",
        cache: false,
        async: false,
        success: function(res) {
            if (res.code === 0) {
                documentCategoryData = res.body;
                selectInit(documentCategoryData); // 初始化渲染select
                form.render('select');
                documentCatAbbr = getDocCatAbbr(documentCategoryData);
            } else if (res.code === 12) {
                layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                    top.location.href = '../login.html';
                });
            } else {
                layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.confirm([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: find all document category request error');
        }
    });
}

// 填充数据 由父页面调用；
function fillData(data) {
    getCat(); // 先获取档案类别 再填充数据
    documentRecordId = data.id;
    form.val('view-form', {
        "documentRecordId": data.id,
        "documentNumber": data.documentNumber,
        "year": data.year,
        "duration": data.duration,
        "security": data.security,
        "documentCategory": data.documentCategory,
        "fileCategory": data.fileCategory,
        "boxNumber": data.boxNumber,
        "responsible": data.responsible,
        "danweiCode": data.danweiCode,
        "danweiName": data.danweiName,
        "fileName": data.fileName,
        "position": data.position,
        "recorder": data.recorder,
        "recordTime": data.recordTime,
        "diskPath": data.diskPath,
        "storePath": data.storePath
    });
    form.render();
    // 获取文件列表
    getFileList(data.id);
    rendUpload();
}


function fileTable(files) {
    if (files instanceof Array)
        files = files.sort();
    else 
        files = [];
    $("#images").empty();
    imgList = [];
    fileListView.empty();
    layui.each(files, function(index, file) {
        if (file === undefined || file === null) {
            return;
        }
        var downloadUrl = '../../../document/downLoadDocumentRecordFile?documentRecordId=' + documentRecordId + '&fileName=' + file;
        var previewUrl = '../../../document/previewDocumentRecordFile?documentRecordId=' + documentRecordId + '&fileName=' + file;
        var tr = "";
        if (!(file.endsWith('jpg') || file.endsWith('.png') || file.endsWith('.jpeg') || file.endsWith('.bmp') || file.endsWith('.gif'))) {
            tr = $([
                '<tr id="upload-' + index + '">', 
                    '<td>' + file + '</td>', 
                    '<td></td>',
                    '<td>已上传</td>', 
                    '<td>', 
                        '<a class="layui-btn layui-btn-xs download-file file-btn" href=\"'+ downloadUrl + '\">下载</a>', 
                        '<button class="layui-btn layui-btn-xs delete-file file-btn">删除</button>', 
                    '</td>', 
                '</tr>'].join(''));
        } else {
            // 该文件是图片，则需要预览图，查看按钮，添加img 的li
            tr = $([
                '<tr id="upload-' + index + '">', 
                    '<td>' + file + '</td>', 
                    '<td class="view-td"><div><img src="' + previewUrl + '" style="width: 30px; height:30px;"></div></td>',
                    '<td>已上传</td>', 
                    '<td>', 
                        '<button class="layui-btn layui-btn-primary layui-btn-xs view-file file-btn" data-name="' + file + '">查看</button>', 
                        '<a class="layui-btn layui-btn-xs download-file file-btn" href=\"'+ downloadUrl + '\">下载</a>', 
                        '<button class="layui-btn layui-btn-xs delete-file file-btn">删除</button>', 
                    '</td>', 
                '</tr>'].join(''));
            $("#images").append("<li><img src='" + previewUrl + "' alt='" + file + "'></li>");
            imgList.push(file);
            // viewer.update();
        }

        // 预览图片
        tr.find('.view-file').on('click', function() {
            var filename = $(this).data()['name'],
                index = imgList.indexOf(filename);
            viewImg(index);
        });
        
        // 删除文件
        tr.find('.delete-file').on('click', function() {
            var thatIdx = index;
            var filename = $(this).data()['name'];
            if(deleteFile(file)) {
                // 文件删除成功: 移除表格行、从uploade删除、从img ul删除、从imgList删除
                tr.remove();
                delete uploadedFiles[thatIdx];
                $('#images').find('li img[alt="' + filename+ '"]').remove(); // 删除ul中的li
                viewer.update();
                var imgIdxx = imgList.indexOf(filename);
                if (imgIdxx > -1) {
                    imgList.splice(imgIdxx, 1); // 从imgList中删除该文件
                }
            }
        });
        fileListView.append(tr);
    });
    viewer.update();
}

// 编辑按钮的点击
$("#edit-btn").on('click', function() {
    $(this).addClass('layui-hide'); // 隐藏编辑按钮
    $(".doc-form").removeClass('layui-disabled').removeAttr('readonly').removeAttr('disabled'); // 表单解除禁用
    $("#save-btn").removeClass('layui-hide').removeClass('layui-disabled'); // 保存按钮显示
    $("#testList").addClass('layui-disabled'); // 禁用文件选择按钮
    $("#testListAction").addClass('layui-disabled'); // 禁用文件上传按钮
    $(".file-btn").addClass("layui-disabled").attr("disabled", "true");
    fileListView.empty();
    $("#upload-container").addClass('layui-hide');
    form.render('select');
});

// 监听案卷类别下拉选择的变化
form.on('select(documentCategory)', function(data) {
    makeDocumentNo();
    var documentCategory = data.value,
        fileCategory = '';
    fileCategoryList = getFileCategoryList(documentCategoryData, documentCategory);
    changeOption('fileCategory', makeFileOption(fileCategoryList));
    form.render('select');
    console.log(documentCategory);
});

// 监听案卷类别下拉选择的变化
form.on('select(fileCategory)', function(data) {
    makeDocumentNo();
});

form.on('select(duration)', function(data) {
    makeDocumentNo();
});
    
$(".docNoUsed").change(function() {
    makeDocumentNo();
});

// 上传按钮和列表
function rendUpload() {
    uploadListIns = upload.render({
        elem: '#testList',
        url: '../../../document/adddocumentrecordfile',
        accept: 'file',
        multiple: true,
        data: {documentRecordId: documentRecordId},
        auto: false,
        bindAction: '#testListAction',
        choose: function(obj) {
            var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result) {
                if (uploadedFiles.indexOf(file.name) !== -1) {
                    layer.msg('已上传过名为\"' + file.name + '\"的文件', {icon: 2, anim: 6}); // 已上传重名文件
                    delete files[index];
                    return;
                } else if (choosedFiles.indexOf(file.name) !== -1) {
                    layer.msg('已选择过名为\"' + file.name + '\"的文件', {icon: 2, anim: 6}); // 已选择重名文件
                    delete files[index];
                    return;
                }

                choosedFiles.push(file.name);
                var tr = '';
                if (file.type.split('/')[0] === 'image') {
                        tr = $([
                            '<tr id="upload-' + index + '">', 
                                '<td>' + file.name + '</td>',
                                '<td><div><img src="' + result + '" style="width: 30px; height:30px;"></div></td>', 
                                '<td>等待上传</td>', 
                                '<td>', 
                                    '<button class="layui-btn layui-btn-primary layui-btn-xs view-file file-btn" data-name="' + file.name + '">查看</button>',
                                    '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>', 
                                    '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete" data-name="' + file.name + '">删除</button>', 
                                '</td>', 
                            '</tr>'].join(''));
                        $("#images").append("<li><img src='" + result + "' alt='" + file.name + "'></li>");
                        imgList.push(file.name);
                        viewer.update();
                    } else {
                        tr = $(['<tr id="upload-' + index + '">', '<td>' + file.name + '</td>','<td>' + file.type + '</td>' , '<td>等待上传</td>', '<td>','', '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>', '<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>', '</td>', '</tr>'].join(''));
                    }

                //单个重传
                tr.find('.demo-reload').on('click', function() {
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function() {
                    var filename = $(this).data()['name'];
                    var uploadedIdx = -1;
                    var thatIdx = index;
                    if ((uploadedIdx = uploadedFiles.indexOf(filename)) !== -1) {
                        if (deleteFile(filename)) { // 删除成功
                            uploadedFiles.splice(uploadedIdx, 1); // 如果该文件在已上传文件列表中，需要从列表中删除，并从服务器删除。
                            tr.remove();
                            uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                        }
                    } else {
                        delete files[index]; // 该文件未上传，只从上传列表删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    }
                    $('#images').find('li img[alt="' + filename+ '"]').remove(); // 删除ul中的li
                    viewer.update(); // 因为从ul中删除，更新viewer；
                    var imgIdxx = imgList.indexOf(filename);
                    if (imgIdxx > -1) {
                        imgList.splice(imgIdxx, 1); // 从imgList中删除该文件
                    }
                    
                    imgidxx = choosedFiles.indexOf(filename);
                    if (imgIdxx > -1) {
                        choosedFiles.splice(imgIdxx, 1); // 从choosedFiles中删除
                    } 
                });

                tr.find('.view-file').on('click', function() {
                    var filename = $(this).data()['name'],
                        index = imgList.indexOf(filename);
                    viewImg(index);
                });

                fileListView.append(tr);
            });
        },
        done: function(res, index, upload) {
            if (res.code === 0) { //上传成功
                var tr = fileListView.find('tr#upload-' + index),
                    tds = tr.children();
                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(3).find('.demo-reload').addClass('layui-hide');
                uploadedFiles.push(this.files[index].filename); // 在已上传文件中添加该文件
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload, res.message);
        },
        error: function(index, upload, msg=undefined) {
            var tr = fileListView.find('tr#upload-' + index),
                tds = tr.children();
            if (msg !== undefined) { // 如果有错误信息则显示错误信息
                tds.eq(2).html('<span style="color: #FF5722;">' + msg +'</span>');
                tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
            } else { // 一般是网络问题造成的错误
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
            }

        }
    });
}

//监听提交
form.on('submit(saveBtn)', function (data) {
    if ($(this).hasClass('layui-disabled')) {
        return;
    }

    uploadedFiles = []; 
    uploadListIns.reload({ // 重载上传实例
        data: {documentRecordId: data.field.documentRecordId}
    });
    // $("#save").addClass("layui-disabled").attr("disabled", "true");
    $.ajax({
        type: "post",
        url: '../../../document/modifyDocumentRecord',
        data: data.field,
        success: function(res) {
            if (res.code === 0) {
                var storePath = res.body;
                $("input[name=sotrePath]").val(storePath);
                layer.msg(res.message, {icon: 1}); // 保存成功，隐藏保存按钮，显示编辑按钮，重新渲染表格。
                $("#save-btn").addClass("layui-hide").addClass("layui-disabled");
                $("#edit-btn").removeClass("layui-hide");
                $(".doc-form").addClass('layui-disabled').attr({'readonly': 'true', 'disabled': 'true'});
                $("#testList").removeClass('layui-disabled');
                $("#testListAction").removeClass("layui-disabled");
                $("#upload-container").removeClass('layui-hide');
                form.render('select');
                getFileList();
            } else if (res.code === 12) {
                layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                    top.location.href = '../../login.html';
                });
            } else {
                layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: modify document record request error');
        }
    });
    
    return false;
});

// 生成档号
function makeDocumentNo() {
    var formData = form.val('view-form');
    var recordGroupNumber = formData.recordGroupNumber,
        year = formData.year,
        duration = formData.duration, 
        documentCategory = formData.documentCategory,
        fileCategory = formData.fileCategory,
        boxNumber = formData.boxNumber;

    if (recordGroupNumber !== "" && year !== "" && duration !== "" && 
        documentCategory !== "" && fileCategory !== "" && boxNumber !== "") {
        var indexList = fileCategoryList.indexOf(fileCategory),
            docCatAbbr = documentCatAbbr[documentCategory]? documentCatAbbr[documentCategory]: documentCategory;
        var no;
        if (indexList > -1) {
            no = indexList < 10? '0' + (indexList + 1).toString(): (indexList + 1).toString();
        } else {
            no = 'idx'
        }
        var documentNumber = recordGroupNumber + '-' + year + '-' + durationDict[duration] + '.' + 
                                docCatAbbr + '.' + no +
                                '-' + boxNumber;
        form.val('view-form', {
            "documentNumber": documentNumber
        });
    }
}

function viewImg(idx) {
    viewer['view'](idx.toString());
}

// 获取格式为yyyymmdd的日期
function getDate() {
    var date = new Date();
    var year = date.getFullYear();
    var month =  date.getMonth();
    var day = date.getDay();
    return year.toString() + (month < 10? '0' + month.toString(): month.toString()) + (day < 10? '0' + day.toString(): day.toString());
}

// 删除服务器上的文件
function deleteFile(filename) {
    /**
     * @description: 删除服务器上的文件
     * @param {documentRecordId: String} 档案ID 
     * @param {filename: String} 文件名
     * @return  返回成功与否{true/false: boolean}
     */
    var successful = false;
    $.ajax({
        url: '../../../document/deleteDocumentRecordFile', 
        type: 'post',
        async: false,
        data: {documentRecordId: documentRecordId, fileName: filename},
        success: function(res) {
            if (res.code === 0) {
                layer.msg('删除成功');
                successful = true
            } else if (res.code === 12) {
                layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                    top.location.href = '../../login.html';
                });
            } else {
                layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
                successful = false;
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: delete document record file request error');
        }
    });
    return successful;
}

function getFileList() {
    $.ajax({
        url: '../../../document/findFileListByDocumentRecordId',
        type: 'get',
        cache: false,
        data: {documentRecordId: documentRecordId},
        success: function(res) {
            if (res.code === 0) {
                uploadedFiles = res.body? res.body: [];
                fileTable(uploadedFiles);
            } else if (res.code === 12) {
                layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                    top.location.href = '../../login.html';
                });
            } else {
                layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: find file list by document recordId request error');
        }
    });
}
