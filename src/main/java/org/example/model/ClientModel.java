package org.example.model;

import org.example.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientModel {
    public boolean saveClient(String username) throws SQLException {
        Connection connection= DbConnection.getInstance().getConnection();
        String sql="INSERT INTO CLIENT(username) VALUES (?)";
        PreparedStatement pstm=connection.prepareStatement(sql);
        pstm.setString(1, username);

        return pstm.executeUpdate() > 0;
    }
}
