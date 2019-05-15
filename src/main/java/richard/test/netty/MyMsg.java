package richard.test.netty;

/**
 * Created by richard on 18/04/2019.
 */
// 消息的主体
public class MyMsg {

    private MsgHeader msgHeader;
    private String content;

    public MyMsg(MsgHeader msgHeader, String content) {
        this.msgHeader = msgHeader;
        this.content = content;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("[version=%d,contentLength=%d,sessionId=%s,content=%s]",
            msgHeader.getVersion(),
            msgHeader.getContentLength(),
            msgHeader.getSessionId(),
            content);
    }
}
