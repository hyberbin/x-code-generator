package com.hyberbin.code.generator.utils;


import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
@Slf4j
public class VelocityUtils {

  private static VelocityEngine velocityEngine;

  static {
    Properties properties = new Properties();            // 初始化参数
    properties.setProperty("file.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    properties.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
    properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
    velocityEngine = new VelocityEngine(properties);
  }






  public static String evaluate(String exp, Map vars) {
    try {
      VelocityContext context = new VelocityContext(vars);
      vars.put("tool",StringUtils.INSTANCE);
      StringWriter stringWriter = new StringWriter();
      velocityEngine.evaluate(context, stringWriter, exp, exp);
      stringWriter.flush();
      return stringWriter.toString();
    } catch (Throwable e) {
      log.warn("classLoaderVelocityEngine evaluate error :" + exp, e);
    }
    return "";
  }

}
