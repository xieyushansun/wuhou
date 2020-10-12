/*
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |   |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.' >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 * 
 * 
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 *            佛祖保佑       永不宕机     永无BUG
 * 
 *        佛曰:  
 *                写字楼里写字间，写字间里程序员；  
 *                程序人员写程序，又拿程序换酒钱。  
 *                酒醒只在网上坐，酒醉还来网下眠；  
 *                酒醉酒醒日复日，网上网下年复年。  
 *                但愿老死电脑间，不愿鞠躬老板前；  
 *                奔驰宝马贵者趣，公交自行程序员。  
 *                别人笑我忒疯癫，我笑自己命太贱；  
 *                不见满街漂亮妹，哪个归得程序员？
 */

const WARNING_SPACE = 1.0;

layui.use(['layer', 'miniAdmin'], function () {
    var layer = layui.layer,
        miniAdmin = layui.miniAdmin,
        miniTongji = layui.miniTongji;
    var options = {
        iniUrl: "api/init.json",    // 初始化接口
        urlHashLocation: true,      // 是否打开hash定位
        bgColorDefault: 1,      // 主题默认配置
        multiModule: true,          // 是否开启多模块
        menuChildOpen: false,       // 是否默认展开菜单
        loadingTime: 0.1,             // 初始化加载时间
        pageAnim: true,             // iframe窗口动画
        maxTabNum: 20,              // 最大的tab打开数量
    };
    miniAdmin.render(options);
    $(document).ready(function() {
        (function() {
            var user = getUser();
            $("#nickname").html(user.nickname);
        })();

        $.ajax({
            url: '../../DiskManage/getCurrentDiskNameAndSpace',
            type: 'get',
            success: function(res) {
                if (res.code === 0) {
                    var disk = res.body;
                    var restSpace = parseFloat(disk.restSpace);
                    
                    if (restSpace < WARNING_SPACE) {
                        layer.alert('磁盘 "' + disk.diskName + '" 仅剩' + disk.restSpace + 'GB存储空间！！<br/>请联系管理员添加磁盘!<br/>防止数据丢失！', {icon: 0});
                    }
                }
            }
        })
    });

    // 百度统计代码，只统计指定域名

    $('.login-out').on("click", function () {
        $.ajax({
            url: '../../user/logout',
            type: 'post',
            success: function(res) {
                if (res.code === 0) {
                    layer.msg('退出登录成功', {time: 700}, function () {
                        window.location = 'login.html';
                    });
                } else if (res.code === 12) {
                    layer.msg('登录已失效', {time: 0.8*1000, anim: 6}, function() {
                        top.location.href = './login.html';
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

    });
    $('#update-password').on('click', function() {
        layer.open({
            id: 'pass',
            title: '修改密码',
            type: 2,
            content: './pages/user-password.html',
            btn: ['取消'],
            area: ['600px', '300px'],
            closeBtn: 0,
            anmi: 5,
            success: function(layero, index) {
                layer.iframeAuto(index);
            }
        });
    });
    // $('#user-info').on('click', function() {
    //     layer.open({
    //         id: 'pass',
    //         title: '基本资料',
    //         type: 2,
    //         content: './pages/user-info.html',
    //         btn: ['取消'],
    //         area: ['600px', '300px'],
    //         closeBtn: 0,
    //         anmi: 5,
    //         success: function(layero, index) {
    //             layer.iframeAuto(index);
    //         }
    //     });
    // });

});
