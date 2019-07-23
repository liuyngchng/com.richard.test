package richard.test.http.async;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class AsyncHttpRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncHttpRequest.class);

    public static void main(String[] argv) {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();

        final CountDownLatch latch = new CountDownLatch(1);
        final HttpGet request = new HttpGet("https://www.baidu.com/");

        LOGGER.info(" caller thread id is : " + Thread.currentThread().getId());

        httpclient.execute(request, new FutureCallback<HttpResponse>() {

            public void completed(final HttpResponse response) {
                latch.countDown();
                LOGGER.info(" callback thread id is : " + Thread.currentThread().getId());
                LOGGER.info(request.getRequestLine() + "->" + response.getStatusLine());
                try {
                    String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                    LOGGER.info(" response content is : " + content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void failed(final Exception ex) {
                latch.countDown();
                LOGGER.info(request.getRequestLine() + "->" + ex);
                LOGGER.info(" callback thread id is : " + Thread.currentThread().getId());
            }

            public void cancelled() {
                latch.countDown();
                LOGGER.info(request.getRequestLine() + " cancelled");
                LOGGER.info(" callback thread id is : " + Thread.currentThread().getId());
            }

        });
        try {
            LOGGER.info("i am waiting for http response.");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            httpclient.close();
        } catch (IOException ignore) {

        }
    }
}