package com.example.demo.entity;

/**
 * Erwin ER图
 * 表与表之间的关系线实体类
 */
public class Relations {
    private String CurrentTableName;
    private String fatherTableName;
    private String childTableName;
    private String relation;
    private String relationName;

    public String getFatherTableName() {
        return fatherTableName;
    }

    public void setFatherTableName(String fatherTableName) {
        this.fatherTableName = fatherTableName;
    }

    public String getChildTableName() {
        return childTableName;
    }

    public void setChildTableName(String childTableName) {
        this.childTableName = childTableName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getCurrentTableName() {
        return CurrentTableName;
    }

    public void setCurrentTableName(String currentTableName) {
        CurrentTableName = currentTableName;
    }
}
