package com.example.demo;

import com.example.demo.entity.Tables;

import java.io.FileInputStream;
import java.util.List;

/**
 * 读取er图转换接口
 */
public interface IUmlParser {
    List<Tables> readER(FileInputStream fis) throws Exception;
}
