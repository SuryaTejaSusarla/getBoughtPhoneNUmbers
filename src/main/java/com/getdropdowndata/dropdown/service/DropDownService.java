package com.getdropdowndata.dropdown.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.getdropdowndata.dropdown.model.PrepDropDownData;
import com.getdropdowndata.dropdown.model.PrepareRetunData;
import com.getdropdowndata.dropdown.repository.DataSourceForMySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class DropDownService {

    public List<PrepareRetunData> getResultSet(ResultSet resultSet ) throws SQLException {
        List<PrepareRetunData> data = new ArrayList<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            //System.out.print(resultSet.getMetaData().getColumnName(i) + "\t");
        }
        //System.out.println();
        while (resultSet.next()) {
           
            for (int i = 1; i <= columnCount; i++) {
                String columnData = resultSet.getString(i);
                //System.out.print(resultSet.getString(i) + "\t");
                PrepareRetunData dropDownData = new PrepareRetunData();
                dropDownData.Insertdata(columnData);
                //System.out.println(dropDownData.getData());
                data.add(dropDownData);
                
            }
            //System.out.println();
        }
        return data;
    }

    private DataSourceForMySql dataSource;

    @Autowired
    public DropDownService(DataSourceForMySql dataSource) {
        this.dataSource = dataSource;
    }
    

    public ResponseEntity<?> getDropDownData(String organisationId, String entityID) {

        PrepDropDownData prepDropDownData = new PrepDropDownData(); 
        prepDropDownData.setOrganisationID(organisationId);
        prepDropDownData.setEntityID(entityID);

        String tableName = "phone_number"; 
        String query = ("SELECT phone_number FROM "+ tableName + " WHERE organisation_id = " + prepDropDownData.getOrganisationID() + " AND entity_id = " + prepDropDownData.getEntityID() + ";" );
       
        try {
            Connection connection = DriverManager.getConnection(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<PrepareRetunData> data = getResultSet(resultSet);
            resultSet.close();
            statement.close();
            connection.close();
            
            return ResponseEntity.ok(data);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

