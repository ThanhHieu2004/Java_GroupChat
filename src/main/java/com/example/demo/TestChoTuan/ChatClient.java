package com.example.demo.TestChoTuan;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Consumer<String> onMessageReceived;//Consumer sẽ được gọi chung với các tin nhắn đi kèm từ server và xử lí chúng
  
    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }
  
    public void sendMessage(String msg) {
        out.println(msg);
    }
  
    public void startClient() {
        //Gọi hàm lambda thêm các tin nhắn đã nhận đến khu vực nhắn tin
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMessageReceived.accept(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
  }
