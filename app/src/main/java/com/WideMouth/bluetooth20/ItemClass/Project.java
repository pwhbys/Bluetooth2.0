package com.WideMouth.bluetooth20.ItemClass;

import com.WideMouth.bluetooth20.ControlGroup.ControlPanel;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Project extends LitePalSupport {
    private int id;
    private Device device;
    private String name;
    private String Date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ControlPanel> getControlPanelList() {
        return LitePal.where("project_id = ?", String.valueOf(id)).find(ControlPanel.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}