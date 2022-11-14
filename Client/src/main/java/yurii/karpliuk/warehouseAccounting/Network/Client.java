package yurii.karpliuk.warehouseAccounting.Network;

import javafx.scene.control.Alert;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client {
    private static Client connection;
    private Socket clientSock;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    private String message;

    public Client() {
        try {
            clientSock = new Socket("localhost", 8080);
            outStream = new ObjectOutputStream(clientSock.getOutputStream());
            inStream = new ObjectInputStream(clientSock.getInputStream());
        } catch (ConnectException e) {
            System.out.println(
                    "Connection Refused Exception as the given hostname and port are invalid : "
                            + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            closeEverything(clientSock, inStream, outStream);
        }
    }

    public Socket getClientSock() {
        return clientSock;
    }

    public static Client getInstance() {
        if (connection == null) {
            connection = new Client();
        }
        return connection;
    }


    public void sendMessage(String message) {
        try {
            if (clientSock.isConnected()) {
                outStream.writeObject(message);
            } else {
                throw  new Exception("No connection");
            }
        }  catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No connection");
            alert.setContentText("No connection to server");
            alert.showAndWait();
            System.exit(0);

        }
    }


    public void sendObject(Object object) {
        try {
            if (clientSock.isConnected()) {
                outStream.flush();
                outStream.writeObject(object);
            } else {
                throw  new Exception("No connection");
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No connection");
            alert.setContentText("No connection to server");
            alert.showAndWait();
            System.exit(0);
        }
    }

    public String readMessage() {
        try {
            if (clientSock.isConnected()) {
                message = (String) inStream.readObject();
            } else {
                throw  new Exception("No connection");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No connection");
            alert.setContentText("No connection to server");
            alert.showAndWait();
            System.exit(0);
        }

        return message;
    }

    public Object readObject() {
        Object object = new Object();
        try {
            if (clientSock.isConnected()) {
                object = inStream.readObject();
            } else {
               throw  new Exception("No connection");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("No connection");
            alert.setContentText("No connection to server");
            alert.showAndWait();
            System.exit(0);
        }
        return object;
    }

    public void closeEverything(Socket socket, InputStream inputStream, OutputStream outputStream) {
        try {
            if (outStream != null) {
                outStream.close();
            }
            if (inStream != null) {
                inStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
