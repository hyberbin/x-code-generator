package com.hyberbin.code.generator.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static final StringUtils INSTANCE = new StringUtils();
    private static final Set<String> INNER_VARS = new HashSet<>(Arrays.asList("classModelMeta,field,tool".split(",")));

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

    /**
     * 获取普通文件中${xxx}的变量名
     *
     * @param fileContent
     * @return
     */
    public static Set<String> getAllVars(String fileContent) {
        Pattern regex = Pattern.compile("\\$\\{([^}]*)\\}");
        Matcher matcher = regex.matcher(fileContent);
        Set<String> vars = new HashSet<>();
        while (matcher.find()) {
            String match = matcher.group(1);
            String var = match.split("[.]")[0];
            if (!INNER_VARS.contains(var)) {
                vars.add(var);
            }
        }
        return vars;
    }

    /**
     * 获取文件是带指定关键字行中的变量，用于排除<version>${project.version}</version>之类的
     *
     * @param content
     * @param keys 需要获取的标签名
     * @return
     */
    public static Set<String> getKeyVars(String content, String... keys) {
        String regx = ".*(" + String.join("|", keys) + ").*";
        StringBuilder fileContent = new StringBuilder();
        Arrays.stream(content.split("\n")).forEach(l -> {
            if (l.matches(regx)) {
                fileContent.append(l).append("\n");
            }
        });
        return getAllVars(fileContent.toString());
    }

}
