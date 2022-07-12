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
