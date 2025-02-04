package com.itmo.lab6.client;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;

public class DatagramChannelBuilder {
    public static DatagramChannel openChannel() throws IOException {
        DatagramChannel channel = DatagramChannel.open(); 
        return channel;
    }
    public static DatagramChannel bindChannel(SocketAddress address) throws IOException {
        return openChannel().bind(address);
    }    
}
