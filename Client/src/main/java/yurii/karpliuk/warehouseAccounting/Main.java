package yurii.karpliuk.warehouseAccounting;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import yurii.karpliuk.warehouseAccounting.Network.Client;

import java.io.IOException;

public class Main extends Application {
    private Client connection= Client.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        stage.setScene(new Scene(fxmlLoader.load(), 450, 600));
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest((WindowEvent we)->{
            connection.sendMessage("shutdown");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}