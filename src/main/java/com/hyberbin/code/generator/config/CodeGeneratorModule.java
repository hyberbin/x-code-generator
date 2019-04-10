package com.hyberbin.code.generator.config;



import com.hyberbin.code.generator.dao.SqliteDao;
import com.hyberbin.code.generator.generate.FileGenerate;
import com.hyberbin.code.generator.ui.frames.CodeGenUIFrame;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.hyberbin.code.generator.ui.frames.SelectTableFrame;

public class CodeGeneratorModule extends AbstractModule {

  public static final Injector INJECTOR = Guice.createInjector(new CodeGeneratorModule());

  @Override
  protected void configure() {
    bind(SqliteDao.class).in(Scopes.SINGLETON);
    bind(CodeGenUIFrame.class).in(Scopes.SINGLETON);
    bind(SelectTableFrame.class).in(Scopes.SINGLETON);
    bind(FileGenerate.class).in(Scopes.SINGLETON);
//    bind(ConfigFrame.class).in(Scopes.SINGLETON);
//    bindInterceptor(Matchers.subclassesOf(ApiRunner.class), Matchers.any(),
//        new ApiRunMethodInvocation());
  }

  private void bindSingleton(Class type, String name, Class impl) {
    bind(type).annotatedWith(Names.named(name)).to(impl).in(Scopes.SINGLETON);
  }

  public static <T> T getInstance(Class<? extends T> clazz, String name) {
    return INJECTOR.getInstance(Key.get(clazz, Names.named(name)));
  }

  public static <T> T getInstance(Class<? extends T> clazz) {
    return INJECTOR.getInstance(clazz);
  }
}
