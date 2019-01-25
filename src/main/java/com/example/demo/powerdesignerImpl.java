package com.example.demo;

import com.example.demo.entity.Attribute;
import com.example.demo.entity.EntityProps;
import com.example.demo.entity.Relations;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName powerdesignerImpl
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 9:14
 * @Version 1.0
 **/
public class powerdesignerImpl implements transformation{

    @Override
    public List<EntityProps> transfor(String filePath) throws Exception {

        List<EntityProps> voS = new ArrayList<EntityProps>();
        EntityProps vo = null;
        File f = new File(filePath);
        SAXReader sr = new SAXReader();
        Document doc = null;

        List<Relations> references = new ArrayList<>();
        try {
            doc = sr.read(f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        //使用selectNotes快速获取节点下的数据
        Iterator itr = doc.selectNodes("//c:Tables//o:Table").iterator();

        while (itr.hasNext()) {
            vo = new EntityProps();
            List<Attribute> list = new ArrayList<Attribute>();
            Attribute col = null;
            Element e_table = (Element) itr.next();
            //拿到name code值 存入节点中
            vo.setName(e_table.elementTextTrim("Name"));
            vo.setCode(e_table.elementTextTrim("Code"));

            //在遍历Columns 下的column
            Iterator itr1 = e_table.element("Columns").elements("Column").iterator();
            while (itr1.hasNext()) {
                try {

                    col = new Attribute();
                    Element e_col = (Element) itr1.next();
                    //得到defaultvalue id name
                    //获取pkID为字段的标志符id
                    String pkID = e_col.attributeValue("Id");
                    col.setDefaultValue(e_col.elementTextTrim("DefaultValue"));
                    col.setAttributeName(e_col.elementTextTrim("Name"));
                    if (e_col.elementTextTrim("DataType").indexOf("(") > 0) {
                        col.setType(e_col.elementTextTrim("DataType").substring(0, e_col.elementTextTrim("DataType").indexOf("(")));
                    } else {
                        col.setType(e_col.elementTextTrim("DataType"));
                    }
                    col.setPhysicalName(e_col.elementTextTrim("Code"));
                    Integer length = e_col.elementTextTrim("Length") == null ? null : Integer.parseInt(e_col.elementTextTrim("Length"));
                    col.setLength(String.valueOf(length));
                    //检查是否有主键
                    if (e_table.element("Keys") != null) {
                        String keys_key_id = e_table.element("Keys").element("Key").attributeValue("Id");
                        String keys_column_ref = e_table.element("Keys").element("Key").element("Key.Columns")
                                .element("Column").attributeValue("Ref");
                        String keys_primarykey_ref_id = e_table.element("PrimaryKey").element("Key").attributeValue("Ref");

                        if (keys_primarykey_ref_id.equals(keys_key_id) && keys_column_ref.equals(pkID)) {
                            col.setPkFlag(true);
                            vo.setPkName(col.getAttributeName());
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
            vo.setAttributes(list);
            voS.add(vo);
            System.out.println(vo);
            System.out.println("======================");
        }
        return voS;
    }
}
