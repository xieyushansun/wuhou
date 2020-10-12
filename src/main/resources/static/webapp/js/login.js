var form = layui.form,
    layer = layui.layer;

$(document).ready(function() {
    var remember = localStorage.getItem('remember');
    var username = localStorage.getItem('username');
    var pwd = localStorage.getItem('pwd');
    if (remember === 'true') {
        $("input[name='username']").val(username);
        $("input[name='password']").val(pwd);
        $("input[name='remember']").prop('checked', true);
        form.render('checkbox');
    }
    localStorage.removeItem('user');
});


// 显示/隐藏密码
$('.bind-password').on('click', function () {
    if ($(this).hasClass('icon-5')) {
        $(this).removeClass('icon-5');
        $("input[name='password']").attr('type', 'password');
    } else {
        $(this).addClass('icon-5');
        $("input[name='password']").attr('type', 'text');
    }
});

// 输入内容时检查是否勾选记住密码
$("input[name=username],input[name=password]").bind('input propertychange', function() {
    if ($("input[name='remember']:checked").val() !== undefined) {
        rememberPassword();
    }
});

form.on('checkbox(remember)', function(data) {
    if (data.elem.checked) {
        rememberPassword();
    } else {
        forgetPassword();
    }
});

// 记住密码
function rememberPassword() {
    var user = $("input[name='username']").val();
    var pwd = $("input[name='password']").val();
    localStorage.setItem('username', user);
    localStorage.setItem('pwd', pwd);
    localStorage.setItem('remember', 'true');
}

function forgetPassword() {
    localStorage.clear();
}

// 进行登录操作
form.on('submit(login)', function (data) {
    data = data.field;
    if (data.username == '') {
        layer.msg('用户名不能为空');
        return false;
    }
    if (data.password == '') {
        layer.msg('密码不能为空');
        return false;
    }
    $.ajax({
        url: '../user/login',
        method: 'post',
        data: {userId: data.username, password: data.password},
        success: function(res) {
            if (res.code == 0) {
                layer.msg('登录成功',  {time: 700},function() {
                    window.location = './index.html';
                });
            } else {
                layer.msg(res.message);
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.alert([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: add document transfer request error');
        }
    });
    return false;
});

$('.forget-password').on('click', function(){
    layer.alert('请联系管理员重置密码', {icon: 0});
});
