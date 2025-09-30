package richard.test.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author H__D
 * @date 2016年12月6日 下午7:01:27
 *
 */
public class MailTest1 {

    /**
     * 使用Transport 非静态方法 发送邮件
     * 连接163服务，给QQ邮箱发邮件
     */
    public static void main(String[] args) throws Exception {

        // 属性
        Properties properties = new Properties();
        // 设置认证属性
        properties.setProperty("mail.smtp.auth", "true");
        // 设置通信协议
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.host", "smtp.163.com");
        // 邮件环境信息
        Session session = Session.getInstance(properties);
        // 调试,打印信息
        session.setDebug(true);

        // 邮件
        Message message = new MimeMessage(session);
        // 主题
        message.setSubject("mail message");
        // 发送人
        message.setFrom(new InternetAddress("liuyngchng@163.com"));
        // 内容
        message.setText("this is content");

        // 邮件传输对象
        Transport transport = session.getTransport();
        // 传输连接：host，port，user，pass/主机，端口，用户名，密码
        transport.connect("smtp.163.com", 25, "liuyngchng@163.com", "P@$$W0rdAcHeNg");
        // 发送邮件
        transport.sendMessage(message, new Address[] { new InternetAddress("liuyngchng@163.com") });

        // 关闭连接
        transport.close();
    }
}
