package com.example.demo.controller;

import com.example.demo.base.Result;

import com.example.demo.util.Results;
import com.jiuqi.bi.bizview.util.erparse.ERFactory;
import com.jiuqi.bi.bizview.util.erparse.IUmlParser;
import com.jiuqi.bi.bizview.util.erparse.entity.Root;
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
        Root root =  s.readER(fis);
        return Results.successWithData(root);
//        String filePath = "C:\\Users\\jiuqi\\Documents\\My Models\\ProductModels.xml";
////        String filePath = "C:\\Users\\jiuqi\\Documents\\My Models\\学生班级.xml";
////        String filePath = "E:\\Powerdesigner\\file\\物理模型2.pdm";
//        FileInputStream fis = new FileInputStream(new File(filePath));
//        IUmlParser s = ERFactory.create(filePath);
//        s.readER(fis);
    }
}
