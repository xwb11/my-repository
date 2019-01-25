package com.example.demo.controller;

import com.example.demo.ERFactory;
import com.example.demo.IUmlParser;
import com.example.demo.base.Result;
import com.example.demo.entity.Tables;
import com.example.demo.util.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@RestController
@RequestMapping("/Er")
public class ErwinController {


    @PostMapping("/ErParser")
    public Result ErParser(@RequestParam String filePath) throws Exception {
        //获取Erwin模型的主要信息
        FileInputStream fis = new FileInputStream(new File(filePath));
//        String name = filePath.substring(filePath.lastIndexOf(".") + 1);
        IUmlParser s = ERFactory.create(filePath);
        List<Tables> tablesList = s.readER(fis);
        return Results.successWithData(tablesList);
    }
}
