package client.controller;

import domain.AgencyEmployee;
import domain.Excursion;
import domain.Reservation;
import domain.validator.ValidationException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AgencyController implements Initializable,IObserver, Serializable {

    IServices server;
    AgencyEmployee agencyEmployee;
    ObservableList<Excursion> modelExcursion = FXCollections.observableArrayList();
    ObservableList<Excursion> modelExcursionFiltered = FXCollections.observableArrayList();

    @FXML
    Spinner<Integer> spinner1;
    @FXML
    Spinner<Integer> spinner2;
    @FXML
    Spinner<Integer> spinner3;
    @FXML
    Spinner<Integer> spinner4;
    @FXML
    Spinner<Integer> spinnerTickets;
    @FXML
    TextField attractionText;
    @FXML
    TableView<Excursion> excursionsTable;
    @FXML
    TableView<Excursion> excursionsFilteredTable;
    @FXML
    TableColumn<Excursion,String> attractionColumn;
    @FXML
    TableColumn<Excursion,String> transportationColumn;
    @FXML
    TableColumn<Excursion,String> timeColumn;
    @FXML
    TableColumn<Excursion,String> priceColumn;
    @FXML
    TableColumn<Excursion,String> seatsColumn;
    @FXML
    TableColumn<Excursion,String> attractionColumn2;
    @FXML
    TableColumn<Excursion,String> transportationColumn2;
    @FXML
    TableColumn<Excursion,String> timeColumn2;
    @FXML
    TableColumn<Excursion,String> priceColumn2;
    @FXML
    TableColumn<Excursion,String> seatsColumn2;
    @FXML
    TextField clientNameText;
    @FXML
    TextField clientPhoneText;


    public AgencyController(){}

    public AgencyController(IServices server){
        this.server=server;
        try{
            UnicastRemoteObject.exportObject(this,0);
        }catch(RemoteException e){
            System.out.println(e);
        }

    }

    public void setServer(IServices srv){
        server =srv;
        try{
            UnicastRemoteObject.exportObject(this,0);
        }catch(RemoteException e){
            System.out.println(e);
        }
    }

    public void setUser(AgencyEmployee employee){
        this.agencyEmployee=employee;
    }

    public void init(){
        initModel();
    }

    @FXML
    public void initialize(){

           attractionColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("touristAttraction"));
           transportationColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("transportationCompany"));
           timeColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("departureTime"));
           priceColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("price"));
           seatsColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("availableSeats"));
           excursionsTable.setItems(modelExcursion);

           attractionColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("touristAttraction"));
           transportationColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("transportationCompany"));
           timeColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("departureTime"));
           priceColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("price"));
           seatsColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("availableSeats"));
           excursionsFilteredTable.setItems(modelExcursionFiltered);



    }

    private void initModel(){
        Iterable<Excursion> excursions= null;
        try {
            excursions = server.getAllExcursions();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        List<Excursion> excursionList= StreamSupport.stream(excursions.spliterator(),false).collect(Collectors.toList());
        modelExcursion.setAll(excursionList);

        Iterable<Excursion> excursions2= null;
        try {
            excursions2 = server.getExcursionsAttractionTime(attractionText.getText(), LocalTime.of(spinner1.getValue(),spinner2.getValue()),LocalTime.of(spinner3.getValue(),spinner4.getValue()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        List<Excursion> excursionList2= StreamSupport.stream(excursions2.spliterator(),false).collect(Collectors.toList());
        modelExcursionFiltered.setAll(excursionList2);

            excursionsTable.setRowFactory(tv -> new TableRow<Excursion>(){
                @Override
                protected void updateItem(Excursion item,boolean empty) {
                    super.updateItem(item,empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else if (item.getAvailableSeats()==0){
                        setStyle("-fx-background-color: #ff7a7a");
                    } else {
                        setStyle("");
                    }

                }
            });

            excursionsFilteredTable.setRowFactory(tv -> new TableRow<Excursion>(){
                @Override
                protected void updateItem(Excursion item,boolean empty) {
                    super.updateItem(item,empty);
                    if (empty || item == null) {
                        setStyle("");
                    } else if (item.getAvailableSeats()==0){
                        setStyle("-fx-background-color: #ff7a7a");
                    } else {
                        setStyle("");
                    }

                }
            });


    }


    public void handleReserveButton(ActionEvent actionEvent) {
        if(clientNameText.getText().equals("") || clientPhoneText.getText().equals("")){
            Alert message=new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText("Enter client name and phone");
            message.showAndWait();
        }
        else{
        if(excursionsTable.getSelectionModel().getSelectedItem()!=null) {
            try {
                server.addReservation(new Reservation(agencyEmployee.getAgencyName(),excursionsTable.getSelectionModel().getSelectedItem(),clientNameText.getText(),clientPhoneText.getText(),spinnerTickets.getValue()));

            } catch (ServiceException se) {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(se.getMessage());
                message.showAndWait();
            }  catch (ValidationException ve) {
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(ve.getMessage());
                message.showAndWait();
            }catch(RemoteException re){
                Alert message = new Alert(Alert.AlertType.ERROR);
                message.setTitle("Error");
                message.setContentText(re.getMessage());
                message.showAndWait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            Alert message=new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText("No excursion selected!");
            message.showAndWait();
        }}
    }

    public void handleFilterButton(ActionEvent actionEvent) {
        Iterable<Excursion> excursions2= null;
        try {
            excursions2 = server.getExcursionsAttractionTime(attractionText.getText(), LocalTime.of(spinner1.getValue(),spinner2.getValue()),LocalTime.of(spinner3.getValue(),spinner4.getValue()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        List<Excursion> excursionList2= StreamSupport.stream(excursions2.spliterator(),false).collect(Collectors.toList());
        modelExcursionFiltered.setAll(excursionList2);
    }

    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)actionEvent.getSource()).getScene().getWindow().hide();
    }

    void logout() {
        try{
            server.logout(agencyEmployee,this);
        }catch(RemoteException re){
            Alert message = new Alert(Alert.AlertType.ERROR);
            message.setTitle("Error");
            message.setContentText(re.getMessage());
            message.showAndWait();
        }
        catch (ServiceException se){
            System.out.println("Logout error");
        }
    }

    @Override
    public void reservationAdded(Reservation reservation) throws ServiceException {

        int poz=0;
        for(Excursion ex:excursionsTable.getItems()){
            if(ex.getId().equals(reservation.getExcursion().getId()))
                break;
            poz++;
        }
        Excursion excursion=new Excursion(reservation.getExcursion().getTouristAttraction(),reservation.getExcursion().getTransportationCompany(),reservation.getExcursion().getDepartureTime(),reservation.getExcursion().getPrice(),reservation.getExcursion().getAvailableSeats()- reservation.getNumberTickets());
        excursion.setId(reservation.getExcursion().getId());
        final int pozz=poz;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                excursionsTable.getItems().set(pozz,excursion);
            }
        });



        int poz2=0;
        for(Excursion ex:excursionsFilteredTable.getItems()){
            if(ex.getId().equals(reservation.getExcursion().getId()))
                break;
            poz2++;
        }
        Excursion excursion2=new Excursion(reservation.getExcursion().getTouristAttraction(),reservation.getExcursion().getTransportationCompany(),reservation.getExcursion().getDepartureTime(),reservation.getExcursion().getPrice(),reservation.getExcursion().getAvailableSeats()- reservation.getNumberTickets());
        excursion2.setId(reservation.getExcursion().getId());
        final int pozz2=poz2;
        if(poz2<excursionsFilteredTable.getItems().size())
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    excursionsFilteredTable.getItems().set(pozz2,excursion2);
                }
            });


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


            attractionColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("touristAttraction"));
            transportationColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("transportationCompany"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("departureTime"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("price"));
            seatsColumn.setCellValueFactory(new PropertyValueFactory<Excursion,String>("availableSeats"));
            excursionsTable.setItems(modelExcursion);

            attractionColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("touristAttraction"));
            transportationColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("transportationCompany"));
            timeColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("departureTime"));
            priceColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("price"));
            seatsColumn2.setCellValueFactory(new PropertyValueFactory<Excursion,String>("availableSeats"));
            excursionsFilteredTable.setItems(modelExcursionFiltered);


    }
}
