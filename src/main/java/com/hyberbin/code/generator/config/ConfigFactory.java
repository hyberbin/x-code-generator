package com.hyberbin.code.generator.config;


import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.DataTypeDo;
import com.hyberbin.code.generator.utils.StringUtils;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
@Slf4j
public class ConfigFactory {


    private static DbConfig DEFAULT_CONFIG = loadConfig();

    private static DbConfig loadConfig() {
        DbConfig defaultConfig = new DbConfig(DbConfig.URL_SQLITE, "sqlite");
        Properties properties=new Properties();
        try (InputStream is=ConfigFactory.class.getResourceAsStream("/config/datasource.properties")){
            properties.load(is);
        }catch (Throwable e){
            log.info("读取配置文件出错，请检查/config/datasource.properties");
            return defaultConfig;
        }
        String names=properties.getProperty("datasource.names");
        String active=properties.getProperty("datasource.active");
        if(StringUtils.isBlank(names)||StringUtils.isBlank(active)){
            log.error("请检查配置文件中datasource.names和datasource.active");
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
}
