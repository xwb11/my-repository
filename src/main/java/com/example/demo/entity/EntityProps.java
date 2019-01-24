package com.example.demo.entity;



import java.util.List;

/**
 * Erwin ER图
 * 各个ER图表的实体类
 */
public class EntityProps {
    //表名（中文）
    private String name;
    //表名（英文）
    private String code;
    private String id;
    //主键名称
    private String PkName;
    //外键名
//    private String FKName;
    private Attribute[] attributes;
    private Relations[] relations;
    private List FkNameList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute[] attributes) {
        this.attributes = attributes;
    }

    public String getPkName() {
        return PkName;
    }

    public void setPkName(String pkName) {
        PkName = pkName;
    }

//    public String getFKName() {
//        return FKName;
//    }
//
//    public void setFKName(String FKName) {
//        this.FKName = FKName;
//    }

    public List getFkNameList() {
        return FkNameList;
    }

    public void setFkNameList(List fkNameList) {
        FkNameList = fkNameList;
    }

    public Relations[] getRelations() {
        return relations;
    }

    public void setRelations(Relations[] relations) {
        this.relations = relations;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
