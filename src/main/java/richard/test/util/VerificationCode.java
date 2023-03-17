package richard.test.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 生成动态验证码图片示例.
 */
public class VerificationCode {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private static int imgWidth = 0;//验证码图片的宽度
    private static int imgHeight = 0;//验证码图片的高度
    private static int codeCount = 0;//验证码的个数
    private static int x = 0;
    private static int fontHeight;
    private static int codeY;
    private static String fontStyle;
    private static final long serialVersionUID = 128554012633034503L;

    public static void test(){
        init();
        try {
//            response = ServletActionContext.getResponse();
//            request = ServletActionContext.getRequest();
//            response.setCharacterEncoding("UTF-8");
//            request.setCharacterEncoding("UTF-8");
//            response.setContentType("text/html;charset=UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 初始化配置参数
     */
    private static void init(){
        String strWidth = "100";            // 宽度
        String strHeight ="30";             // 高度
        String strCodeCount = "4";          // 字符个数

        fontStyle = "Times New Roman";

        // 将配置的信息转换成数值
        try {
            imgWidth = Integer.parseInt(strWidth);
            imgHeight = Integer.parseInt(strHeight);
            codeCount = Integer.parseInt(strCodeCount);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        x = imgWidth / (codeCount + 1);
        fontHeight = imgHeight - 2;
        codeY = imgHeight - 12;
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        HttpSession session = request.getSession();
        BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = getGraph2D(image);
        String sRand = addNum(g);
        // 图象生效
        g.dispose();
        session.setAttribute("rand", sRand);
        ServletOutputStream responseOutputStream = response.getOutputStream();
        // 输出图象到页面
        ImageIO.write(image, "JPG", responseOutputStream);
        //
        // // 以下关闭输入流！
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    /**
     * 生成2D图形
     * @param image
     * @return
     */
    private static Graphics2D getGraph2D(BufferedImage image) {
        Graphics2D g = image.createGraphics();      // 获取图形上下文
        g.setColor(Color.WHITE);                    // 设定背景色
        g.fillRect(0, 0, imgWidth, imgHeight);
        // 设定字体
        g.setFont(new Font(fontStyle, Font.PLAIN + Font.ITALIC, fontHeight));
        g.setColor(new Color(55, 55, 12));  // 画边框
        g.drawRect(0, 0, imgWidth - 1, imgHeight - 1);


        g.setColor(getRandColor(160, 200));
        drawLines(g);
        return g;
    }

    /**
     * 随机产生100条干扰线，使图像中的认证码不易被自动识别
     * @param g
     */
    private static void drawLines(Graphics2D g) {
        Random random = new Random();               // 生成随机类
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(imgWidth);
            int y = random.nextInt(imgHeight);
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
    private static String addNum(Graphics2D g) {
        Random random = new Random();               // 生成随机类
        String sRand = "";
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < codeCount; i++) {
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
                    retWord = getLowerOrUpperChar(0);
                    break;
                case 2:
                    retWord = getLowerOrUpperChar(1);
                    break;
            }
            sRand += String.valueOf(retWord);
            g.setColor(new Color(red, green, blue));
            //关系到验证码是否居中显示。要根据大小进行调整。
            g.drawString(String.valueOf(retWord), (i) * x + 10, codeY + 7);
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

    /**
     * 示范
     */
    public void execute(){
        try {
            processRequest(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Object> getNumGraph() {
        List<Object> list = new ArrayList<>(2);
        init();
        BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = getGraph2D(image);
        String sRand = addNum(g);
        System.out.println("number=" + sRand);
        g.dispose();
        list.add(sRand);
        list.add(image);
        return list;
    }

    public static void main(String[] args) {

        List<Object> list = getNumGraph();
        if (list.get(0).getClass().equals(String.class)) {
            System.out.println("num=" + (String)list.get(0));
        }
        BufferedImage image = null;
        if (list.get(1).getClass().equals(BufferedImage.class)) {
            image = (BufferedImage)list.get(1);
        }
        if (null == image) {
            return;
        }
        try {
            File file = new File("test.jpg");
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ImageIO.write(image, "JPG", fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
