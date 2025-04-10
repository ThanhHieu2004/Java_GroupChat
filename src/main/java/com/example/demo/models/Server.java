package com.example.demo.models;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private int port = 2025;
	private ServerSocket server;

	public Server() {

	}

	public Server(ServerSocket server) {
		this.server = server;
	}

	public void startServer() {
		try {
			server = new ServerSocket(port);
			System.out.println("Server Listening on port: " + port);
			while (true) {
				// Keep listening for new Client
				Socket newClient = server.accept();
				System.out.println("A new client has connected!");
				ClientHandler clientHandler = new ClientHandler(newClient);
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		} catch (IOException e) {
			System.out.println("Start Server Error!");
			e.printStackTrace();
		}
	}

	public void closeServer() {
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
