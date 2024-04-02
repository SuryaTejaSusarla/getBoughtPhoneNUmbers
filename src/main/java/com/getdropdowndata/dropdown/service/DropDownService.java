package com.getdropdowndata.dropdown.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.getdropdowndata.dropdown.model.PrepareRetunData;
import com.getdropdowndata.dropdown.repository.DataSourceForMySql;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DropDownService {

    private DataSourceForMySql dataSource;

    @Autowired
    public DropDownService(DataSourceForMySql dataSource) {
        this.dataSource = dataSource;
    }

    public ResponseEntity<?> getDropDownData(String organizationIdStr, String entityIdStr) {
        // Convert the string UUIDs to byte arrays
        byte[] organizationIdBytes = uuidToBytes(UUID.fromString(organizationIdStr));
        byte[] entityIdBytes = uuidToBytes(UUID.fromString(entityIdStr));

        String tableName = "phone_number";
        String query = "SELECT phone_number FROM " + tableName + " WHERE organization_id = ? AND entity_id = ?";

        try (Connection connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBytes(1, organizationIdBytes);
            statement.setBytes(2, entityIdBytes);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<PrepareRetunData> data = getResultSet(resultSet);
                return ResponseEntity.ok(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<PrepareRetunData> getResultSet(ResultSet resultSet) throws SQLException {
        List<PrepareRetunData> data = new ArrayList<>();
        while (resultSet.next()) {
            String phoneNumber = resultSet.getString("phone_number");
            PrepareRetunData dropDownData = new PrepareRetunData();
            dropDownData.Insertdata(phoneNumber);
            data.add(dropDownData);
        }
        return data;
    }

    private byte[] uuidToBytes(UUID uuid) {
        long hiBits = uuid.getMostSignificantBits();
        long loBits = uuid.getLeastSignificantBits();
        return ByteBuffer.allocate(16).putLong(hiBits).putLong(loBits).array();
    }
}
