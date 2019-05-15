package richard.test.spring;

import org.springframework.stereotype.Component;

/**
 * Created by richard on 18/03/2019.
 */

@Component
public class Order {

    private String orderId;

    private String type;

    private String price;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
