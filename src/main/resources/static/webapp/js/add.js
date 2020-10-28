layui.use(['form', 'layer', 'upload', 'util'], function () {
    var form = layui.form,
        layer = layui.layer,
        upload = layui.upload,
        util = layui.util;
    var Viewer = window.Viewer;

    var documentCategoryData, // 档案类别的数据
        documentCatAbbr; // 档案类别缩写；

    var documentCategory = parent.documentCategory,
        fileCategory = parent.fileCategory,
        fileCategoryList = [];

    var durationDict = {
        '10年': 'D',
        '30年': 'Z',
        '50年': 'C',
        '100年': 'H',
        '永久': 'Y'
    };
    
    var user = getUser();
    var viewer = new Viewer(document.getElementById('images'), {
        toolbar: true,
    });

    $(document).ready(function(){
        (function() {
        $.ajax({
            url: "../../../DocumentCategory/FindAllDocumentCategory", // 获取档案类别
            type: "get",
            cache: false,
            success: function(res) {
                if (res.code == 0) {
                    documentCategoryData = res.body;
                    documentCatAbbr = getDocCatAbbr(documentCategoryData);
                    selectInit(documentCategoryData); // 初始化渲染select
                    form.render('select');
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
        })
    })(); // 获取档案类别
        form.val("add-form", {
            //
            "documentCategory": documentCategory,
            "fileCategory": fileCategory,
            "recorder": user.username,
            "recordTime": getDate()
        });
        form.render('select');
    });

    // laydate.render({
    //     elem: "#year",
    //     type: 'year',
    //     // showBottom: false,
    //     done: function(value, date, endDate) {
    //         makeDocumentNo();
    //     }
    // });

    
    $(".docNoUsed").change(function() {
        makeDocumentNo();
    });

    // 清空添加按钮的点击
    $("#new-add").on('click', function() {
        form.val('add-form', {
            "documentNumber": "",
            "year": "",
            "duration": "",
            "security": "",
            "documentCategory": "",
            "fileCategory": "",
            "boxNumber": "",
            "responsible": "",
            "danweiCode": "",
            "danweiName": "",
            "fileName": "",
            "generateTime": "",
            "order": "",
            "pageNumber": "",
            "sex": "", 
            "position": "",
            "recorder": user.username,
            "recordTime": getDate()
        });
        $("#save").removeClass("layui-disabled").removeAttr("disabled");
        $("#mount-btn").addClass("layui-disabled");
        $("#fileList").empty();
        $("#upload-container").css("display", "none");
        $("images").empty();
        choosedFiles = [];
        uploadedFiles = [];
        imgList = [];
    });
    $("#continue-add").on('click', function() {
        var last_order = $('input[name="order"]').val();
        form.val('add-form', {
            "fileName": "",
            "order": last_order + 1,
            "pageNumber": "",
            "recorder": user.username,
            "recordTime": getDate()
        });
        $("#save").removeClass("layui-disabled").removeAttr("disabled");
        $("#mount-btn").addClass("layui-disabled");
        $("#fileList").empty();
        $("#upload-container").css("display", "none");
        $("images").empty();
        choosedFiles = [];
        uploadedFiles = [];
        imgList = [];
    });

    // 监听案卷类别下拉选择的变化
    form.on('select(documentCategory)', function(data) {
        makeDocumentNo();
        var documentCategory = data.value,
            fileCategory = '';
        fileCategoryList = getFileCategoryList(documentCategoryData, documentCategory);
        changeOption('fileCategory', makeFileOption(fileCategoryList));
        form.render('select');
    });

    // 监听案卷类别下拉选择的变化
    form.on('select(fileCategory)', function(data) {
        makeDocumentNo();
        fileCategory = data.value;
    });

    form.on('select(duration)', function(data) {
        makeDocumentNo();
    });

    var documentRecordId = 3;

    // 提交后才能挂载文件
    $('#mount-btn').on('click', function() {
        if ($(this).hasClass('layui-disabled')) {
            layer.tips('请先保存档案再挂载文档', '#mount-btn', {time: 1*1000});
            return;
        }
        // laytpl(uploadTpl.innerHTML).render(fileList, function(html) {
        //     $("#file-list").html(html);
        // });

        $("#upload-container").css("display", "block");

    });

    var choosedFiles = [];
    var uploadedFiles = [];
    var imgList = [];
    // 上传按钮和列表
    var fileListView = $('#fileList');
        uploadListIns = upload.render({
            elem: '#testList',
            url: '../../../document/adddocumentrecordfile',
            accept: 'file',
            multiple: true,
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

                    // 图片预览
                    tr.find('.view-file').on('click', function() {
                        var filename = $(this).data()['name'],
                            index = imgList.indexOf(filename);
                        viewImg(index);
                    });

                    //删除
                    tr.find('.demo-delete').on('click', function() {
                        var filename = $(this).data()['name'];
                        var uploadedIdx = -1;
                        var thatIdx = index;
                        if ((uploadedIdx = uploadedFiles.indexOf(filename)) !== -1) {
                            if (deleteFile(documentRecordId, filename)) { // 删除成功
                                uploadedFiles.splice(uploadedIdx, 1); // 如果该文件在已上传文件列表中，需要从列表中删除，并从服务器删除。
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            }
                        } else {
                            delete files[thatIdx]; // 该文件未上传，只从上传列表删除对应的文件
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
                    fileListView.append(tr); 
                });
            },
            done: function(res, index, upload) {
                if (res.code === 0) { //上传成功
                    var tr = fileListView.find('tr#upload-' + index),
                        tds = tr.children();
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    tds.eq(3).find('.demo-reload').addClass('layui-hide');
                    uploadedFiles.push(this.files[index].name); // 在已上传文件中添加该文件
                    return delete this.files[index]; //删除文件队列已经上传成功的文件
                }
                this.error(index, upload, res.message);
            },
            error: function(index, upload, msg=undefined) {
                var tr = fileListView.find('tr#upload-' + index),
                    tds = tr.children();
                if (msg !== undefined) { // 如果有错误信息则显示错误信息
                    tds.eq(2).html('<span style="color: #FF5722;">' + msg +'</span>');
                } else { // 一般是网络问题造成的错误
                    tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                    tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
                }
            }
        });

    //监听提交
    form.on('submit(saveBtn)', function (data) {
        if ($(this).hasClass('layui-disabled')) {
            return;
        }
        // $("#mount-btn").removeClass("layui-disabled");
        // $("#save").addClass("layui-disabled").attr("disabled", "true");
        $.ajax({
            type: "post",
            url: '../../../document/adddocumentrecord',
            data: data.field,
            success: function(res) {
                if (res.code == 0) {
                    layer.msg(res.message, {icon: 1});
                    documentRecordId = res.body;
                    $("#mount-btn").removeClass("layui-disabled");
                    $("#save").addClass("layui-disabled").attr("disabled", "true");
                    uploadListIns.reload({ // 重载上传实例
                        data: {documentRecordId: documentRecordId}
                    });
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
                console.log('error: add document record request error');
            }
        });
        
        return false;
    });
    
    $("#del-all").on('click', function() {
        $.ajax({
            url: "../../../document/deleteAllFile",
            type: "get",
            data: {'documentRecordId': documentRecordId},
            success: function(res) {
                if (res.code === 0) {
                    layer.msg('清空成功');
                    $("#fileList").empty();
                    $("images").empty();
                    choosedFiles = [];
                    uploadedFiles = [];
                    imgList = [];
                } else if (res.code === 12) {
                    layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                        top.location.href = '../../login.html';
                    });
                } else {
                    layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
                }
            }
        });
    });

    // 生成档号
    function makeDocumentNo() {
        var formData = form.val('add-form');
        var recordGroupNumber = formData.recordGroupNumber,
            year = formData.year,
            duration = formData.duration, 
            documentCategory = formData.documentCategory,
            fileCategory = formData.fileCategory,
            boxNumber = formData.boxNumber;

        if (recordGroupNumber !== "" && year !== "" && duration !== "" && 
            documentCategory !== "" && fileCategory !== "" && boxNumber !== "") {
            // var indexList = fileCategoryList.indexOf(fileCategory);
            var docCatAbbr = documentCatAbbr[documentCategory]? documentCatAbbr[documentCategory]: documentCategory;
            // var no;
            // if (indexList > -1) {
            //     no = indexList < 10? '0' + (indexList + 1).toString(): (indexList + 1).toString();
            // } else {
            //     no = 'idx'
            // }
            var documentNumber = recordGroupNumber + '-' + year + '-' + durationDict[duration] + '.' + 
                                 docCatAbbr + '.' + fileCategory +
                                 '-' + boxNumber;
            form.val('add-form', {
                "documentNumber": documentNumber
            });
        }
    }

    function viewImg(idx) {
        viewer['view'](idx.toString());
    }
    
    // 获取格式为yyyymmdd的日期
    function getDate() {
        return util.toDateString(new Date(), 'yyyyMMdd');
    }

    // 删除服务器上的文件
    function deleteFile(documentRecordId, filename) {
        /**
         * @description: 删除服务器上的文件
         * @param {documentRecordId: String} 档案ID 
         * @param {filename: String} 文件名
         * @return  返回成功与否{true/false: boolean}
         */
        var successful = false;
        $.ajax({
            url: '../../../deleteDocumentRecordFile', 
            type: 'post',
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
});