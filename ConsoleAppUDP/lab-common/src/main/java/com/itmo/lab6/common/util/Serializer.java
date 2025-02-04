package com.itmo.lab6.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;


public class Serializer {
    public static ByteBuffer serializeRequest(Request request) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
            oos.flush();
            return ByteBuffer.wrap(baos.toByteArray());
        }
    }
    public static Response deserializeResponse(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        buffer.flip();
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
            return (Response) ois.readObject();
        }
    }

    public static Request deserializeRequest(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        buffer.flip();
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
            return (Request) ois.readObject();
        }
    }

    public static ByteBuffer serializeResponse(Response response) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
            return ByteBuffer.wrap(baos.toByteArray());
        }
    }


}
