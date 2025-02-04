package com.itmo.lab6.server;


import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.Serializer;
import com.itmo.lab6.server.managers.CSVManager;
import com.itmo.lab6.server.managers.CollectionManager;
import com.itmo.lab6.server.managers.CommandExecutor;
import com.itmo.lab6.server.managers.CommandManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.LocalDateTime;
import java.util.logging.Logger;



public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final CollectionManager collectionManager = new CollectionManager();
    private static final CommandManager commandManager = new CommandManager(collectionManager);
    private static final CommandExecutor commandExecutor = new CommandExecutor(commandManager);
    private static DatagramChannel server;
    private static int host;
    private static String filePath;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        filePath = args[0];
        try {
            host = Integer.parseInt(args[1]);
        }catch (NumberFormatException exception) {
            logger.warning("wrong parametr for host\nterminating server...");
        }
        if (filePath.isEmpty()) {
            logger.warning("empty path to file\nterminating server");
            System.exit(0);
        }
        CSVManager.load(filePath, collectionManager);
        logger.info("load collection from file");
        collectionManager.setLastInitTime();
            try {
            new Thread(() -> {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in)); 
                while (true) {
                    try {
                        String input = consoleReader.readLine().trim();
                        if (input.equals("exit")) {
                            logger.info("terminating server...");
                            System.exit(0);
                        } else if (input.equals("save")) {
                            CSVManager.writeCollection(args[0], collectionManager);
                            logger.info("saving collection to the file");
                            collectionManager.setLastSaveTime();
                        } 
                    } catch (Exception exception) {

                    }
                }
            }).start();
            } catch (Exception exception) {}
        server = startServer();
        logger.info(String.format("server started at: %s\non port: %d", LocalDateTime.now() + " ", host));
        while (true) {
            getAndSend();
        }
    }
    public static DatagramChannel startServer() throws IOException {
        InetSocketAddress address = new InetSocketAddress("localhost", host); 
        DatagramChannel server = bindChannel(address);
        return server;
    }

    public static void getAndSend() throws IOException, ClassNotFoundException {
        ByteBuffer getBuffer = ByteBuffer.allocate(4096);
        SocketAddress remoteAddress = server.receive(getBuffer);
        Request request = Serializer.deserializeRequest(getBuffer);
        logger.info(String.format("request from server: %s", request.toString()));
        Response response = commandExecutor.execute(request);
        logger.info(String.format("sending response for client: %s", response.toString()));
        ByteBuffer responseBuffer = Serializer.serializeResponse(response);
        server.send(responseBuffer, remoteAddress);
    }

    public static DatagramChannel openChannel() throws IOException {
        DatagramChannel channel = DatagramChannel.open(); 
        return channel;
    }
    public static DatagramChannel bindChannel(SocketAddress address) throws IOException {
        return openChannel().bind(address);
    }  
}
