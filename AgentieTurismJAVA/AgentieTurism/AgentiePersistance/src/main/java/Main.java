import repository.AgencyEmployeeDBRepository;
import repository.ExcursionDBRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties=new Properties();
        try{
            properties.load(new FileReader("AgentieTurism\\AgentiePersistance\\bd.config"));
        }catch(IOException e){
            System.out.println("Missing db config file "+ e);
        }

        ExcursionDBRepository excursionRepo=new ExcursionDBRepository(properties);
        excursionRepo.findAll();
    }
}
