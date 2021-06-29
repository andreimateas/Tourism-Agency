package repository;

import domain.Excursion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ExcursionDBRepository implements ExcursionRepository{

    private DbUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ExcursionDBRepository(Properties properties){
        dbUtils=new DbUtils(properties);
    }

    @Override
    public Iterable<Excursion> filterAttractionTime(String attraction, LocalTime start, LocalTime end) {

        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<Excursion> excursions=new ArrayList<>();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Excursions where touristAttraction=?")){
            preStmt.setString(1,attraction);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=Long.parseLong(String.valueOf(result.getInt("id_excursion")));
                    String attr=result.getString("touristAttraction");
                    String transportation=result.getString("transportationCompany");
                    LocalTime time=LocalTime.parse(result.getString("departureTime"),DateTimeFormatter.ISO_LOCAL_TIME);
                    Float price=result.getFloat("price");
                    Integer seats=result.getInt("availableSeats");
                    Excursion excursion=new Excursion(attr,transportation,time,price,seats);
                    excursion.setId(id);
                    excursions.add(excursion);

                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Database error "+exception);
        }
        List<Excursion> excursionsTime=new ArrayList<>();
        for(Excursion e :excursions){
            if(e.getDepartureTime().compareTo(start)>=0 && e.getDepartureTime().compareTo(end)<=0)
                excursionsTime.add(e);
        }
        logger.traceExit(excursionsTime);
        return excursionsTime;
    }

    @Override
    public void reserveSeats(Excursion excursion, int noSeats) {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt=connection.prepareStatement("update Excursions set availableSeats=? where id_excursion=?")){
            preStmt.setInt(1, excursion.getAvailableSeats()-noSeats);
            preStmt.setInt(2,excursion.getId().intValue());
            preStmt.executeUpdate();
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Database error" + exception);
        }
        logger.traceExit();

    }

    @Override
    public Iterable<Excursion> findAll() {
        logger.traceEntry();
        Connection connection= dbUtils.getConnection();
        List<Excursion> excursions=new ArrayList<>();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Excursions")){
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=Long.parseLong(String.valueOf(result.getInt("id_excursion")));
                    String attraction=result.getString("touristAttraction");
                    String transportation=result.getString("transportationCompany");
                    LocalTime time=LocalTime.parse(result.getString("departureTime"));
                    Float price=result.getFloat("price");
                    Integer seats=result.getInt("availableSeats");
                    Excursion excursion=new Excursion(attraction,transportation,time,price,seats);
                    excursion.setId(id);
                    excursions.add(excursion);
                }
            }
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Database error "+exception);
        }
        logger.traceExit(excursions);
        return excursions;
    }

    @Override
    public Excursion save(Excursion entity) {
        return null;
    }

    @Override
    public Excursion delete(Long aLong) {
        return null;
    }
}
