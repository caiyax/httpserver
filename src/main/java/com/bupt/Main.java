package com.bupt;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Main {
    private static final int BOSS_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    private static EventLoopGroup boss = new NioEventLoopGroup(BOSS_SIZE);
    private static EventLoopGroup work = new NioEventLoopGroup();
    public static void main(String[] args) {
        int port=8080;
        try {
            ServerBootstrap b=new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInit());
            ChannelFuture future=b.bind(port).sync();
            Channel channel=future.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }
}
