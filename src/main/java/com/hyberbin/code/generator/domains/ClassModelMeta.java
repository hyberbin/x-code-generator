package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class ClassModelMeta extends TableMeta {

    private String className;

    public ClassModelMeta() {

    }

    public ClassModelMeta(TableMeta tableMeta) {
        this.setDbName(tableMeta.getDbName());
        this.setComment(tableMeta.getComment());
        this.setTableName(tableMeta.getTableName());
    }
}
