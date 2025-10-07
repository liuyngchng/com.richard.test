package richard.test.mail;

/**
 * Created by Richard on 8/28/16.
 */
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();
    public DefaultServiceLocator() {}

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}