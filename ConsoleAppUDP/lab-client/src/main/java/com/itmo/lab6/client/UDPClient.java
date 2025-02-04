package com.itmo.lab6.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

import com.itmo.lab6.common.models.Person;
import com.itmo.lab6.common.util.*;
import com.itmo.lab6.common.models.creation.NameValidator;


public class UDPClient {
    private static Scanner scanner = new Scanner(System.in);
    private static DatagramChannel client;
    private static InetSocketAddress address;
    private static int port;

    public static void start() throws ClassNotFoundException {
        while (true) {
            try {
                client = startClient();
                address = new InetSocketAddress("localhost", port);
                System.out.println("everything is working fine! Welcome!");
                break; 
            } catch (IOException e) {
                System.out.println("Failed to connect to server. Retrying...");
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); 
                    return; 
                }
            }
        }
        while (true) {
            String command = scanner.nextLine().trim().toLowerCase();
            if (command.isEmpty()) continue;
            else if (command.equals("exit")) {
                System.out.println("terminating client side...");
                System.exit(1);
            }
            else if (command.contains("insert") || command.contains("remove_lower") || command.contains("replace_if_greater") || command.contains("update")) {
                Person person = NameValidator.validateName(scanner);
                Request request = new Request.RequestBuilder()
                    .setCommand(command)
                    .setPerson(person)
                    .build();
                sendRequest(request);
                System.out.println(getResponse());
            } 
            else if (command.contains("execute_script")) {
                ScriptExecutor.executeScript(command);
            }
            else {
                Request request = new Request.RequestBuilder()
                    .setCommand(command)
                    .build();
                sendRequest(request);
                System.out.println(getResponse()); 
            }
        }
    }
    
    public static DatagramChannel startClient() throws IOException {
        DatagramChannel client = DatagramChannelBuilder.bindChannel(null); 
        return client;
    }
    
    public static void sendRequest(Request request) {
        try {
            ByteBuffer buffer = Serializer.serializeRequest(request); 
            while (buffer.hasRemaining()) {
                client.send(buffer, address);
            }
        }catch (IOException exception) {
            System.err.println("error during sending request");
        }
    }
    
    public static Response getResponse() throws ClassNotFoundException {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(client.socket().getReceiveBufferSize());
            client.receive(buffer);
            Response response = (Response) Serializer.deserializeResponse(buffer);
            buffer.clear(); 
            return response;
        }catch (IOException exception) {
            System.err.println("error getting response from server");
            return null;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException exception) {
            System.err.println("wrong port");
            System.exit(0);
        }
        start();
    }
}
