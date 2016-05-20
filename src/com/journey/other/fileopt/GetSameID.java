package com.journey.other.fileopt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 两个文件找相同的ID
 * Created by xiaxiangnan on 16/3/31.
 */
public class GetSameID {

    /**
     * 小文件处理
     */
    public static void smallfile() throws Exception {
        String srcFile1 = "a.txt";
        String srcFile2 = "b.txt";
        String resultFile = "rs.txt";
        List<String> list1 = fileToList(srcFile1);
        List<String> list2 = fileToList(srcFile2);
        //两个list交集
        list1.retainAll(list2);
        list1.forEach(System.out::println);
        listToFile(list1, resultFile);
    }

    public static void main(String[] args) throws Exception {
        smallfile();
    }

    /**
     * 读文件(小文件)内容返回list
     *
     * @param file 文件名
     * @return list
     * @throws Exception
     */
    public static List<String> fileToList(String file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> list = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line.trim());
            }
            return list;
        }
    }

    /**
     * list写入文件
     *
     * @param list 文件名
     * @param file filename
     * @throws Exception
     */
    public static void listToFile(List<String> list, String file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            list.forEach((str) -> {
                try {
                    writer.write(str);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }


}
