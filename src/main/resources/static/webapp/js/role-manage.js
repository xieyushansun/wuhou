var table = layui.table,
    layer = layui.layer;
table.render({
    elem: '#role-table',
    id: 'role-table',
    url: '../../../role/getAllRole',
    // data: [{id: 1, roleName: 'xxx', permissions:[]}],
    toolbar: '#topToolBar',
    defaultToolbar: [],
    cols: [[
        {field: 'roleName', title: '角色名'},
        {title: '操作', minWidth: 150, toolbar: '#rowToolBar', align: 'center'}
    ]],
    width: 500,
    limits: [15, 20, 50, 100],
    limit: 15,
    page: {
        layout: ['limit', 'count', 'refresh', 'prev', 'page', 'next', 'skip'],
    },
    parseData: function(res) {
        var data = res.body? res.body: [];
        var count = data.length;
        return {
            "code": res.code,
            "msg": res.message,
            "data": data,
            "count": count
        }
    },
    done: function(res) {
        if (res.code === 12) {
            layer.msg('登录超时', {time: 0.8*1000, anim: 6});
            top.location.href = '../../login.html';
        }
    }
});

table.on('toolbar(role-table)', function(obj) {
    if (obj.event = 'add') {
        var index = layer.open({
            title: '添加角色',
            type: 2,
            shade: 0.2,
            btn: ['确定', '取消'],
            area: ['700px', '80%'],
            content: './add-role.html',
            // success: function(layero, index) {
            //     var iframeWin = window[layero.find('iframe')[0]['name']];
            //     iframeWin.renderTable([]);
            // },
            yes: function(index, layero) {
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.addRole();
            }
        });
        // $(window).on('resize', function() {
        //     layer.full(index);
            
        // });
        return false;
    }
});

// 监听表格行工具
table.on('tool(role-table)', function(obj) {
    var data = obj.data;
    var event = obj.event;
    if (event === 'edit') {
        var index = layer.open({
            title: '编辑角色',
            type: 2,
            shade: 0.2,
            btn: ['确定', '取消'],
            area: ['700px', '80%'],
            content: './mod-role.html',
            success: function(layero, index) {
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.renderTable(data.roleName, data.permissions);
            },
            yes: function(index, layero) {
                var iframeWin = window[layero.find('iframe')[0]['name']];
                iframeWin.modRole(data.id);
            }
        });
        // $(window).on('resize', function() {
        //     layer.full(index);
        // });
        return false;
    } else if (event === 'delete') {
        layer.confirm('确定删除角色：' + data.roleName + '?', {icon: 3}, function(index) {
            delRole(obj);
        });
    }
});

function delRole(obj) {
    $.ajax({
        url: '../../../role/deleteRole',
        type: 'post',
        data: {roleId: obj.data.id},
        success: function(res) {
            if (res.code === 0) {
                obj.del();
                layer.msg('删除成功', {time: 0.5 * 1000});
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
        }
    });
}