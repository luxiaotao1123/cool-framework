package com.core.generators;

import com.core.common.Cools;
import com.core.generators.domain.Column;
import com.core.generators.utils.GeneratorUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 2019-06-18
 */
public class CoolGenerator {

    private static final String BASE_DIR = "src/main/";
    private static final String JAVA_DIR = BASE_DIR + "java/";
    private static final String XML_DIR = BASE_DIR + "resources/mapper/";
    private static final String HTML_DIR = BASE_DIR + "webapp/";
    private static final String[] ALL_TEMPLATES = new String[]{
            "Controller",
            "Service",
            "ServiceImpl",
            "Mapper",
            "Entity",
            "Xml",
            "Html",
            "Js"};

    public String url;
    public String username;
    public String password;
    public String table;
    public String packagePath;
    public boolean controller = true;
    public boolean service = true;
    public boolean mapper = true;
    public boolean entity = true;
    public boolean xml = true;
    public boolean html = true;
    public boolean js = true;

    private List<Column> columns = new ArrayList<>();
    private String fullEntityName;
    private String simpleEntityName;
    private String entityImport;
    private String entityContent;
    private String xmlContent;
    private String htmlContent;
    private String jsTableContent;
    private String jsDetailContent;

    public void build() throws Exception {
        init();
        for (String template : ALL_TEMPLATES){
            boolean pass = false;
            String lowerCase = template.toLowerCase();
            String templatePath = lowerCase.contains("impl")?lowerCase.substring(0,lowerCase.length()-4)+"/"+lowerCase.substring(lowerCase.length()-4):lowerCase;
            String directory="";
            String fileName="";
            switch (template){
                case "Controller":
                    pass = controller;
                    directory = JAVA_DIR + packagePath.replace(".", "/")+"/"+templatePath+"/";
                    fileName = fullEntityName+template+".java";
                    break;
                case "Service":
                    pass = service;
                    directory = JAVA_DIR + packagePath.replace(".", "/")+"/"+templatePath+"/";
                    fileName = fullEntityName+template+".java";
                    break;
                case "ServiceImpl":
                    pass = service;
                    directory = JAVA_DIR + packagePath.replace(".", "/")+"/"+templatePath+"/";
                    fileName = fullEntityName+template+".java";
                    break;
                case "Mapper":
                    pass = mapper;
                    directory = JAVA_DIR + packagePath.replace(".", "/")+"/"+templatePath+"/";
                    fileName = fullEntityName+template+".java";
                    break;
                case "Entity":
                    pass = entity;
                    directory = JAVA_DIR + packagePath.replace(".", "/")+"/"+templatePath+"/";
                    fileName = fullEntityName+".java";
                    break;
                case "Xml":
                    pass = xml;
                    directory = XML_DIR;
                    fileName = fullEntityName+"Mapper.xml";
                    break;
                case "Html":
                    pass = html;
                    directory = HTML_DIR + "/view/" + simpleEntityName + "/";
                    fileName = simpleEntityName+".html";
                    break;
                case "Js":
                    pass = js;
                    directory = HTML_DIR + "/static/js/" + simpleEntityName + "/";
                    fileName = simpleEntityName+".js";
                    break;
                default:
                    break;
            }
            if (!pass){ continue; }
            String content = readFile(template);
            writeFile(content, directory, fileName, template);
        }
    }

    private void init() throws Exception {
        gainDbInfo();
        fullEntityName = GeneratorUtils.getNameSpace(table);
        simpleEntityName = fullEntityName.substring(0, 1).toLowerCase()+fullEntityName.substring(1);
        entityContent = createEntityMsg();
        xmlContent = createXmlMsg();
        htmlContent = createHtmlMsg();
        jsTableContent = createJsTableMsg();
        jsDetailContent = createJsDetailMsg();
    }

    private String readFile(String template){
        StringBuilder txtContentBuilder=new StringBuilder();
        ClassPathResource classPath=new ClassPathResource("templates/"+template + ".txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPath.getInputStream()))) {
            String lineContent;
            while ((lineContent = reader.readLine()) != null) {
                txtContentBuilder.append(lineContent).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return txtContentBuilder.toString();
    }

    private void writeFile(String content, String directory, String fileName, String template) throws IOException {
        File codeDirectory=new File(directory);
        if(!codeDirectory.exists()){
            codeDirectory.mkdirs();
        }
        File writerFile=new File(directory+fileName);
        if(!writerFile.exists()){
            content=content.
                    replaceAll("@\\{TABLENAME}", table)
                    .replaceAll("@\\{ENTITYIMPORT}", entityImport)
                    .replaceAll("@\\{ENTITYCONTENT}", entityContent)
                    .replaceAll("@\\{ENTITYNAME}", fullEntityName)
                    .replaceAll("@\\{SIMPLEENTITYNAME}", simpleEntityName)
                    .replaceAll("@\\{UENTITYNAME}", simpleEntityName)
                    .replaceAll("@\\{COMPANYNAME}",packagePath)
                    .replaceAll("@\\{XMLCONTENT}", xmlContent)
                    .replaceAll("@\\{HTMLCONTENT}", htmlContent)
                    .replaceAll("@\\{JSTABLECONTENT}", jsTableContent)
                    .replaceAll("@\\{JSDETAILCONTENT}", jsDetailContent)
            ;
            writerFile.createNewFile();
            BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
            writer.write(content);
            writer.flush();
            writer.close();
            System.out.println(fullEntityName+template+" 源文件创建成功！");
        }else{
            System.out.println(fullEntityName+template+" 源文件已经存在创建失败！");
        }
    }

    private void gainDbInfo() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:mysql://"+url, username, password);

        PreparedStatement ps = conn.prepareStatement("select * from " + table);
        ResultSetMetaData meta = ps.executeQuery().getMetaData();
        // 单表字段数量
        int count = meta.getColumnCount();
        ResultSet resultSet = ps.executeQuery("show full columns from " + table);
        for (int i = 1; i < count + 1; i++) {
            String columnName = meta.getColumnName(i);
            if (resultSet.next() && columnName.equals(resultSet.getString("Field"))){
                columns.add(new Column(
                        meta.getColumnName(i),
                        GeneratorUtils.getType(meta.getColumnType(i)),
                        resultSet.getString("Comment"),
                        resultSet.getString("Key").equals("PRI")
                ));
            }
            columns.forEach(column -> System.out.println(column.toString()));
        }
    }






























    /**********************************************************************************************/
    /************************************* Entity动态字段 *******************************************/
    /**********************************************************************************************/

    private String createEntityMsg(){
        if (columns.isEmpty()){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder entityIm = new StringBuilder();
        boolean setTableField = true;
        boolean setTableId = true;
        for (Column column : columns){
            if (column.getType().equals("Date")){
                entityIm.append("import java.util.Date;").append("\n");
            }
            // 主键修饰
            if (column.isPrimaryKey()){
                if (setTableId){
                    entityIm.append("import com.baomidou.mybatisplus.annotations.TableId;").append("\n")
                            .append("import com.baomidou.mybatisplus.enums.IdType;").append("\n");
                    setTableId = false;
                }
                sb.append("    ")
                        .append("@TableId(value = \"")
                        .append(column.getName())
                        .append("\", type = IdType.AUTO)")
                        .append("\n");
            }

            // 命名转换注解
            if (!column.getName().equals(column.getHumpName())){
                if (setTableField){
                    entityIm.append("import com.baomidou.mybatisplus.annotations.TableField;").append("\n");
                    setTableField = false;
                }
                sb.append("    ")
                        .append("@TableField(\"")
                        .append(column.getName())
                        .append("\")")
                        .append("\n");
            }

            // 注释
            if (!Cools.isEmpty(column.getComment())){
                sb.append("    /**\n")
                        .append("     * ")
                        .append(column.getComment())
                        .append("\n")
                        .append("     */")
                        .append("\n");
            }

            sb.append("    ")
                    .append("private ")
                    .append(column.getType())
                    .append(" ")
                    .append(column.getHumpName())
                    .append(";")
                    .append("\n")
                    .append("\n");
        }
        // get set
        for (Column column : columns){
            // get
            sb.append("    ")
                    .append("public ")
                    .append(column.getType())
                    .append(" ")
                    .append("get").append(column.getHumpName().substring(0, 1).toUpperCase()).append(column.getHumpName().substring(1))
                    .append("() {")
                    .append("\n")
                    .append("        return ")
                    .append(column.getHumpName())
                    .append(";\n")
                    .append("    }\n\n");
            // set
            sb.append("    ")
                    .append("public void set")
                    .append(column.getHumpName().substring(0, 1).toUpperCase()).append(column.getHumpName().substring(1))
                    .append("(")
                    .append(column.getType())
                    .append(" ")
                    .append(column.getHumpName())
                    .append(") {\n")
                    .append("        this.")
                    .append(column.getHumpName())
                    .append(" = ")
                    .append(column.getHumpName())
                    .append(";\n")
                    .append("    }\n\n");
        }
        entityImport = entityIm.toString();
        return sb.toString();
    }


    /**********************************************************************************************/
    /*************************************** Xml动态字段 ********************************************/
    /**********************************************************************************************/

    private String createXmlMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            sb.append("        ")
                    .append("<")
                    .append(column.isPrimaryKey()?"id":"result")
                    .append(" column=\"")
                    .append(column.getName())
                    .append("\" property=\"")
                    .append(column.getHumpName())
                    .append("\" />\n");
        }
        return sb.toString();
    }


    /**********************************************************************************************/
    /*************************************** Html动态字段 *******************************************/
    /**********************************************************************************************/

    private String createHtmlMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            sb.append("        <div class=\"layui-form-item\">\n")
                    .append("            <label class=\"layui-form-label\">")
                    .append(column.getComment())
                    .append("</label>\n")
                    .append("            <div class=\"layui-input-block\">\n")
                    .append("                <input id=\"")
                    .append(column.getHumpName())
                    .append("\" class=\"layui-input\" type=\"text\" placeholder=\"")
                    .append(column.getComment())
                    .append("\">\n")
                    .append("            </div>\n")
                    .append("        </div>\n");
        }
        return sb.toString();
    }

    /**********************************************************************************************/
    /**************************************** Js动态字段 ********************************************/
    /**********************************************************************************************/

    private String createJsTableMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            sb.append("            ,{field: '")
                    .append(column.getHumpName())
                    .append("', align: 'center',title: '")
                    .append(column.getComment())
                    .append("'}\n");
        }
        return sb.toString();
    }

    private String createJsDetailMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            sb.append("            ")
                    .append(column.getHumpName())
                    .append(": \\$('#")
                    .append(column.getHumpName())
                    .append("').val(),\n");
        }
        return sb.toString();
    }

}
