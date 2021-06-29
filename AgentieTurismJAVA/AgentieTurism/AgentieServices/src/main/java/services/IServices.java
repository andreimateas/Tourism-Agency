package services;

import domain.AgencyEmployee;
import domain.Excursion;
import domain.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface IServices extends Remote {

    AgencyEmployee findEmployee(String username, String password,IObserver client) throws ServiceException, RemoteException;
    Iterable<Excursion> getAllExcursions() throws ServiceException,RemoteException;
    Iterable<Excursion> getExcursionsAttractionTime(String attraction, LocalTime start, LocalTime end) throws ServiceException,RemoteException;
    void addReservation(Reservation reservation) throws ServiceException,RemoteException;
    void logout(AgencyEmployee employee,IObserver client) throws ServiceException,RemoteException;
}
