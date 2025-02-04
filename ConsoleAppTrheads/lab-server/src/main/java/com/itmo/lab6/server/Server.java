package com.itmo.lab6.server;


import com.itmo.lab6.common.util.Request;
import com.itmo.lab6.common.util.Response;
import com.itmo.lab6.common.util.Serializer;
import com.itmo.lab6.server.db.DBManager;
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
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final CollectionManager collectionManager = new CollectionManager();
    private static final DBManager dbManager = new DBManager();
    private static final CommandManager commandManager = new CommandManager(collectionManager, dbManager);
    private static final CommandExecutor commandExecutor = new CommandExecutor(commandManager, dbManager);
    private static DatagramChannel server;
    private static int host;

    // Пулы потоков
    private static final ExecutorService requestReaderPool = Executors.newCachedThreadPool();
    private static final ForkJoinPool requestHandlerPool = new ForkJoinPool(4); 
    private static final ForkJoinPool responseSenderPool = new ForkJoinPool(4); 

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            host = Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            logger.warning("Wrong parameter for host\nTerminating server...");
            System.exit(1);
        }

        try {
            new Thread(() -> {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    try {
                        String input = consoleReader.readLine().trim();
                        if (input.equals("exit")) {
                            logger.info("Terminating server...");
                            System.exit(0);
                        }
                    } catch (Exception exception) {
                        logger.warning("Error reading console input: " + exception.getMessage());
                    }
                }
            }).start();
        } catch (Exception exception) {
            logger.severe("Error starting console reader thread: " + exception.getMessage());
        }

        server = startServer();
        logger.info(String.format("Server started at: %s\nOn port: %d", LocalDateTime.now(), host));

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

        if (remoteAddress != null) {
            getBuffer.flip();

            
            requestReaderPool.execute(() -> {
                try {
                    Request request = Serializer.deserializeRequest(getBuffer);
                    logger.info(String.format("Request received from: %s\nRequest: %s", remoteAddress.toString(), request.toString()));

                    requestHandlerPool.submit(() -> {
                        try {
                            Response response = commandExecutor.execute(request);

                            responseSenderPool.execute(() -> {
                                try {
                                    ByteBuffer responseBuffer = Serializer.serializeResponse(response);
                                    server.send(responseBuffer, remoteAddress);
                                    logger.info(String.format("Response sent to: %s\nResponse: %s", remoteAddress.toString(), response.toString()));
                                } catch (IOException e) {
                                    logger.severe("Error sending response: " + e.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            logger.severe("Error handling request: " + e.getMessage());
                        }
                    });
                } catch (ClassNotFoundException e) {
                    logger.severe("Error deserializing request: " + e.getMessage());
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        } else {
            logger.warning("Client address is null");
        }
    }

    public static DatagramChannel openChannel() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        return channel;
    }

    public static DatagramChannel bindChannel(SocketAddress address) throws IOException {
        return openChannel().bind(address);
    }
}
