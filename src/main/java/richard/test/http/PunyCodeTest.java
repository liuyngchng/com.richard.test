package richard.test.http;

public class PunyCodeTest {

    public static void main(String[] args) {

        String punyCode  = java.net.IDN.toASCII("测试域名.子域名");

        System.out.println("punnyCode  = " + punyCode);
        String unicode = java.net.IDN.toUnicode(punyCode);
        System.out.println("unicode = " + unicode);
    }
}
