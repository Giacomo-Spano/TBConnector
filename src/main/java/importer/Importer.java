package importer;

import java.time.LocalDateTime;

public class Importer {
    private String name;
    private String importer;
    private String host;
    private String user;
    private String password;
    private String port;
    private String DBname;

    public Importer() {

    }

    public Importer(Importer importer) {
        this.name = importer.name;
        this.importer = importer.importer;
        this.host = importer.host;
        this.user = importer.user;
        this.password = importer.password;
    }

    public Importer(String name, String importer) {
        super();
        this.name = name;
        this.importer = importer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String gethost() {
        return host;
    }

    public void setThost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDBname() {
        return DBname;
    }

    public void setDBname(String DBname) {
        this.DBname = DBname;
    }

    public void init() {
    }
}
