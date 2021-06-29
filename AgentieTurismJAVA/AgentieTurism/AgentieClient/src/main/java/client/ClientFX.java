package client;

import client.controller.AgencyController;
import client.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import services.IServices;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public class ClientFX extends Application {
    private Stage primaryStage;

    //private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Properties clientProperties = new Properties();
        try {
            clientProperties.load(ClientFX.class.getResourceAsStream("/client.properties"));
            clientProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Client properties not found " + e);
            return;
        }

        String name=clientProperties.getProperty("agency.rmi.serverID","default");
        String serverIP = clientProperties.getProperty("agency.server.host", defaultServer);

        try {

            Registry registry= LocateRegistry.getRegistry(serverIP);
            IServices server = (IServices) registry.lookup(name);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/loginView.fxml"));
            Parent root = loader.load();

            LoginController ctrl = loader.<LoginController>getController();
            ctrl.setServer(server);


            FXMLLoader cloader = new FXMLLoader();
            cloader.setLocation(getClass().getResource("/views/agencyView.fxml"));
            Parent croot = cloader.load();
            AgencyController agencyCtrl = cloader.<AgencyController>getController();
            agencyCtrl.setServer(server);

            ctrl.setAgencyController(agencyCtrl);
            ctrl.setParent(croot);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (Exception e) {
            System.err.println("Client exception:"+e);
            e.printStackTrace();
        }
    }
}
