package com.core.generators;

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

    private static final String[] ALL_TEMPLATES = new String[]{
            "Controller",
            "Service",
            "ServiceImpl",
            "Mapper",
            "Entity"};

    public String url;
    public String username;
    public String password;
    public String table;
    public String packagePath;
    private List<Column> columns = new ArrayList<>();
    private boolean controller = true;
    private boolean service = true;
    private boolean mapper = true;
    private boolean entity = true;
    private String fullEntityName;
    private String simpleEntityName;
    private String entityImport;
    private String entityContent;

    public void build() throws Exception {
        init();
        for (String template : ALL_TEMPLATES){
            boolean pass = false;
            switch (template){
                case "Controller":
                    pass = controller;
                    break;
                case "Service":
                    pass = service;
                    break;
                case "ServiceImpl":
                    pass = service;
                    break;
                case "Mapper":
                    pass = mapper;
                    break;
                case "Entity":
                    pass = entity;
                    break;
                default:
                    break;
            }
            if (!pass){ continue; }
            String content = readFile(template);
            writerFile(content, template);
        }
    }

    private void init() throws Exception {
        gainDbInfo();
        fullEntityName = GeneratorUtils.getNameSpace(table);
        simpleEntityName = fullEntityName.substring(0, 1).toLowerCase()+fullEntityName.substring(1);
        entityContent = createEntityMsg();
    }

    // java entity 字段遍历修饰
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

            // 命名转换
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

    private void writerFile(String content, String template) throws IOException {
        String lowerCase = template.toLowerCase();
        String templatePath = lowerCase.contains("impl")?lowerCase.substring(0,lowerCase.length()-4)+"/"+lowerCase.substring(lowerCase.length()-4):lowerCase;
        String directory="src/main/java/"+packagePath.replace(".", "/")+"/"+templatePath+"/";
        File codeDirectory=new File(directory);
        if(!codeDirectory.exists()){
            codeDirectory.mkdirs();
        }
        if (template.equals("Entity")) template="";
        File writerFile=new File(directory+fullEntityName+template+".java");
        if(!writerFile.exists()){
            content=content.
                    replaceAll("@\\{TABLENAME}", table).
                    replaceAll("@\\{ENTITYIMPORT}", entityImport).
                    replaceAll("@\\{ENTITYCONTENT}", entityContent).
                    replaceAll("@\\{ENTITYNAME}", fullEntityName).			 	//实体
                    replaceAll("@\\{SIMPLEENTITYNAME}", simpleEntityName). 		//实体简写
                    replaceAll("@\\{UENTITYNAME}", simpleEntityName).	//实体大字
                    replaceAll("@\\{COMPANYNAME}",packagePath);	//实体数据表前缀
            writerFile.createNewFile();
            BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
            writer.write(content);
            writer.flush();
            writer.close();
            System.out.println(fullEntityName+template+".java 源文件创建成功！");
        }else{
            System.out.println(fullEntityName+template+".java  源文件已经存在创建失败！");
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
                columns.add(new Column(meta.getColumnName(i),
                        GeneratorUtils.getType(meta.getColumnType(i)),
                        resultSet.getString("Comment"),
                        resultSet.getString("Key").equals("PRI")));
            }
        }
    }


    public void setController(boolean controller) {
        this.controller = controller;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public void setMapper(boolean mapper) {
        this.mapper = mapper;
    }

    public void setEntity(final boolean entity) {
        this.entity = entity;
    }
}
