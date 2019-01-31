package com.jiuqi.bi.bizview.util.erparse.entity;

import java.util.List;

/**
 * @ClassName Tables
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 11:33
 * @Version 1.0
 **/
public class Table {

    private List<Field> fields;
    private String filter;
    private List folders;
    private String guid;
    private List<String> hierarchies;
    private List<Key> keys;
    private String name;
    private String physicalSchemaName;
    private String physicalTableName;
    private List<Relation> relations;
    private List<String> sumFields;
    private String title;
    private Integer type;
    private boolean visible;
    private List<String> FkNameList;
//    //主键名称
//    private String PkName;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List getFolders() {
        return folders;
    }

    public void setFolders(List folders) {
        this.folders = folders;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<String> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(List<String> hierarchies) {
        this.hierarchies = hierarchies;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhysicalSchemaName() {
        return physicalSchemaName;
    }

    public void setPhysicalSchemaName(String physicalSchemaName) {
        this.physicalSchemaName = physicalSchemaName;
    }

    public String getPhysicalTableName() {
        return physicalTableName;
    }

    public void setPhysicalTableName(String physicalTableName) {
        this.physicalTableName = physicalTableName;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<String> getSumFields() {
        return sumFields;
    }

    public void setSumFields(List<String> sumFields) {
        this.sumFields = sumFields;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

//    public List<String> getFkNameList() {
//        return FkNameList;
//    }
//
//    public void setFkNameList(List<String> fkNameList) {
//        FkNameList = fkNameList;
//    }

//    public String getPkName() {
//        return PkName;
//    }
//
//    public void setPkName(String pkName) {
//        PkName = pkName;
//    }


}
