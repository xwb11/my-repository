package com.example.demo.entity;

/**
 * @ClassName References
 * @Description powerdesigner 表之间关系实体类
 * @Author jiuqi
 * @Date 2019/1/22 15:36
 * @Version 1.0
 **/
public class References {

    private String code;
    private String name;
    private String comment;
    private String parentTable;
    private String childTable;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public String getChildTable() {
        return childTable;
    }

    public void setChildTable(String childTable) {
        this.childTable = childTable;
    }
}
