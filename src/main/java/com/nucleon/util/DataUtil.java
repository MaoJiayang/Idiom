package com.nucleon.util;
import com.nucleon.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class DataUtil {
    public static String readData() {
        InputStream in = Main.class.getClassLoader().getResourceAsStream("idiom.json");
        if (in == null) {
            return null;
        }
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
