layui.use(['layer', 'table', 'form', 'soulTable'], function() {
    var layer = layui.layer,
        table = layui.table,
        form = layui.form,
        soulTable = layui.soulTable;

    var device = layui.device(),
        callFn = Function();
    
    callFn = function() {
        var errs = [];
        for (var i = 0, l = arguments.length; i < l; i++) {
            var arg = arguments[i];
            if (arg.code !== 0) {
                errs.push(arg.message);
            }
        }
        layer.alert(errs.join(), {title: '错误', icon:2});
    };

    
    var modified = new Set(),
        randCode = 0,
        tableData = [];

    var currDocId = '';
    
    soulTable.config({
        drag: false,  // 关闭列拖动
        rowDrag: true  // 开启行拖动
    });
    
    table.render({
        elem: "#left-table",
        id: "left-table",
        data: tableData,
        cols: [[
            {field: 'documentCategory', title: '档案类别', edit: 'text'},
            {field: 'documentCategoryShortName', title: '缩写', edit: 'text'},
            {title: '操作', toolbar: '#toolBar', minWidth: 160}
        ]],
        limit: 20,
        page: true
    });

    table.render({
        elem: "#right-table",
        id: "right-table",
        data: [],
        cols: [[
            {type: 'numbers', title: '编号', fixed: 'left'},
            {field: 'fileCategory', title: "案卷类别", edit: 'text'},
            {title: '操作', toolbar: '#toolBar2'}
        ]],
        done: function() {
            soulTable.render(this);
        },
        limit: 100,
        page: true
    });

    function getData() {
        $.ajax({
            url: "../../../DocumentCategory/FindAllDocumentCategory",
            type: "get",
            success: function(res) {
                if (res.code === 0) {
                    tableData = res.body;
                    table.reload('left-table', {
                        data: tableData
                    }); // 重新渲染左边的档案类别表
                    table.reload('right-table', {
                        data: []
                    }); // 情况右边的案卷类别表
                    currDocId = "";
                    $("#docCat").text("");
                }  else if (res.code === 12) {
                    layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                        top.location.href = '../../login.html';
                    });
                } else {
                    layer.msg(res.message, {time: top.ERROR_TIME, icon: 2});
                }
            },
            error: function(jqxhr, textStatus, errorThrown) {
                layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
                console.log('error: find all documentCategory request error');
            }
        });
    }

    getData();

    table.on('edit(left-table)', function(obj) {
        var data = obj.data;
        console.log(data.id);
        modified.add(data.id);
    });

    // 左边档案类别的添加按钮点击事件
    $('#add-docCat').on('click', function() {
        tableData = table.cache['left-table'];
        randCode++;
        var newID = 'new-add-' + randCode;
        var newData = {'id': newID, 'documentCategory': '', 'documentCategoryShortName': '', 'fileCategory': []};
        tableData.push(newData);
        table.reload('left-table', {
            data: tableData,
            page: {
                curr: 1
            }
        });
    });
    
    // 左边档案类别表的操作
    table.on('tool(left-table)', function(obj) {
        if (obj.event === 'edit') {
            var rightPart = $('#right');
            if (rightPart.hasClass("layui-hide")) {
                rightPart.removeClass("layui-hide");
            } else {
                // 不是第一次编辑案卷类别，记录上次的案卷类别修改
                var oldFileCatData = fileCategoryDataReTran(table.cache['right-table']);
                var currTableData = table.cache['left-table'];
                fileModify(currTableData, oldFileCatData);
                table.reload('left-table', {
                    data: currTableData
                });
            }
            currDocId = obj.data.id;
            table.reload('right-table', {
                data: fileCategoryDataTran(obj.data.fileCategory)
            });
            $("#docCat").text(obj.data.documentCategory);
            // obj.tr.css('background', '#79a29e');
        } else if (obj.event === 'del') {
            delDocCat(obj.data.id, obj);
        }
    });

    // 右边案卷类别添加按钮的点击事件
    $("#add-fileCat").on('click', function() {
        if (currDocId === '') {
            layer.msg('请先选择档案类别', {time: 0.4 * 1000});
            return;
        }
        var fileData = table.cache['right-table'];
        var newData = {fileCategory: '未命名档案类别'};
        fileData.push(newData);
        table.reload('right-table', {
            data: fileData
        });
    });

    // 右边案卷类别表的操作
    table.on('tool(right-table)', function(obj) {
        if (obj.event === 'del') {
            obj.del();
        }
    });

    // 保存按钮的点击
    $("#save-btn").on("click", function() {
        var oldFileCatData = fileCategoryDataReTran(table.cache['right-table']);
        var currTableData = table.cache['left-table'];
        fileModify(currTableData, oldFileCatData);
        if (modified.size === 0) {
            return;
        }
        var dfds = [];
        var layIndex = layer.load();
        modified.forEach(function(value1, value2) {
            layui.each(currTableData, function(index, item) {
                if (item.id === value1) {
                    if (item.id.indexOf('new-add') !== -1) {
                        var addDfd = addDocCat(item);
                        dfds.push(addDfd);
                    } else {
                        var modDfd = modifyDocCat(item);
                        dfds.push(modDfd);
                    }
                    return false; // 相当于实现layui.each的break
                }
            });
        });
        
        // success后的回调
        $.when.apply(this, dfds).done(function() {
            var errs = [];
            for (var i = 0, l = arguments.length; i < l; i++) {
                var arg = arguments[i];
                if (arg.code !== 0) {
                    if (args.code === 12) {
                        top.location.href = '../../login.html';
                    }
                    errs.push(arg.message);
                }
            }
            if(errs.length === 0) {
                layer.msg("保存成功", {time: 0.7 * 1000});
                getData();
            } else {
                layer.alert(errs.join(' '), {title: '错误', icon: 2});
            }
        }).fail(function() {
            var errs = [];
            for (var i = 0, l = arguments.length; i < l; i++) {
                var arg = arguments[i];
                if (arg.code !== 0) {
                    errs.push(arg.message);
                }
            }
            layer.alert(errs.join(';'), {title: '错误', icon: 2});
        }).always(function() {
            layer.close(layIndex);
        });
    });

    // 将案卷类别转化为表格数据
    function fileCategoryDataTran(data) {
        if (!data instanceof Array) {
            return [];
        }
        var newData = [];
        layui.each(data, function(index, item) {
            newData.push({fileCategory: item});
        });
        return newData;
    }

    // 将表格中的案卷类别转化为Array
    function fileCategoryDataReTran(data) {
        var newData = [];
        layui.each(data, function(index, item) {
            newData.push(item.fileCategory);
        });
        return newData;
    }

    // 找到当前修改的档案类别判断是否有修改，有的话就修改当前的数据
    function fileModify(currTableData, newData) {
        newData = newData.filter(Boolean);
        layui.each(currTableData, function(index, item) {
            if (item.id === currDocId) {
                if (!arrayEquals(item.fileCategory, newData)) {
                    item.fileCategory = newData;
                    modified.add(currDocId);
                }
            }
        });
    }

    // 创建新的档案类别,返回一个jq延迟对象
    function addDocCat(data) {
        var addDfd = new $.Deferred()
        $.ajax({
            url: '../../../DocumentCategory/addDocumentCategory',
            type: 'post',
            data: {
                documentCategory: data.documentCategory,
                documentCategoryShortName: data.documentCategoryShortName,
                fileCategory: data.fileCategory
            },
            traditional: true,
            cache: false,
            success: function(res) {
                addDfd.resolve(res);
            },
            error: function(xhr, textStatus, errorThrown) {
                addDfd.reject([textStatus, xhr.statusText].join(':'))
                // layer.alert([textStatus, xhr.statusText].join(':'), {icon: 2});
            }
        });
        return addDfd.promise();
    }

    // 修改档案类别, 返回一个jq延迟对象
    function modifyDocCat(data) {
        var modDfd = new $.Deferred(); 
        $.ajax({
            url: '../../../DocumentCategory/modifyDocumentCategory',
            type: 'post',
            data: data,
            traditional: true,
            cache: false,
            success: function(res) {
                modDfd.resolve(res);
            },
            error: function(xhr, textStatus, errorThrown) {
                modDfd.reject([textStatus, xhr.statusText].join(':'))
                // layer.alert([textStatus, xhr.statusText].join(':'), {icon: 2});
            }
        });
        return modDfd.promise();
    }

    // 删除档案
    function delDocCat(id, obj) {
        $.ajax({
            url: '../../../DocumentCategory/deleteDocumentCategory',
            type: 'post',
            data: {id: id},
            success: function(res) {
                if (res.code === 0) {
                    layer.msg('删除成功', {time: 0.8*1000});
                    obj.del();
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
                console.log('error: delete documentCategory request error');
            }
        });
    }

    // 判断两个数组是否相等
    function arrayEquals(array1, array2) {
        if(!(array1 || array1)) {
        return false;
        }
        // 先比较长度 
        if (array1.length !== array2.length)
            return false;

        for (var i = 0, l=array1.length; i < l; i++) {
            // 检查是否为内嵌数组
            if (array1[i] instanceof Array && array2[i] instanceof Array) {
                // 递归比较数组
                if (!arrayEquals(array1[i],array2[i]))
                    return false;       
            } else if (array1[i] !== array2[i]) { //标量比较
                return false;   
            }           
        }       
        return true;
    }

});