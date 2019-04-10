package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class DataSource extends BaseDo{
  private String datasource;
  private String url;
  private String username;
  private String password;
}
