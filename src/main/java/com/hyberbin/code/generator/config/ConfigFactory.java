package com.hyberbin.code.generator.config;


import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.DataTypeDo;
import com.hyberbin.code.generator.utils.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);


    private static DbConfig DEFAULT_CONFIG = loadConfig();

    private static DbConfig loadConfig() {
        DbConfig defaultConfig = new DbConfig(DbConfig.URL_SQLITE, "sqlite");
        Properties properties=new Properties();
        try (InputStream is=ConfigFactory.class.getResourceAsStream("/config/datasource.properties")){
            properties.load(is);
        }catch (Throwable e){
            String configPath = SqliteUtil.getProperty("configPath");
            if(StringUtils.isBlank(configPath)){
                JFileChooser fileChooser=new JFileChooser();
                fileChooser.setCurrentDirectory(new File("./"));
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().endsWith(".properties");
                    }

                    @Override
                    public String getDescription() {
                        return "请选择一个properties文件";
                    }
                });
                fileChooser.showDialog(null,"请选择一个properties文件");
                File selectedFile = fileChooser.getSelectedFile();
                configPath=selectedFile.getPath();
                SqliteUtil.setProperty("configPath",configPath);
            }
            try (InputStream is=new FileInputStream(configPath)){
                properties.load(is);
            }catch (Throwable t){
                logger.info("读取配置文件出错，请检查/config/datasource.properties");
                return defaultConfig;
            }
        }
        String names=properties.getProperty("datasource.names");
        String active=properties.getProperty("datasource.active");
        if(StringUtils.isBlank(names)||StringUtils.isBlank(active)){
            logger.error("请检查配置文件中datasource.names和datasource.active");
            return defaultConfig;
        }
        String username=properties.getProperty(String.join(".","datasource",active,"username"));
        String password=properties.getProperty(String.join(".","datasource",active,"password"));
        String url=properties.getProperty(String.join(".","datasource",active,"url"));
        DbConfig dbConfig = new DbConfig(url, username, password, active);
        SimpleConfigurator.addConfigurator(dbConfig);
        return dbConfig;
    }

    public static DbConfig getDbConfig() {
        return DEFAULT_CONFIG;
    }


    public static IDbManager getSimpleManage() {
        return new SimpleManager(getDbConfig().getConfigName());
    }

    public static IDbManager getSimpleManage(String configName) {
        return new SimpleManager(configName);
    }

    public static Map<String, DataTypeDo> getDataTypeMapping() {
        Map<String, DataTypeDo> map = new HashMap<>();
        SqliteDao sqliteDao = CodeGeneratorModule.getInstance(SqliteDao.class);
        List<DataTypeDo> all = sqliteDao.getAll(DataTypeDo.class);
        for (DataTypeDo typeModel : all) {
            map.put(typeModel.getDbType(), typeModel);
        }
        return map;
    }

    public static String getCurrentVersion(){
        Properties properties=new Properties();
        try (InputStream is=ConfigFactory.class.getResourceAsStream("/version/version.properties")){
            properties.load(is);
            String version = properties.getProperty("version");
            logger.info("当前版本:{}",version);
            return version;
        }catch (Throwable e){
            return null;
        }
    }
}
