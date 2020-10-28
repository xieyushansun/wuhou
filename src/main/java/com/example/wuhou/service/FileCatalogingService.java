package com.example.wuhou.service;

import com.example.wuhou.Dao.DocumentFileDao;
import com.example.wuhou.Dao.DocumentRecordDao;
import com.example.wuhou.Dao.FileCatalogingDao;
import com.example.wuhou.Dao.LogDao;
import com.example.wuhou.constant.PathConstant;
import com.example.wuhou.constant.ResponseConstant;
import com.example.wuhou.entity.DocumentRecord;
import com.example.wuhou.util.WorderToNewWordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class FileCatalogingService {
    @Autowired
    FileCatalogingDao fileCatalogingDao;
    @Autowired
    DocumentRecordDao documentRecordDao;
    @Autowired
    LogDao logDao;
    @Autowired
    DocumentFileDao documentFileDao;
    public void outputFileCatalog_Last(String documentNumber) throws Exception {
        List<DocumentRecord> documentRecordList = fileCatalogingDao.getDocumentRecordBydocumentNumber(documentNumber);
        if (documentRecordList == null){
            throw new Exception("当前档号下无任何记录！");
        }
        List<String[]> tableList = new ArrayList<String[]>();
        //件号
        int jian = 1;
        //上一条页数结束
        int lastendnumber = 0;

        String startDate = documentRecordList.get(0).getRecordTime();
        String endDate = documentRecordList.get(0).getRecordTime();

        for (DocumentRecord dR : documentRecordList) {
            if (dR.getRecordTime().compareTo(endDate) > 0) {
                endDate = dR.getRecordTime();
            }
            if (dR.getRecordTime().compareTo(startDate) < 0) {
                startDate = dR.getRecordTime();
            }

            String[] record = new String[6];
//            record[0] = String.valueOf(jian);
            record[0] = String.format("%03d", jian);
            jian++;
            record[1] = dR.getDanweiCode();
            record[2] = dR.getDanweiName();
            record[3] = dR.getFileName();
            record[4] = dR.getRecordTime();

            List<String> fileList = documentFileDao.findFileListByDocumentRecordId(dR.getId());
            if (fileList.isEmpty()) {
                record[5] = "无";
            } else {
                record[5] = String.valueOf(lastendnumber + 1) + "-" + String.valueOf(lastendnumber + fileList.size());
            }
            lastendnumber = lastendnumber + fileList.size();
            tableList.add(record);
        }

        int len = documentRecordList.size();
        int a = len / 20;
        // 补充条数
        int addContentNum = (a + 1) * 20 -len;
        String[] addNull = new String[6];
        addNull[0] = "";
        addNull[1] = "";
        addNull[2] = "";
        addNull[3] = "";
        addNull[4] = "";
        addNull[5] = "";

        for (int i = 0; i < addContentNum; i++){

            tableList.add(addNull);
        }

        String documentCategory = documentRecordList.get(0).getDocumentCategory();
        String fileCategory = documentRecordList.get(0).getFileCategory();
        String duration = documentRecordList.get(0).getDuration();

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("documentCategory", documentCategory);
        testMap.put("fileCategory", fileCategory);
        testMap.put("documentNumber", documentNumber);
        testMap.put("startDate", startDate);
        testMap.put("endDate", endDate);
        testMap.put("duration", duration);
        testMap.put("totalJian", String.valueOf(jian-1));
        testMap.put("totalPage", String.valueOf(lastendnumber));


        String inputPath = PathConstant.WORD_TEMPLATE;
        String outputPath = PathConstant.WORD_OUTPUT + "\\" + documentNumber + "-编目导出.docx";

        WorderToNewWordUtils.changWord(inputPath, outputPath, testMap, tableList);

        File file = new File(outputPath);
        if (!file.exists()){
           throw new Exception("导出失败");
        }
        logDao.insertLog("档案导出操作", "导出", "导出档案记录中档号为: " + documentNumber + " 的档案编目");
    }
    public void outputFileCatalog(String documentNumber) throws Exception {
        List<DocumentRecord> documentRecordList = fileCatalogingDao.getDocumentRecordBydocumentNumber(documentNumber);

        for (DocumentRecord documentRecord : documentRecordList){
            if (documentRecord.getOrder() == null || documentRecord.getOrder().equals("")){
                throw new Exception("案卷题名为: " + documentRecord.getFileName() + " 的档案记录的序号为空，请先补充后再进行编目！");
            }
        }

        // documentRecordList排序
        Comparator<DocumentRecord> netTypeComparator = new Comparator<DocumentRecord>() {
            @Override
            public int compare(DocumentRecord o1, DocumentRecord o2) {
                return Integer.valueOf(o1.getOrder()) - Integer.valueOf(o2.getOrder());
            }
        };
        Collections.sort(documentRecordList, netTypeComparator);

        if (documentRecordList == null){
            throw new Exception("当前档号下无任何记录！");
        }

        //总页数
        int totalPage = 0;
        //总件数
        int totalJianNumber = documentRecordList.size();

        List<String[]> tableList = new ArrayList<>();

        String startDate = documentRecordList.get(0).getRecordTime();
        String endDate = documentRecordList.get(0).getRecordTime();

        for (DocumentRecord dR : documentRecordList) {
            totalPage = totalPage + Integer.parseInt(dR.getPageNumber());
            if (dR.getRecordTime().compareTo(endDate) > 0) {
                endDate = dR.getRecordTime();
            }
            if (dR.getRecordTime().compareTo(startDate) < 0) {
                startDate = dR.getRecordTime();
            }

            String[] record = new String[6];
//            record[0] = String.valueOf(jian);
//            record[0] = String.format("%03d", dR.getOrder());
            record[0] = dR.getOrder();
            record[1] = dR.getDanweiCode();
            record[2] = dR.getDanweiName();
            record[3] = dR.getFileName();
            record[4] = dR.getRecordTime();
            record[5] = dR.getPageNumber();

            tableList.add(record);
        }

        int len = documentRecordList.size();
        int a = len / 20;
        // 不满一页补充条数
        int addContentNum = (a + 1) * 20 -len;
        String[] addNull = new String[6];
        addNull[0] = "";
        addNull[1] = "";
        addNull[2] = "";
        addNull[3] = "";
        addNull[4] = "";
        addNull[5] = "";
        for (int i = 0; i < addContentNum; i++){
            tableList.add(addNull);
        }

        String documentCategory = documentRecordList.get(0).getDocumentCategory();
        String fileCategory = documentRecordList.get(0).getFileCategory();
        String duration = documentRecordList.get(0).getDuration();

        Map<String, String> testMap = new HashMap<String, String>();
        testMap.put("documentCategory", documentCategory);
        testMap.put("fileCategory", fileCategory);
        testMap.put("documentNumber", documentNumber);
        testMap.put("startDate", startDate);
        testMap.put("endDate", endDate);
        testMap.put("duration", duration);
        testMap.put("totalJian", String.valueOf(totalJianNumber));
        testMap.put("totalPage", String.valueOf(totalPage));


        String inputPath = PathConstant.WORD_TEMPLATE;
        String outputPath = PathConstant.WORD_OUTPUT + "\\" + documentNumber + "-编目导出.docx";

        WorderToNewWordUtils.changWord(inputPath, outputPath, testMap, tableList);

        File file = new File(outputPath);
        if (!file.exists()){
            throw new Exception("导出失败");
        }
        logDao.insertLog("档案导出操作", "导出", "导出档案记录中档号为: " + documentNumber + " 的档案编目");
    }

}
