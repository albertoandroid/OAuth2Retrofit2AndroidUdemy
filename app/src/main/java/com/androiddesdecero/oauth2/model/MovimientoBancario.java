package com.androiddesdecero.oauth2.model;

/**
 * Created by albertopalomarrobledo on 26/12/18.
 */

public class MovimientoBancario {

    private Long userID;

    private String importe;

    private String name;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
