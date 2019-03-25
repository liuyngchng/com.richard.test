package richard.test.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by richard on 18/03/2019.
 */
public class SpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringTest.class);

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        try {
            User user = (User)context.getBean("myUser");
            User user1 = context.getBean(User.class);
            LOGGER.info("user == user1 :{}", user == user1);
        } catch (Exception ex) {
            LOGGER.error("error", ex);
        }

    }

}
