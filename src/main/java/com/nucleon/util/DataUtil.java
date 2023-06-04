package com.nucleon.util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
public class DataUtil {
    public static String readData(String fileName) {
        String jsonString = null;
        try {
            InputStream inputStream = DataUtil.class.getResourceAsStream(fileName);
            jsonString = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (jsonString == null) {
            throw new RuntimeException("读取数据失败:错误出现在DataUtil.readData()");
        }
        else{
            return jsonString;
        }
        
    }
}
