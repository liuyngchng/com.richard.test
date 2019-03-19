package richard.test.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by richard on 3/16/18.
 */
public class TestProtobuf {

    public static void main(String[] args) throws Exception {
        GpsData.gps_data.Builder gps_builder = GpsData.gps_data.newBuilder();
        gps_builder.setAltitude(10);
        gps_builder.setDirection(20L);
        gps_builder.setId(10L);
        GpsData.gps_data gps_data = gps_builder.build();
        System.out.println(gps_data.toString());
        System.out.println("===== 构建GPS模型结束 =====");
        GpsInfo info = new GpsInfo();
        info.setAltitude(10);
        info.setDirection(20L);
        info.setId(10L);
//        System.out.printf("gpsInfo string is %s", info.toString());
        System.out.println("===== gps Byte 开始=====");
        byte[] pbArray = gps_data.toByteArray();
//        FileOutputStream fs = new FileOutputStream("gpsInfo");
//        ObjectOutputStream os = new ObjectOutputStream(fs);
//        os.writeObject(info);
//        os.close();
//        fs.close();
//        fs = new FileOutputStream("gps_data");
//        os = new ObjectOutputStream(fs);
//        os.writeObject(gps_data);
//        os.close();
//        fs.close();
        System.out.printf(
            "pbArray length is %s\n",
            pbArray.length
        );
        for(byte b : pbArray){
            System.out.print(b + " ");
        }
        System.out.println("\n" + "bytes长度" + gps_data.toByteString().size());
        System.out.println("===== gps Byte 结束 =====");

        System.out.println("===== 使用gps 反序列化生成对象开始 =====");
        GpsData.gps_data gd = null;
        try {
            gd = GpsData.gps_data.parseFrom(gps_data.toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.print(gd.toString());
        System.out.println("===== 使用gps 反序列化生成对象结束 =====");
    }
}
