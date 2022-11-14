package yurii.karpliuk.warehouseAccounting.Network;

import yurii.karpliuk.warehouseAccounting.database.DBWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;

public class StartServer extends Thread{
        public static void main(String[] args) {

            ServerSocket serverSocket;
            MultiThreadServer server;
            DBWorker db = DBWorker.getInstance();

            try {
                serverSocket = new ServerSocket(8080);
                server = new MultiThreadServer(serverSocket);
                System.out.println("...........................................................................................");
                System.out.println("Server is up and running");
                System.out.println("...........................................................................................");
                System.out.println("Connected to database "+db.getConnection().getCatalog());
                System.out.println("...........................................................................................");

                server.run();
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

}
