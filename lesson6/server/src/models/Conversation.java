package models;

import java.awt.*;

public abstract class Conversation {

    protected static final String ROOT_IMG_PATH = "server/src/";

    protected int id;
    protected String login;
    protected String name;
    protected Image img;

    public Conversation(String login) {
        this.id = 0;
        this.login = login;
        this.name = null;
        this.img = null;
    }

    public Conversation(int id, String login, String name, Image img) {
        this(login);
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public String getLogin() { return login; }

    public String getName() { return name; }

    public Image getImg() { return img; }

    public abstract boolean equals(Object obj);

    @Override
    public int hashCode() { return login.hashCode(); }
}
