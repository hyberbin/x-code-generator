package com.hyberbin.code.generator.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.ConsoleAppender;
import java.io.IOException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class FrameAppender extends ConsoleAppender {

  private static RSyntaxTextArea log=null;
  @Override
  protected void writeOut(Object e) throws IOException {
    if(log!=null){
      LoggingEvent event = (LoggingEvent) e;
      log.append(event.toString()+"\n");
      if(event.getThrowableProxy()!=null){
        log.append(ThrowableProxyUtil.asString(event.getThrowableProxy())+"\n");
      }
    }
    super.writeOut(e);
  }

  public static void setLog(RSyntaxTextArea log) {
    FrameAppender.log = log;
  }
}
