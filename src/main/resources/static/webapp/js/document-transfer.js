layui.use(['form', 'table', 'laydate'], function () {
    var form = layui.form,
        table = layui.table,
        laydate = layui.laydate;

    laydate.render({
        elem: '#dateRange',
        range: '-',
        format: 'yyyy/MM/dd'
    });

    table.render({
        elem: '#transfor-table',
        id: 'transfor-table',
        url: '../../../DocumentTransfer/normalFindDocumentTransfer',
        toolbar: '#toolbarDemo',
        defaultToolbar: [],
        cols: [[
            {field: 'boxNumber', minWidth: 120, title: '盒号'},
            {field: 'borrower', minWidth: 100, title: '调卷人'},
            {field: 'borrowDate',  title: '调出时间', minWidth:150},
            {field: 'borrowRecorder', title: '调卷登记人', minWidth: 100},
            {field: 'returnDate', title: '归还时间', minWidth: 150},
            {field: 'returnRecorder', title: '归还登记人'},
            {title: '操作', minWidth: 150, width: 150, templet: '#currentTableBar', align: "center", unresize: true}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
        page: {
            layout: ['limit', 'count', 'refresh', 'prev', 'page', 'next', 'skip'],
        },
        parseData: function(res) {
            return {
                "code": res.code,
                "msg": res.message,
                "data": res.body,
                "count": res.totalElement
            }
        },
        request: {
            pageName: 'currentPage',
            limitName: 'pageSize'
        },
        done: function(res) {
            if (res.code === 12) {
                layer.msg('登录超时', {time: 0.8*1000, anim: 6});
                top.location.href = '../../login.html';
            }
        }
    });

    // 监听搜索操作
    form.on('submit(data-search-btn)', function (data) {
        //执行搜索重载
        table.reload('transfor-table', {
            url: '../../../DocumentTransfer/normalFindDocumentTransfer',
            page: {curr: 1},
            where: data.field
        });

        return false;
    });

    /**
     * toolbar监听事件
     */
    table.on('toolbar(transfor-table)', function (obj) {
        if (obj.event === 'add') {  // 监听添加操作
            var index = layer.open({
                title: '登记',
                type: 1,
                shade: 0.2,
                id: 'add-new',
                shadeClose: false,
                area: ['700px', '240px'],
                btn: ['确定', '取消'],
                content: [
                    '<div class="layui-form layui-form-pane" lay-filter="add-form" style="margin-left:40px;margin-top:55px">',
                        '<div class="layui-form-item">',
                            '<div class="layui-inline">',
                                '<label class="layui-form-label">盒号</label>',
                                '<div class="layui-input-inline">',
                                    '<input type="text" name="boxNumber" lay-verify="required" autocomplete="off" class="layui-input">',
                                '</div>',
                            '</div>',
                            '<div class="layui-inline">',
                                '<label class="layui-form-label">调卷人</label>',
                                '<div class="layui-input-inline">',
                                    '<input type="text" name="borrower" lay-verify="required" autocomplete="off" class="layui-input">',
                                '</div>',
                            '</div>',
                        '</div>',
                    '</div>'].join(''),
                success: function(layero, index) {
                    form.render(null, 'add-form');
                },
                yes: function(index, layero) {
                    var data = form.val('add-form');
                    $.ajax({
                        url: '../../../DocumentTransfer/addDocumentTransfer',
                        type: 'post',
                        data: data,
                        success: function(res) {
                            if (res.code === 0) {
                                layer.msg('登记成功', {time: 0.6*1000, icon: 1});
                                layer.close(index); // 添加成后关闭
                                table.reload('transfor-table', {
                                    url: '../../../DocumentTransfer/findAllDocumentTransfer',
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
                            console.log('error: add document transfer request error');
                        }
                    });
                }
            });
        } else if (obj.event === 'unreturn') {  // 监听删除操作
            table.reload('transfor-table', {
                url: '../../../DocumentTransfer/findDocumentTransferByNotReturned',
                page: {curr: 1}
            });
        }
    });

    table.on('tool(transfor-table)', function (obj) {
        var data = obj.data;
        if (obj.event === 'return') {
            layer.confirm('确定归还？', {icon: 3}, function(index) {
                $.ajax({
                    url: '../../../DocumentTransfer/returnDocumentTransfer',
                    type: 'post',
                    data: {id: data._id},
                    success: function(res) {
                        if (res.code === 0) {
                            layer.msg('归还成功', {time: 0.6*1000, icon: 1});
                            table.reload('transfor-table', {
                                url: '../../../DocumentTransfer/findAllDocumentTransfer',
                                page: {
                                    curr: 1
                                }
                            });
                        } else if (res.code === 12) {
                            layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                                top.location.href = '../../login.html';
                            });
                        } else {
                            layer.msg(res.message, {time: 0.8*1000, icon: 2});
                        }
                    },
                    error: function(jqxhr, textStatus, errorThrown) {
                        layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
                        console.log('error: return document transfer request error');
                    }
                });
            });
        } else if (obj.event === 'delete') {
            layer.confirm('真的删除行么', function (index) {
                $.ajax({
                    url: '../../../DocumentTransfer/delteDocumentTransfer',
                    type: 'post',
                    data: {id: data._id},
                    success: function(res) {
                        if (res.code === 0) {
                            layer.msg('删除成功！', {time: 0.6*1000, icon: 1});
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
                        console.log('error: add document transfer request error');
                    },
                    complete: function() {
                        layer.close(index);
                    }
                });
            });
        }
    });
});