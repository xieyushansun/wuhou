layui.use(['form', 'table'], function() {
    var user = getUser();
    var form = layui.form,
        table = layui.table;
    
    form.val('user-info', {
        username: user.username,
        nickname: user.nickname,
        rolename: user.rolename
    });

    var permissionNames = (function() {
        var arr = [];
        layui.each(user.permissions, function(index1, item1) {
            layui.each(PERMISSIONS, function(index2, item2) {
                if (item1 === item2.permission) {
                    arr.push({permission_name: item2.permission_name});
                    return false;
                }
            });
        });
        return arr;
    })();

    // permissionNames = [{permission_name: 1},{permission_name: 1},{permission_name: 1},{permission_name: 1}]

    table.render({
        elem: '#permission-table',
        id: 'permission-table',
        data: permissionNames,
        defaultToolbar: [],
        cols: [[
            {field: 'permission_name', title: '权限', align: 'center'}
        ]],
        width: 300,
        limit: 50
    });
});