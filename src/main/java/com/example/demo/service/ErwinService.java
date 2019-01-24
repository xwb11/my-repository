package com.example.demo.service;

import com.example.demo.entity.Attribute;
import com.example.demo.entity.EntityProps;
import com.example.demo.entity.References;
import com.example.demo.entity.Relations;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ErwinService
 * @Description erwin 解析文件service类
 * @Author xwb
 * @Date 2019/1/22 11:15
 * @Version 1.0
 **/
@Service
public class ErwinService {
    /*
     * @Author xwb
     * @Description //TODO 获取表关系和内容方法
     * @Date  2019/1/22
     * @Param
     * @return
     **/
    public  EntityProps[] ErWin(String filePath) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(filePath));//读取xml文档

        Element root = document.getRootElement();//得到xml文档根节点元素
        System.out.println("输出根节点元素的名字===" + root.getName());

        //循环遍历出更节点下面的子节点
        Iterator it1 = root.selectNodes("//EMX:Model").iterator();

        Attribute attributes = null;
        Attribute cols[] = new Attribute[]{};

        List<EntityProps> tables = new ArrayList<>();
        EntityProps ep = null;
        EntityProps eps[] = new EntityProps[]{};

        Relations rels[] = new Relations[]{};

        while (it1.hasNext()) {
            Element element1 = (Element) it1.next();
            Iterator iterator = element1.element("Entity_Groups").elements("Entity").iterator();
            while (iterator.hasNext()) {//遍历获取到所有的表
                ep = new EntityProps();

                List<Relations> relationsList = new ArrayList<>();

                List fknamelist = new ArrayList();
                List<Attribute> columns = new ArrayList<>();
                //得到节点entity
                Element entity = (Element) iterator.next();

                //获取到表的名称和id
                Element entityProps = entity.element("EntityProps");
                System.out.println(entityProps.elementTextTrim("Name"));
                System.out.println(entityProps.elementTextTrim("Long_Id"));
                String enetityId = entityProps.elementTextTrim("Long_Id");

                ep.setId(entity.attributeValue("id"));
                ep.setName(entity.attributeValue("name"));


                Element parent_ref = entityProps.element("Parent_Relationships_Ref_Array");
                Element child_ref = entityProps.element("Child_Relationships_Ref_Array");

                //判断本节点是否有为父节点
                if (parent_ref != null) {
                    //遍历自身为父节点的id
                    Iterator relations = entityProps.element("Parent_Relationships_Ref_Array").elements("Parent_Relationships_Ref").iterator();
                    while (relations.hasNext()) {
                        Element relation = (Element) relations.next();
                        //得到关系relations中的id和此时表的名称
                        String tableParentName = entityProps.elementTextTrim("Name");
                        String Parent_Relationships_Ref = relation.getText();
                        Relations relationList = new Relations();
                        Iterator relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
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
                                Iterator entityIterator = element1.element("Entity_Groups").elements("Entity").iterator();
                                while (entityIterator.hasNext()) {
                                    Element entity2 = (Element) entityIterator.next();
                                    String entityID = entity2.attributeValue("id");
                                    if (child_entity_ref.equals(entityID)) {

                                        String childtableName = entity2.attributeValue("name");
                                        System.out.println("子表为--->" + childtableName);

                                        relationList.setChildTableName(childtableName);
                                        relationList.setFatherTableName(tableParentName);
                                        relationList.setCurrentTableName(tableParentName);
                                        //关系线的名称
                                        String relationName = relationProps.elementTextTrim("Name");
                                        relationList.setRelationName(relationName);
                                        //Formatted 名称
                                        String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
                                        relationList.setRelation(relationFormatted);
                                        relationsList.add(relationList);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                //判断本节点是否有为子节点
                if (child_ref != null) {
                    //若节点为子节点，遍历子节点.与父节点相反
                    Iterator relationsChild = entityProps.element("Child_Relationships_Ref_Array").elements("Child_Relationships_Ref").iterator();
                    while (relationsChild.hasNext()) {
                        Element realtionChild = (Element) relationsChild.next();
                        Relations relationList = new Relations();
                        //enetity中的child_Relationships_Ref
                        String child_Relationships_Ref = realtionChild.getText();
                        String childTableName = entityProps.elementTextTrim("name");

                        Iterator relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
                        while (relationGroup.hasNext()) {
                            Element relationShip = (Element) relationGroup.next();
                            Element relationProps = relationShip.element("RelationshipProps");

                            String relationShipId = relationProps.elementTextTrim("Long_Id");
                            String child_entity_ref = relationProps.elementTextTrim("Child_Entity_Ref");

                            if (child_Relationships_Ref.equals(relationShipId) && child_entity_ref.equals(enetityId)) {
                                //父表的id
                                String father_entity_ref = relationProps.elementTextTrim("Parent_Entity_Ref");

                                Iterator entityIterator = element1.element("Entity_Groups").elements("Entity").iterator();
                                while (entityIterator.hasNext()) {
                                    Element entity2 = (Element) entityIterator.next();
                                    String entityID = entity2.attributeValue("id");
                                    if (father_entity_ref.equals(entityID)) {

                                        String fathertableName = entity2.attributeValue("name");
                                        System.out.println("父表为--->" + fathertableName);

                                        relationList.setFatherTableName(fathertableName);
                                        relationList.setChildTableName(ep.getName());
                                        relationList.setCurrentTableName(ep.getName());
                                        //关系线的名称
                                        String relationName = relationProps.elementTextTrim("Name");
                                        relationList.setRelationName(relationName);
                                        //Formatted 名称
                                        String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
                                        relationList.setRelation(relationFormatted);
                                        break;
                                    }
                                }
                                //没有对应的entity id则为sub-category关系
                                Iterator Subtype_Symbol_Groups = element1.element("Subtype_Symbol_Groups").elements("Subtype_Symbol").iterator();
                                while (Subtype_Symbol_Groups.hasNext()) {
                                    Element Subtype_Symbol = (Element) Subtype_Symbol_Groups.next();
                                    String subtype_symbol_id = Subtype_Symbol.attributeValue("id");
                                    if (subtype_symbol_id.equals(father_entity_ref)) {
                                        String fatherName = Subtype_Symbol.attributeValue("name");
                                        relationList.setFatherTableName(fatherName);

                                        relationList.setCurrentTableName(ep.getName());
                                        relationList.setChildTableName(ep.getName());
                                        //关系线的名称
                                        String relationName = relationProps.elementTextTrim("Name");
                                        relationList.setRelationName(relationName);
                                        //Formatted 名称
                                        String relationFormatted = relationProps.elementTextTrim("User_Formatted_Name");
                                        relationList.setRelation(relationFormatted);
                                        break;
                                    }
                                }
                                relationsList.add(relationList);
                                break;
                            }
                        }
                    }
                }
                //获取表字段
//               Iterator attribute = entity.element("Attribute_Groups").elements("Attribute").iterator();
                Iterator attribute = entity.elements("Attribute_Groups").iterator();
                while (attribute.hasNext()) {//遍历表中字段

                    //得到attribute下的属性（表字段的属性）
                    Element att = (Element) attribute.next();
                    //得到attribute下的AttributeProps属性值
                    Iterator att1 = att.elements("Attribute").iterator();
                    while (att1.hasNext()) {
                        attributes = new Attribute();
                        Element att2 = (Element) att1.next();
                        Element attProps = att2.element("AttributeProps");

                        String pkid = attProps.elementTextTrim("Long_Id");
                        //字段id
                        attributes.setAttributeId(attProps.elementTextTrim("Long_Id"));
                        //字段名称
                        attributes.setAttributeName(attProps.elementTextTrim("Name"));
                        //字段物理名称(英文字母)
                        attributes.setPhysicalName(attProps.elementTextTrim("User_Formatted_Physical_Name"));
                        //字段类型
                        attributes.setType(attProps.elementTextTrim("Physical_Data_Type"));
                        String type = attProps.elementTextTrim("Physical_Data_Type");

                        if (type.contains("(")) {//判断类型中是否存在"（）"，代表设置了字段的长度
                            String length = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
                            attributes.setLength(length);
                        } else {//不存在则代表为设置长度，添加为0
                            attributes.setLength("0");
                        }

                        //判断是否为外键
                        Element parent_attribute_ref = attProps.element("Parent_Attribute_Ref");
                        if (parent_attribute_ref != null) {
                            String FkName = attProps.elementTextTrim("Name");
                            //添加外键标识
                            attributes.setFKFlag(true);
                            fknamelist.add(FkName);
                            ep.setFkNameList(fknamelist);
                        }

                        /**
                         * 以下是导出为简单xml文件时，没有能确定表联系的标签，只能用外键本身来确定表之间的关系
                         */
                        //表中没有直接的对应父子关系id，则用外键
                        if (child_ref == null && parent_ref == null) {
                            /**
                             * 下面是根据外键来确定表之间的关系
                             */
                            Relations relationList = new Relations();
                            //查看字段中是否存在parent_Relationship_Ref，此字段为外键并查找到两个表之间的关系
                            Element parent_Relationship_Ref = attProps.element("Parent_Relationship_Ref");
                            if (parent_Relationship_Ref != null) {
                                //获取关系值
                                String parent_relationship_ref_content = attProps.elementTextTrim("Parent_Relationship_Ref");
                                //遍历所有的关系标签
                                Iterator relationGroup = element1.element("Relationship_Groups").elements("Relationship").iterator();
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
                                                System.out.println(fathertableName + "-" + Parent_To_Child_Verb_Phrase + "-" + ep.getName());

                                                relationList.setFatherTableName(fathertableName);
                                                relationList.setChildTableName(ep.getName());
                                                relationList.setRelation(Parent_To_Child_Verb_Phrase);
                                                relationList.setRelationName(relationName);
                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                                relationsList.add(relationList);
                            }
                        }

                        //判断主键是否已检索过
                        if (ep.getPkName() == null) {

                            //获取到每个表的主键
                            Iterator key = entity.element("Key_Group_Groups").elements("Key_Group").iterator();
                            while (key.hasNext()) {
                                Element key1 = (Element) key.next();

                                //此处加入判断是否为外键根据Key_GroupProps 标签的Key_Group_Type是否为pk
                                Element key_groups = key1.element("Key_GroupProps");
                                //取得键的类型是否为主键PK
                                String pk = key_groups.elementTextTrim("Key_Group_Type");
                                if (pk.equals("PK")) {
                                    System.out.println(key_groups.elementTextTrim("Key_Group_Type"));

                                    //获取到主键的id，name
                                    Element PKid = (Element) key1.element("Key_Group_Member_Groups").element("Key_Group_Member")
                                            .element("Key_Group_MemberProps");

                                    //获取到Key_Group_MemberProps里的Attribute_Ref 值与product_id 的id值进行比较如果相等就为主键
                                    String refId = PKid.elementTextTrim("Attribute_Ref");
                                    if (refId.equals(pkid)) {//比较成功此时的Kid.elementTextTrim("Name")获取的名称就是主键名
                                        attributes.setPkFlag(true);
                                        ep.setPkName(PKid.elementTextTrim("User_Formatted_Physical_Name"));
                                    }
                                    System.out.println(PKid.elementTextTrim("Name"));
                                    System.out.println(PKid.elementTextTrim("Attribute_Ref"));
                                }
                            }
                        }
                        columns.add(attributes);
                        System.out.println(columns);
                        System.out.println("================");
                    }
                }
                ep.setAttributes(columns.toArray(cols));
                ep.setRelations(relationsList.toArray(rels));
                tables.add(ep);
                System.out.println(ep);
                System.out.println("==============");
            }
        }
        return tables.toArray(eps);
    }

    /*
     * @Author xwb
     * @Description //TODO 查找表内容
     * @Date  2019/1/22
     * @Param
     * @return
     **/
    public EntityProps[] parsePom(String filePath) {
        EntityProps[] tabs = new EntityProps[]{};
        List<EntityProps> voS = new ArrayList<EntityProps>();
        EntityProps vo = null;
        Attribute[] cols = null;
        File f = new File(filePath);
        SAXReader sr = new SAXReader();
        Document doc = null;

        List<Relations> references = new ArrayList<>();
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
            vo = new EntityProps();
            cols = new Attribute[]{};
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
            vo.setAttributes(list.toArray(cols));
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
            while(itr.hasNext()){//遍历所有的表
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
