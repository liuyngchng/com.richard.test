package richard.test;

import com.alibaba.fastjson.JSONObject;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.jsoup.Jsoup;
import java.io.*;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * 将非maven项目转换为maven项目
 */
public class PomGenerator {
    public static void main(String[] args) throws IOException {
        File file = new File("pomDependency.xml");
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        Element dependencies = new DOMElement("dependencies");
        File dir = new File("/home/rd/workspace/access.client/lib");        //需生成pom.xml 文件的 lib路径
        for (File jar : dir.listFiles()) {
            dependencies.add(readFile(jar));
        }
        FileWriter writer = new FileWriter(file);
        writer.write(dependencies.asXML().replace("><", ">\r\n<"));
        writer.close();
        System.out.println("finished, pom output as file " + file.getName());
    }

    /**
     * Read jar file.
     * @param jar
     * @return
     * @throws IOException
     */
    public static Element readFile(File jar) throws IOException {
        JarInputStream jis = new JarInputStream(new FileInputStream(jar));
        Manifest mainmanifest = jis.getManifest();
        jis.close();
        String bundleName = mainmanifest.getMainAttributes().getValue("Bundle-Name");
        String bundleVersion = mainmanifest.getMainAttributes().getValue("Bundle-Version");
        Element ele = null;
//            System.out.println(jar.getName());
        StringBuffer sb = new StringBuffer(jar.getName());
        if (bundleName != null) {
            bundleName = bundleName.toLowerCase().replace(" ", "-");
            sb.append(bundleName+"\t").append(bundleVersion);
            ele = getDependencies(bundleName, bundleVersion);
//                System.out.println(sb.toString());
//                System.out.println(ele.asXML());
        }
        if (ele == null || ele.elements().size() == 0) {
            bundleName = "";
            bundleVersion = "";
            String[] ns = jar.getName().replace(".jar", "").split("-");
            for (String s : ns) {
                if (Character.isDigit(s.charAt(0))) {
                    bundleVersion += s + "-";
                } else {
                    bundleName += s + "-";
                }
            }
            if (bundleVersion.endsWith("-")) {
                bundleVersion = bundleVersion.substring(0, bundleVersion.length() - 1);
            }
            if (bundleName.endsWith("-")) {
                bundleName = bundleName.substring(0, bundleName.length() - 1);
            }
            ele = getDependencies(bundleName, bundleVersion);
            sb.setLength(0);
            sb.append(bundleName+"\t").append(bundleVersion);
//                System.out.println(sb.toString());
            System.out.println("found element:" +ele.asXML());
        }

        ele = getDependencies(bundleName, bundleVersion);
        if (ele.elements().size() == 0) {
            ele.add(new DOMElement("groupId").addText("not found"));
            ele.add(new DOMElement("artifactId").addText(bundleName));
            ele.add(new DOMElement("version").addText(bundleVersion));
            ele.add(new DOMElement("scope").addText("system"));
            ele.add(new DOMElement("systemPath").addText("${basedir}/lib/" + jar.getName()));
        }
//        System.out.println();
        return ele;
    }

    public static Element getDependencies(String key, String ver) {
        Element dependency = new DOMElement("dependency");
        // 设置代理
        // System.setProperty("http.proxyHost", "127.0.0.1");
        // System.setProperty("http.proxyPort", "8090");
        try {
            String url = "http://search.maven.org/solrsearch/select?q=a%3A%22" + key + "%22%20AND%20v%3A%22" + ver + "%22&rows=3&wt=json";
            org.jsoup.nodes.Document doc = Jsoup.connect(url).ignoreContentType(true).timeout(30000).get();
            String elem = doc.body().text();
            JSONObject response = JSONObject.parseObject(elem).getJSONObject("response");
            if (response.containsKey("docs") && response.getJSONArray("docs").size() > 0) {
                JSONObject docJson = response.getJSONArray("docs").getJSONObject(0);
                Element groupId = new DOMElement("groupId");
                Element artifactId = new DOMElement("artifactId");
                Element version = new DOMElement("version");
                groupId.addText(docJson.getString("g"));
                artifactId.addText(docJson.getString("a"));
                version.addText(docJson.getString("v"));
                dependency.add(groupId);
                dependency.add(artifactId);
                dependency.add(version);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dependency;
    }
}