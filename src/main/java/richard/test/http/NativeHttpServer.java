package richard.test.http;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by richard on 25/03/2019.
 */
public class NativeHttpServer {
    public static void serverStart() throws IOException {
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(8080), 100);
        //监听端口8080,

        httpserver.createContext("/t", new RestGetHandler());
        httpserver.setExecutor(null);
        httpserver.start();
        System.out.println("server started");
    }

    public static void main(String[] args) throws IOException {
        serverStart();
    }
}
