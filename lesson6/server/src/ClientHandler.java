import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import models.Conversation;
import models.Group;
import models.User;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler {
    private static final Logger logger = Logger.getLogger(ClientHandler.class);
    private static final String LOG_CONF_PATH = "server/src/configs/log4j.properties";

    private ChatServer server;
    private DataInputStream in;
    private DataOutputStream out;
    private User user;

    public ClientHandler(ChatServer server, Socket socket) {
        PropertyConfigurator.configure(LOG_CONF_PATH);

        try {
            this.server = server;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    if (checkUser()) {
                        sendContacts();

                        while (true) {
                            String str = in.readUTF();

                            if (str.startsWith("/wanttochating")) initDialog(str.split(":")[1]);
                            else if (str.equals("/exitonclose")) break;
                            else if (str.startsWith("/sendto")) {
                                String[] tokens = str.split(":");
                                sendMsgTo(tokens[1], tokens[2]);
                            } else if (str.startsWith("/blockuser")) {
                                String[] tokens = str.split(":");
                                addBlockUser(tokens[1]);
                            } else if (str.startsWith("/unblockuser")) {
                                String[] tokens = str.split(":");
                                deleteBlockUser(tokens[1]);
                            }
                        }
                    }
                } catch (IOException ex) {
                    logger.error(ex.getCause());
                    logger.error(ex.getMessage());
                    logger.error(ex.getStackTrace());
                } finally {
                    logger.error(ClientHandler.class + " of " + user.getLogin() + " stopped...");
                    try {
                        logger.debug("Close input stream of " + user.getLogin());
                        in.close();
                    } catch (IOException ex) {
                        logger.error(ex.getCause());
                        logger.error(ex.getMessage());
                        logger.error(ex.getStackTrace());
                    }
                    try {
                        logger.debug("Close output stream of " + user.getLogin());
                        out.close();
                    } catch (IOException ex) {
                        logger.error(ex.getCause());
                        logger.error(ex.getMessage());
                        logger.error(ex.getStackTrace());
                    }
                    try {
                        logger.debug("Close socket of " + user.getLogin());
                        socket.close();
                    } catch (IOException ex) {
                        logger.error(ex.getCause());
                        logger.error(ex.getMessage());
                        logger.error(ex.getStackTrace());
                    }
                    server.unsubscribe(user.getLogin());
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBlockUser(String dstLogin) {
        try {
            logger.debug("Try to add blocked user " + dstLogin + " for " + user.getLogin());
            User.addBlockUserByDstLoginForSrcLogin(user.getDBConnection(), user.getLogin(), dstLogin);
        } catch (SQLException ex) {
            logger.error("Error to add blocked user " + dstLogin + " for " + user.getLogin());
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    private void deleteBlockUser(String dstLogin) {
        try {
            logger.debug("Try to delete blocked user " + dstLogin + " for " + user.getLogin());
            User.deleteBlockUserByDstLoginForSrcLogin(user.getDBConnection(), user.getLogin(), dstLogin);
        } catch (SQLException ex) {
            logger.error("Error to delete blocked user " + dstLogin + " for " + user.getLogin());
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    private void sendMsgTo(String dstLogin, String msg) {
        try {
            logger.debug("Try to send message to " + dstLogin + " from " + user.getLogin());
            if (!User.isDstBlockedSrc(user.getDBConnection(), dstLogin, user.getLogin())) {
                User.createMsgFromSrcLoginToDstLogin(user.getDBConnection(), user.getLogin(), dstLogin, msg);

                String whoIsDst = User.whoIs(user.getDBConnection(), dstLogin);

                switch (whoIsDst) {
                    case ("u"):
                        server.sendMsgToUser(user.getLogin(), dstLogin);
                        break;
                    case ("g"):
                        server.sendMsgToGroup(user.getLogin(), dstLogin);
                        break;
                }
            }
        } catch (SQLException ex) {
            logger.error("Error send message to user " + dstLogin + " from " + user.getLogin());
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    public User getUser() {
        return user;
    }

    private void initDialog(String dstLogin) {
        try {
            logger.debug("Init dialog between dst user " + dstLogin + " and src user " + user.getLogin());
            String whoIs = whoIsDst(dstLogin);
            String correspondence = null;

            switch (whoIs) {
                case ("u"):
                    correspondence = getCorrespondenceString(
                            User.getCorrespondenceBySrcLoginAndDstLogin(user.getDBConnection(),
                                    user.getLogin(), dstLogin));
                    break;
                case ("g"):
                    correspondence = getCorrespondenceString(
                            Group.getCorrespondenceByDstLogin(user.getDBConnection(), dstLogin));
                    break;
            }

            if (correspondence != null) sendCorrespondence(correspondence);
        } catch (SQLException ex) {
            logger.error("Error init dialog between dst user " + dstLogin + " and src user " + user.getLogin());
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }
    
    private void sendCorrespondence(String correspondence) {
        String[] msgs = splitTo64kBData(correspondence, "/dialogmsgsbegin",
                "/dialogmsgsnext", "/dialogmsgsend");
        for (String msg : msgs) sendMsg(msg);
    }

    private String getCorrespondenceString(ArrayList<String> correspondence) {
        StringBuilder res = new StringBuilder();
        for (String s : correspondence) 
            res.append(":").append(s);
        
        return res + ":";
    }

    private String whoIsDst(String dstLogin) throws SQLException {
        return User.whoIs(user.getDBConnection(), dstLogin);
    }

    public void sendMsg(String msg) {
        try {
            logger.debug("Send message");
            out.writeUTF(msg);
        } catch (IOException ex) {
            logger.error("Error send message");
            logger.error(ex.getCause());
            logger.error(ex.getMessage());
            logger.error(ex.getStackTrace());
        }
    }

    private void sendContacts() {
        while (true) {
            try {
                logger.debug("Send contacts");

                String str = in.readUTF();

                if (str.equals("/getcontacts")) {
                    try {
                        ArrayList<User> users = User.getContactsById(user.getDBConnection(), user.getId());
                        User[] usersContacts = users.toArray(new User[0]);

                        ArrayList<Group> groups = Group.getContactsById(user.getDBConnection(), user.getId());
                        Group[] groupsContacts = groups.toArray(new Group[0]);

                        String[] msgs = splitTo64kBData(createContactsMsg(usersContacts, groupsContacts),
                                "/getcontactsok", "/getcontactsnext", "/getcontactsend");
                        for (String msg : msgs) sendMsg(msg);
                        break;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                logger.error("Error send contacts");
                logger.error(ex.getCause());
                logger.error(ex.getMessage());
                logger.error(ex.getStackTrace());
            }
        }
    }

    private String[] splitTo64kBData(String msg, String startInfo, String midInfo, String endInfo) {
        int nBytes = 65000, msgLength = msg.length();
        String[] msgs = new String[(int) Math.ceil((double) msgLength / (double) nBytes)];

        for (int i = 0; i < msgs.length; i++)
            msgs[i] = (i != 0 ? midInfo : startInfo) +
                    msg.substring(i * nBytes, Math.min((i + 1) * nBytes, msgLength)) +
                    (i != (msgs.length - 1) ? midInfo : endInfo);

        return msgs;
    }

    private String createContactsMsg(User[] usersContacts, Group[] groupsContacts) {
        return addContactsToMsg(usersContacts) + addContactsToMsg(groupsContacts);
    }

    private String addContactsToMsg(Conversation[] contacts) {
        StringBuilder msg = new StringBuilder();

        for (Conversation c : contacts)
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write((RenderedImage) c.getImg(), "jpg", baos);
                baos.flush();
                //Лучше конечно, сериализовать, но я торопился))
                msg.append(String.format(":%s:%s:%s", c.getLogin(), c.getName(), Base64.encode(baos.toByteArray())));
                baos.close();
            } catch (IOException ex) {
                logger.error("Error read image");
                logger.error(ex.getCause());
                logger.error(ex.getMessage());
                logger.error(ex.getStackTrace());
            }

        return msg.toString();
    }

    private boolean checkUser() throws IOException {
        while (true) {
            String str = in.readUTF();

            if (str.startsWith("/auth")) {
                String[] tokens = str.split(":");
                user = new User(server.getConnection(), tokens[1], tokens[2]);

                if (user.getId() > 0 && !server.isUserAlreadyAuthorized(user.getLogin())) {
                    sendMsg("/authok");
                    server.subscribe(user.getLogin(),ClientHandler.this);
                    break;
                } else {
                    sendMsg("Incorrect login/password");
                }
            }
        }

        return true;
    }
}
