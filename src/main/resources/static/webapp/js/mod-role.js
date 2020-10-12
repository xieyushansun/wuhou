var $ = layui.jquery,
    form = layui.form,
    table = layui.table,
    layer = layui.layer;

function renderTable(rolename, hasPermissions) {
    $("#rolename").val(rolename);
    var allPermissions = dataTrans(PERMISSIONS, hasPermissions);
    table.render({
        elem: '#permission-table',
        id: 'permission-table',
        data: allPermissions,
        cols: [[
            {field: 'permission_name', title: '权限名称', width: 200, unresize: true, align: 'center'},
            {title:'赋予权限', width:110, templet: '#checkboxTpl', unresize: true, align: 'center'},
            {field: 'tips', title: '备注', width: 200, unresize: true, align: 'center'}
        ]],
        width: 514,
        limit: 20
    });
}

function modRole(id) {
    var roleName = $("#rolename").val();
    if (roleName === "") {
        layer.tips('请填写角色名', '#rolename');
        return;
    }
    var hasPermissions = Object.keys(form.val('table-form'));
    $.ajax({
        url: '../../../role/modifyRole',
        type: 'post',
        data: {
            roleId: id,
            roleName: roleName,
            permissionList: hasPermissions
        },
        traditional: true,
        success: function(res) {
            if (res.code === 0) {
                parent.layer.msg('修改成功', {time: 0.7 * 1000});
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
                parent.table.reload('role-table');
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

function dataTrans(allPermissions, hasPermissions) {
    layui.each(allPermissions, function(index, item) {
        if (hasPermissions.indexOf(item.permission) !== -1) {
            item['has'] = true;
        } else {
            item['has'] = false;
        }
    });
    return allPermissions;
}