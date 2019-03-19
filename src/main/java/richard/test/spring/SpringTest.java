package richard.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by richard on 18/03/2019.
 */
public class SpringTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        User user = (User)context.getBean("user");

        Order order = user.getOrder();

        final User user1 = new User();
        Order order1 = user1.getOrder();
    }

}
