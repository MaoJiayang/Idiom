package com.nucleon.util;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nucleon.entity.Idiom;
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
    public static void writeIdiomsToFile(List<Idiom> idioms, String filename) {//idioms是成语列表,filename是文件名
        /*
         * 将成语列表写入文件.成语的格式为:
         * word,notAllowHomophoneNum,allowHomophoneNum
         */
        try (FileWriter writer = new FileWriter(filename)) {
            //先写入表头
            writer.write("word,notAllowHomophoneNum,allowHomophoneNum\n");
            for (Idiom idiom : idioms) {
                String line = idiom.getWord() + "," + idiom.getNotAllowHomophoneNum() + "," + idiom.getAllowHomophoneNum() + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing idioms to file: " + e.getMessage());
        }
    }
    public static void writePinyinZiListMapToFile(Map<String, Set<Character>> map, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<String, Set<Character>> entry : map.entrySet()) {
                String line = entry.getKey() + ",";
                for (Character c : entry.getValue()) {
                    line += c + ",";
                }
                line = line.substring(0, line.length() - 1) + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing map to file: " + e.getMessage());
        }
    }

    public static void writeInitialWordListMapToFile(Map<Character, List<String>> map, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<Character, List<String>> entry : map.entrySet()) {
                String line = entry.getKey() + ",";
                for (String s : entry.getValue()) {
                    line += s + ",";
                }
                line = line.substring(0, line.length() - 1) + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing map to file: " + e.getMessage());
        }
    }

    public static void writeWordIdiomMapToFile(Map<String, Idiom> map, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<String, Idiom> entry : map.entrySet()) {
                String line = entry.getKey() + ",";
                Idiom idiom = entry.getValue();
                line += idiom.getAbbreviation() + ",";
                line += idiom.getDerivation() + ",";
                line += idiom.getExample() + ",";
                line += idiom.getExplanation() + ",";
                line = line.substring(0, line.length() - 1) + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            System.err.println("Error writing map to file: " + e.getMessage());
        }
    }
}
