package com.hyberbin.code.generator.utils;

/**
 * 字符串处理
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils{

  public static final StringUtils INSTANCE=new StringUtils();

  private StringUtils() {
  }

  public String getCamelCaseString(String inputString, boolean firstCharacterUppercase) {
    StringBuilder sb = new StringBuilder();

    boolean nextUpperCase = false;
    for (int i = 0; i < inputString.length(); i++) {
      char c = inputString.charAt(i);

      switch (c) {
        case '_':
        case '-':
        case '@':
        case '$':
        case '#':
        case ' ':
        case '/':
        case '&':
          if (sb.length() > 0) {
            nextUpperCase = true;
          }
          break;

        default:
          if (nextUpperCase) {
            sb.append(Character.toUpperCase(c));
            nextUpperCase = false;
          } else {
            sb.append(Character.toLowerCase(c));
          }
          break;
      }
    }

    if (firstCharacterUppercase) {
      sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
    }

    return sb.toString();
  }

  public String firstLower(String inputString) {
    return (inputString.charAt(0) + "").toLowerCase() + inputString.substring(1);
  }

  public String firstUpper(String inputString) {
    return (inputString.charAt(0) + "").toUpperCase() + inputString.substring(1);
  }


}
