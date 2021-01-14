package hr.fer.littlegreen.parkirajme.webservice.data.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulator {

    public Simulator(Connection databaseConnection) {

        String sql = """
            BEGIN transaction;
                update parking_object p
                set free_slots =
                (SELECT floor(random() * (capacity - floor(0.9*capacity)::int + 1) + floor(0.9*capacity))::int)
                - (select COUNT(*) from reservation r
                where r.object_uuid = p.object_uuid);
            COMMIT TRANSACTION;
            """;
        Runnable freeSlotsRunnable = () -> {
            Savepoint savepoint = null;
            try(PreparedStatement stmt = databaseConnection.prepareStatement(sql)) {
                savepoint = databaseConnection.setSavepoint();
                stmt.executeUpdate();
                System.out.println("ispravno");
            } catch (SQLException ex) {
                if (savepoint != null) {
                    try {
                        databaseConnection.rollback(savepoint);
                    } catch (SQLException e2) {
                        e2.printStackTrace();
                    }
                }
                //ex.printStackTrace();
            }
        };
        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(freeSlotsRunnable , 0, 20, TimeUnit.SECONDS);
    }
}
