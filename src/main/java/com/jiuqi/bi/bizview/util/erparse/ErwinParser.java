package com.jiuqi.bi.bizview.util.erparse;

import java.io.InputStream;
import java.util.*;

import com.jiuqi.bi.bizview.util.erparse.entity.*;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @ClassName ErwinParser
 * @Description TODO
 * @Author xwb
 * @Date 2019/1/24 20:07
 * @Version 1.0
 **/
public class ErwinParser implements IUmlParser {

    @Override
    public Root readER(InputStream fis) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(fis);//读取xml文档

        Element roots = document.getRootElement();//得到xml文档根节点元素
        System.out.println("输出根节点元素的名字===" + roots.getName());

        //循环遍历出更节点下面的子节点
        Iterator<Element> it1 = roots.selectNodes("//EMX:Model").iterator();

        //字段
        Field field = null;

        //表
        List<Table> tables = new ArrayList<Table>();
        Table table = null;

        List<Root> rootList = new ArrayList<Root>();
        Root root = new Root();

        while (it1.hasNext()) {
            Element element1 = (Element) it1.next();
            Iterator<Element> iterator = element1.element("Entity_Groups").elements("Entity").iterator();
            while (iterator.hasNext()) {//遍历获取到所有的表
//                ep = new EntityProps();
                table = new Table();
//                List<Relations> relationsList = new ArrayList<Relations>();
                List<Relation> relationList = new ArrayList<Relation>();
                List<String> fknamelist = new ArrayList<String>();
                List<Key> keys = new ArrayList<Key>();

//                List<Attribute> columns = new ArrayList<Attribute>();
                List<Field> fieldList = new ArrayList<Field>();
                //得到节点entity
                Element entity = (Element) iterator.next();

                //获取到表的名称和id
                Element entityProps = entity.element("EntityProps");
                System.out.println(entityProps.elementTextTrim("Name"));
                System.out.println(entityProps.elementTextTrim("Long_Id"));
                String enetityId = entityProps.elementTextTrim("Long_Id");

                String id = entity.attributeValue("id");
                String tableId = id.substring(id.indexOf("{") + 1, id.indexOf("}"));
//                ep.setId(tableId);
//                ep.setName(entity.attributeValue("name"));
                //表id
                table.setGuid(tableId);
                //表名
                table.setPhysicalTableName(entityProps.elementTextTrim("User_Formatted_Physical_Name"));
                //物理表名（存贮数据库的名称）
                table.setTitle(entity.attributeValue("name"));
                table.setName(entity.attributeValue("name"));

                Element parent_ref = entityProps.element("Parent_Relationships_Ref_Array");
                Element child_ref = entityProps.element("Child_Relationships_Ref_Array");

                /**
                 * 获取表字段
                 */
                Iterator<Element> attribute = entity.elements("Attribute_Groups").iterator();
                while (attribute.hasNext()) {//遍历表中字段

                    //得到attribute下的属性（表字段的属性）
                    Element att = (Element) attribute.next();
                    //得到attribute下的AttributeProps属性值
                    Iterator<Element> att1 = att.elements("Attribute").iterator();
                    while (att1.hasNext()) {
//                        attributes = new Attribute();
                        field = new Field();
                        Element att2 = (Element) att1.next();
                        Element attProps = att2.element("AttributeProps");

                        String pkid = attProps.elementTextTrim("Long_Id");
                        String colId = pkid.substring(pkid.indexOf("{") + 1, pkid.indexOf("}"));
                        //字段id
//                        attributes.setAttributeId(colId);
                        field.setID(colId);
                        //字段名称
//                        attributes.setAttributeName(attProps.elementTextTrim("Name"));
                        field.setTitle(attProps.elementTextTrim("Name"));
                        //字段物理名称(英文字母)
//                        attributes.setPhysicalName(attProps.elementTextTrim("User_Formatted_Physical_Name"));
                        field.setName(attProps.elementTextTrim("User_Formatted_Physical_Name"));
                        //字段类型
//                        attributes.setType(attProps.elementTextTrim("Physical_Data_Type"));

                        String type = attProps.elementTextTrim("Physical_Data_Type");
                        if(type !=null){
                            if (type.contains("(")) {//判断类型中是否存在"（）"
                                String dataType = type.substring(0, type.indexOf("("));
                                if (dataType.equals("DATE") || dataType.equals("DATETIME")) {
                                    //日期型
                                    field.setDatatype(2);
                                } else if (dataType.equals("FLOAT") || dataType.equals("REAL") || dataType.equals("DOUBLE ")) {
                                    //浮点型
                                    field.setDatatype(3);
                                } else if (dataType.equals("INTEGER")) {
                                    //整型
                                    field.setDatatype(5);
                                } else if (dataType.equals("VARCHAR") || dataType.equals("VARCHAR2") || dataType.equals("CHAR")) {
                                    //字符串类型
                                    field.setDatatype(6);
                                } else {
                                    field.setDatatype(0);
                                }
                            }else{
                                if (type.equals("DATE") || type.equals("DATETIME")) {
                                    //日期型
                                    field.setDatatype(2);
                                } else if (type.equals("FLOAT") || type.equals("REAL") || type.equals("DOUBLE ")) {
                                    //浮点型
                                    field.setDatatype(3);
                                } else if (type.equals("INTEGER")) {
                                    //整型
                                    field.setDatatype(5);
                                } else if (type.equals("VARCHAR") || type.equals("VARCHAR2") || type.equals("CHAR")) {
                                    //字符串类型
                                    field.setDatatype(6);
                                } else {
                                    field.setDatatype(0);
                                }
                            }

                        }
//                        if (type.contains("(")) {//判断类型中是否存在"（）"，代表设置了字段的长度
//                            String length = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
//                            attributes.setLength(length);
//
//                        } else {//不存在则代表为设置长度，添加为0
//                            attributes.setLength("0");
//                        }

                        //判断是否为外键
                        Element parent_attribute_ref = attProps.element("Parent_Attribute_Ref");
                        if (parent_attribute_ref != null) {
                            String FkName = attProps.elementTextTrim("Name");
                            //添加外键标识
//                            attributes.setFKFlag(true);
//                            field.setFKFlag(true);
                            fknamelist.add(FkName);
//                            ep.setFkNameList(fknamelist);
//                            table.setFkNameList(fknamelist);
                        }

                        /**
                         * 以下是导出为简单xml文件时，没有能确定表联系的标签，只能用外键本身来确定表之间的关系
                         */
                        //表中没有直接的对应父子关系id，则用外键
                        if (child_ref == null && parent_ref == null) {
                            /**
                             * 下面是根据外键来确定表之间的关系
                             */
//                            Relations relationList = new Relations();
                            Relation rel = new Relation();
                            //查看字段中是否存在parent_Relationship_Ref，此字段为外键并查找到两个表之间的关系
                            Element parent_Relationship_Ref = attProps.element("Parent_Relationship_Ref");
                            if (parent_Relationship_Ref != null) {
                                //获取关系值
                                String parent_relationship_ref_content = attProps.elementTextTrim("Parent_Relationship_Ref");
                                //遍历所有的关系标签
                                Iterator<Element> relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
                                while (relationGroup.hasNext()) {
                                    //遍历relationship节点，寻找id与表中parentid相等的节点--->即为两个表之间的关系
                                    Element relationShip = (Element) relationGroup.next();
                                    Element relationProps = relationShip.element("RelationshipProps");

                                    String relationShipId = relationProps.elementTextTrim("Long_Id");
                                    String parent_entity_ref = relationProps.elementTextTrim("Parent_Entity_Ref");
                                    String child_entity_ref = relationProps.elementTextTrim("Child_Entity_Ref");
                                    //关系线的名称
                                    String relationName = relationProps.elementTextTrim("Name");

                                    if (parent_relationship_ref_content.equals(relationShipId)) {
                                        //遍历所有的entity?  找到该字段对应的父表name?
                                        Iterator entityIterator = element1.element("Entity_Groups").elements("Entity").iterator();
                                        while (entityIterator.hasNext()) {
                                            Element entity2 = (Element) entityIterator.next();
                                            String entityID = entity2.attributeValue("id");
//                                       String childtableName = entity2.attributeValue("name");
                                            String fathertableName = entity2.attributeValue("name");
                                            if (parent_entity_ref.equals(entityID)) {

                                                String Parent_To_Child_Verb_Phrase = relationProps.elementTextTrim("Parent_To_Child_Verb_Phrase");


//                                                relationList.setFatherTableName(fathertableName);
//                                                relationList.setChildTableName(ep.getName());
//                                                relationList.setChildTableName(table.getPhysicalTableName());
//                                                relationList.setRelation(Parent_To_Child_Verb_Phrase);
//                                                relationList.setRelationName(relationName);
                                                //当前表
                                                rel.setSrctable(table.getPhysicalTableName());
                                                //目标表
                                                rel.setTargettable(fathertableName);
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
//                                relationsList.add(relationList);
                                relationList.add(rel);
                            }
                        }
                        /**
                         *  判断主键
                         */
                        List<String> fields = new ArrayList<String>();
                        //获取到每个表的主键
                        Iterator<Element> key = entity.element("Key_Group_Groups").elements("Key_Group").iterator();
                        while (key.hasNext()) {
                            Element key1   = (Element) key.next();
                            Key key2 = new Key();
                            //此处加入判断是否为外键根据Key_GroupProps 标签的Key_Group_Type是否为pk
                            Element key_groups = key1.element("Key_GroupProps");
                            //取得键的类型是否为主键PK
                            String pk = key_groups.elementTextTrim("Key_Group_Type");
                            if (pk.equals("PK")&& pk!=null) {
                                System.out.println(key_groups.elementTextTrim("Key_Group_Type"));

                                Element iskey = key1.element("Key_Group_Member_Groups");
                                if(iskey != null){
                                    //获取到主键的id，name
                                    Element PKid = (Element) key1.element("Key_Group_Member_Groups").element("Key_Group_Member")
                                            .element("Key_Group_MemberProps");
                                    //获取到Key_Group_MemberProps里的Attribute_Ref 值与product_id 的id值进行比较如果相等就为主键
                                    String refId = PKid.elementTextTrim("Attribute_Ref");
                                    if (refId.equals(pkid)) {//比较成功此时的Kid.elementTextTrim("Name")获取的名称就是主键名
//                                        field.setIskey(true);
                                        //存入主键名称
//                                        key2.setName(PKid.elementTextTrim("Name"));
                                        //主键字段
                                        fields.add(PKid.elementTextTrim("User_Formatted_Physical_Name"));
                                        key2.setFields(fields);
                                        keys.add(key2);

                                    }
                                }

                                 table.setKeys(keys);
                            }
                        }
                        fieldList.add(field);
                    }
                }

                /**
                 * 判断本节点是否作为父节点
                 */
                if (parent_ref != null) {
                    //遍历自身为父节点的id
                    Iterator<Element> relations = entityProps.element("Parent_Relationships_Ref_Array").elements("Parent_Relationships_Ref").iterator();
                    while (relations.hasNext()) {
                        Element relation = (Element) relations.next();
                        //得到关系relations中的id和此时表的名称
                        String tableParentName = entityProps.elementTextTrim("Name");
                        String Parent_Relationships_Ref = relation.getText();

//                        Relations relationList = new Relations();
                        Relation rel = new Relation();
                        Iterator<Element> relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
                        while (relationGroup.hasNext()) {
                            //遍历relationship节点，寻找id与表中parentid相等的节点--->即为两个表之间的关系
                            Element relationShip = (Element) relationGroup.next();
                            Element relationProps = relationShip.element("RelationshipProps");

                            String relationShipId = relationProps.elementTextTrim("Long_Id");
                            String parent_entity_ref = relationProps.elementTextTrim("Parent_Entity_Ref");

                            if (Parent_Relationships_Ref.equals(relationShipId) && parent_entity_ref.equals(enetityId)) {
                                //子表的id
                                String child_entity_ref = relationProps.elementTextTrim("Child_Entity_Ref");
                                //遍历所有的entity?  找到对应的子表name?
                                Iterator<Element> entityIterator = element1.element("Entity_Groups").elements("Entity").iterator();
                                while (entityIterator.hasNext()) {
                                    Element entity2 = (Element) entityIterator.next();
                                    String entityID = entity2.attributeValue("id");
                                    if (child_entity_ref.equals(entityID)) {

                                        String childtableName = entity2.element("EntityProps").elementTextTrim("User_Formatted_Physical_Name");
                                        System.out.println("子表为--->" + childtableName);

//                                        relationList.setChildTableName(childtableName);
//                                        relationList.setFatherTableName(tableParentName);
//                                        relationList.setCurrentTableName(tableParentName);
                                        //当前表
                                        rel.setSrctable(tableParentName);
                                        //目标表（子表）
                                        rel.setTargettable(childtableName);
                                        //遍历子表的字段
                                        Iterator<Element> childIterator = entity2.element("Attribute_Groups").elements("Attribute").iterator();
                                        while(childIterator.hasNext()){
                                            Map<String, String> fieldmap = new HashMap<String, String>();

                                            Element attribute1 = childIterator.next();
                                             Element attributeProps = attribute1.element("AttributeProps");
                                            //判断子表中的字段是否有为外键
                                            Element parent_Attribute_Ref = attributeProps.element("Parent_Attribute_Ref");
                                            if(parent_Attribute_Ref !=null){
                                                String ChildColumnAttributeRef  = attributeProps.elementTextTrim("Parent_Attribute_Ref");
                                                String colId = ChildColumnAttributeRef.substring(ChildColumnAttributeRef.indexOf("{") + 1, ChildColumnAttributeRef.indexOf("}"));
                                                String ChildColumnName = attributeProps.elementTextTrim("User_Formatted_Physical_Name");

                                                for(int i=0;i<fieldList.size();i++){
                                                    if(fieldList.get(i).getID().equals(colId)){
                                                        fieldmap.put(fieldList.get(i).getName(),ChildColumnName);
                                                        rel.setFieldmap(fieldmap);
                                                    }
                                                }
                                            }else {
                                                continue;
                                            }
                                        }

                                        //关系线的名称
                                        String relationName = relationProps.elementTextTrim("Name");
//                                        relationList.setRelationName(relationName);
                                        //Formatted 名称
                                        String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
//                                        relationList.setRelation(relationFormatted);
                                        relationList.add(rel);
//                                        relationsList.add(relationList);
                                        break;
                                    }
                                }
                                break;
                            }

                        }
                    }
                }

                /**
                 *  判断本节点是否作为子节点
                 */
                if (child_ref != null) {
                    //若节点为子节点，遍历子节点.与父节点相反
                    Iterator<Element> relationsChild = entityProps.element("Child_Relationships_Ref_Array").elements("Child_Relationships_Ref").iterator();
                    while (relationsChild.hasNext()) {
                        Element realtionChild = (Element) relationsChild.next();
//                        Relations relationList = new Relations();
                        Relation rel = new Relation();
                        //enetity中的child_Relationships_Ref
                        String child_Relationships_Ref = realtionChild.getText();
                        String childTableName = entityProps.elementTextTrim("name");

                        Iterator<Element> relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
                        while (relationGroup.hasNext()) {
                            Element relationShip = (Element) relationGroup.next();
                            Element relationProps = relationShip.element("RelationshipProps");

                            String relationShipId = relationProps.elementTextTrim("Long_Id");
                            String child_entity_ref = relationProps.elementTextTrim("Child_Entity_Ref");

                            if (child_Relationships_Ref.equals(relationShipId) && child_entity_ref.equals(enetityId)) {
                                //父表的id
                                String father_entity_ref = relationProps.elementTextTrim("Parent_Entity_Ref");

                                Iterator<Element> entityIterator = element1.element("Entity_Groups").elements("Entity").iterator();
                                while (entityIterator.hasNext()) {
                                    Element entity2 = (Element) entityIterator.next();
                                    String entityID = entity2.attributeValue("id");
                                    if (father_entity_ref.equals(entityID)) {

                                        String fathertableName = entity2.attributeValue("name");
                                        System.out.println("父表为--->" + fathertableName);

//                                        relationList.setFatherTableName(fathertableName);
//                                        relationList.setChildTableName(ep.getName());
//                                        relationList.setChildTableName(table.getPhysicalTableName());
//                                        relationList.setCurrentTableName(ep.getName());
//                                        relationList.setCurrentTableName(table.getPhysicalTableName());
                                        rel.setTargettable(fathertableName);
                                        rel.setSrctable(table.getPhysicalTableName());


                                        //遍历父表的字段
                                        Iterator<Element> childIterator = entity2.element("Attribute_Groups").elements("Attribute").iterator();
                                        while(childIterator.hasNext()){
                                            Map<String, String> fieldmap = new HashMap<String, String>();
                                            Element attribute1 = childIterator.next();
                                            Element attributeProps = attribute1.element("AttributeProps");
                                            String attributeId = attributeProps.elementTextTrim("Long_Id");
                                            String attributeName = attributeProps.elementTextTrim("User_Formatted_Physical_Name");
                                            //获取父表的主键
                                            Iterator<Element> key = entity2.element("Key_Group_Groups").elements("Key_Group").iterator();
                                            while(key.hasNext()){
                                                Element key1 = (Element) key.next();
                                                Element key_groups = key1.element("Key_GroupProps");
                                                //取得键的类型是否为主键PK
                                                String pk = key_groups.elementTextTrim("Key_Group_Type");
                                                if (pk.equals("PK")) {
                                                    //获取到主键的id，name
                                                    Element PKid = (Element) key1.element("Key_Group_Member_Groups").element("Key_Group_Member")
                                                            .element("Key_Group_MemberProps");

                                                    //获取到Key_Group_MemberProps里的Attribute_Ref 值与product_id 的id值进行比较如果相等就为主键
                                                    String refId = PKid.elementTextTrim("Attribute_Ref");
                                                    if (refId.equals(attributeId)) {//比较成功此时的Kid.elementTextTrim("Name")获取的名称就是主键名
                                                       String relatinoColumnName = PKid.elementTextTrim("User_Formatted_Physical_Name");
                                                       for(int i=0;i<fieldList.size();i++){
                                                           if(fieldList.get(i).getName().equals(attributeName)){
                                                               fieldmap.put(fieldList.get(i).getName(),relatinoColumnName);
                                                               rel.setFieldmap(fieldmap);
                                                           }
                                                       }
                                                    }
                                                }
                                            }
                                        }

                                        //关系线的名称
                                        String relationName = relationProps.elementTextTrim("Name");
//                                        relationList.setRelationName(relationName);
                                        //Formatted 名称
                                        String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
//                                        relationList.setRelation(relationFormatted);
                                        break;
                                    }
                                }
                                Element subtype = element1.element("Subtype_Symbol_Groups");
                                if(subtype !=null) {
                                    //没有对应的entity id则为sub-category关系
                                    Iterator<Element> Subtype_Symbol_Groups = element1.element("Subtype_Symbol_Groups").elements("Subtype_Symbol").iterator();
                                    while (Subtype_Symbol_Groups.hasNext()) {
                                        Element Subtype_Symbol = (Element) Subtype_Symbol_Groups.next();
                                        String subtype_symbol_id = Subtype_Symbol.attributeValue("id");
                                        if (subtype_symbol_id.equals(father_entity_ref)) {
                                            String fatherName = Subtype_Symbol.attributeValue("User_Formatted_Physical_Name");
//                                        relationList.setFatherTableName(fatherName);
                                            //当前表
                                            rel.setTargettable(fatherName);
//                                        relationList.setCurrentTableName(ep.getName());
//                                        relationList.setCurrentTableName(table.getPhysicalTableName());
                                            //目标表
                                            rel.setSrctable(table.getPhysicalTableName());
//                                        relationList.setChildTableName(ep.getName());
//                                        relationList.setChildTableName(table.getPhysicalTableName());
                                            //关系线的名称
                                            String relationName = relationProps.elementTextTrim("Name");
//                                        relationList.setRelationName(relationName);
                                            //Formatted 名称
                                            String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
//                                        relationList.setRelation(relationFormatted);
                                            break;
                                        }
                                    }
                                }
//                                relationsList.add(relationList);
                                relationList.add(rel);
                                break;
                            }
                        }
                    }
                }

                table.setFields(fieldList);
                table.setRelations(relationList);
                tables.add(table);
                root.setTables(tables);
            }
        }
//        table.setTables(tables);
//        tablesList.add(table);
//        roots.setTables(table);
//        rootList.add(roots)
        return root;
    }
}

