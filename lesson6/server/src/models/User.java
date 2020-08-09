package models;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class User extends Conversation {
    private static final Logger logger = Logger.getLogger(User.class);
    private static final String LOG_CONF_PATH = "server/src/configs/log4j.properties";

    private Connection conn;

    public User(Connection conn, String login, String password) {
        super(login);

        PropertyConfigurator.configure(LOG_CONF_PATH);

        try {
            this.conn = conn;

            if (isUserExistByLoginAndPassword(this.conn, login, password))
                getUserDataByLoginAndPassword(login, password);
        } catch (SQLException ex) {
            logger.error("Error checking existing user " + login);
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    public User(int id, String login, String name, Image img) {
        super(id, login, name, img);
    }

    public User(String login) {
        super(login);
        try {
            this.conn = DB.getConnection();

            if (isUserExistByLogin(this.conn, login)) getUserDataByLogin(login);

            DB.closeConnection(this.conn);
        } catch (SQLException ex) {
            logger.error("Error checking existing user " + login);
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    public static String whoIs(Connection conn, String dstLogin) throws SQLException {
        String query = "SELECT type FROM accounts WHERE login=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, dstLogin);

        ResultSet res = preStatement.executeQuery();

        return res.getString(1);
    }

    public static ArrayList<String> getCorrespondenceBySrcLoginAndDstLogin(Connection conn, String srcLogin,
                                                                String dstLogin) throws SQLException {
        String query = "SELECT src.login, img, msg_text " +
                "FROM (SELECT msg_text, src_id, dst_id " +
                "FROM messages " +
                "INNER JOIN correspondence ON messages.id=correspondence.msg_id) " +
                "INNER JOIN accounts AS src ON src.id=src_id " +
                "INNER JOIN accounts AS dst ON dst.id=dst_id " +
                "INNER JOIN users ON users.id=src.id " +
                "WHERE (src.login=? AND dst.login=?) OR (src.login=? AND dst.login=?)";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, srcLogin);
        preStatement.setString(2, dstLogin);
        preStatement.setString(3, dstLogin);
        preStatement.setString(4, srcLogin);

        ResultSet res = preStatement.executeQuery();

        ArrayList<String> correspondence = new ArrayList<>();
        while (res.next()) {
            correspondence.add(res.getString(1));

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(ImageIO.read(new File(ROOT_IMG_PATH +
                        res.getString(2))), "jpg", baos);
                baos.flush();
                correspondence.add(Base64.encode(baos.toByteArray()));
                baos.close();
            } catch (IOException ex) {
                logger.error("Error read image");
                logger.error(ex.getCause());
                logger.error(ex.getMessage());
                logger.error(ex.getStackTrace());
            }

            correspondence.add(res.getString(3));
        }

        return correspondence;
    }

    public static void createMsgFromSrcLoginToDstLogin(Connection conn, String srcLogin,
                                                       String dstLogin, String msg) throws SQLException {
        String query = "INSERT INTO messages (msg_text) VALUES (?)";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, msg);

        preStatement.executeUpdate();

        query = "INSERT INTO correspondence (src_id, dst_id, msg_id) VALUES ((SELECT id FROM users WHERE login=?), (SELECT id FROM users WHERE login=?), (SELECT COUNT(*) FROM messages))";

        preStatement = conn.prepareStatement(query);
        preStatement.setString(1, srcLogin);
        preStatement.setString(2, dstLogin);

        preStatement.executeUpdate();
    }

    public static boolean isDstBlockedSrc(Connection conn, String dstLogin, String srcLogin) throws SQLException {
        String query = "SELECT COUNT(blocklist.id) " +
                "FROM blocklist " +
                "INNER JOIN users ON user_id=users.id " +
                "INNER JOIN users AS blocked_users ON blocked_user_id=blocked_users.id " +
                "WHERE users.login=? AND blocked_users.login=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, dstLogin);
        preStatement.setString(2, srcLogin);

        ResultSet res = preStatement.executeQuery();

        return res.getInt(1) == 1;
    }

    public static void addBlockUserByDstLoginForSrcLogin(Connection conn, String userLogin,
                                                         String blockedUserLogin) throws SQLException {
        String query = "INSERT INTO blocklist " +
                "(user_id, blocked_user_id) " +
                "VALUES " +
                "((SELECT id FROM users WHERE login=? LIMIT 1), " +
                "(SELECT id FROM users WHERE login=? LIMIT 1))";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, userLogin);
        preStatement.setString(2, blockedUserLogin);

        preStatement.executeUpdate();
    }

    public static void deleteBlockUserByDstLoginForSrcLogin(Connection conn, String userLogin,
                                                            String blockedUserLogin) throws SQLException {
        String query = "DELETE FROM blocklist " +
                "WHERE user_id=(SELECT id FROM users WHERE login=? LIMIT 1) AND " +
                "blocked_user_id=(SELECT id FROM users WHERE login=? LIMIT 1)";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, userLogin);
        preStatement.setString(2, blockedUserLogin);

        preStatement.executeUpdate();
    }

    private boolean isUserExistByLogin(Connection conn, String login) throws SQLException {
        String query = "SELECT id FROM users WHERE login=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, login);

        ResultSet res = preStatement.executeQuery();

        return res.next();
    }

    private void getUserDataByLogin(String login) throws SQLException {
        String query = "SELECT id, name, img FROM users WHERE login=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, login);

        ResultSet res = preStatement.executeQuery();

        this.id = res.getInt(1);
        this.name = res.getString(2);
        try {
            this.img = ImageIO.read(new File(ROOT_IMG_PATH + res.getString(3)));
        } catch (IOException ex) {
            logger.error("Error read image");
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    private void getUserDataByLoginAndPassword(String login, String password) throws SQLException {
        String query = "SELECT id, name, img FROM users WHERE login=? AND password=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, login);
        preStatement.setString(2, password);

        ResultSet res = preStatement.executeQuery();

        this.id = res.getInt(1);
        this.name = res.getString(2);
        try {
            this.img = ImageIO.read(new File(ROOT_IMG_PATH + res.getString(3)));
        } catch (IOException ex) {
            logger.error("Error read image");
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    public Connection getDBConnection() { return conn; }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public Image getImg() {
        return img;
    }

    public static ArrayList<User> getContactsById(Connection conn, int id) throws SQLException {
        String usersQuery = "SELECT users.id, users.login, users.name, users.img " +
                "FROM users " +
                "INNER JOIN contacts ON contacts.req_user_id=users.id OR contacts.accept_id=users.id " +
                "WHERE (contacts.req_user_id=? OR contacts.accept_id=?) AND users.id!=?";

        PreparedStatement preStatement = conn.prepareStatement(usersQuery);
        preStatement.setInt(1, id);
        preStatement.setInt(2, id);
        preStatement.setInt(3, id);

        ResultSet res = preStatement.executeQuery();

        ArrayList<User> contacts = new ArrayList<>();
        while (res.next()) {
            try {
                contacts.add(new User(res.getInt(1),
                        res.getString(2),
                        res.getString(3),
                        ImageIO.read(new File(ROOT_IMG_PATH + res.getString(4)))));
            } catch (IOException ex) {
                logger.error("Error read image");
                logger.error(ex.getCause());
                logger.error(ex.getMessage());
                logger.error(ex.getStackTrace());
            }
        }

        return contacts;
    }

    public static boolean isUserExistByLoginAndPassword(Connection conn, String login,
                                                        String password) throws SQLException {
        String query = "SELECT id FROM users WHERE login=? AND password=? LIMIT 1";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, login);
        preStatement.setString(2, password);

        ResultSet res = preStatement.executeQuery();

        return res.next();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj == null) return false;
        else if (getClass() != obj.getClass()) return false;

        return login.equals(((User) obj).getLogin());
    }
}
