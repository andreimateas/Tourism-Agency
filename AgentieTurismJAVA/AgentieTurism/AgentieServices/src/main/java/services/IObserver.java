package services;

import domain.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {

    void reservationAdded(Reservation reservation) throws ServiceException, RemoteException;
}
