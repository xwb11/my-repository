package com.example.demo.util;

import com.example.demo.entity.Column;
import com.example.demo.entity.References;
import com.example.demo.entity.Table;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName PowerdesignerUtil
 * @Description 解析powerdesigner pdm文件工具类
 * @Author jiuqi
 * @Date 2019/1/22 15:03
 * @Version 1.0
 **/
public class PowerdesignerUtil {

    /*
     * @Author xwb
     * @Description //TODO 查找表内容
     * @Date  2019/1/22
     * @Param
     * @return
     **/
    public static Table[] parsePom(String filePath) {
        Table[] tabs = new Table[]{};
        List<Table> voS = new ArrayList<Table>();
        Table vo = null;
        Column[] cols = null;
        File f = new File(filePath);
        SAXReader sr = new SAXReader();
        Document doc = null;

        List<References> references = new ArrayList<>();
//        References[] references1 = new References[]{};
//        References references2 = null;
        try {
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        //使用selectNotes快速获取节点下的数据
        Iterator itr = doc.selectNodes("//c:Tables//o:Table").iterator();

        while (itr.hasNext()) {
            vo = new Table();
            cols = new Column[]{};
            List<Column> list = new ArrayList<Column>();
            Column col = null;
            Element e_table = (Element) itr.next();
            //拿到name code值 存入节点中
            vo.setTableName(e_table.elementTextTrim("Name"));
            vo.setTableCode(e_table.elementTextTrim("Code"));

            //在遍历Columns 下的column
            Iterator itr1 = e_table.element("Columns").elements("Column").iterator();
            while (itr1.hasNext()) {
                try {

                    col = new Column();
                    Element e_col = (Element) itr1.next();
                    //得到defaultvalue id name
                    //获取pkID为字段的标志符id
                    String pkID = e_col.attributeValue("Id");
                    col.setDefaultValue(e_col.elementTextTrim("DefaultValue"));
                    col.setName(e_col.elementTextTrim("Name"));
                    if (e_col.elementTextTrim("DataType").indexOf("(") > 0) {
                        col.setType(e_col.elementTextTrim("DataType").substring(0, e_col.elementTextTrim("DataType").indexOf("(")));
                    } else {
                        col.setType(e_col.elementTextTrim("DataType"));
                    }
                    col.setCode(e_col.elementTextTrim("Code"));
                    col.setLength(e_col.elementTextTrim("Length") == null ? null : Integer.parseInt(e_col.elementTextTrim("Length")));
                    //检查是否有主键
                    if (e_table.element("Keys") != null) {
                        String keys_key_id = e_table.element("Keys").element("Key").attributeValue("Id");
                        String keys_column_ref = e_table.element("Keys").element("Key").element("Key.Columns")
                                .element("Column").attributeValue("Ref");
                        String keys_primarykey_ref_id = e_table.element("PrimaryKey").element("Key").attributeValue("Ref");

                        if (keys_primarykey_ref_id.equals(keys_key_id) && keys_column_ref.equals(pkID)) {
                            col.setPkFlag(true);
                            vo.setPkField(col.getCode());
                        }

                    }
                    list.add(col);
                    System.out.println(col);
                } catch (Exception ex) {
                    // col.setType(e_col.elementTextTrim("DataType"));
                    System.out.println("+++++++++有错误++++");
                    ex.printStackTrace();
                }
            }
            vo.setCols(list.toArray(cols));
//            vo.setReferences(references.toArray(references1));
            voS.add(vo);
            System.out.println(vo);
            System.out.println("======================");
            System.out.println();
        }
        return voS.toArray(tabs);
    }
    /*
     * @Author xwb
     * @Description //TODO 查找表关系方法
     * @Date  2019/1/22
     * @Param
     * @return
     **/
    public static References[] references(String filePath){
        File f = new File(filePath);
        SAXReader sr = new SAXReader();
        Document doc = null;

        List<References> references = new ArrayList<>();
        References[] references1 = new References[]{};
        References references2 = null;
        try {
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Iterator reference = doc.selectNodes("//c:References//o:Reference").iterator();
        while (reference.hasNext()) {
            Element table = (Element) reference.next();
            references2 = new References();

            String parentTable_id = table.element("ParentTable").element("Table").attributeValue("Ref");
            String childTable_id  = table.element("ChildTable").element("Table").attributeValue("Ref");
            //关联线名称
            String name = table.elementTextTrim("Name");
            //关联线代码名
            String code = table.elementTextTrim("Code");
            //关联线内容
            String comment = table.elementTextTrim("Comment");
            references2.setName(name);
            references2.setCode(code);
            references2.setComment(comment);

            int parentFlag = 0;
            int childFlag = 0;
            Iterator itr = doc.selectNodes("//c:Tables//o:Table").iterator();
            while(itr.hasNext()){
                Element e_table = (Element) itr.next();
                String tableId = e_table.attributeValue("Id");
                String tableName = e_table.elementTextTrim("Name");
                if(parentFlag==0 || childFlag==0){
                    if(parentTable_id.equals(tableId)){
                        System.out.println("父表："+tableName);
                        references2.setParentTable(tableName);
                        parentFlag = 1;
                    }else if(childTable_id.equals(tableId)){
                        System.out.println("子表:"+tableName);
                        references2.setChildTable(tableName);
                        childFlag=1;
                    }
                }
            }
            references.add(references2);
        }
        return references.toArray(references1);
    }
}
