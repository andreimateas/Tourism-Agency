package repository;


import domain.AgencyEmployee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class AgencyEmployeeDBRepository implements AgencyEmployeeRepository{

    private DbUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public AgencyEmployeeDBRepository(Properties properties){
        dbUtils=new DbUtils(properties);
    }

    @Override
    public AgencyEmployee findByUsername(String username) {
        logger.traceEntry();
        Connection connection=dbUtils.getConnection();
        AgencyEmployee employee=null;
        try(PreparedStatement preStmt=connection.prepareStatement("select * from AgencyEmployees where username=? ")){
            preStmt.setString(1,username);
            try(ResultSet result=preStmt.executeQuery()){
                while(result.next()){
                    Long id=Long.parseLong(String.valueOf(result.getInt("id")));
                    String user=result.getString("username");
                    String password=result.getString("password");
                    String agency=result.getString("agencyName");
                    employee=new AgencyEmployee(user,password,agency);
                    employee.setId(id);
                }
            }
        }catch (SQLException exception){
            logger.error(exception);
            System.err.println("Database error" + exception);
        }
        logger.traceExit(employee);
        return employee;
    }

    @Override
    public Iterable<AgencyEmployee> findAll() {
        return null;
    }

    @Override
    public AgencyEmployee save(AgencyEmployee entity) {
        return null;
    }

    @Override
    public AgencyEmployee delete(Long aLong) {
        return null;
    }

}
