package com.example.demo;


/**
 * @ClassName Main
 * @Description TODO 工厂类实现
 * @Author xwb
 * @Date 2019/1/24 20:10
 * @Version 1.0
 **/
public class Main {

    //测试
    public static void main(String[] args) throws Exception {
        String filepath = "E:\\Powerdesigner\\file\\物理模型2.pdm";
        transformation s = Main.create(filepath);
        s.transfor(filepath);
    }
    //根据参数实例不同的类
    public static transformation create(String filepath){
        String name = filepath.substring(filepath.lastIndexOf(".")+1);
        if(name.equals("xml")){
            return  new erwinimpl();
        }else if(name.equals("pdm")){
            return new powerdesignerImpl();
        }else
            return null;
    }
}
