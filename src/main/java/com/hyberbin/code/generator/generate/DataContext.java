package com.hyberbin.code.generator.generate;

import com.hyberbin.code.generator.domains.ClassModelMeta;
import com.hyberbin.code.generator.domains.FieldMeta;
import java.util.List;

import com.hyberbin.code.generator.ui.model.PathTreeBind;
import lombok.Data;

@Data
public class DataContext {

    private ClassModelMeta classModelMeta;
    private List<? extends FieldMeta> fieldMetas;
    private List<PathTreeBind> templates;
    private String selectedEnv;
}
