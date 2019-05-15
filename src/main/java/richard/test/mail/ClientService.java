package richard.test.mail;

public class ClientService {
    private static ClientService clientService = new ClientService();
    public ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }

    private String name = "My name is ClientService";

    public String getName(){
        return this.name;
    }
}