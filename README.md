# x-code-generator
万能的web代码自动生成，模板可以自定义

x-code-generator 是一个根据数据库表结构自动生成代码的工具，非常适用于javaWeb工程。基于velocity模板生成代码。

## 使用方法

下载dist目录下面的文件
/XCodeGenerator-1.0-SNAPSHOT.jar


java -jar XCodeGenerator-1.0-SNAPSHOT.jar 即可运行

## 操作手册

主界面如下

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/1.jpg" >

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/7.jpg" >

1 我们先在这个树目录下建好工程模板，代码结构要与实际工程结构路径相同

2 然后点击设置菜单，来设置数据库数据源、环境变量、数据类型映射等

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/2.jpg" >

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/3.jpg" >

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/4.jpg" >

关于环境变量：
* 环境变量就是模板中用到的全局变量
* basePath 必需要有，这个是指定代码的生成路径

3 点击主界面菜单中的生成按钮，选择需要生成的表，其中类名必填，填完后需要点击保存按钮，然后点下一步。

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/5.jpg" >

选择需要生成的数据源，勾选相应的表，可以使用模糊查询功能。

4 编辑相关的表字段信息用来生成model

<img src="https://raw.githubusercontent.com/hyberbin/x-code-generator/master/dist/img/6.jpg" >

fieldName如果不填写则会自动按照表中的字段名驼峰命名

## 模板语法说明

类元数据：classModelMeta   
* 类名 ${classModelMeta.className}
* 类的说明 ${classModelMeta.comment}

类中的字段信息：fieldMetas
* 数据库字段名 ${field.columnName}
* java类字段名 ${field.fieldName}
* 是否主键 ${field.isPrimaryKey}
* 字段注释说明 ${field.comment}
* 字段的java类型 ${field.javaType}
* 字段的JDBC类型 ${field.jdbcType}

工具类说明 tool
* 类名首字段小写 ${tool.firstLower($classModelMeta.className)}
* 类名首字段大写 ${tool.firstUpper($classModelMeta.className)}





