package com.hyberbin.code.generator.generate;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.hyberbin.code.generator.config.CodeGeneratorModule;
import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.domains.TreeNodeModel;
import com.hyberbin.code.generator.domains.VarDo;
import com.hyberbin.code.generator.ui.frames.ConfigFrame;
import com.hyberbin.code.generator.ui.model.PathTreeBind;
import com.hyberbin.code.generator.utils.VelocityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jplus.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileGenerate {

    private SqliteDao sqliteDao;
    private List<VarDo> varDos;

    @Inject
    public FileGenerate(SqliteDao sqliteDao) {
        this.sqliteDao = sqliteDao;
    }

    public void generate(DataContext context) {
        varDos = null;
        List<PathTreeBind> all = context.getTemplates();
        for(PathTreeBind model:all){
            if(Objects.equals(2,model.getModel().getType())){
                generate(model.getNodePath(),context,model.getModel());
            }
        }
    }

    @SneakyThrows
    private void generate(String filePath, DataContext context, TreeNodeModel nodeModel) {

        HashMap vars = new HashMap();
        if (varDos == null) {
            varDos = sqliteDao.getAll(VarDo.class);
        }
        varDos.forEach(v -> {
            if (StringUtils.isEmpty(v.getProject()) || Objects.equals("global", v.getProject())) {
                vars.put(v.getKey(), v.getValue());
            }
        });
        varDos.forEach(v -> {
            if (Objects.equals(nodeModel.getProject(), v.getProject())) {
                vars.put(v.getKey(), v.getValue());
            }
        });
        varDos.forEach(v -> {
            if (Objects.equals(context.getSelectedEnv(), v.getProject())) {
                vars.put(v.getKey(), v.getValue());
            }
        });
        vars.put("classModelMeta", context.getClassModelMeta());
        vars.put("fieldMetas", context.getFieldMetas());
        String path = VelocityUtils.evaluate("${basePath}" + filePath, vars);
        //文件路径src后如果带有-需要把-替换成/
        String[] srcs = path.split("src");
        if (srcs.length == 2) {
            path = srcs[0] + "src" + srcs[1].replace("-", "/");
        }
        String dir =path.substring(0, path.lastIndexOf("/") + 1).replace(".", "/");
        if (!new File(dir).exists()) {
            new File(dir).mkdirs();
            log.info("新建文件夹:{}", dir);
        }

        String fileName = path.substring(path.lastIndexOf("/") + 1);
        log.info("当前环境变量:{}", JSON.toJSONString(vars));
        String content = VelocityUtils.evaluate(nodeModel.getTemplate(), vars);
        log.info("生成文件:{}", dir + fileName);
        if(fileName.endsWith("java")){
            String[] lines = content.split("\n");
            StringBuilder fileContent = new StringBuilder();
            for (String line : lines) {
                String trim = line.trim();
                if (trim.startsWith("package") && trim.endsWith(";")) {
                    fileContent.append(trim.replace("-", ".")).append("\n");
                } else {
                    fileContent.append(line).append("\n");
                }
            }
            content=fileContent.toString();
        }
        FileCopyUtils.copy(content.getBytes("utf-8"), new FileOutputStream(dir + fileName));
    }
}
