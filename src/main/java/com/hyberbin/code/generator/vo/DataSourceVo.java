package com.hyberbin.code.generator.vo;

import com.hyberbin.code.generator.domains.DataSource;
import lombok.Data;

@Data
public class DataSourceVo extends DataSource {

    private boolean isLocal;

    public DataSourceVo(DataSource dataSource, boolean isLocal) {
        this.setDatasource(dataSource.getDatasource());
        this.setPassword(dataSource.getPassword());
        this.setUrl(dataSource.getUrl());
        this.setUsername(dataSource.getUsername());
        this.setId(dataSource.getId());
        this.isLocal = isLocal;
    }
}
