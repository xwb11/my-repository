package com.example.demo.controller;

import com.example.demo.base.Result;
import com.example.demo.entity.EntityProps;
import com.example.demo.enums.BaseEnums;
import com.example.demo.service.ErwinService;
import com.example.demo.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/erwin")
public class ErwinController {

    @Autowired
    private ErwinService service;

    @PostMapping("/transformation")
    public Result ErwinParser(@RequestParam String filePath) throws Exception {
        //获取Erwin模型的主要信息
        String name = filePath.substring(filePath.lastIndexOf(".") + 1);
        if (name.equals("xml")) {
            List<EntityProps> erlist = service.ErWin(filePath);
            return Results.successWithData(erlist, BaseEnums.SUCCESS.code(), BaseEnums.SUCCESS.desc());
        } else if (name.equals("pdm")) {
            List<EntityProps> powerlist = service.parsePom(filePath);
            return Results.successWithData(powerlist, BaseEnums.SUCCESS.code(), BaseEnums.SUCCESS.desc());
        } else {
            return Results.failureWithData(BaseEnums.NOT_FOUND);
        }


    }

}
