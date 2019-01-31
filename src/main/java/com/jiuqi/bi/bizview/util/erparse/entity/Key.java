package com.jiuqi.bi.bizview.util.erparse.entity;

import java.util.List;

/**
 * @ClassName Key
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/28 17:30
 * @Version 1.0
 **/
public class Key {
    private String name;
    private List<String> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
