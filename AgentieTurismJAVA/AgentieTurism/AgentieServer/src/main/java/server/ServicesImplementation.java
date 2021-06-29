package server;

import domain.AgencyEmployee;
import domain.Excursion;
import domain.Reservation;
import domain.validator.ReservationValidator;
import repository.AgencyEmployeeRepository;
import repository.ExcursionRepository;
import repository.ReservationRepository;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.rmi.RemoteException;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementation implements IServices {

    private AgencyEmployeeRepository agencyRepo;
    private ExcursionRepository excursionRepo;
    private ReservationRepository reservationRepo;
    private Map<Long, IObserver> loggedClients;

    public ServicesImplementation(AgencyEmployeeRepository agencyRepo, ExcursionRepository excursionRepo, ReservationRepository reservationRepo) {
        this.agencyRepo=agencyRepo;
        this.excursionRepo=excursionRepo;
        this.reservationRepo=reservationRepo;
        loggedClients=new ConcurrentHashMap<>();
    }

    private final int defaultThreads=5;

    @Override
    public synchronized AgencyEmployee findEmployee(String username, String password,IObserver client) throws ServiceException, RemoteException {
        AgencyEmployee employee=agencyRepo.findByUsername(username);
        if (employee==null)
            throw new ServiceException("Non-existent user");
        if(!employee.getPassword().equals(password))
            throw new ServiceException("Wrong password");
        if(loggedClients.get(employee.getId())!=null)
            throw new ServiceException("User already logged in");
        loggedClients.put(employee.getId(), client);
        return employee;
    }

    @Override
    public synchronized Iterable<Excursion> getAllExcursions() throws ServiceException,RemoteException {
        return excursionRepo.findAll();
    }

    @Override
    public synchronized Iterable<Excursion> getExcursionsAttractionTime(String attraction, LocalTime start, LocalTime end) throws ServiceException,RemoteException {
        return excursionRepo.filterAttractionTime(attraction,start,end);
    }

    @Override
    public synchronized void addReservation(Reservation reservation) throws ServiceException,RemoteException {
        if(reservation.getNumberTickets()>reservation.getExcursion().getAvailableSeats())
            throw new ServiceException("Not enough available seats");
        ReservationValidator validator=new ReservationValidator();
        validator.validate(reservation);
        reservationRepo.save(reservation);
        excursionRepo.reserveSeats(reservation.getExcursion(), reservation.getNumberTickets());
        notifyAddedReservation(reservation);
    }

    @Override
    public synchronized void logout(AgencyEmployee employee, IObserver client) throws ServiceException,RemoteException {
        IObserver removedClient=loggedClients.remove(employee.getId());
        if(removedClient==null)
            throw new ServiceException("User not logged in");
    }

    private void notifyAddedReservation(Reservation reservation) throws ServiceException{
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreads);
        for(IObserver client: loggedClients.values()){
            executor.execute(()->{
                try{
                    client.reservationAdded(reservation);
                }catch (ServiceException | RemoteException e){
                    System.err.println("Error notify add reservation" + e);
                }

            });
        }
        executor.shutdown();
    }
}
