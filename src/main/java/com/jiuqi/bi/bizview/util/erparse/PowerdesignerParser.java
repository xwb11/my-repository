package com.jiuqi.bi.bizview.util.erparse;

import com.jiuqi.bi.bizview.util.erparse.entity.*;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.*;

import org.dom4j.Document;

/**
 * @ClassName PowerdesignerParser
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/25 9:14
 * @Version 1.0
 **/
public class PowerdesignerParser implements IUmlParser {

    @Override
    public Root readER(InputStream fis) throws Exception {

        List<Table> tableList = new ArrayList<Table>();
        Table table = null;

        SAXReader sr = new SAXReader();
        Document doc = null;

        List<Root> rootList = new ArrayList<Root>();
        Root root = new Root();

        Relation rel = null;

        doc = sr.read(fis);


        //使用selectNotes快速获取节点下的数据
        Iterator<Element> itr = doc.selectNodes("//c:Tables//o:Table").iterator();

        while (itr.hasNext()) {
            table = new Table();
            //主键
            List<Key> keys = new ArrayList<Key>();
            List<Field> fieldList = new ArrayList<Field>();
            List<Relation> relationList = new ArrayList<Relation>();
            Map<String, String> fieldmap = null;
            Field field = null;
            Element e_table = (Element) itr.next();

            int flag = 0;
            //表名
            table.setTitle(e_table.elementTextTrim("Name"));
            table.setName(e_table.elementTextTrim("Code"));
            //表名（显示在数据库中的）
            table.setPhysicalTableName(e_table.elementTextTrim("Code"));
            //表id
            table.setGuid(e_table.attributeValue("Id"));


            //在遍历Columns 下的column
            Iterator<Element> itr1 = e_table.element("Columns").elements("Column").iterator();
            while (itr1.hasNext()) {

                field = new Field();
                Element e_col = (Element) itr1.next();
                Key key2 = new Key();
                //得到defaultvalue id name
                //获取pkID为字段的标志符id
                String pkID = e_col.attributeValue("Id");
//                    col.setDefaultValue(e_col.elementTextTrim("DefaultValue"));
//                    field.setKeyfield(e_col.attributeValue("Id"));
                field.setID(pkID);
                //字段标题
                field.setTitle(e_col.elementTextTrim("Name"));
                //字段名称（存入数据库）
//                    col.setAttributeName(e_col.elementTextTrim("Name"));
                field.setName(e_col.elementTextTrim("Code"));
                //存入字段id
//                    col.setAttributeId(e_col.attributeValue("Id"));

                if(e_col.elementTextTrim("DataType")!=null){
                    if(e_col.elementTextTrim("DataType").contains("(")){
                        String dataType = e_col.elementTextTrim("DataType").substring(0, e_col.elementTextTrim("DataType").indexOf("("));
                        //数据类型
                        if (dataType.equals("date") || dataType.equals("datetime")) {
                            //日期型
                            field.setDatatype(2);
                        } else if (dataType.equals("float") || dataType.equals("real") || dataType.equals("double ")) {
                            //浮点型
                            field.setDatatype(3);
                        } else if (dataType.equals("integer") || dataType.equals("int")) {
                            //整型
                            field.setDatatype(5);
                        } else if (dataType.equals("varchar") || dataType.equals("char")) {
                            //字符串类型
                            field.setDatatype(6);
                        } else {
                            field.setDatatype(0);
                        }

                    }else {
                        String dataType = e_col.elementTextTrim("DataType");
                        //数据类型
                        if (dataType.equals("date") || dataType.equals("datetime")) {
                            //日期型
                            field.setDatatype(2);
                        } else if (dataType.equals("float") || dataType.equals("real") || dataType.equals("double ")) {
                            //浮点型
                            field.setDatatype(3);
                        } else if (dataType.equals("integer") || dataType.equals("int")) {
                            //整型
                            field.setDatatype(5);
                        } else if (dataType.equals("varchar") || dataType.equals("char")) {
                            //字符串类型
                            field.setDatatype(6);
                        } else {
                            field.setDatatype(0);
                        }
                    }
                }
//                    if (e_col.elementTextTrim("DataType").indexOf("(") > 0) {
//                        col.setType(e_col.elementTextTrim("DataType").substring(0, e_col.elementTextTrim("DataType").indexOf("(")));
//                    } else {
//                        col.setType(e_col.elementTextTrim("DataType"));
//                    }
//                    col.setPhysicalName(e_col.elementTextTrim("Code"));
//                Integer length = e_col.elementTextTrim("Length") == null ? null : Integer.parseInt(e_col.elementTextTrim("Length"));
//                    col.setLength(String.valueOf(length));

                List<String> fields = new ArrayList<String>();
                //检查是否有主键
                if (e_table.element("Keys") != null) {
                    String keys_key_id = e_table.element("Keys").element("Key").attributeValue("Id");
                    String keys_column_ref = e_table.element("Keys").element("Key").element("Key.Columns")
                            .element("Column").attributeValue("Ref");
                    String keys_primarykey_ref_id = e_table.element("PrimaryKey").element("Key").attributeValue("Ref");

                    if (keys_primarykey_ref_id.equals(keys_key_id) && keys_column_ref.equals(pkID)) {
//                            col.setPkFlag(true);
//                            field.setIskey(true);
//                            vo.setPkName(col.getAttributeName());
//                            table.setKeys();
                        fields.add(field.getName());
                        key2.setFields(fields);
                        keys.add(key2);
                        table.setKeys(keys);
                    }
                }
                fieldList.add(field);
            }
            //遍历出当前表的所有字段 与

            Iterator<Element> referenceIter = doc.selectNodes("//c:References//o:Reference").iterator();
            while (referenceIter.hasNext()) {
                Element reference = (Element) referenceIter.next();
                rel = new Relation();

                //关系键中的父表id
                String parentTable_id = reference.element("ParentTable").element("Table").attributeValue("Ref");
                //关系键中的子表id
                String childTable_id = reference.element("ChildTable").element("Table").attributeValue("Ref");

                //查找符合条件的子表
                Iterator<Element> targetIter = doc.selectNodes("//c:Tables//o:Table").iterator();
                while (targetIter.hasNext()) {
                    Element targetTable = targetIter.next();
                    String targetId = targetTable.attributeValue("Id");
                    String tatgetTableName = targetTable.elementTextTrim("Code");

                    if (parentTable_id.equals(table.getGuid())) {
                        if (targetId.equals(childTable_id)) {
                            rel.setSrctable(table.getName());
                            rel.setTargettable(tatgetTableName);
                            break;
                        }
                    } else if (childTable_id.equals(table.getGuid())) {

                        if (targetId.equals(parentTable_id)) {
                            rel.setSrctable(table.getName());
                            rel.setTargettable(tatgetTableName);
                            break;
                        }
                    }
                }
                //判断如果relation里边没有符合的表，则跳出循环。避免没有关系还新增一个relation
                if (rel.getSrctable() == null && rel.getTargettable() == null) {
                    continue;
                }
                //遍历关系字段
                String columnRef1 = reference.element("Joins").element("ReferenceJoin").element("Object1").element("Column").attributeValue("Ref");
                String columnRef2 = reference.element("Joins").element("ReferenceJoin").element("Object2").element("Column").attributeValue("Ref");

                fieldmap = new HashMap<String, String>();

                //此时的关联表中的id 是否存在于当前表中
                Iterator<Element> columnList = doc.selectNodes("//c:Columns//o:Column").iterator();

                while (columnList.hasNext()) {
                    Element column = columnList.next();
                    String columnID = column.attributeValue("Id");
                    String columnName = column.elementTextTrim("Code");

                    //遍历当前表的字段
                    for (int i = 0; i < fieldList.size(); i++) {
                        if (columnRef1.equals(fieldList.get(i).getID())) {
                            //找到与本表的字段相关联的字段
                            if (columnRef2.equals(columnID)) {
                                fieldmap.put(fieldList.get(i).getName(), columnName);
//                                    rel.setFieldmap(fieldmapList);
                            }
                        } else if (columnRef2.equals(fieldList.get(i).getID())) {
                            if (columnRef1.equals(columnID)) {
                                fieldmap.put(fieldList.get(i).getName(), columnName);
                            }
                        }
                    }
                }
                rel.setFieldmap(fieldmap);
                relationList.add(rel);
            }
            table.setFields(fieldList);
            table.setRelations(relationList);
            tableList.add(table);
        }
        root.setTables(tableList);
        rootList.add(root);
        return root;
    }
}
