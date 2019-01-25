package com.example.demo.entity;



import java.util.List;

/**
 * @ClassName Relation
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 11:41
 * @Version 1.0
 **/
public class Relation {

    private List<String> fieldmap;
    private boolean pivot;
    private Integer relationmode;
    private String srctable;
    private String targettable;

    public List<String> getFieldmap() {
        return fieldmap;
    }

    public void setFieldmap(List<String> fieldmap) {
        this.fieldmap = fieldmap;
    }

    public boolean isPivot() {
        return pivot;
    }

    public void setPivot(boolean pivot) {
        this.pivot = pivot;
    }

    public Integer getRelationmode() {
        return relationmode;
    }

    public void setRelationmode(Integer relationmode) {
        this.relationmode = relationmode;
    }

    public String getSrctable() {
        return srctable;
    }

    public void setSrctable(String srctable) {
        this.srctable = srctable;
    }

    public String getTargettable() {
        return targettable;
    }

    public void setTargettable(String targettable) {
        this.targettable = targettable;
    }
}
