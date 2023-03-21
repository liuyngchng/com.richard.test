package richard.test.util;

import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUnits;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成SVG格式的动态验证码图片示例.
 */
public class VerificationCodeSvg {
    private static int imgWidth = 0;//验证码图片的宽度
    private static int imgHeight = 0;//验证码图片的高度
    private static int codeCount = 0;//验证码的个数
    private static int codeXOffset = 18;
    private static int codeYoffset = 1;
    private static int x = 0;
    private static int fontHeight;
    private static int codeY;
    private static String fontStyle;
    private static final long serialVersionUID = 128554012633034503L;

    private static void init(int imgWidth, int imgHeight, int codeCount){
        fontStyle = "Times New Roman";
        VerificationCodeSvg.imgWidth = imgWidth;
        VerificationCodeSvg.imgHeight = imgHeight;
        VerificationCodeSvg.codeCount = codeCount;
        x = imgWidth / (codeCount + 1);
        fontHeight = imgHeight - 2;
        codeY = imgHeight - 12;
    }

    /**
     * 初始化配置参数
     */
    private static void init(){
        VerificationCodeSvg.init(100, 30, 4);
    }



    /**
     * 生成2D SVG图形
     * @return
     */
    private static SVGGraphics2D getGraph2D() {
        final SVGGraphics2D svg = new SVGGraphics2D(imgWidth, imgHeight, SVGUnits.PX);
        svg.setColor(Color.WHITE);                    // 设定背景色
        svg.fillRect(0, 0, imgWidth, imgHeight);
        // 设定字体
        svg.setFont(new Font(fontStyle, Font.PLAIN + Font.ITALIC, fontHeight));
        svg.setColor(new Color(55, 55, 12));  // 画边框
        svg.drawRect(0, 0, imgWidth - 1, imgHeight - 1);
        svg.setColor(getRandColor(160, 200));
        VerificationCodeSvg.drawLines(svg);
        return svg;
    }


    /**
     * 随机产生100条干扰线，使图像中的认证码不易被自动识别
     * @param g
     */
    private static void drawLines(SVGGraphics2D g) {
        Random random = new Random();               // 生成随机类
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(VerificationCodeSvg.imgWidth);
            int y = random.nextInt(VerificationCodeSvg.imgHeight);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
    }

    /**
     * 获取随机产生的认证码(4位数字)
     * @param g
     * @return
     */
    private static String addNum(SVGGraphics2D g) {
        Random random = new Random();               // 生成随机类
        String sRand = "";
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < VerificationCodeSvg.codeCount; i++) {
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            int wordType = random.nextInt(3);
            char retWord = 0;
            switch (wordType) {
                case 0:
                    retWord = getSingleNumberChar();
                    break;
                case 1:
                    retWord = getUpperChar(0);
                    break;
                case 2:
                    retWord = getUpperChar(1);
                    break;
            }
            sRand += String.valueOf(retWord);
            g.setColor(new Color(red, green, blue));
            //关系到验证码是否居中显示。要根据大小进行调整。
            g.drawString(String.valueOf(retWord), i * (x + codeXOffset), codeY + codeYoffset);
        }
        sRand=sRand.toLowerCase();
        return sRand;
    }

    private static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    private static char getSingleNumberChar() {
        Random random = new Random();
        int numberResult = random.nextInt(10);
        int ret = numberResult + 48;
        return (char) ret;
    }

    private static char getLowerOrUpperChar(int upper) {
        Random random = new Random();
        int numberResult = random.nextInt(26);
        int ret = 0;
        if (upper == 0) {                   // 小写
            ret = numberResult + 97;
        } else if (upper == 1) {            // 大写
            ret = numberResult + 65;
        }
        return (char) ret;
    }

    private static char getUpperChar(int upper) {
        Random random = new Random();
        int numberResult = random.nextInt(26);
        int ret = numberResult + 65;
        return (char) ret;
    }

    public static List<String> getNumGraph(int imgWidth, int imgHeight, int codeCount) {
        List<String> list = new ArrayList<>(2);
        init(imgWidth, imgHeight, codeCount);
        SVGGraphics2D g = VerificationCodeSvg.getGraph2D();
        String sRand = addNum(g);
        System.out.println("number=" + sRand);
        g.dispose();
        list.add(sRand);
        list.add(g.getSVGElement("svg"));
        return list;
    }


    public static List<String> getNumGraph() {
        return getNumGraph(300, 100, 4);
    }

    public static void main(String[] args) {

        List<String> list = getNumGraph();
        System.out.println("num=" + list.get(0));
        System.out.println("svg=\r\n" + list.get(1));
        try {
            File file = new File("test.svg");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(list.get(1).getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
