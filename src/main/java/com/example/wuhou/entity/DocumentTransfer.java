package com.example.wuhou.entity;

public class DocumentTransfer {
    private String _id;
    // 盒号
    private String boxNumber;
    // 借阅人
    private String borrower;
    // 借阅日期
    private String borrowDate;
    // 归还日期
    private String returnDate;
    // 借阅记录者
    private String borrowRecorder;
    // 归还记录者
    private String returnRecorder;

    public DocumentTransfer(){
        this._id = null;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getBorrowRecorder() {
        return borrowRecorder;
    }

    public void setBorrowRecorder(String borrowRecorder) {
        this.borrowRecorder = borrowRecorder;
    }

    public String getReturnRecorder() {
        return returnRecorder;
    }

    public void setReturnRecorder(String returnRecorder) {
        this.returnRecorder = returnRecorder;
    }

    @Override
    public String toString() {
        return "DocumentTransfer{" +
                "盒号='" + boxNumber + '\'' +
                ", 借阅人='" + borrower + '\'' +
                ", 借阅日期='" + borrowDate + '\'' +
                ", 归还日期='" + returnDate + '\'' +
                ", 借阅记录者='" + borrowRecorder + '\'' +
                ", 归还记录者='" + returnRecorder + '\'' +
                '}';
    }
}
