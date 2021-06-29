package client.controller;

import domain.AgencyEmployee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import services.IServices;
import services.ServiceException;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class LoginController implements Serializable {

    IServices server;
    AgencyController agencyCtrl;
    AgencyEmployee currentUser;
    Parent agencyParent;

    @FXML
    TextField usernameText;
    @FXML
    PasswordField passwordText;

    public void setServer(IServices srv){

        server =srv;
    }

    public void setParent(Parent p){
        agencyParent=p;
    }

    public void setUser(AgencyEmployee user){
        this.currentUser=user;
    }

    public void setAgencyController(AgencyController agencyController){
        this.agencyCtrl=agencyController;
    }

    @FXML
    public void initialize(){
    }

    public void handleLoginButton(ActionEvent actionEvent) {
        if(usernameText.getText().equals("") || passwordText.getText().equals("")){
            Alert message=new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText("Type your username and password");
            message.showAndWait();
        }else {

            try {
                AgencyEmployee employee = server.findEmployee(usernameText.getText(), passwordText.getText(),agencyCtrl);

                Stage stage=new Stage();
                stage.setTitle("User: " + employee.getUsername() + "   Agency: " + employee.getAgencyName());
                stage.setScene(new Scene(agencyParent));

                stage.show();
                agencyCtrl.setUser(employee);
                agencyCtrl.init();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

            } catch (ServiceException se) {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(se.getMessage());
                message.showAndWait();
            }catch(RemoteException re){
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(re.getMessage());
                message.showAndWait();
            }
            catch (Exception e) {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(e.getMessage());
                message.showAndWait();
            }
        }

        usernameText.clear();
        passwordText.clear();
    }
}
