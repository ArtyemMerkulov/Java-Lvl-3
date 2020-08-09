package models;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Group extends Conversation {
    private static final Logger logger = Logger.getLogger(Group.class);
    private static final String LOG_CONF_PATH = "server/src/configs/log4j.properties";

    public Group(int id, String login, String name, Image img) {
        super(id, login, name, img);

        PropertyConfigurator.configure(LOG_CONF_PATH);
    }

    public static ArrayList<Group> getContactsById(Connection conn, int userId) throws SQLException {
        String groupQuery = "SELECT groups.id, groups.login, groups.name, groups.img " +
                "FROM groups " +
                "INNER JOIN users_groups ON users_groups.group_id=groups.id " +
                "WHERE users_groups.user_id=?";

        PreparedStatement preStatement = conn.prepareStatement(groupQuery);
        preStatement.setInt(1, userId);

        ResultSet res = preStatement.executeQuery();

        ArrayList<Group> contacts = new ArrayList<>();
        while (res.next()) {
            try {
                contacts.add(new Group(res.getInt(2),
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

    public static ArrayList<String> getCorrespondenceByDstLogin(Connection conn,
                                                                String dstLogin) throws SQLException {
        String query = "SELECT src.login, img, msg_text " +
                "FROM (SELECT msg_text, src_id, dst_id " +
                "FROM messages " +
                "INNER JOIN correspondence ON messages.id=correspondence.msg_id) " +
                "INNER JOIN accounts AS src ON src.id=src_id " +
                "INNER JOIN accounts AS dst ON dst.id=dst_id " +
                "INNER JOIN users ON users.id=src.id " +
                "WHERE dst.login=?";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, dstLogin);

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

    public static ArrayList<String> getUsersFromGroupByLogin(Connection conn, String dstLogin) throws SQLException {
        String query = "SELECT users.login " +
                "FROM users " +
                "INNER JOIN users_groups ON users.id=users_groups.user_id " +
                "INNER JOIN groups ON users_groups.group_id=groups.id " +
                "WHERE groups.login=?";

        PreparedStatement preStatement = conn.prepareStatement(query);
        preStatement.setString(1, dstLogin);

        ResultSet res = preStatement.executeQuery();

        ArrayList<String> usersLogins = new ArrayList<>();
        while (res.next()) usersLogins.add(res.getString(1));

        return usersLogins;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj == null) return false;
        else if (getClass() != obj.getClass()) return false;

        return login.equals(((Group) obj).getLogin());
    }
}
