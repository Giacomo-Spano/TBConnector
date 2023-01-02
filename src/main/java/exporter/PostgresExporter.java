package exporter;

import config.Configuration;
import importer.Importer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PostgresExporter extends Exporter {
    public PostgresExporter(Exporter exporter) {
        this.setName(exporter.getName());
        this.setThost(exporter.gethost());
        this.setDBname(exporter.getDBname());
        this.setPort(exporter.getPort());
    }

    public void publishTelemetry(String name, String type, String token, LocalDateTime localDateTime, double power) {

        Connection c = null;
        Statement stmt = null;
        //int id = 10;

        try {
            Class.forName("org.postgresql.Driver");
            String host = Configuration.getExporters().get(0).gethost();
            String dbname = Configuration.getExporters().get(0).getDBname();
            String port = Configuration.getExporters().get(0).getPort();

            String connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            System.out.println("Connecting to database " + connectionUrl);

            c = DriverManager
                    .getConnection(connectionUrl     /*"jdbc:postgresql://localhost:5432/postgres"*/,
                            Configuration.getExporters().get(1).getUser(), Configuration.getExporters().get(1).getPassword()/*"docker"*/);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            String sql = "INSERT INTO power (id, name, time, power) "
                    + "VALUES ('" + "id_" + name + "','" + name + "', timestamp '" + localDateTime/*.atZone(ZoneId.of("Europe/Rome"))*/ + "', " + power + " );";

            System.out.println("SQL statement = " + sql);
            stmt.executeUpdate(sql);

            stmt.close();
            c.commit();
            c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0);
        }
    }

    public  void init() {

    }
}
