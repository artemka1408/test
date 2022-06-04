package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try (
                final ServerSocket serverSocket = new ServerSocket(9999);
        ) {
            while (true) {
                try {
                final Socket socket = serverSocket.accept();
                    handleClient(socket);
                } catch (Exception e) {
                    // если произошла любая пролема с клиентом
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // если не удалось запустить сервер
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) throws IOException {
        try (
                socket; //  закрыть сокет
                final OutputStream out = socket.getOutputStream();
                final InputStream in = socket.getInputStream();
        ) {
            System.out.println(socket.getInetAddress());
            out.write("Enter command\n".getBytes(StandardCharsets.UTF_8));

            //внутренний цикл чтения
            final String message = readMessage(in);
            System.out.println("message = " + message);
        }
    }

    private static String readMessage(final InputStream in) throws IOException {
        final byte[] buffer = new byte[4096];
        int offset = 0;
        int length = buffer.length;
        while (true) {
            final int read = in.read(buffer, offset, length); // сколько байт было прочитано
            offset += read;
            length = buffer.length - offset;
            final byte lastByte = buffer[offset - 1];
            if (lastByte == '\n') { //13 - \r, 10 - \n
                break;
            }
        }

        final String message = new String(buffer, 0, buffer.length - length, StandardCharsets.UTF_8).trim();
        return message;
    }
}