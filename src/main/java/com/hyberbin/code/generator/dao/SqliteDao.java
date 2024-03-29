package com.hyberbin.code.generator.dao;

import com.hyberbin.code.generator.config.ConfigFactory;
import com.hyberbin.code.generator.domains.BaseDo;
import com.hyberbin.code.generator.domains.ColumnMeta;
import com.hyberbin.code.generator.domains.DataSource;
import com.hyberbin.code.generator.domains.TableMeta;
import com.hyberbin.code.generator.utils.StringUtils;
import lombok.SneakyThrows;
import org.jplus.hyb.database.bean.ParmeterPair;
import org.jplus.hyb.database.config.ConfigCenter;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.config.SimpleConfigurator;
import org.jplus.hyb.database.crud.DatabaseAccess;
import org.jplus.hyb.database.crud.Hyberbin;
import org.jplus.hyb.database.sqlite.SqliteUtil;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.jplus.hyb.database.util.ISqlout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SqliteDao {

    private static final Logger logger = LoggerFactory.getLogger(SqliteDao.class);

    public SqliteDao() {
        dbSetup();
    }

    /**
     * 初始化本地数据库
     */
    public void dbSetup() {
        ConfigCenter.INSTANCE.setSqlout(Sqlout.class.getName());
        if (!SqliteUtil.tableExist("TreeNodeModel")) {
            SqliteUtil.execute(
                    "create table TreeNodeModel("
                    + "id text PRIMARY KEY"
                    + ",type integer"
                    + ",pathName text"
                    + ",fileName text"
                    + ",parentId text"
                    + ",project text"
                    + ",template text);");
        }
        if (!SqliteUtil.tableExist("DataSource")) {
            SqliteUtil.execute(
                    "CREATE TABLE \"DataSource\" ("
                            + "  \"id\" text,"
                            + "  \"datasource\" text NOT NULL ON CONFLICT FAIL,"
                            + "  \"url\" text,"
                            + "  \"username\" text,"
                            + "  \"password\" text,"
                            + "  PRIMARY KEY (\"id\"),"
                            + "  CONSTRAINT \"u_datasource\" UNIQUE (\"datasource\" ASC) ON CONFLICT FAIL"
                            + ");");
        }
        if (!SqliteUtil.tableExist("FieldMeta")) {
            SqliteUtil.execute(
                    "create table FieldMeta("
                    + "id text PRIMARY KEY"
                    + ",tableName text"
                    + ",columnName text"
                    + ",comment text"
                    + ",dataType text"
                    + ",isPrimaryKey integer"
                    + ",charMaxLength integer"
                    + ",className text"
                    + ",fieldName text"
                    + ");");
        }
        if (!SqliteUtil.tableExist("ClassModelMeta")) {
            SqliteUtil.execute(
                    "create table ClassModelMeta("
                    + "id text PRIMARY KEY"
                    + ",tableName text"
                    + ",className text"
                    + ",comment text"
                    + ",dbName text"
                    + ");");
        }
        if (!SqliteUtil.tableExist("VarDo")) {
            SqliteUtil.execute(
                    "create table VarDo("
                    + "id text PRIMARY KEY"
                    + ",project text"
                    + ",key text"
                    + ",value text"
                    + ",note text"
                    + ");");
        }
        if (!SqliteUtil.tableExist("DataTypeDo")) {
            SqliteUtil.execute(
                    "create table DataTypeDo("
                    + "id text PRIMARY KEY"
                    + ",dbType text"
                    + ",javaType text"
                    + ",jdbcType text"
                    + ");");
        }
        if (!SqliteUtil.tableExist("VersionDo")) {
            SqliteUtil.execute(
                    "create table VersionDo("
                    + "id text PRIMARY KEY"
                    + ",version text"
                    + ",httpPath text"
                    + ",localPath text"
                    + ");");
        }
    }

    /**
     * 保存一个对象到默认数据库
     *
     * @param object
     * @param <T>
     */
    public <T extends BaseDo> void saveDO(T object) {
        saveDO(object, ConfigFactory.getSimpleManage());
    }

    /**
     * 保存一个对象到指定数据库
     *
     * @param object
     * @param dbManager
     * @param <T>
     */
    public <T extends BaseDo> void saveDO(T object, IDbManager dbManager) {
        try {
            Hyberbin hyberbin = new Hyberbin(object, dbManager);
            if (object.getId() != null) {
                hyberbin.updateByKey("id");
            } else {
                object.setId(IDGenerator.SNOW_FLAKE_STRING.generate());
                hyberbin.insert("");
            }
        } catch (SQLException e) {
            logger.error("TreeNodeModel error!", e);
        }
    }

    public boolean validateDbConfig(DataSource dataSource) {
        try {
            DbConfig dbConfig = new DbConfig(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword(), dataSource.getDatasource());
            SimpleConfigurator.addConfigurator(dbConfig);
            DatabaseAccess databaseAccess = new DatabaseAccess(new SimpleManager(dbConfig.getConfigName()));
            databaseAccess.queryUnique("select 1+1");
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 新增一个对象
     *
     * @param object
     * @param <T>
     */
    public <T extends BaseDo> void insertDO(T object) {
        insertDO(object, ConfigFactory.getSimpleManage());
    }

    /**
     * 新增一个对象到指定数据库
     *
     * @param object
     * @param dbManager
     * @param <T>
     */
    public <T extends BaseDo> void insertDO(T object, IDbManager dbManager) {
        try {
            Hyberbin hyberbin = new Hyberbin(object, dbManager);
            hyberbin.insert("");
        } catch (SQLException e) {
            logger.error("TreeNodeModel error!", e);
        }
    }

    /**
     * 获取指定类型的所有对象
     *
     * @param type
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends BaseDo> List<T> getAll(Class<T> type) {
        return getAll(type, ConfigFactory.getSimpleManage());
    }

    /**
     * 获取指定数据库中指定类型的所有对象
     *
     * @param type
     * @param dbManager
     * @param <T>
     * @return
     */
    @SneakyThrows
    public <T extends BaseDo> List<T> getAll(Class<T> type, IDbManager dbManager) {
        Hyberbin hyberbin = new Hyberbin(type.newInstance(), dbManager);
        return hyberbin.showAll();
    }

    /**
     * 获取一个默认数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param <T>
     * @return
     */
    public <T extends BaseDo> T getOne(Class<T> type, String filed, Object value) {
        return getOne(type, filed, value, ConfigFactory.getSimpleManage());
    }

    /**
     * 获取一个指定数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param dbManager
     * @param <T>
     * @return
     */
    public <T extends BaseDo> T getOne(Class<T> type, String filed, Object value, IDbManager dbManager) {
        try {
            Hyberbin hyberbin = new Hyberbin(type.newInstance(), dbManager);
            return (T) hyberbin
                    .showOne("select * from " + type.getSimpleName() + " where `" + filed + "`=?", value);
        } catch (Throwable e) {
            logger.error("getChild error!", e);
        }
        return null;
    }

    /**
     * 删除默认数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param <T>
     * @return
     */
    public <T extends BaseDo> int deleteOne(Class<T> type, String filed, Object value) {
        return deleteOne(type, filed, value, ConfigFactory.getSimpleManage());
    }

    /**
     * 删除指定数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param <T>
     * @return
     */
    public <T extends BaseDo> int deleteOne(Class<T> type, String filed, Object value, IDbManager dbManager) {
        try {
            Hyberbin hyberbin = new Hyberbin(type.newInstance(), dbManager);
            return hyberbin
                    .delete(" where `" + filed + "`='" + value + "'");
        } catch (Throwable e) {
            logger.error("getChild error!", e);
        }
        return 0;
    }

    /**
     * 获取所有默认数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param <T>
     * @return
     */
    public <T extends BaseDo> List<T> getList(Class<T> type, String filed, Object value) {
        return getList(type, filed, value, ConfigFactory.getSimpleManage());
    }

    /**
     * 获取所有指定数据库中指定类型指定字段值的对象
     *
     * @param type
     * @param filed
     * @param value
     * @param <T>
     * @return
     */
    public <T extends BaseDo> List<T> getList(Class<T> type, String filed, Object value, IDbManager dbManager) {
        try {
            Hyberbin hyberbin = new Hyberbin(type.newInstance(), dbManager);
            return hyberbin
                    .showList("select * from " + type.getSimpleName() + " where `" + filed + "`=?", value);
        } catch (Throwable e) {
            logger.error("getChild error!", e);
        }
        return null;
    }

    /**
     * 获取数据库中指定表的字段信息列表
     *
     * @param tableName 表名
     * @param dataSource 数据库名
     * @return
     */
    @SneakyThrows
    public List<ColumnMeta> getColumnMetas(String tableName, String dataSource) {
        IDbManager simpleManage = ConfigFactory.getSimpleManage(dataSource);
        Hyberbin hyberbin = new Hyberbin(new ColumnMeta(), simpleManage);
        hyberbin.removeField("id");
        return hyberbin.showList("select "
                + "column_name columnName,"
                + "table_name tableName,"
                + "column_comment comment,"
                + "data_type dataType,"
                + "COLUMN_KEY='PRI' isPrimaryKey,"
                + "CHARACTER_MAXIMUM_LENGTH charMaxLength"
                + " from information_schema.columns "
                + " where table_name=? and table_schema=database()", tableName);
    }

    /**
     * 获取数据库中表的信息列表，可以根据表名模糊匹配
     *
     * @param dataSource 数据库名
     * @param tableName 表名可以模糊匹配
     * @return
     */
    @SneakyThrows
    public List<TableMeta> getTableMetas(String dataSource, String tableName) {
        if (StringUtils.isBlank(tableName)) {
            tableName = "";
        }
        IDbManager simpleManage = ConfigFactory.getSimpleManage(dataSource);
        Hyberbin hyberbin = new Hyberbin(new TableMeta(), simpleManage);
        hyberbin.removeField("id");
        return hyberbin.showList("select "
                + "table_name tableName,"
                + "table_schema dbName,"
                + "table_comment comment"
                + " from information_schema.tables "
                + " where  table_schema=database() and table_name like ?", "%" + tableName + "%");
    }

    /**
     * 获取数据库名
     *
     * @param dataSource
     * @return
     */
    @SneakyThrows
    public String getDbName(String dataSource) {
        IDbManager simpleManage = ConfigFactory.getSimpleManage(dataSource);
        DatabaseAccess access = new DatabaseAccess(simpleManage);
        return (String) access.queryUnique("select database() as dbName");
    }

    /**
     * 简单实现数据库操作SQL打印的类
     */
    public static final class Sqlout implements ISqlout {

        @Override
        public void sqlout(String sql, List<ParmeterPair> parmeters) {
            List<String> params = parmeters.stream().map(o -> "'" + o.getParmeter() + "'").collect(Collectors.toList());
            logger.info(sql.replaceAll("[?]", "{}"), params.toArray());
        }

        @Override
        public void setSqlout(boolean needout) {

        }

        @Override
        public boolean isSqlout() {
            return true;
        }
    }
}
