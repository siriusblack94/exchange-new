package com.blockeng;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Author: Q.Zou
 * Email: qiang.ins@gmail.com
 */
public class MysqlGenerator {


    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        /* 获取 JDBC 配置文件 */
        Properties props = getProperties();
        // 自定义需要填充的字段
/*        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("ASDD_SS", FieldFill.ISERT_UPDATE));*/

//        // 代码生成器
//        AutoGenerator mpg = new AutoGenerator().setGlobalConfig(
//                // 全局配置
//                new GlobalConfig()
//                        .setOutputDir(getRootPath() + "/blockeng-service/src/main/java")//输出目录
//                        .setFileOverride(true)// 是否覆盖文件
//                        .setActiveRecord(true)// 开启 activeRecord 模式
//                        .setEnableCache(false)// XML 二级缓存
//                        .setBaseResultMap(true)// XML ResultMap
//                        .setBaseColumnList(true)// XML columList
//                        .setAuthor("qiang")
//                        // 自定义文件命名，注意 %s 会自动填充表实体属性！
//                        // .setMapperName("%sDao")
//                        // .setXmlName("%sDao")
//                        .setServiceName("%sService")
//                // .setServiceImplName("%sServiceDiy")
//                // .setControllerName("%sAction")
//        ).setDataSource(
//                // 数据源配置
//                new DataSourceConfig()
//                        .setDbType(DbType.MYSQL)// 数据库类型
//                        .setTypeConvert(new MySqlTypeConvert() {
//                            // 自定义数据库表字段类型转换【可选】
//                            @Override
//                            public DbColumnType processTypeConvert(String fieldType) {
//                                System.out.println("转换类型：" + fieldType);
//                                // if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
//                                //    return DbColumnType.BOOLEAN;
//                                // }
//                                return super.processTypeConvert(fieldType);
//                            }
//                        })
//                        .setDriverName(props.getProperty("spring.datasource.driver-class-name"))
//                        .setUsername(props.getProperty("spring.datasource.username"))
//                        .setPassword(props.getProperty("spring.datasource.password"))
//                        .setUrl(props.getProperty("spring.datasource.url"))
//        ).setStrategy(
//                // 策略配置
//                new StrategyConfig()
//                        // .setCapitalMode(true)// 全局大写命名
//                        // .setDbColumnUnderline(true)//全局下划线命名
//                        //.setTablePrefix(new String[]{"platform_"})// 此处可以修改为您的表前缀
//                        .setNaming(NamingStrategy.underline_to_camel)// 表名生成策略
//                        .setInclude(new String[] {"work_issue" }) // 需要生成的表
//                // .setExclude(new String[]{"test"}) // 排除生成的表
//                // 自定义实体父类
//                // .setSuperEntityClass("com.baomidou.demo.TestEntity")
//                // 自定义实体，公共字段
//                //.setSuperEntityColumns(new String[]{"test_id"})
//                //.setTableFillList(tableFillList)
//                // 自定义 mappers 父类
//                // .setSuperMapperClass("com.baomidou.demo.TestMapper")
//                // 自定义 service 父类
//                // .setSuperServiceClass("com.baomidou.demo.TestService")
//                // 自定义 service 实现类父类
//                // .setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl")
//                // 自定义 web 父类
//                // .setSuperControllerClass("com.baomidou.demo.TestController")
//                // 【实体】是否生成字段常量（默认 false）
//                // public static final String ID = "test_id";
//                 //.setEntityColumnConstant(true)
//                // 【实体】是否为构建者模型（默认 false）
//                // public User setName(String name) {this.name = name; return this;}
//                // .setEntityBuilderModel(true)
//                // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
//                .setEntityLombokModel(true)
//                // Boolean类型字段是否移除is前缀处理
//                // .setEntityBooleanColumnRemoveIsPrefix(true)
//                 .setRestControllerStyle(true)
//                // .setControllerMappingHyphenStyle(true)
//        ).setPackageInfo(
//                // 包配置
//                new PackageConfig()
//                        //.setModuleName("cloudfactory")
//                        .setParent("com.blockeng")// 自定义包路径
//                        .setController("web")// 这里是控制器包名，默认 web
//        ).setCfg(
//                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
//                new InjectionConfig() {
//                    @Override
//                    public void initMap() {
//                        Map<String, Object> map = new HashMap<>();
//                        //map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                        this.setMap(map);
//                    }
//                }.setFileOutConfigList(Collections.<FileOutConfig>singletonList(new FileOutConfig("/templates/mapper.xml.vm") {
//                    // 自定义输出文件目录
//                    @Override
//                    public String outputFile(TableInfo tableInfo) {
//                        return getRootPath() + "/blockeng-service/src/main/resources/mappers/" + tableInfo.getEntityName() + "Mapper.xml";
//                    }
//                }))
//        ).setTemplate(
//                // 关闭默认 xml 生成，调整生成 至 根目录
//                new TemplateConfig().setXml(null)
//                // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
//                // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
//                // .setController("...");
//                // .setEntity("...");
//                // .setMapper("...");
//                // .setXml("...");
//                // .setService("...");
//                // .setServiceImpl("...");
//        );
//
//        // 执行生成
//        mpg.execute();

        // 打印注入设置，这里演示模板里面怎么获取注入内容【可无】
        //System.err.println(mpg.getCfg().getMap().get("abc"));
    }

    /**
     * 获取配置文件
     *
     * @return 配置Props
     */
    private static Properties getProperties() {
        // 读取配置文件
        Resource resource = new ClassPathResource("/application-dev.properties");
        Properties props = new Properties();
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    /**
     * 获取项目根路径
     *
     * @return 项目路径
     */
    private static String getRootPath() {
        File directory = new File("");// 参数为空
        String courseFile = null;
        try {
            courseFile = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseFile;
    }
}