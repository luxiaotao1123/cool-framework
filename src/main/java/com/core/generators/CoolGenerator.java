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
            "Mapper"};

    public String url;
    public String username;
    public String password;
    public String table;
    public String packagePath;
    private List<Column> list = new ArrayList<>();
    private boolean controller = true;
    private boolean service = true;
    private boolean mapper = true;
    private String fullEntityName;
    private String simpleEntityName;

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
    }

    private void writerFile(String content, String template) throws IOException {
        String lowerCase = template.toLowerCase();
        String templatePath = lowerCase.contains("impl")?lowerCase.substring(0, lowerCase.length() - 4)+"/"+lowerCase.substring(lowerCase.length() - 4):lowerCase;
        String directory="src/main/java/"+packagePath.replace(".", "/")+"/"+templatePath+"/";
        File codeDirectory=new File(directory);
        if(!codeDirectory.exists()){
            codeDirectory.mkdirs();
        }
        File writerFile=new File(directory+fullEntityName+template+".java");
        if(!writerFile.exists()){
            content=content.
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
                list.add(new Column(meta.getColumnName(i), GeneratorUtils.getType(meta.getColumnType(i)), resultSet.getString("Comment")));
            }
        }
        list.forEach(column -> System.out.println(column.toString()));
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
}
