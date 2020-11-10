package hr.fer.littlegreen.parkirajme.webservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                "jdbc:postgresql://ec2-54-216-202-161.eu-west-1.compute.amazonaws.com:5432/deg1t0cmdemhn7",
                "icfjttdivtiins",
                "447e4ba24d1cc40dff940de459899fbe24bf4c5999da88ff23508aafe16138dc"
            );
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

}
