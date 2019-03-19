package richard.test.fileresize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by richard on 6/21/18.
 */
public class ImageResizer {

    /***
     * 功能 :调整图片大小 开发：wuyechun 2011-7-22
     * @param srcImgPath 原图片路径
     * @param distImgPath  转换大小后图片路径
     * @param width   转换后图片宽度
     * @param height  转换后图片高度
     */
    public static void resizeImage(String srcImgPath, String distImgPath,
                                   int width, int height) throws IOException {

        File srcFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage buffImg = null;
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffImg.getGraphics().drawImage(
            srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0,
            0, null);

        ImageIO.write(buffImg, "JPEG", new File(distImgPath));

    }

    public static final void main(String[] args) throws Exception {
//        resizeImage("/Users/richard/Desktop/sample.jpg", "/Users/richard/Desktop/10M.jpg", 10240, 17000);
//        File file = new File("/Users/richard/Desktop/test");
//        FileWriter fileWriter = new FileWriter(file);
//        for (int i = 0; i < 10*1024*1024; i ++) {
//            fileWriter.write(new char[]{'a'});
//        }
//        fileWriter.close();


        RandomAccessFile raf = null;
        try {
            File filename = new File("/Volumes/Extend/poc/client/10M.jpg");
            long length = filename.length();
            //建立一个指定大小的空文件
            raf = new RandomAccessFile("/Volumes/Extend/poc/client/10M.jpg", "rw");
            raf.setLength(length*10);
            raf.seek(123L);
            System.out.println( System.currentTimeMillis() );
        } catch (Exception e) {
        } finally {
            if ( raf != null ) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
    }

}
