package Server;

import Client.ClientHandler;
import org.json.JSONObject;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java .net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class Server {
    public ServerSocket serverSocket;
    ClientHandler clientHandler;
    public Scanner scanner;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.scanner = new Scanner(System.in);
    }

    public void startServer() throws IOException {

        try {
            Thread messageThread = new Thread(() -> serverMessageHandler());
            messageThread.start();

            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("A new user has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                this.clientHandler = clientHandler;
                thread.start();
            }
        }
    }
}
