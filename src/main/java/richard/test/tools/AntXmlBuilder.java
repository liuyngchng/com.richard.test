package richard.test.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AntXmlBuilder {

    public static void main(String[] args) throws IOException {
        File file = new File("ant_build_input.xml");
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        String fileDir = "/home/rd/workspace/access.client/lib";
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<project name=\"com.richard\"  basedir=\"");
        sb.append(fileDir);
        sb.append("\" default=\"makeSuperJar\" >\n" +
                "    <target name=\"makeSuperJar\" description=\"description\" >\n" +
                "        <jar destfile=\"com.richard.jar\" >\n");
        File dir = new File(fileDir);        //需生成pom.xml 文件的 lib路径
        for (File jar : dir.listFiles()) {
            sb.append("           <zipfileset src=\"");
            sb.append(jar.getName());
            sb.append("\" />\n");
        }
        sb.append("        </jar>\n" +
                "    </target>\n" +
                "</project>");
        FileWriter writer = new FileWriter(file);
        writer.write(sb.toString());
        writer.close();
        System.out.println("finished, ant build xml file  output as: " + file.getName());
    }
}
