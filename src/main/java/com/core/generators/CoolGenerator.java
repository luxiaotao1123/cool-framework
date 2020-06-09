package com.core.generators;

import com.core.common.Cools;
import com.core.generators.domain.Column;
import com.core.generators.utils.GeneratorUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            "HtmlDetail",
            "Js",
            "Sql"};

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
    public boolean htmlDetail = true;
    public boolean js = true;
    public boolean sql = true;

    private List<Column> columns = new ArrayList<>();
    private String fullEntityName;
    private String simpleEntityName;
    private String entityImport;
    private String entityContent;
    private String xmlContent;
    private String htmlContent;
    private String htmlDetailContent;
    private String jsTableContent;
    private String jsDetailContent;
    private String jsForeignKeyContent;
    private String jsDateContent;
    private String jsPrimaryKeyDoms;
    private String primaryKeyColumn;
    private String majorColumn;

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
                    directory = HTML_DIR + "/views/" + simpleEntityName + "/";
                    fileName = simpleEntityName+".html";
                    break;
                case "HtmlDetail":
                    pass = htmlDetail;
                    directory = HTML_DIR + "/views/" + simpleEntityName + "/";
                    fileName = simpleEntityName+"_detail.html";
                    break;
                case "Js":
                    pass = js;
                    directory = HTML_DIR + "/static/js/" + simpleEntityName + "/";
                    fileName = simpleEntityName+".js";
                    break;
                case "Sql":
                    pass = sql;
                    directory = JAVA_DIR;
                    fileName = simpleEntityName+".sql";
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
        htmlDetailContent = createHtmlDetailMsg();
        jsTableContent = createJsTableMsg();
        jsDetailContent = createJsDetailMsg();
        jsForeignKeyContent = createJsFkContent();
        jsDateContent = createJsDateContent();
        jsPrimaryKeyDoms = createJsPrimaryKeyMsg();
        primaryKeyColumn = createPrimaryMsg();
        majorColumn = createMajorMsg();
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
                    .replaceAll("@\\{HTMLDETAILCONTENT}", htmlDetailContent)
                    .replaceAll("@\\{JSTABLECONTENT}", jsTableContent)
                    .replaceAll("@\\{JSDETAILCONTENT}", jsDetailContent)
                    .replaceAll("@\\{JSFOREIGNKEYCONTENT}", jsForeignKeyContent)
                    .replaceAll("@\\{JSDATECONTENT}", jsDateContent)
                    .replaceAll("@\\{JSPRIMARYKEYDOMS}", jsPrimaryKeyDoms)
                    .replaceAll("@\\{MAJORCOLUMN}", GeneratorUtils.humpToLine(majorColumn))
                    .replaceAll("@\\{PRIMARYKEYCOLUMN}", GeneratorUtils.firstCharConvert(primaryKeyColumn, false))
                    .replaceAll("@\\{UPCASEMARJORCOLUMN}", GeneratorUtils.firstCharConvert(majorColumn, false))
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
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://"+url, username, password);
//        Class.forName("com.mysql.jdbc.Driver").newInstance();
//        Connection conn = DriverManager.getConnection("jdbc:mysql://"+url, username, password);
        this.columns = getSqlServerColumns(conn, table, true);
    }

    // mysql
    public static List<Column> getMysqlColumns(Connection conn, String table, boolean init) throws Exception {
        List<Column> result = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("select * from " + table);
        ResultSetMetaData meta = ps.executeQuery().getMetaData();
        // 单表字段数量
        int count = meta.getColumnCount();
        ResultSet resultSet = ps.executeQuery("show full columns from " + table);
        for (int i = 1; i < count + 1; i++) {
            String columnName = meta.getColumnName(i);
            if (resultSet.next() && columnName.equals(resultSet.getString("Field"))){
                result.add(new Column(
                        conn,
                        meta.getColumnName(i),
                        GeneratorUtils.getType(meta.getColumnType(i)),
                        resultSet.getString("Comment"),
                        resultSet.getString("Key").equals("PRI"),
                        false,
                        resultSet.getString("Null").equals("NO"),
                        GeneratorUtils.getColumnLength(resultSet.getString("Type")),
                        init
                ));
            }
//            result.forEach(column -> System.out.println(column.toString()));
        }
        return result;
    }

    // sqlserver
    public static List<Column> getSqlServerColumns(Connection conn, String table, boolean init) throws Exception {
        List<Column> result = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("select * from " + table);
        ResultSetMetaData meta = ps.executeQuery().getMetaData();
        // 单表字段数量
        int count = meta.getColumnCount();
        StringBuilder sql = new StringBuilder("SELECT \n" +
                "       'Field'= a.name,\n" +
                "       'Comment'= isnull(g.[value],''),\n" +
                "       'Key'= case when COLUMNPROPERTY( a.id,a.name,'IsIdentity')=1 then 'PRI' else '' end,\n" +
                "       'Main'= case when exists(SELECT 1 FROM sysobjects where xtype='PK' and parent_obj=a.id and name in (SELECT name FROM sysindexes WHERE indid in( SELECT indid FROM sysindexkeys WHERE id = a.id AND colid=a.colid))) then 'PRI' else '' end,"+
                "       'Type'= b.name,\n" +
                "       'Length'= COLUMNPROPERTY(a.id,a.name,'PRECISION'),\n" +
                "       'Decimals'= isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0),\n" +
                "       'Null'= case when a.isnullable=1 then 'Yes' else 'No' end,\n" +
                "       'Default' = isnull(e.text,'')\n" +
                "FROM  syscolumns a\n" +
                "LEFT JOIN  systypes b on a.xusertype=b.xusertype\n" +
                "INNER JOIN sysobjects d on  a.id=d.id  and d.xtype='U' and  d.name<>'dtproperties'\n" +
                "LEFT JOIN  syscomments e on  a.cdefault=e.id\n" +
                "LEFT JOIN  sys.extended_properties g on  a.id=G.major_id and a.colid=g.minor_id  \n" +
                "LEFT JOIN  sys.extended_properties f on  d.id=f.major_id and f.minor_id=0 where d.name = '")
                .append(table).append("' ORDER BY a.colorder ASC");
        ResultSet resultSet = conn.prepareStatement(sql.toString()).executeQuery();
        for (int i = 1; i < count + 1; i++) {
            String columnName = meta.getColumnName(i);
            if (resultSet.next() && columnName.equals(resultSet.getString("Field"))){
                result.add(new Column(
                        conn,
                        meta.getColumnName(i),
                        GeneratorUtils.getType(meta.getColumnType(i)),
                        resultSet.getString("Comment"),
                        resultSet.getString("Key").equals("PRI"),
                        resultSet.getString("Main").equals("PRI"),
                        resultSet.getString("Null").equals("No"),
                        GeneratorUtils.getColumnLength(resultSet.getString("Type")),
                        init
                ));
            }
        }
        result.forEach(column -> System.out.println(column.toString()));
        return result;
    }






























    /**********************************************************************************************/
    /************************************* Entity动态字段 *******************************************/
    /**********************************************************************************************/

    private String createEntityMsg(){
        if (columns.isEmpty()){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder entityIm = new StringBuilder("import com.core.common.Cools;");
        boolean setTableField = true;
        boolean setTableId = true;
        for (Column column : columns){
            if (column.getType().equals("Date")){
                entityIm.append("import java.text.SimpleDateFormat;\n")
                        .append("import java.util.Date;\n");
            }

            // 注释
            if (!Cools.isEmpty(column.getComment())){
                sb.append("    /**\n")
                        .append("     * ")
                        .append(column.getWholeComment())
                        .append("\n")
                        .append("     */")
                        .append("\n");
            }

            // swagger
            entityIm.append("import io.swagger.annotations.ApiModelProperty;\n");
            sb.append("    @ApiModelProperty(value= \"")
                    .append(column.getWholeComment())
                    .append("\")\n");


            // 主键修饰
            if (column.isPrimaryKey()){
                if (setTableId){
                    entityIm.append("import com.baomidou.mybatisplus.annotations.TableId;").append("\n")
                            .append("import com.baomidou.mybatisplus.enums.IdType;").append("\n");
                    setTableId = false;
                }
                if (column.isOnly()){
                    sb.append("    ")
                            .append("@TableId(value = \"")
                            .append(column.getName())
                            .append("\", type = IdType.AUTO)")
                            .append("\n");
                } else {
                    sb.append("    ")
                            .append("@TableId(value = \"")
                            .append(column.getName())
                            .append("\", type = IdType.INPUT)")
                            .append("\n");
                }

            }

            // 外键修饰
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                entityIm.append("import com.core.common.SpringUtils;\n")
                        .append("import ").append(packagePath).append(".service.").append(column.getForeignKey()).append("Service;\n");
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

            sb.append("    ")
                    .append("private ")
                    .append(column.getType())
                    .append(" ")
                    .append(column.getHumpName())
                    .append(";")
                    .append("\n")
                    .append("\n");
        }

        // default constructor
        sb.append("    public ").append(fullEntityName).append("() {}\n\n");
        // full constructor
        sb.append("    public ").append(fullEntityName).append("(");
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            sb.append(column.getType()).append(" ").append(column.getHumpName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(") {\n");
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            sb.append("        this.").append(column.getHumpName()).append(" = ").append(column.getHumpName()).append(";\n");
        }
        sb.append("    }\n\n");
        // constructor tips
        sb.append("//    ").append(fullEntityName).append(" ").append(simpleEntityName).append(" = new ").append(fullEntityName).append("(\n");
        for (int i = 0; i<columns.size(); i++) {
            if (columns.get(i).isPrimaryKey()){ continue;}
            sb.append("//            null");
            if (i < columns.size()-1){
                sb.append(",");
            }
            sb.append("    // ").append(columns.get(i).getComment()).append(columns.get(i).isNotNull()?"[非空]":"");
            if (i < columns.size()-1){
                sb.append("\n");
            }
        }
        sb.append("\n//    );\n\n");

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
            // 时间字段增加$格式化
            if ("Date".equals(column.getType())){
                sb.append("    public String get")
                        .append(column.getHumpName().substring(0, 1).toUpperCase()).append(column.getHumpName().substring(1))
                        .append("\\$")
                        .append("(){\n")
                        .append("        if (Cools.isEmpty(this.").append(column.getHumpName()).append(")){\n")
                        .append("            return \"\";\n")
                        .append("        }\n")
                        .append("        return new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").format(this.")
                        .append(column.getHumpName())
                        .append(");\n")
                        .append("    }\n\n");
            // 枚举字段增加$格式化
            } else if (!Cools.isEmpty(column.getEnums())){
                sb.append("    public String get")
                        .append(column.getHumpName().substring(0, 1).toUpperCase()).append(column.getHumpName().substring(1))
                        .append("\\$")
                        .append("(){\n")
                        .append("        if (null == this.").append(column.getHumpName()).append("){ return null; }\n")
                        .append("        switch (this.").append(column.getHumpName()).append("){\n");
                for (Map<String, Object> map : column.getEnums()){
                    for (Map.Entry<String, Object> entry : map.entrySet()){
                        sb.append("            case ").append(entry.getKey()).append(":\n")
                                .append("                return \"").append(entry.getValue()).append("\";\n");
                    }
                }
                sb.append("            default:\n")
                        .append("                return String.valueOf(this.").append(column.getHumpName()).append(");\n")
                        .append("        }\n")
                        .append("    }\n\n");
            }

            // 外键修饰
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                sb.append("    public String get").append(column.getHumpName().substring(0, 1).toUpperCase()).append(column.getHumpName().substring(1)).append("\\$").append("(){\n")
                        .append("        ").append(column.getForeignKey()).append("Service service = SpringUtils.getBean(").append(column.getForeignKey()).append("Service.class);\n")
                        .append("        ").append(column.getForeignKey()).append(" ").append(GeneratorUtils.firstCharConvert(column.getForeignKey()))
                        .append(" = service.selectById(this.").append(column.getHumpName()).append(");\n")
                        .append("        if (!Cools.isEmpty(").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append(")){\n")
                        .append("            return String.valueOf(").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append(".get").append(column.getForeignKeyMajor()).append("());\n")
                        .append("        }\n")
                        .append("        return null;\n")
                        .append("    }\n\n");
            }

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
    /*********************************** Controller动态字段 *****************************************/
    /**********************************************************************************************/

    private String createPrimaryMsg(){
        String defaultMajor = "id";
        for (Column column: columns){
            if (column.isPrimaryKey()){
                defaultMajor = column.getHumpName();
            }
        }
        return defaultMajor;
    }

    private String createMajorMsg(){
        String defaultMajor = "id";
        for (Column column: columns){
            if (column.isPrimaryKey()){
                defaultMajor = column.getHumpName();
            }
            if (column.isMajor()){
                return column.getHumpName();
            }
        }
        return defaultMajor;
    }

    /**********************************************************************************************/
    /*************************************** Xml动态字段 ********************************************/
    /**********************************************************************************************/

    private String createXmlMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            sb.append("        ")
                    .append("<")
                    .append(column.isOnly()?"id":"result")
                    .append(" column=\"")
                    .append(column.getName())
                    .append("\" property=\"")
                    .append(column.getHumpName())
                    .append("\" />\n");
        }
        return sb.toString();
    }

    /**********************************************************************************************/
    /************************************** Html动态字段 *******************************************/
    /**********************************************************************************************/

    private String createHtmlMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
            if (column.isPrimaryKey()){ continue;}
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                sb.append("    <div class=\"layui-inline\">\n")
                        .append("        <label class=\"layui-form-label\">").append(GeneratorUtils.supportHtmlName(column.getComment())).append("：</label>\n")
                        .append("        <div class=\"layui-input-inline cool-auto-complete\">\n")
                        .append("            <input id=\"").append(column.getHumpName()).append("\"")
                        .append(" class=\"layui-input\" name=\"").append(column.getName()).append("\" type=\"text\" placeholder=\"请输入\" autocomplete=\"off\" style=\"display: none\">\n")
                        .append("            <input id=\"").append(column.getHumpName()).append("\\$")
                        .append("\" class=\"layui-input cool-auto-complete-div\" onclick=\"autoShow(this.id)\" type=\"text\" placeholder=\"请输入\" onfocus=this.blur()>\n")
                        .append("            <div class=\"cool-auto-complete-window\">\n")
                        .append("                <input class=\"cool-auto-complete-window-input\" data-key=\"").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("Query").append("By").append(column.getHumpName()).append("\" onkeyup=\"autoLoad(this.getAttribute('data-key'))\">\n")
                        .append("                <select class=\"cool-auto-complete-window-select\" data-key=\"").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("Query").append("By").append(column.getHumpName()).append("Select\" onchange=\"confirmed(this.getAttribute('data-key'))\" multiple=\"multiple\">\n")
                        .append("                </select>\n")
                        .append("            </div>\n")
                        .append("        </div>\n")
                        .append("    </div>\n");
            }
        }
        return sb.toString();
    }

    /**********************************************************************************************/
    /*********************************** HtmlDetail动态字段 *****************************************/
    /**********************************************************************************************/

    private String createHtmlDetailMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
//            if (column.isPrimaryKey()){ continue;}
            sb.append("        <div class=\"layui-inline\"  style=\"width:");
            if (column.isImage()){
                sb.append("97%");
            } else if (null == column.getLength() || column.getLength() <= 256){
                sb.append("31%");
            } else if (column.getLength() <= 1024){
                sb.append("64%");
            } else {
                sb.append("97%");
            }
            sb.append(";\">\n")
                    .append("            <label class=\"layui-form-label");
            // 注释过长适配：长度>=6时字体变小
            if (column.getComment().length() > 5) {
                sb.append("\" style=\"font-size: x-small");
            }
            sb.append("\">");
            // 非空判断
            if (column.isNotNull()){
                sb.append("<span class=\"not-null\">*</span>");
            }
            sb.append(GeneratorUtils.supportHtmlName(column.getComment()))
                    .append("：")
                    .append("</label>\n")
                    .append("            <div class=\"layui-input-inline");
            // 关联外键
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                sb.append(" cool-auto-complete");
            }
            sb.append("\">\n");

            // 输入框类型
            if (Cools.isEmpty(column.getEnums())){
                sb.append("                <input id=\"")
                        .append(column.getHumpName());
                // 时间格式化
                if ("Date".equals(column.getType())){
                    sb.append("\\$");
                }
                sb.append("\" class=\"layui-input\" type=");
                // 复选框
                if (column.isCheckBox()){
                    sb.append("\"checkBox\" lay-skin=\"primary\" lay-filter='detailCheckbox'");
                } else {
                    sb.append("\"text\"");
                }
                // 主键
                if (column.isPrimaryKey()){
                    sb.append(" onkeyup=\"check(this.id, '").append(simpleEntityName).append("')\"");
                }
                // 非空判断
                List<String> list = new ArrayList<>();
                if (column.isNotNull() && !column.isOnly()){
                    list.add("required");
//                    sb.append(" lay-verify=\"required\" ");
                }
                // 数字判断
                if ("ShortIntegerLongDouble".contains(column.getType())){
                    list.add("number");
                }
                if (list.size() > 0){
                    sb.append(" lay-verify=\"");
                    for (String str : list) {
                        sb.append(str).append("|");
                    }
                    if (sb.substring(sb.length() - 1).equals("|")) {
                        sb.deleteCharAt(sb.length()-1);
                    }
                    sb.append("\" ");
                }
                // 关联外键
                if (!Cools.isEmpty(column.getForeignKeyMajor())){
                    sb.append(" style=\"display: none\"");
                }
                // 时间字段去除历史记录
                if ("Date".equals(column.getType())){
                    sb.append(" autocomplete=\"off\"");
                }
                sb.append(">\n");
                // 关联外键
                if (!Cools.isEmpty(column.getForeignKeyMajor())){
                    sb.append("                <input id=\"").append(column.getHumpName()).append("\\$")
                            .append("\" class=\"layui-input cool-auto-complete-div\" onclick=\"autoShow(this.id)\" type=\"text\" placeholder=\"请输入...\" onfocus=this.blur()>\n");
//                            .append("\" class=\"layui-input cool-auto-complete-div\" onclick=\"autoShow(this.id)\" type=\"text\" onfocus=this.blur()>\n");
                    sb.append("                <div class=\"cool-auto-complete-window\">\n")
                            .append("                    <input class=\"cool-auto-complete-window-input\" data-key=\"")
                            .append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("Query").append("By").append(column.getHumpName()).append("\" onkeyup=\"autoLoad(this.getAttribute('data-key'))\">\n")
                            .append("                    <select class=\"cool-auto-complete-window-select\" data-key=\"").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("Query").append("By").append(column.getHumpName()).append("Select\" onchange=\"confirmed(this.getAttribute('data-key'))\" multiple=\"multiple\">\n")
                            .append("                    </select>\n")
                            .append("                </div>\n");
                }
                // 图片字段
                if (column.isImage()){
                    sb.append("                <img id=\"").append(column.getHumpName()).append("Img\" class=\"cool-img\" src=\"\" onclick=\"reviewImg(this.src)\" style=\"display: none\">\n");
                }
            // 枚举类型
            } else {
                sb.append("                <select id=\"")
                        .append(column.getHumpName());
                // 非空判断
                if (column.isNotNull()){
                    sb.append("\" lay-verify=\"required");
                }
                sb.append("\">\n")
                        .append("                    <option value=\"\" style=\"display: none\"></option>\n");
                for (Map<String, Object> map : column.getEnums()){
                    for (Map.Entry<String, Object> entry : map.entrySet()){
                        sb.append("                    <option value=\"")
                                .append(entry.getKey())
                                .append("\">")
                                .append(entry.getValue())
                                .append("</option>\n");
                    }
                }
                sb.append("                </select>\n");
            }
            sb.append("            </div>\n")
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
//            if (column.isPrimaryKey()){ continue;}
            sb.append("            ,{field: '");
            if ("Date".equals(column.getType()) || !Cools.isEmpty(column.getEnums())){
                // 时间、枚举  格式化
                sb.append(column.getHumpName()).append("\\$");
            } else {
                // 主键修饰
                if (!Cools.isEmpty(column.getForeignKeyMajor())){
                    sb.append(column.getHumpName()).append("\\$");
                } else {
                    sb.append(column.getHumpName());
                }
            }
            sb.append("', align: 'center',title: '").append(column.getComment()).append("'");
            // 复选框
            if (column.isCheckBox()){
                sb.append(", templet:function(row){\n")
                        .append("                    var html = \"<input value='")
                        .append(column.getHumpName()).append("' type='checkbox' lay-skin='primary' lay-filter='tableCheckbox' table-index='\"+row.LAY_TABLE_INDEX+\"'\";\n")
                        .append("                    if(row.").append(column.getHumpName()).append(" === 'Y'){html += \" checked \";}\n")
                        .append("                    html += \">\";\n")
                        .append("                    return html;\n")
                        .append("                }");
            }
            // 关联表
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                sb.append(",event: '")
                        .append(column.getHumpName())
                        .append("', style: 'cursor:pointer'");
            }
            sb.append("}\n");
        }
        return sb.toString();
    }

    private String createJsDetailMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
//            if (column.isPrimaryKey()){ continue;}
            sb.append("            ")
                    .append(column.getHumpName())
                    .append(": ");
            // 时间字段增加$格式化
            if ("Date".equals(column.getType())){
                sb.append("top.strToDate(\\$('#")
                        .append(column.getHumpName())
                        .append("\\\\\\\\\\$').val()),\n");
            } else {
                 sb.append("\\$('#")
                        .append(column.getHumpName())
                        .append("').val(),\n");
            }
        }
        return sb.toString();
    }

    private String createJsFkContent(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns){
//            if (column.isPrimaryKey()){ continue;}
            // 如果有关联外健
            if (!Cools.isEmpty(column.getForeignKeyMajor())){
                sb.append("            case '").append(column.getHumpName()).append("':\n")
                        .append("                var param = top.reObject(data).").append(column.getHumpName()).append(";\n")
                        .append("                if (param === undefined) {\n")
                        .append("                    layer.msg(\"无数据\");\n")
                        .append("                } else {\n")
                        .append("                   layer.open({\n")
                        .append("                       type: 2,\n")
                        .append("                       title: '").append(column.getComment()).append("详情',\n")
                        .append("                       maxmin: true,\n")
                        .append("                       area: [top.detailWidth, top.detailHeight],\n")
                        .append("                       shadeClose: true,\n")
                        .append("                       content: '../").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("/").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("_detail.html',\n")
                        .append("                       success: function(layero, index){\n")
                        .append("                           \\$.ajax({\n")
                        .append("                               url: \"baseUrl+/").append(GeneratorUtils.firstCharConvert(column.getForeignKey())).append("/\"+ param").append(" +\"/auth\",\n")
                        .append("                               headers: {'token': localStorage.getItem('token')},\n")
                        .append("                               method: 'GET',\n")
                        .append("                               success: function (res) {\n")
                        .append("                                   if (res.code === 200){\n")
                        .append("                                       setFormVal(layer.getChildFrame('#detail', index), res.data, true);\n")
                        .append("                                       top.convertDisabled(layer.getChildFrame('#data-detail :input', index), true);\n")
                        .append("                                       layer.getChildFrame('#data-detail-submit-save,#data-detail-submit-edit,#prompt', index).hide();\n")
                        .append("                                       layer.iframeAuto(index);layer.style(index, {top: ((\\$(window).height()-layer.getChildFrame('#data-detail', index).height())/3)+\"px\"});\n")
                        .append("                                       layero.find('iframe')[0].contentWindow.layui.form.render('select');\n")
                        .append("                                       layero.find('iframe')[0].contentWindow.layui.form.render('checkbox');\n")
                        .append("                                   } else if (res.code === 403){\n")
                        .append("                                       top.location.href = baseUrl+\"/\";\n")
                        .append("                                   }else {\n")
                        .append("                                       layer.msg(res.msg)\n")
                        .append("                                   }\n")
                        .append("                               }\n")
                        .append("                           })\n")
                        .append("                       }\n")
                        .append("                   });\n")
                        .append("                }\n")
                        .append("                break;\n");
            }
        }
        return sb.toString();
    }

    private String createJsDateContent(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                continue;
            }
            if ("Date".equals(column.getType())){
                sb.append("    layDate.render({\n")
                        .append("        elem: '#").append(column.getHumpName()).append("\\\\\\\\\\$',\n")
                        .append("        type: 'datetime'\n")
                        .append("    });\n");
            }
        }
        return sb.toString();
    }

    private String createJsPrimaryKeyMsg(){
        StringBuilder sb = new StringBuilder();
        for (Column column : columns) {
            if (column.isPrimaryKey()) {
                sb.append("#").append(column.getHumpName()).append(",");
            }
        }
        if (sb.length() > 1){
            if (sb.substring(sb.length() - 1).equals(",")) {
                sb.deleteCharAt(sb.length()-1);
            }
        }
        return sb.toString();
    }

}
