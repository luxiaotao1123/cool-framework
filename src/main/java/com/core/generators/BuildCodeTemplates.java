package com.core.generators;

import org.springframework.core.io.ClassPathResource;

import java.io.*;


public class BuildCodeTemplates {
	
//	private String[] FILECODES={"Controller","Service","ServiceImpl","Mapper"};
//
//	private String[] FILETYPES={"controller","service","service/impl","mapper"};

	private String[] FILECODES={"Controller"};

	private String[] FILETYPES={"controller"};
	/**
	 * 包路径
	 */
	private String packagepath;
	/**
	 * 包名全小写
	 */
	private String packageName;
	/**
	 * 完整的实体名,首字母大写
	 */
	private String fullEntityName;
	/**
	 * 实体名简写,首字母小写
	 */
	private String simpleEntityName;
	
	private DaoType type;

	public BuildCodeTemplates(String packagepath, String packageName, String entityName){
		this(packagepath, packageName, entityName, DaoType.Sql);
	}
	
	public BuildCodeTemplates(String packagepath, String packageName, String entityName, DaoType type){
		this.packagepath=packagepath;
		/**
		 * 默认包名为小写顾全转为小写
		 */
		this.packageName=packageName.toLowerCase();
		/**
		 * 把实体名首字母转为大写
		 */
		this.fullEntityName=entityName.substring(0,1).toUpperCase()+entityName.substring(1);
		/**
		 * 把实体名首字母转为小写
		 */
		this.simpleEntityName=entityName.substring(0,1).toLowerCase()+entityName.substring(1);
		
		this.type=type;
	}
	
	public void buildCodeFile() throws IOException{
		for(int i=0;i<FILETYPES.length;i++){
			/**
			 * 文本的内容
			 */
			StringBuilder txtContentBuilder=new StringBuilder();
			ClassPathResource classPath=new ClassPathResource("templates/"+FILECODES[i]+".txt");

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(classPath.getInputStream()))) {
				String lineContent;
				while ((lineContent = reader.readLine()) != null) {
					txtContentBuilder.append(lineContent).append("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	        String types=FILETYPES[i];
	        writerFile(txtContentBuilder.toString(),types,FILECODES[i]);
		}
	}
	/**
	 * @param content
	 * 文本内容
	 * 生成的类型 action层;service层;service/impl层;dao层;dao/daoimpl层
	 * @param fileCode
	 * 所属的层类型
	 */
	private void writerFile(String content, String builderType, String fileCode) throws IOException{
		String directory="src/main/java/"+packagepath+"/"+packageName+"/"+builderType.toLowerCase()+"/";
		/**
		 * 目录不存在则创建
		 */
		File codeDirectory=new File(directory);
		if(!codeDirectory.exists()){
			codeDirectory.mkdirs();
		}
		if(fileCode.equals("Entity"))fileCode="";
		File writerFile=new File(directory+fullEntityName+fileCode+".java");
		if(!writerFile.exists()){
			/**
			 * 表的前缀默认取包名的前三位
			 */
			String tabHeader;
			if(packageName.length()>3){
				tabHeader=packageName.substring(0,3);
			}else{
				tabHeader=packageName;
			}
			content=content.
				replaceAll("@\\{PACKAGENAME}",packageName).					//包
				replaceAll("@\\{ENTITYNAME}", fullEntityName).			 	//实体
				replaceAll("@\\{SIMPLEENTITYNAME}", simpleEntityName). 		//实体简写
				replaceAll("@\\{UENTITYNAME}", simpleEntityName).	//实体大字
				replaceAll("@\\{TABEXTESION}",tabHeader).						//实体数据表前缀
				replaceAll("@\\{DAOTYPE}",this.type.name()).						//类型
				replaceAll("@\\{COMPANYNAME}",packagepath.replace("/", "."));	//实体数据表前缀
			writerFile.createNewFile();
			BufferedWriter writer=new BufferedWriter(new FileWriter(writerFile));
			writer.write(content);
			writer.flush();
			writer.close();
			System.out.println(fullEntityName+fileCode+".java 源文件创建成功！");
		}else{
			System.out.println(fullEntityName+fileCode+".java  源文件已经存在创建失败！");
		}
	}

	public enum DaoType{
		Sql
		;
	}

}