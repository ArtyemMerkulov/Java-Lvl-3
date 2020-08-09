import models.DB;
import models.Group;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final Logger logger = Logger.getLogger(ChatServer.class);
    private static final String LOG_CONF_PATH = "server/src/configs/log4j.properties";

    private static final int PORT = 12345;

    private ConcurrentHashMap<String, ClientHandler> clients;

    private Connection conn;

    public ChatServer() {
        PropertyConfigurator.configure(LOG_CONF_PATH);

        logger.info("Start server");

        ServerSocket server = null;
        Socket socket = null;
        conn = null;

        try {
            logger.info("Init connection to Database");
            conn = DB.getConnection();
        } catch (SQLException ex) {
            logger.error("Connection to Database failed: " + ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }

        try {
            clients = new ConcurrentHashMap<>();
            server = new ServerSocket(PORT);

            logger.info("Server running...");

            while (true) {
                socket = server.accept();
                logger.info("Client " + socket + " connected...");

                new ClientHandler(this, socket);
                logger.debug("Create ClientHandler for " + socket);
            }
        } catch (IOException e) {
            logger.error(e.getCause());
            logger.error(e.getMessage());
            logger.error(e.getStackTrace());
        } finally {
            logger.error("Sever stopped...");
            try {
                logger.debug("Close connection to Database");
                DB.closeConnection(conn);
            } catch (SQLException e) {
                logger.error("Error close connection to Database");
                logger.error(e.getMessage());
                logger.error(e.getStackTrace());
            }
            try {
                logger.debug("Close socket for accept connection...");
                socket.close();
            } catch (IOException e) {
                logger.debug("Error close socket for accept connection");
                logger.error(e.getMessage());
                logger.error(e.getStackTrace());
            }
            try {
                logger.debug("Close server socket...");
                server.close();
            } catch (IOException e) {
                logger.error("Error close server socket");
                logger.error(e.getMessage());
                logger.error(e.getStackTrace());
            }
        }
    }

    public Connection getConnection() { return conn; }

    public void subscribe(String login, ClientHandler client) {
        clients.put(login, client);
    }

    public void unsubscribe(String login) {
        clients.remove(login);
    }

    public boolean isOnline(String login) {
        return clients.containsKey(login);
    }

    public boolean isUserAlreadyAuthorized(String login) {
        return clients.containsKey(login);
    }

    public void sendMsgToUser(String srcLogin, String dstLogin) {
        if (isOnline(dstLogin)) clients.get(dstLogin).sendMsg("/sendtouserfrom:" + srcLogin);
    }

    public void sendMsgToGroup(String srcLogin, String dstLogin) {
        try {
            Connection conn = clients.get(srcLogin).getUser().getDBConnection();
            for (String userDstLogin : Group.getUsersFromGroupByLogin(conn, dstLogin))
                if (isOnline(userDstLogin))
                    clients.get(userDstLogin).sendMsg("/sendtogroup:" + dstLogin + ":" + srcLogin);
        } catch (SQLException ex) {
            logger.warn("failed to sending message to group " + dstLogin + " from " + srcLogin);
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }
}
