package com.hyberbin.code.generator.utils;

import com.alibaba.fastjson.JSON;

public class ModelUtils {

    public static <T> T copy(T source) {
        return (T) JSON.parseObject(JSON.toJSONString(source), source.getClass());
    }
}
