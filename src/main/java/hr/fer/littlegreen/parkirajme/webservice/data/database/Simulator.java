package hr.fer.littlegreen.parkirajme.webservice.data.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator {

    public Simulator(Connection databaseConnection) {

        String sql = """
            BEGIN TRANSACTION;
            update parking_object
            set free_slots = (floor(random() * (capacity + 1)*0.8)::int)::int;
            COMMIT TRANSACTION;
            """;
        Runnable freeSlotsRunnable = () -> {
            Savepoint savepoint = null;
            try(PreparedStatement stmt = databaseConnection.prepareStatement(sql)) {
                savepoint = databaseConnection.setSavepoint();
                stmt.executeUpdate();
            } catch (SQLException ex) {
                if (savepoint != null) {
                    try {
                        databaseConnection.rollback(savepoint);
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }
                }
                ex.printStackTrace();
            }
        };
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(freeSlotsRunnable , 0, 10, TimeUnit.SECONDS);
    }
}
