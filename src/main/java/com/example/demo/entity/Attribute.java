package com.example.demo.entity;

/**
 * Erwin ER图
 * 表字段实体类
 */
public class Attribute {
    private String attributeId;
    private String attributeName;
    private String type;
    private String length;
    //主键标识
    private boolean PkFlag=false;
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
}
