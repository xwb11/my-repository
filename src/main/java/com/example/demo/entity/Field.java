package com.example.demo.entity;

/**
 * @ClassName Field
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 11:34
 * @Version 1.0
 **/
public class Field {

    private Integer aggregationtype;
    private Integer applyType;
    /**
     * 数据类型
     * 0:未知，2:日期，3：浮点，5：整型，6：字符串
     */
    private Integer datatype;
    private boolean defaultQuickCondition;
    private boolean drillable;
    /**
     * 字段类型
     * 1：普通维度，2：时间维度，3：度量，4：描述信息
     */
    private Integer fieldtype;
    private boolean iskey;
    private boolean istimekey;
    private String keyfield;
    private String name;
    private String namefield;
    private String tableName;
    private String timegranularity;
    private String title;
    private boolean visible;

    public Integer getAggregationtype() {
        return aggregationtype;
    }

    public void setAggregationtype(Integer aggregationtype) {
        this.aggregationtype = aggregationtype;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public Integer getDatatype() {
        return datatype;
    }

    public void setDatatype(Integer datatype) {
        this.datatype = datatype;
    }

    public boolean isDefaultQuickCondition() {
        return defaultQuickCondition;
    }

    public void setDefaultQuickCondition(boolean defaultQuickCondition) {
        this.defaultQuickCondition = defaultQuickCondition;
    }

    public boolean isDrillable() {
        return drillable;
    }

    public void setDrillable(boolean drillable) {
        this.drillable = drillable;
    }

    public Integer getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(Integer fieldtype) {
        this.fieldtype = fieldtype;
    }

    public boolean isIskey() {
        return iskey;
    }

    public void setIskey(boolean iskey) {
        this.iskey = iskey;
    }

    public boolean isIstimekey() {
        return istimekey;
    }

    public void setIstimekey(boolean istimekey) {
        this.istimekey = istimekey;
    }

    public String getKeyfield() {
        return keyfield;
    }

    public void setKeyfield(String keyfield) {
        this.keyfield = keyfield;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamefield() {
        return namefield;
    }

    public void setNamefield(String namefield) {
        this.namefield = namefield;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTimegranularity() {
        return timegranularity;
    }

    public void setTimegranularity(String timegranularity) {
        this.timegranularity = timegranularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
