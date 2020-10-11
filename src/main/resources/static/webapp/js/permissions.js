/*
 * @Author: liyan
 * @Date: 2020-10-05 10:01:06
 * @LastEditTime: 2020-10-11 22:16:13
 * @LastEditors: liyan
 * @Description: 权限列表
 * @FilePath: \wuhou\src\main\resources\static\webapp\js\permissions.js
 * @liyan@cilab@uestc
 */
const PERMISSIONS = [
    {
        "permission_name": "超级管理员",
        "permission": "SUPER_ADMIN",
        "tips": "所有权限"
    }, {
        "permission_name": "系统管理",
        "permission": "SUPER_ONLY",
        "tips": "磁盘管理、日志管理"
    }, {
        "permission_name": "用户管理",
        "permission": "USER_MANAGE"
    }, {
        "permission_name": "角色管理",
        "permission": "ROLE_MANAGE"
    }, {
        "permission_name": "档案及案卷类别管理",
        "permission": "DOCUMENT_CATEGORY_MANAGE"
    }, {
        "permission_name": "调卷登记管理",
        "permission": "DOCUMENT_TRANSFER_MANAGE",
    }, {
        "permission_name": "档案添加",
        "permission": "DOCUMENT_RECORD_ADD"
    }, {
        "permission_name": "档案查看",
        "permission": "DOCUMENT_RECORD_CHECK"
    }, {
        "permission_name": "档案删除",
        "permission": "DOCUMENT_RECORD_DELETE",
        "pre_permission": "DOCUMENT_RECORD_CHECK",
        "tips": "需要档案查看权限"
    }, {
        "permission_name": "档案修改",
        "permission": "DOCUMENT_RECORD_MODIFY",
        "pre_permission": "DOCUMENT_RECORD_CHECK",
        "tips": "需要档案查看权限"
    }, {
        "permission_name": "档案附件下载",
        "permission": "DOCUMENT_RECORD_FILE_DOWNLOAD",
        "pre_permission": "DOCUMENT_RECORD_CHECK",
        "tips": "需要档案查看权限"
    }, {
        "permission_name": "案卷编目",
        "permission": "FILE_CATALOG"
    }, {
        "permission_name": "游客",
        "permission": "GUEST",
    }
]
