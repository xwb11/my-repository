package com.example.demo;

import java.io.File;
import java.io.FileInputStream;

/**
 * @ClassName ERFactory
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 17:01
 * @Version 1.0
 **/
public class ERFactory {
    //根据参数实例不同的类
    public static IUmlParser create(String filepath){
        String name = filepath.substring(filepath.lastIndexOf(".")+1);
        if(name.equals("xml")){
            return  new ErwinParser();
        }else if(name.equals("pdm")){
            return new PowerdesignerParser();
        }else
            return null;
    }

    //测试
    public static void main(String[] args) throws Exception {
        String filepath = "C:\\Users\\jiuqi\\Documents\\My Models\\ProductModels.xml";
        FileInputStream fis = new FileInputStream(new File(filepath));
        IUmlParser s = ERFactory.create(filepath);
        s.readER(fis);
    }

}
