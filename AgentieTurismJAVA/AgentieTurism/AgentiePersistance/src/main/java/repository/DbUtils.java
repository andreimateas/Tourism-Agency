package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {

    private Properties jdbcProperties;
    private static final Logger logger= LogManager.getLogger();

    public DbUtils(Properties properties){
        jdbcProperties=properties;
    }

    private Connection instance=null;

    private Connection getNewConnection(){
        logger.traceEntry();

        String url=jdbcProperties.getProperty("jdbc.url");
        String user=jdbcProperties.getProperty("jdbc.user");
        String pass=jdbcProperties.getProperty("jdbc.pass");

        Connection connection=null;

        try{
            if (user!=null && pass!=null)
                connection= DriverManager.getConnection(url,user,pass);
            else
                connection=DriverManager.getConnection(url);
        }catch(SQLException exception){
            logger.error(exception);
            System.out.println("Connection error "+ exception);
        }

        return connection;
    }

    public Connection getConnection(){
        logger.traceEntry();

        try{
            if (instance==null || instance.isClosed())
                instance=getNewConnection();
        }catch(SQLException exception){
            logger.error(exception);
            System.out.println("Database error "+exception);
        }
        logger.traceExit(instance);

        return instance;
    }

}
