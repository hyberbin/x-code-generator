/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyberbin.code.generator.domains;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class TreeNodeModel extends BaseDo{


  private Integer type;//1目录、2文件
  private String pathName;
  private String fileName;
  private String template;
  private String parentId;
  private String project;
}
