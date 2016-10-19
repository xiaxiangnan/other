package com.journey.other.complier;

import java.io.*;

/**
 * EasyClassLoader
 * Created by xiaxiangnan on 16/8/5.
 */
public class EasyClassLoader extends ClassLoader {

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }

    }


    public static void main(String[] args) throws Exception {
        EasyClassLoader loader = new EasyClassLoader();
        Class c1 = loader.loadClass("com.journey.other.complier.EasyClassLoader");
        Class c2 = EasyClassLoader.class;
        System.out.println(c1.getClassLoader());
        System.out.println(c2.getClassLoader());
    }

}
