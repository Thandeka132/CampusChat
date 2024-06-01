package Client;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import User.User;
import org.json.JSONObject;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //keeps track of all clients
    public static ArrayList<User> usersInChat = new ArrayList<>(); // will keep track of the users whene they enter the chat
    private static Socket socket; //socket passed from the server
    private BufferedReader bufferedReader; //reads messages sent by the client
    private BufferedWriter bufferedWriter; // sends data to the user
    private String clientUsername;
    public static Scanner scanner;

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String username = bufferedReader.readLine();
            System.out.println("Username: " + username);

            while (isClientUsernameDuplicate(username)) {
                sendMessage("SERVER: The username '" + username + "' is already in use. Please choose a different username.");
                username = bufferedReader.readLine().split(": ")[1];
            }

            this.clientUsername = username;
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + username + " has entered the chat");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        JSONObject clientRequest;
        User user;
        Client client;

//        while(socket.isConnected()) {
//            //test if connection is valid
//            try {
//                messageFromClient = bufferedReader.readLine();
//
//                try
//            }
//        }
    }

    public static boolean isClientUsernameDuplicate(String username) {
        for (ClientHandler clientHandler : clientHandlers) {
            System.out.println(clientHandler.clientUsername + " " + clientHandler.clientUsername.equals(username));
            if (clientHandler.clientUsername.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addToUsersInTheChat(User user) {
        usersInChat.add(user);
    }

    public static void removeUserInChat(User user) {
        usersInChat.remove(user);
    }

    public static ArrayList<User> getUsersInChat() {
        return usersInChat;
    }

    public static User findUser(String username) {
        for(User user: usersInChat) {
            if(username.equals(user.getUserName())) {
                return user;
            }
        }
        return null;
    }

    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch(IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void broadcastMessage(String messageToSend){
        //Broadcast messages to clients. This is only for demonstration purposes, it'll be not be used in the overall project

        for (ClientHandler clientHandler: clientHandlers){
            try{
                if (!clientHandler.clientUsername.equals(clientUsername)){
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                System.out.println("client has exited the chat");
            }
        }
        System.out.println(messageToSend);
    }

    public void broadcastMessage(String messageToSend, String receiver){
        //Broadcast messages to clients. This is only for demonstration purposes, it'll be not be used in the overall project

        for (ClientHandler clientHandler: clientHandlers){
            try{
                if (clientHandler.clientUsername.equals(receiver)){
                    clientHandler.bufferedWriter.write("SERVER-" + receiver + "~" + messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                System.out.println("client has exited the chat");
            }
        }
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if(bufferedReader != null) {
                bufferedReader.close();
            }
            if(bufferedWriter != null) {
                bufferedReader.close();
            }
            if(socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSocket() throws IOException {
        socket.close();
    }

    public static void exit() throws IOException {
        for(int i=0; i < clientHandlers.size(); i++) {
            closeSocket();
        }
    }

    public void notifyUsers() {
        sendMessage("SERVER: The world has ended!...");
    }
}
