/*
 * @Author: liyan
 * @Date: 2020-10-08 10:13:37
 * @LastEditTime: 2020-10-11 18:11:10
 * @LastEditors: liyan
 * @Description: 
 * @FilePath: \wuhou\src\main\resources\static\webapp\js\get-user.js
 * @liyan@cilab@uestc
 */
function getCurrUser() {
    var url1 = '../user/getCurrentUser',
        url2 = '../../user/getCurrentUser',
        url3 = '../../../user/getCurrentUser',
        logUrl1 = './login.html',
        logUrl2 = '../login.html',
        logUrl3 = '../../login.html',
        url = '',
        logUrl = '',
        location = window.location,
        path = location.pathname.split('/').reverse()[1];
    if (path === 'webapps') {
        url = url1;
        logUrl = logUrl1;
    } else if (path === 'pages') {
        url = url2;
        logUrl = logUrl2;
    } else {
        url = url3;
        logUrl = logUrl3;
    }
    
    var user = {};
    $.ajax({
        url: url,
        type: 'get',
        async: false,
        success: function(res) {
            if (res.code === 0) {
                var body = res.body;
                user['username'] = body.userId;
                user['nickname'] = body.nickname;
                user['rolename'] = body.rolename;
                user['permissions'] = body.permissions;
                localStorage.setItem('user', JSON.stringify(user));
            } else if (res.code === 12) {
                layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                    top.location.href = logUrl;
                });
            }
        },
        error: function(jqxhr, textStatus, errorThrown) {
            layer.confirm([textStatus, errorThrown].join(':'), {icon: 2});
            console.log('error: get current user request error');
        }
    });

    return user;
}

function getUser() {
    var user = JSON.parse(localStorage.getItem('user'));
    if (user) {
        return user;
    } else {
        return getCurrUser();
    }
}