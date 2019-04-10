package com.hyberbin.code.generator.config;


import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.DataTypeDo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jplus.hyb.database.config.DbConfig;
import org.jplus.hyb.database.transaction.IDbManager;
import org.jplus.hyb.database.transaction.SimpleManager;
import org.jplus.hyb.database.transaction.SingleManager;

public class ConfigFactory {

  private static final DbConfig DEFAULT_CONFIG = new DbConfig(DbConfig.URL_SQLITE,
      "sqlite");


  public static DbConfig getDbConfig() {
    return DEFAULT_CONFIG;
  }


  public static IDbManager getSimpleManage() {
    return new SimpleManager(getDbConfig().getConfigName());
  }

  public static IDbManager getSimpleManage(String configName) {
    return new SimpleManager(configName);
  }

  public static IDbManager getTxManage() {
    return new SingleManager(getDbConfig().getConfigName());
  }

  public static Map<String, DataTypeDo> getDataTypeMaping(){
    Map<String, DataTypeDo> map=new HashMap<>();
    SqliteDao sqliteDao = CodeGeneratorModule.getInstance(SqliteDao.class);
    List<DataTypeDo> all = sqliteDao.getAll(DataTypeDo.class);
    for(DataTypeDo typeModel:all){
      map.put(typeModel.getDbType(),typeModel);
    }
    return map;
  }
}
