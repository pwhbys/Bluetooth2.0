package com.WideMouth.bluetooth20.ItemClass;

import org.litepal.crud.LitePalSupport;

public class Device extends LitePalSupport {
    int id;
    int project_id;
    String name;
    String address;
    int bondState;

    public Device(String name, String address, int bondState) {
        this.name = name;
        this.address = address;
        this.bondState = bondState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBondState() {
        return bondState;
    }

    public void setBondState(int bondState) {
        this.bondState = bondState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}