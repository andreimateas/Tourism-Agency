package repository;

import domain.Reservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ReservationDBRepository implements ReservationRepository{

    private DbUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public ReservationDBRepository(Properties properties){
        dbUtils=new DbUtils(properties);
    }

    @Override
    public Iterable<Reservation> findAll() {
        return null;
    }

    @Override
    public Reservation save(Reservation entity) {

        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        try(PreparedStatement preStmt=connection.prepareStatement("insert into Reservations (agency,excursion,clientName,clientPhone,numberTickets) values (?,?,?,?,?)")){
            preStmt.setString(1, entity.getAgency());
            preStmt.setInt(2, entity.getExcursion().getId().intValue());
            preStmt.setString(3,entity.getClientName());
            preStmt.setString(4,entity.getClientPhone());
            preStmt.setInt(5,entity.getNumberTickets());
            preStmt.executeUpdate();
            logger.trace("Saved entity {}", entity);
        }catch(SQLException exception){
            logger.error(exception);
            System.err.println("Database error" + exception);
        }
        logger.traceExit();

        return null;
    }

    @Override
    public Reservation delete(Long aLong) {
        return null;
    }
}
