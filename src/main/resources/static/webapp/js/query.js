layui.use(['element', 'form', 'laytpl'], function() {
    var element = layui.element,
        form = layui.form,
        laytpl = layui.laytpl;
    var documentCategoryData = [],
        documentCategory = '',
        fileCategory = '';
    $(document).ready(function() {
        (function() {
            $.ajax({
                url: "../../../DocumentCategory/FindAllDocumentCategory", // 获取档案类别
                type: "get",
                success: function(res) {
                    if (res.code == 0) {
                        documentCategoryData = res.body;
                        selectInit(documentCategoryData); // 初始化渲染select
                        form.render('select');
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
                    console.log('error: get documentCategory error');
                }
            });
        })(); // 获取档案类别
    });
    
    form.on('select(documentCategory)', function(data) {
        documentCategory = data.value;
        fileCategory = '';
        fileCategoryList = getFileCategoryList(documentCategoryData, documentCategory);
        changeOption('fileCategory', makeOption(fileCategoryList));
        form.render('select', 'class-query');
    });
    
    var idx = 3;
    var getTpl = $("#query-condition").html();
    $("#combined-query-tab").on("click", ".mod-btn", function() {
        var that = this,
            formDom = $(that).parents(".layui-form"),
            btn = $(that),
            btnGroup = btn.parent();
        if(btn.hasClass('add-btn')) {
            // 点击了加号
            btn.addClass('layui-hide'); // 把+隐藏
            laytpl(getTpl).render(idx, function(html) {
                formDom.after(html); // form 的后面添加新的HTML
                form.render(null, 'combined-query-' + idx); // 重新渲染新增的form
                idx++;
            });
            if (btnGroup.hasClass('second-line')) {
                // 点击的是第二行，把-显示
                btn.prev().removeClass('layui-hide');
            }
        } else {
            // 点击了减号
            if (formDom.next().length === 0) {
                // 如果是最后一个,则把上一个的+显示
                var prevBtnGroup = formDom.prev().find('.layui-btn-group');
                prevBtnGroup.find('.add-btn').removeClass('layui-hide'); // 显示+
                if (prevBtnGroup.hasClass('second-line')) {
                    // 如果上一行是第二行，删去最后一行只剩两行，把第二行的-隐藏
                    prevBtnGroup.find('.sub-btn').addClass('layui-hide'); // 隐藏-
                }
            } else if(btnGroup.hasClass('second-line')) {
                // 该行不是最后一行，且是第二行，则删除后后面一行变为第二行
                formDom.next().find('.layui-btn-group').addClass('second-line'); // 给后面的btnGroup加上第二行标识 
                if (formDom.nextAll().length === 1) {
                    // 后面只剩一行，即删除后只剩两行，则把新的第二行的-隐藏
                    formDom.next().find('.sub-btn').addClass('layui-hide');
                }
            }
            formDom.remove(); // 删除该行
        }
    });

    // 监听select的选择
    form.on('select', function(data) {
        if (!$(data.elem).hasClass('field')) {
            return;
        }
        var dom = $(data.elem), // 字段select
            fieldName = data.value, // 选中的值
            idx = dom.data()['idx'], // idx
            filter = "oper-" + idx, // operator的filter
            formDom = dom.parents('.layui-form'), // 该行所在form
            formFilter = formDom.attr('lay-filter'), // form 的filter
            operDom = formDom.find('.operator');

        if (fieldName === 'year' || fieldName === 'recordTime') {
            // 字段是年或著录时间，可以修改operator进行大小比较
            operDom.removeAttr('disabled'); // 取消下拉禁用
            form.render('select', formFilter);
        } else {
            if (operDom.attr('disabled')) {
                // 已禁用，不用再操作，节省渲染时间
                return;
            }
            operDom.attr('disabled', 'disabled'); // 禁用下拉
            operDom.val('is');
            form.render('select', formFilter);
        }
    });
});