package com.example.demo.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{

	// Broadcast messages to all clients
	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private User user;
	
	public ClientHandler(Socket socket) {
		try {
			this.socket = socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String clientName = this.bufferedReader.readLine();
			this.user = new User(clientName);
			System.out.println(clientName + " has connected to the server!");
		} catch (IOException e) {
			closeEverything(socket, bufferedWriter, bufferedReader);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				broadCastMessage(messageFromClient);
			} catch (IOException e) {
				closeEverything(socket, bufferedWriter, bufferedReader);
				break;
			}
		}
	}
	
	public void broadCastMessage(String message) {
		for (ClientHandler clientHandler : clientHandlers) {
			try {
				if (!clientHandler.user.getUsername().equals(user.getUsername())) {
					clientHandler.bufferedWriter.write(message);
					clientHandler.bufferedWriter.newLine();
					clientHandler.bufferedWriter.flush();
				}
			} catch (IOException e) {
				// TODO: handle exception
				closeEverything(socket, bufferedWriter, bufferedReader);
			}
		}
	}
	
	public void removeClientHandler() {
		clientHandlers.remove(this);
		broadCastMessage("Server: " + this.user.getUsername() + " has left the chat!");
	}
	
	public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
		removeClientHandler();
		try {
			if (socket != null) {
				socket.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
