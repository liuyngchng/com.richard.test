package richard.test.classloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by richard on 19/03/2019.
 */
public class Test {

    private static final Logger LOGGER = LoggerFactory.getLogger(Test.class);

    public static void main(String[] arg){

        ClassLoader c  = Test.class.getClassLoader();  //获取Test类的类加载器

        System.out.println(c);

        ClassLoader c1 = c.getParent();  //获取c这个类加载器的父类加载器

        System.out.println(c1);

        ClassLoader c2 = c1.getParent();//获取c1这个类加载器的父类加载器

        System.out.println(c2);

        FileSystemClassLoader fileSystemClassLoader = new FileSystemClassLoader("/Users/richard/workspace/com.richard.test/target/richard.test-1.0-SNAPSHOT.jar");
        try {
            Class<?> clazz = fileSystemClassLoader.loadClass("richard.test.classloader.MyLoadClassTest");
            Object object = clazz.newInstance();
            fileSystemClassLoader.clearAssertionStatus();
            Field[] files=clazz.getDeclaredFields();
            LOGGER.info("field name is {}", files[0].getName());
            LOGGER.info("class is {}", clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}