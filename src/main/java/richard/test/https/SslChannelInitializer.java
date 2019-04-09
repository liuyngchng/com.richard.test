package richard.test.https;


import java.nio.charset.Charset;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import richard.test.netty.HttpServerHandler;

public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;

    public SslChannelInitializer(SslContext context) {
        this.context = context;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        SSLEngine engine = context.newEngine(ch.alloc());
        engine.setUseClientMode(false);
        ch.pipeline().addFirst("ssl", new SslHandler(engine));
        ChannelPipeline pipeline = ch.pipeline();
        ch.pipeline().addLast(new HttpResponseEncoder());
        ch.pipeline().addLast(new HttpRequestDecoder());
        ch.pipeline().addLast(new HttpObjectAggregator(65535));
        pipeline.addLast("serverHandler", new HttpServerHandler());
    }
}