package richard.test.gui;

/**
 * Created by richard on 11/03/2019.
 */

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class JFrameWindow extends JFrame{     //需要继承JFrame

    public JFrameWindow(String title)
    {
        JFrame jf = new JFrame(title);
        Container conn = jf.getContentPane();    //得到窗口的容器
        JLabel label1 = new JLabel("Hello,world!");    //创建一个标签 并设置初始内容
        label1.setSize(100,350);
        label1.setLocation(200,500);
        conn.add(label1);

        jf.setBounds(200,200,800,400); //设置窗口的属性 窗口位置以及窗口的大小
        jf.setVisible(true);//设置窗口可见
        jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //设置关闭方式 如果不设置的话 似乎关闭窗口之后不会退出程序
    }

    public static void main(String[] args) {
        new JFrameWindow("窗口");        //创建窗口
    }

}
