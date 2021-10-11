package com.hyberbin.code.generator.config;

import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.DataSource;
import com.hyberbin.code.generator.domains.DataTypeDo;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
    public static final DbConfig SQLITE_DB = new DbConfig(DbConfig.URL_SQLITE, "sqlite");
    public static final IDbManager SQLITE_MANAGER = getSimpleManage(SQLITE_DB.getConfigName());
    public static DbConfig DEFAULT_CONFIG = loadConfig();
    public static final String REMOTE_DB_CONFIG_ENABLE = "remoteDbConfigEnable";
    public static final String REMOTE_DB_CONFIG_NAME = "远程配置中心";

    /**
     * 加载配置中心
     *
     * @return
     */
    public static DbConfig loadConfig() {
        boolean remoteDbEnable = SqliteUtil.getBoolProperty(REMOTE_DB_CONFIG_ENABLE);
        if (!remoteDbEnable) {
            return SQLITE_DB;
        }
        SqliteDao sqliteDao = CodeGeneratorModule.getInstance(SqliteDao.class);
        DataSource remoteDatasource = sqliteDao.getOne(DataSource.class, "datasource", REMOTE_DB_CONFIG_NAME, SQLITE_MANAGER);
        if (remoteDatasource == null) {
            remoteDatasource = new DataSource();
            remoteDatasource.setDatasource(REMOTE_DB_CONFIG_NAME);
            remoteDatasource.setUrl(DbConfig.getMysqlUrl("127.0.0.1", "x-generator"));
            remoteDatasource.setUsername("root");
            remoteDatasource.setPassword("root");
            sqliteDao.saveDO(remoteDatasource, getSimpleManage(SQLITE_DB.getConfigName()));
        }
        DbConfig dbConfig = new DbConfig(remoteDatasource.getUrl(), remoteDatasource.getUsername(), remoteDatasource.getPassword(), REMOTE_DB_CONFIG_NAME);
        SimpleConfigurator.addConfigurator(dbConfig);
        DEFAULT_CONFIG = dbConfig;
        return dbConfig;
    }

    public static DbConfig getDbConfig() {
        return DEFAULT_CONFIG;
    }

    public static IDbManager getSimpleManage() {
        return new SimpleManager(getDbConfig().getConfigName());
    }

    public static IDbManager getRemoteManage() {
        return new SimpleManager(REMOTE_DB_CONFIG_NAME);
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

    public static String getCurrentVersion() {
        Properties properties = new Properties();
        try (InputStream is = ConfigFactory.class.getResourceAsStream("/version/version.properties")) {
            properties.load(is);
            String version = properties.getProperty("version");
            logger.info("当前版本:{}", version);
            return version;
        } catch (Throwable e) {
            return null;
        }
    }
}
