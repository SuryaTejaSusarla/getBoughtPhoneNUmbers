package com.getdropdowndata.dropdown.model;

public class PrepDropDownData {

    private String organisationId;
    private String entityID;
    String tabelName = "PHONENUMBER";

    public void setOrganisationID(String organisationId) {
        this.organisationId = organisationId;
    
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getOrganisationID() {
        return organisationId;
    }
    public String getEntityID() {
        return entityID;
    }


}
