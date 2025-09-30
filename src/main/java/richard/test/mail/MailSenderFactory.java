package richard.test.mail;

/**
 * 发件箱工厂
 *
 * @author MZULE
 *
 */
public class MailSenderFactory {

    /**
     * 服务邮箱
     */
    private static MailSender serviceSms = null;

    /**
     * 获取邮箱
     *
     * @return 符合类型的邮箱
     */
    public static MailSender getSender(String userName, String password) {
        if (serviceSms == null) {
            serviceSms = new MailSender(userName, password);
        }
        return serviceSms;
    }

}