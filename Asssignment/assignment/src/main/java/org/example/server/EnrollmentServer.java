package org.example.server;

import org.example.service.EnrollmentService;

import java.io.*;
import java.net.*;

public class EnrollmentServer implements Runnable {
    private static final int PORT = 12345;

    @Override
    public void run() {
        System.out.println("Enrollment Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected...");

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
            ) {
                System.out.println("Reading data from client...");
                String studentId = (String) in.readObject();
                String courseId = (String) in.readObject();
                int semester = in.readInt();

                System.out.println("Received: " + studentId + ", " + courseId + ", " + semester);

                boolean result = EnrollmentService.enrollStudent(studentId, courseId, semester);
                System.out.println("Enrollment result: " + result);

                out.writeObject(result ? "Enrollment successful" : "Enrollment failed");
                out.flush(); // Ensure data is sent

                System.out.println("Response sent to client...");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}