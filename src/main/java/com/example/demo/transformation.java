package com.example.demo;

import com.example.demo.entity.EntityProps;

import java.util.List;


public interface transformation {

    List<EntityProps> transfor(String filePath) throws Exception;
}
