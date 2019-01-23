package com.example.demo.controller;



import com.example.demo.base.Result;

import com.example.demo.entity.References;
import com.example.demo.entity.Table;
import com.example.demo.enums.BaseEnums;
import com.example.demo.util.PowerdesignerUtil;
import com.example.demo.util.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName Powerdesigner
 * @Description TODO
 * @Author jiuqi
 * @Date 2019/1/22 14:48
 * @Version 1.0
 **/
@RestController
@RequestMapping("/powerdesigner")
public class PowerdesignerController {

    @PostMapping("/powerTransformation")
    public Result PdmParser(@RequestParam String filePath,@RequestParam String flag){

        if(flag.equals("1")){//标志为1代表获得各个表的信息等
            //获得表字段
            Table[] tables = PowerdesignerUtil.parsePom(filePath);
            return Results.successWithData(tables,BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
        }else if(flag.equals("2")){//标志为2 代表或得各个表之间的关系等
            //获得表之间的关系
            References[] references = PowerdesignerUtil.references(filePath);
            return Results.successWithData( references,BaseEnums.SUCCESS.code(),BaseEnums.SUCCESS.desc());
        }else {
            return Results.failure(BaseEnums.NOT_FOUND.code(),BaseEnums.NOT_FOUND.desc());
        }
    }
}
