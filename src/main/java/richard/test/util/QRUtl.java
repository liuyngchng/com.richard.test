package richard.test.util;

//<dependency>
//    <groupId>com.google.zxing</groupId>
//    <artifactId>javase</artifactId>
//    <version>3.5.0</version>
//</dependency>
//<dependency>
//    <groupId>org.apache.xmlgraphics</groupId>
//    <artifactId>batik-svggen</artifactId>
//    <version>1.14</version>
//</dependency>

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUnits;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

public class QRUtl {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void genQRCdImg(String txt, int width, int height, String filePath,
                                  HashMap hints) throws Exception {
        final QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(txt, BarcodeFormat.QR_CODE, width, height, hints);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "png", path);
    }

    public static String genQRCdSvg(String txt) throws Exception {
        return QRUtl.genQRCdSvg(txt, 100, 100, null);
    }

    public static String genQRCdSvg(String txt, int width, int height,
                                    HashMap hints) throws Exception {
        final QRCodeWriter writer = new QRCodeWriter();
        final BitMatrix bmx = writer.encode(txt, BarcodeFormat.QR_CODE, width, height, hints);
        final MatrixToImageConfig config = new MatrixToImageConfig();
        return QRUtl.toSvgDocument(bmx, config);
    }

    private static String toSvgDocument(BitMatrix matrix, MatrixToImageConfig config) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        SVGGraphics2D g = new SVGGraphics2D(200, 200, SVGUnits.PX);
        g.setColor(new Color(config.getPixelOffColor()));
        g.fillRect(0,0, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x,y)) {
                    g.setColor(new Color(config.getPixelOnColor()));
                    g.fillRect(x, y, 1, 1);
                }
            }
        }
        g.dispose();
        return g.getSVGElement("svg");
    }


    public static void prsQRCodeImg(String filePath, HashMap hints)
        throws Exception {
        QRCodeReader qrCodeReader = new QRCodeReader();
        File file = new File(filePath);
        BufferedImage bufferedImage = ImageIO.read(file);
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(luminanceSource);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Result result = qrCodeReader.decode(binaryBitmap);
        System.out.println(result.getText());
    }
}
