package com.hyberbin.code.generator.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarDo extends BaseDo {

  private String key;
  private String value;
  private String note;
  private String project;
}
