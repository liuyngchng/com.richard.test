package richard.test;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 将没有通过maven pom引入的Jar包安装在本地，不再需要在pom中通过
 * <scope>system</scope>
 * <systemPath></systemPath>
 * 进行引入
 * mvn install:install-file
 * -Dfile=/home/rd/workspace/access.client/lib/com.cetc28.tracer.agent.http-2.6.0.jar
 * -DgroupId=com.cetccloud
 * -DartifactId=com.cetc28.tracer.agent.http
 * -Dversion=1.0.0 -Dclassifier=core
 * -Dpackaging=jar
 */
public class MavenJarInstaller {
    public static void main(String[] args) throws IOException {
        File shFile = new File("mvn_jar_install.sh");
        if (shFile.exists()) {
            shFile.delete();
        } else {
            shFile.createNewFile();
        }
        File dir = new File("/home/rd/workspace/access.client/lib");        //需生成pom.xml 文件的 lib路径
        StringBuilder sb = new StringBuilder();
        for (File jar : dir.listFiles()) {
            sb.append(buildCmd(jar));
        }
        FileWriter writer = new FileWriter(shFile);
        writer.write(sb.toString());
        writer.close();
        System.out.print("finished, pom output as file " + shFile.getName());
    }

    /**
     * Read jar file.
     * @param jar
     * @return
     * @throws IOException
     */
    public static String buildCmd(File jar) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("mvn install:install-file -Dfile=");
        sb.append(jar.getAbsolutePath());
        sb.append(" -DgroupId=com.richard");
        sb.append(" -DartifactId=");
        sb.append(jar.getName().replace(".jar", ""));
        sb.append(" -Dversion=1.0.0 -Dclassifier=core -Dpackaging=jar");
        sb.append("\r\n");
        String str = sb.toString();
        System.out.print(str);
        return str;
    }

}