//package richard.test.jmeter;
//
//import org.apache.jmeter.protocol.tcp.sampler.ReadException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.*;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingDeque;
//
///**
// * Jmeter 性能测试 TCP 取样器实现.
// * 1.TCP 性能测试实现步骤
// *  1.1 实现接口。实现 org.apache.jmeter.protocol.tcp.sampler.TCPClient
// *  1.2 打JAR包。 打jar包，放在 Jmeter_setup_dir/lib/ext 下
// *  1.3 配置实现类类名。使用Jmeter 的GUI 界面，在 TCP Plan/Thread group/Tcp sample/下的
// *      TCPClient classname 填写自己实现类的类名，例如 richard.test.TcpClient.
// *  1.4 保存JMX文件。配置好线程数，host，port，生成JMX 文件；
// *  1.5 运行。运行非GUI 进程，jmeter -n -t [jmx file] -l [results file] -e -o [Path to web report folder]
// * 2. 接口 org.apache.jmeter.protocol.tcp.sampler.TCPClient 说明
// *  2.1 入参。JMX 中配置的 TCP 发送的文本，通过方法 write(OutputStream os, String s) 传入，
// *      s 为 JMX 中的字符串，OutputStream 为 TCP 客户端的输出流。
// *  2.2 出参。将发送的文本写入 TCP 流之后，就开始执行 read(InputStream is, org.apache.jmeter.samplers.SampleResult sr)
// *      其中的 InputStream 为 TCP 客户端的输入流
// * @author Richard Liu
// * @since 2018.08.03
// */
//public class TcpClient implements org.apache.jmeter.protocol.tcp.sampler.TCPClient {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
//
////    private BlockingQueue<String> contentQueue = new LinkedBlockingDeque<String>(1024);
////
////    private class Consumer implements Runnable {
////        private final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);
////        public void run() {
////            System.out.println("consumer started to echo message received.");
////            while (true) {
////                try {
////                    String content = contentQueue.take();
////                    System.out.printf("client received echo back [%s]\r\n", content);
////
////                } catch ( InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
//    public void setupTest() {
//        System.out.println("setup test");
////        LOGGER.info("setup Test");
////        new Thread(new Consumer()).start();
//
//    }
//
//    public void teardownTest() {
//        System.out.println("tear down Test");
////        LOGGER.info("tear down Test");
//    }
//
//    public void write(OutputStream outputStream, InputStream inputStream) throws IOException {
//        System.out.println("write os, is");
////        LOGGER.info("write os, is");
//        DataInputStream dis = new DataInputStream(inputStream);
//        DataOutputStream dos = new DataOutputStream(outputStream);
//        String read;
//        while (true) {
//            read = dis.readUTF();
//            LOGGER.info("readUTF {}", read);
//            dos.writeUTF(read);
//        }
//    }
//
//
//    /**
//     * implement your biz logic.
//     * @param outputStream
//     * @param s
//     * @throws IOException
//     */
//    public void write(OutputStream outputStream, String s) throws IOException {
//        System.out.printf("write string to outputStream, %s\r\n", s);
////        LOGGER.info("write");
//        DataOutputStream dos = new DataOutputStream(outputStream);
//        dos.writeBytes(s);
//        dos.close();
//    }
//
//    public String read(InputStream inputStream) throws ReadException {
//        System.out.println("read inputStream");
////        LOGGER.info("read inputStream");
//        DataInputStream dis = new DataInputStream(inputStream);
//        StringBuffer sb = new StringBuffer(512);
//        byte[] buffer = new byte[512];
//        int byteCount;
//        while (true) {
//            try {
//                byteCount = dis.read(buffer);
//                if (byteCount > 0 ) {
//                    sb.append(new String(buffer, 0, byteCount));
//                } else {
//                    System.out.println("stream eof.");
////                    LOGGER.info("stream eof.");
//                    break;
//                }
//            } catch (IOException e) {
//                LOGGER.error("error", e);
//                break;
//            }
//        }
//        return sb.toString();
//    }
//
//    public String read(InputStream inputStream, org.apache.jmeter.samplers.SampleResult sampleResult) throws ReadException {
//        System.out.println("read input stream and sample result");
////        try {
////            Thread.sleep(10000L);
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        LOGGER.info("read input stream and sample result, result is {}", sampleResult.getResponseData().toString());
//        DataInputStream dis = new DataInputStream(inputStream);
//        StringBuffer sb = new StringBuffer(512);
//        byte[] buffer = new byte[512];
//        int byteCount;
//        while (true) {
//            try {
//                byteCount = dis.read(buffer);
//                if (byteCount > 0 ) {
//                    System.out.printf("received [%s]\r\n", new String(buffer, 0 , byteCount));
////                    contentQueue.add(new String(buffer, 0, byteCount));
//                } else {
//                    System.out.println("stream eof.");
////                    LOGGER.info("stream eof.");
//                    break;
//                }
//            } catch (IOException e) {
//                LOGGER.error("error", e);
//                break;
//            }
//        }
//
//        return sb.toString();
//    }
//
//    public byte getEolByte() {
//        System.out.println("getEolByte");
////        LOGGER.info("getEolByte");
//        return 0;
//    }
//
//    public String getCharset() {
//        System.out.println("getCharset");
////        LOGGER.info("getCharset");
//        return null;
//    }
//
//    public void setEolByte(int i) {
//        System.out.println("setEolByte");
////        LOGGER.info("setEolByte");
//
//    }
//}
