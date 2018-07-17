package com.tbl.offline.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * author sunbin
 * date 2018-6-12 16:45:02
 */
public class LoadConfs {
    // 私有为了避免外界的代码不小心错误的更新了Properties中某个key对应的value
    private static Properties prop = new Properties();

    static {
        try {
            // 该类默认加载resource文件夹下的paramsConf.properties配置文件
            InputStream in = LoadConfs.class
                    .getClassLoader().getResourceAsStream("paramsConf.properties");
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来自定义制定配置文件路径
     * @param path
     *          配置文件路径
     * @return
     */
    public static Properties getPropertyFromFile(String path){

        Properties prop = new Properties();

        try {
            // 可以获取到一个针对指定文件的输入流（InputStream）
            InputStream in = LoadConfs.class
                    .getClassLoader().getResourceAsStream(path);
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return prop;
    }


    /**
     * 获取整数类型的配置项
     * @param key
     * @return value
     */
    public static Integer getInteger(String key) {
        String value = getProperty(key);
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     * 获取布尔类型的配置项
     * @param key
     * @return value
     */
    public static Boolean getBoolean(String key) {
        String value = getProperty(key);
        try {
            return Boolean.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取Long类型的配置项
     * @param key
     * @return
     */
    public static Long getLong(String key) {
        String value = getProperty(key);
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
