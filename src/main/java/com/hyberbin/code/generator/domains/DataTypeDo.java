package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class DataTypeDo extends BaseDo {

    private String dbType;
    private String javaType;
    private String jdbcType;
}
