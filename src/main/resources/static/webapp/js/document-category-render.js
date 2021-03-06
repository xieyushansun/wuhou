/*
 * @Author: liyan
 * @Date: 2020-09-21 20:16:16
 * @LastEditTime: 2020-10-29 16:02:32
 * @LastEditors: liyan
 * @Description: 用于档案类别和案卷类别下拉选择的渲染和数据处理
 * @FilePath: \wuhou\src\main\resources\static\webapp\js\document-category-render.js
 */

// 获取档案类别
function getDocumentCategoryList(data) {
    /**
     * @description: 获取档案类别 
     * @param {Array: data格式为[
                {
                    "documentCategory": "string",
                    "documentCategoryShortName": "string",
                    "fileCategory": [
                    "string"
                    ],
                    "id": "string"
                }
            ]} 
     * @return {Array: documentCategoryList} 
     */
    if (!Array.isArray(data)) {
        return [];
    }
    var documentCategoryList = [];
    for (var idx = 0; idx < data.length; idx++) {
        var cat = data[idx];
        documentCategoryList.push(cat['documentCategory']);
    }
    return documentCategoryList;
}

function getDocCatAbbr(data) {
    if (!Array.isArray(data)) {
        return [];
    }
    var abbr = {};
    for (var idx = 0; idx < data.length; idx++) {
        var cat = data[idx];
        abbr[cat['documentCategory']] = cat['documentCategoryShortName'];
    }
    return abbr;
}

// 根据档案类别获取案卷类别
function getFileCategoryList(data, documentCategory=null) {
    
    if (!Array.isArray(data)) {
        return [];
    }
    var fileCategoryList = [];
    if (documentCategory === null || documentCategory === '' || documentCategory === undefined) {
        for (var docuIdx = 0; docuIdx < data.length; docuIdx++) {
            fileCategoryList = fileCategoryList.concat(data[docuIdx]['fileCategory']); 
        }
    } else {
        for (var docuIdx = 0; docuIdx < data.length; docuIdx++) {
            if (data[docuIdx]['documentCategory'] === documentCategory) {
                fileCategoryList = fileCategoryList.concat(data[docuIdx]['fileCategory']); 
            }
        }
    }
    return fileCategoryList;
}

// 创建option list
function makeDocumentOption(data_list, documentCatAbbr){
    if (!Array.isArray(data_list)) {
        return [];
    }
    var option_list = [{val: '', text: ''}];
    for (var indexList = 0; indexList < data_list.length; indexList++) {
        var data = data_list[indexList];
        var abbr = documentCatAbbr[data];
        option_list.push({"val": data, "text": data + '-' + abbr});
    }
    return option_list;
}

function makeFileOption(data_list){
    if (!Array.isArray(data_list)) {
        return [];
    }
    var option_list = [{val: '', text: ''}];
    for (var indexList = 0; indexList < data_list.length; indexList++) {
        var data = data_list[indexList];
        var no = indexList < 10? '0' + (indexList + 1).toString(): (indexList + 1).toString();
        option_list.push({"val": data + '-' + no, "text": data + '-' + no});
    }
    return option_list;
}

function makeOption(data_list) {
    if (!Array.isArray(data_list)) {
        return [];
    }
    var option_list = [{val: '', text: ''}];
    for (var indexList = 0; indexList < data_list.length; indexList++) {
        var data = data_list[indexList];
        option_list.push({"val": data, "text": data});
    }
    return option_list;
}

// 根据lay-filter修改select的option
function changeOption(filter, option_list){
    var selector = "[lay-filter="+filter+"]";
    $(selector).empty();
    if (!(option_list instanceof Array)) {
        return;
    }
    if(option_list)
        for(var i = 0, len = option_list.length; i < len; i++){
            var option = $("<option>").val(option_list[i].val).text(option_list[i].text).data('abbr', 'xs');
            $(selector).append(option);
        }
}

// select标签的初始化渲染
function selectInit(data, add=false) {
    /**
     * @description: select标签的初始化渲染
     * @param {data: "后台获取的档案案卷类别数据", add: "是否在option后面添加-缩写（序号）"}
     * @return {*}
     */
    var documentCategoryList = getDocumentCategoryList(data); // 获取档案类别的list
    var fileCategoryList = getFileCategoryList(data);
    if (add) {
        var documentCatAbbr = getDocCatAbbr(data);
        changeOption('documentCategory', makeDocumentOption(documentCategoryList, documentCatAbbr));
        changeOption('fileCategory', makeFileOption(fileCategoryList));
    } else {
        changeOption('documentCategory', makeOption(documentCategoryList)); //修改档案类别的option;
        changeOption('fileCategory', makeOption(fileCategoryList));
    }
}

// 根据选择的select重新渲染表格
function changeTable(documentCategory='', fileCategory='') {
    documentTable.reload({
        where: {documentCategory: documentCategory, fileCategory: fileCategory},
        page: {curr: 1}
    });
}