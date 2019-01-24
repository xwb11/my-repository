package com.example.demo.entity;

/**
 * Erwin ER图
 * 表字段实体类
 */
public class Attribute {
    //默认值
    private String defaultValue;
    //字段id
    private String attributeId;
    //字段名称（中文）
    private String attributeName;
    //字段名称（英文）
    private String physicalName;
    //字段类型
    private String type;
    //字段长度
    private String length;
    //主键标识
    private boolean PkFlag=false;
    //外键标识
    private boolean FKFlag=false;


    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPkFlag() {
        return PkFlag;
    }

    public void setPkFlag(boolean pkFlag) {
        PkFlag = pkFlag;
    }

    public boolean isFKFlag() {
        return FKFlag;
    }

    public void setFKFlag(boolean FKFlag) {
        this.FKFlag = FKFlag;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPhysicalName() {
        return physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
