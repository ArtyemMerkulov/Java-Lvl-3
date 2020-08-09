package models;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static final Logger logger = Logger.getLogger(DB.class);
    private static final String LOG_CONF_PATH = "server/src/configs/log4j.properties";

    private static final String DBCredentialsFile = "server/src/configs/DBConfig.conf";

    public static Connection getConnection() throws SQLException {
        PropertyConfigurator.configure(LOG_CONF_PATH);

        Properties props = new Properties();
        logger.debug("Try to read database credentials");
        try(InputStream in = Files.newInputStream(Paths.get(DBCredentialsFile))) {
            props.load(in);
        } catch (IOException ex) {
            logger.debug("Error to read database credentials");
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }

        String url = props.getProperty("url");

        logger.debug("Try to getting org.sqlite.JDBC drive");
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            logger.debug("Error to getting org.sqlite.JDBC drive");
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
        return DriverManager.getConnection(url);
    }

    public static void closeConnection(Connection con) throws SQLException {
        con.close();
    }
}
