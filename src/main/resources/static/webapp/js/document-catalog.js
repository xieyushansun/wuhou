layui.use(['table', 'form', 'layer'], function () {
    var $ = layui.$;
        table = layui.table,
        form = layui.form,
        layer = layui.layer;

    table.render({
        elem: '#search-table',
        id: 'search-table',
        data: [],
        page: {
            layout: ['limit', 'count', 'refresh', 'prev', 'page', 'next', 'skip'],
        },
        toolbar: '#currentTableBar',
        defaultToolbar: [],
        cols: [[
            {field: 'documentNumber', title: '档号', width: 170, minWidth: 170},
            {field: 'recordGroupNumber', title: '全宗号', width: 75, minWidth: 75},
            {field: 'year', title: '年度', width: 65, minWidth: 65},
            {field: 'duration', title: '保管期限', width: 90, minWidth: 90},
            {field: 'security', title: '密级', width: 70, minWidth: 70},
            {field: 'documentCategory', title: '档案类别', width: 150, minWidth: 120},
            {field: 'fileCategory', title: '案卷类型', width: 150, minWidth: 120},
            {field: 'boxNumber', title: '案卷号', width: 80, minWidth: 80},
            {field: 'responsible', title: '责任者', width: 150, minWidth: 150},
            {field: 'danweiCode', title: '单位代码', width: 200, minWidth: 150},
            {field: 'danweiName', title: '单位名称', width: 150, minWidth: 150},
            {field: 'fileName', title: '案卷题名', width: 200, minWidth: 150},
            {field: 'position', title: '存放位置', width: 150, minWidth: 150},
            {field: 'recorder', title: '著录人', width: 80, minWidth: 50},
            {field: 'recordTime', title: '著录时间', width: 100, minWidth: 80},
            {title: '存放路径', width: 100, templet: function(d) {
                return d.diskPath + ':\\' + d.storePath;
            }}
        ]],
        request: {
            pageName: 'currentPage',
            limitName: 'pageSize'
        },
        parseData: function(res) {
            return {
                "code": res.code,
                "msg": res.message,
                "count": res.totalElement? res.totalElement: 0,
                "data": res.body? res.body : []
            }
        },
        limits: [10, 15, 20, 25, 50, 100],
        limit: 15,
    });

    // 监听搜索操作
    $("#search-btn").on("click", function() {
        var documentNumber = $("#document-no").val();
        if (documentNumber === "")  {
            layer.tips('请填写完整档号', '#document-no');
            return;
        }
        table.reload('search-table', {
            url: '../../../document/normalFindDocumentRecord',
            where: {
                documentNumber: documentNumber,
                blurryFind: '0'
            },
            page: {
                curr: 1
            },

            done: function(res, curr, count) {
                if (res.code === 0 && count > 0) {
                    $(".start-btn").removeClass('layui-disabled');
                    $('.down-btn').addClass('layui-hide').removeAttr('href');
                } else if (res.code === 12) {
                    top.layui.layer.msg('登录失效', {time: 1.2*1000});
                    top.location.href = '../../login.html';
                }
            }
        });
    });

    /**
     * toolbar监听事件
     */
    table.on('toolbar(search-table)', function (obj) {
        if (obj.event === 'start') {  // 监听添加操作
            var that = this;
            if ($(that).hasClass('layui-disabled')) {
                layer.tips('请先输入档号进行查询', '#search-btn');
                return;
            }
            var documentNumber = $('#document-no').val();
            $.ajax({
                url: '../../../cataloging/generateFileCatalog',
                data: {documentNumber: documentNumber},
                type: 'get',
                cache: false,
                beforeSend: function() {
                    $('#search-btn').addClass('layui-disabled'); // 查询按钮禁用
                    $('.loading').removeClass('layui-hide'); // 显示加载按钮
                },
                success: function(res) {
                    if (res.code === 0) {
                        var downUrl = '../../../cataloging/outputFileCatalog?fileName=' + res.body;
                        $('.down-btn').removeClass('layui-hide'); // 显示下载按钮
                        $('.down-btn').attr('href', downUrl);
                        $('#search-btn').removeClass('layui-disabled');
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
                    $('.loading').addClass('layui-hide'); // 不管编目成功与否，隐藏加载
                }
            });
        }
    });
});