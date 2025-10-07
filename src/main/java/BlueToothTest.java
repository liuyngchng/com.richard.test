import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * 128_bit_value = 16_bit_value * 2^96 + Bluetooth_Base_UUID
 * 2^96 = 2^4^24,也就是16进制左移24位
 * http://t.zoukankan.com/jiangu66-p-3170403.html
 * 16bit uuid for members
 * https://btprodspecificationrefs.blob.core.windows.net/assigned-values/16-bit%20UUID%20Numbers%20Document.pdf
 *
 * https://www.bluetooth.org/DocMan/handlers/DownloadDoc.ashx?doc_id=521059
 *
 * #蓝牙串口服务
 * SerialPortServiceClass_UUID = ‘{00001101-0000-1000-8000-00805F9B34FB}’
 * 自定义蓝牙服务时要申请UUID,每个 UUID 2,500 美元.
 * https://www.bluetooth.com/specifications/assigned-numbers/
 * 蓝牙服务UUID以及服务特征字段，在蓝牙服务交互过程中起着非常重要的作用，而SIG标准中允许用户自定义服务，采用128位完成蓝牙服务，
 * 以及128位特征字段定义。在实际应用中，通常不同厂商的蓝牙模块都会各自定义蓝牙服务以及特征字，这就导致了市面上不同厂商生产的蓝牙模块相互间无法通讯。
 * 为了更好的兼容不同厂商的蓝牙服务定义，蓝牙模块需要支持服务自定义配置功能，通过支持自定义蓝牙服务和特征字兼容不同的蓝牙定义，从而实现不同厂商模块间的互通。
 */
public class BlueToothTest {

    public static void main(String[] args) {

        String blueToothBaseUUID = "00000000-0000-1000-8000-00805F9B34FB";
        BigInteger a = new BigInteger("1000800000805F9B34FB", 16);

        String sdp = "0x0001";
        int sdpNum = 1; //Integer.parseInt(sdp);

        double b = Math.pow(2, 96);
        BigDecimal bigDecimal = new BigDecimal(b);
        BigDecimal result = bigDecimal.add(new BigDecimal(a));
        System.out.println(result);




    }
}
