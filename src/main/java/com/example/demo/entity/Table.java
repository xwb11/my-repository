package com.example.demo.entity;

/**
 * powerdesigner er图
 * 各个表的实体类
 */
public class Table {

    private String tableName;
    private String tableCode;
    private String pkField;
    private Column[] cols;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getPkField() {
        return pkField;
    }

    public void setPkField(String pkField) {
        this.pkField = pkField;
    }

    public void setCols(Column[] cols) {
        this.cols = cols;
    }

    public Column[] getCols() {
        return cols;
    }

}
