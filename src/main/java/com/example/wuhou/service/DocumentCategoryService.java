package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentCategoryDao;
import com.example.wuhou.entity.DocumentCategory;
import com.example.wuhou.exception.ExistException;
import com.example.wuhou.exception.NotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentCategoryService {
    @Autowired
    DocumentCategoryDao documentCategoryDao;
    public void addDocumentCategory(String documentCategory, String documentCategoryShortName, String[] fileCategory) throws ExistException {
        DocumentCategory documentCategory1 = new DocumentCategory();

        documentCategory1.setDocumentCategory(documentCategory);
        documentCategory1.setDocumentCategoryShortName(documentCategoryShortName);
        documentCategory1.setFileCategory(fileCategory);

        documentCategoryDao.addDocumentCategory(documentCategory1);

    }
    public void deleteDocumentCategory(String id) throws NotExistException {
        documentCategoryDao.deleteDocumentCategory(id);
    }

    public void modifyDocumentCategory(String id, String documentCategory, String documentCategoryShortName, String[] fileCategory ) throws NotExistException{
        DocumentCategory documentCategory1 = new DocumentCategory();
        documentCategory1.setId(id);
        documentCategory1.setDocumentCategory(documentCategory);
        documentCategory1.setDocumentCategoryShortName(documentCategoryShortName);
        documentCategory1.setFileCategory(fileCategory);
        documentCategoryDao.modifyDocumentCategory(documentCategory1);
    }
    public List<DocumentCategory> findAllDocumentCategory(){
        List<DocumentCategory> documentCategories = documentCategoryDao.findAllDocumentCategory();
        return documentCategories;
    }
}
