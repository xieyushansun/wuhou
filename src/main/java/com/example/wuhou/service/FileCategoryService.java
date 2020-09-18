package com.example.wuhou.service;

import com.example.wuhou.Dao.FileCategoryDao;
import com.example.wuhou.Dao.UserDao;
import com.example.wuhou.entity.FileCategory;
import com.example.wuhou.exception.ExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileCategoryService {
    @Autowired
    FileCategoryDao fileCategoryDao;
    public void addfileCategory(String fileCategory, String duration, String department, List<String> filelist, String documentCategory) throws ExistException {
        FileCategory filecategory = new FileCategory();

        filecategory.setFileCategory(fileCategory);
        filecategory.setDuration(duration);
        filecategory.setFilelist(filelist);
        filecategory.setDepartment(department);
        filecategory.setDocumentCategory(documentCategory);

        fileCategoryDao.addFileCategory(filecategory);

    }

}
