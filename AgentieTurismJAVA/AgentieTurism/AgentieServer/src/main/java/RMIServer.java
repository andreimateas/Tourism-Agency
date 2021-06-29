import repository.*;
import server.ServicesImplementation;
import services.IServices;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class RMIServer {

    //private static int defaultPort=55555;

    public static void main(String[] args){
        Properties serverProperties=new Properties();
        try {
            serverProperties.load(RMIServer.class.getResourceAsStream("/server.properties"));
            serverProperties.list(System.out);
        } catch (IOException e) {
            System.err.println("Properties not found "+e);
            return;
        }
        AgencyEmployeeRepository agencyRepo=new AgencyEmployeeDBRepository(serverProperties);
        ExcursionRepository excursionRepo=new ExcursionDBRepository(serverProperties);
        ReservationRepository reservationRepo=new ReservationDBRepository(serverProperties);
        IServices serverService=new ServicesImplementation(agencyRepo,excursionRepo,reservationRepo);

        try{

            String name=serverProperties.getProperty("agency.rmi.serverID","default");
            IServices stub=(IServices) UnicastRemoteObject.exportObject(serverService,0);
            Registry registry= LocateRegistry.getRegistry();
            registry.rebind(name,stub);
        }catch(Exception e){
            System.err.println("Server exception" + e);
            e.printStackTrace();
        }


    }
}
