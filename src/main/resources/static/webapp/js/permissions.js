/*
 * @Author: liyan
 * @Date: 2020-10-05 10:01:06
 * @LastEditTime: 2020-10-11 17:25:52
 * @LastEditors: liyan
 * @Description: 权限列表
 * @FilePath: \wuhou\src\main\resources\static\webapp\js\permissions.js
 * @liyan@cilab@uestc
 */
const PERMISSIONS = [
    {
        "permission_name": "用户管理",
        "permission": "USER_MANAGE"
    }, {
        "permission_name": "角色管理",
        "permission": "ROLE_MANAGE"
    }, {
        "permission_name": "档案添加",
        "permission": "DOCUMENT_RECORD_ADD"
    }, {
        "permission_name": "档案删除",
        "permission": "DOCUMENT_RECORD_DELETE"
    }, {
        "permission_name": "档案修改",
        "permission": "DOCUMENT_RECORD_MODIFY"
    }, {
        "permission_name": "档案查看",
        "permission": "DOCUMENT_RECORD_CHECK"
    }, {
        "permission_name": "档案文件下载",
        "permission": "DOCUMENT_RECORD_FILE_DOWNLOAD",
        "pre_permission": "DOCUMENT_RECORD_CHECK"
    }, {
        "permission_name": "档案类别管理",
        "permission": "DOCUMENT_CATEGORY_MANAGE"
    }, {
        "permission_name": "仅超级用户可以",
        "permission": "SUPER_ONLY"
    }, {
        "permission_name": "访客",
        "permission": "GUEST"
    }, {
        "permission_name": "超级管理员",
        "permission": "SUPERADMIN"
    }, {
        "permission_name": "案卷编目",
        "permission": "FILE_CATALOG"
    }, {
        "permission_name": "调卷登记",
        "permission": "DOCUMENT_TRANSFER_MANAGE",
    }
]
