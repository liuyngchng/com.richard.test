package richard.test.pojo;

/**
 * Created by Richard on 8/11/17.
 */
public class SimpleMail {

    public SimpleMail(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    private String subject;

    private String content;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
