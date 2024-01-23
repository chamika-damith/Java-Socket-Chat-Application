package org.example.server;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.controller.LoginFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static ArrayList<Socket> socketArrayList = new ArrayList<>();
    public static int index = 0;
    public static ArrayList<Thread> threadList = new ArrayList<>();


    public static void startServer() {
        System.out.println("Server started");
        new Thread(() -> {

            try {
                ServerSocket ss = new ServerSocket(3000);
                while (true) {

                    Socket socket = ss.accept();
                    socketArrayList.add(socket);
                    //Notifying the other clients who joined the chat
                    for (Socket s : socketArrayList) {
                        if (s.getPort() == socket.getPort()) {
                            //Avoid sending the message to the sender
                            continue;
                        }
                        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                        int index = socketArrayList.indexOf(socket);
                        //dos.writeUTF(LoginFormController.clientsNames.get(index) + " " + "has joined the chat.");
                        dos.flush();
                    }
                    System.out.println("Client connected to the server");

                    //Handling each client from a separate thread
                    handleClient(socket);


                }
            } catch (IOException e) {
                Platform.runLater(() -> {
//                    System.out.println("Error while starting the server : " + e.getLocalizedMessage());
                });
            }

        }).start();


    }

    public static void handleClient(Socket socket) {
        new Thread(() -> {
            //Adding the current thread to an arrayList
            threadList.add(Thread.currentThread());

            String clientMsg = "";
            try {
                while (true) {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    clientMsg = dis.readUTF();
                    //Checking if an image has received
                    //Checking if the client sent an image
                    if (clientMsg.equals("img")) {
                        handleReceivedImage(dis, socket);
                    } else {
                        sendMsgToOthers(clientMsg, socket);
                    }


                }


            } catch (IOException e) {
                throw new RuntimeException(e);


            }


        }).start();

    }


    public static void handleReceivedImage(DataInputStream dis, Socket senderSocket) {
        try {
            //Reading the image length
            int imageDataLength = dis.readInt();
            byte[] imageData = new byte[imageDataLength];
            dis.readFully(imageData);
            sendImageToOthers(imageData, senderSocket);
        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error while handling the received image: " + e.getLocalizedMessage());
                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
                alert.showAndWait();
            });
        }
    }

    public static void sendImageToOthers(byte[] imageData, Socket senderSocket) {
        for (Socket s : socketArrayList) {
            try {
                if (s.getPort() == senderSocket.getPort()) {
                    //Avoid sending the image to the sender
                    continue;
                }
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                //Sending a special message indicating the start of an image transmission
                dos.writeUTF("img");

                //Letting the server know the size of the image.This is useful to allocate resources properly
                dos.writeInt(imageData.length);
                // Sending the image data
                dos.write(imageData);
                dos.flush();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while getting the output stream: " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                });
            }
        }
    }


    public static void sendMsgToOthers(String msg, Socket socket) {

        for (Socket s : socketArrayList) {
            try {
                if (s.getPort() == socket.getPort()) {
                    //Avoid sending the message to the sender.
                    continue;

                }
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                //Since socketArray index == clientsNames array client name index
                index = socketArrayList.indexOf(socket);
                dos.writeUTF(LoginFormController.clientsNames.get(index) + " : " + msg);
                dos.flush();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while getting the output stream : " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                });
            }

        }

    }

    public static void handleExitedClient(int exitedClientIndex){
        Socket exitedClient = Server.socketArrayList.get(exitedClientIndex);
        for (Socket s : Server.socketArrayList) {
            if (s.getPort() == exitedClient.getPort()) {
                continue;

            }
            try {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(LoginFormController.clientsNames.get(exitedClientIndex) + " has left the chat!");
                dos.flush();
            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while handling the client exit! : " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                });
            }

        }
        //Closing the socket and interrupting the relevant thread
        new Thread(() -> {
            try {
                exitedClient.close();
                Server.threadList.get(exitedClientIndex).interrupt();

            } catch (IOException e) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error while exiting the client socket. : " + e.getLocalizedMessage());
                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.getScene().getStylesheets().add(Server.class.getResource("/style/notification.css").toExternalForm());
                    alert.showAndWait();
                });
            }
        }).start();

    }


}