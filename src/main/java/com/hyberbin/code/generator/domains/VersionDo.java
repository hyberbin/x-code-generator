package com.hyberbin.code.generator.domains;

import lombok.Data;

@Data
public class VersionDo extends BaseDo {

    private String version;
    private String httpPath;
    private String localPath;
}
