
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

public class PostgresPublisher {

    //Connection c = null;

    /*public void init()  {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/postgres",
                            "postgres", "docker");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            //c.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }*/

    public void publish(LocalDateTime localDateTime, String id, String name, float power)  {

        Connection c = null;
        Statement stmt = null;
        //int id = 10;

        try {
            Class.forName("org.postgresql.Driver");
            String host = TopicSubscriber.configuration.getPostgresURL();
            String dbname = TopicSubscriber.configuration.getPostgresDBName();
            String port = TopicSubscriber.configuration.getPostgresPort();

            String connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            System.out.println("Connecting to database " + connectionUrl);

            c = DriverManager
                    .getConnection(connectionUrl     /*"jdbc:postgresql://localhost:5432/postgres"*/,
                            Configuration.getPostgresUser()/*"postgres"*/, Configuration.getPostgresPassword()/*"docker"*/);
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            /*stmt = c.createStatement();
            String sql = "CREATE TABLE COMPANY " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " AGE            INT     NOT NULL, " +
                    " ADDRESS        CHAR(50), " +
                    " SALARY         REAL)";
            stmt.executeUpdate(sql);*/

            //LocalDateTime date = LocalDateTime.of(2017, Month.FEBRUARY,3,6,30,40,50000);
            //LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
            //LocalDateTime localDateTime = LocalDateTime.now();
            System.out.println(localDateTime);
            System.out.println("offset = " + localDateTime.atZone(ZoneId.of("Europe/Rome")).getOffset().getTotalSeconds());
            /*if (localDateTime.atZone(ZoneId.of("Europe/Rome")).getOffset().getTotalSeconds() != 3600) {
              int offset =   localDateTime.atZone(ZoneId.of("Europe/Rome")).getOffset().getTotalSeconds();
              localDateTime.plusSeconds(3600);
            }*/

            stmt = c.createStatement();
            String sql = "INSERT INTO power (id, name, time, power) "
                    + "VALUES ('" + id + "','" + name + "', timestamp '" + localDateTime/*.atZone(ZoneId.of("Europe/Rome"))*/ + "', " + power + " );";

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
}
