package com.WideMouth.bluetooth20.ControlGroup;

import com.WideMouth.bluetooth20.ItemClass.Project;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class ControlPanel extends LitePalSupport {
    private int id;
    private int project_id;

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

    public List<ControlSlider> getControlSliderList() {
        return LitePal.where("controlPanel_id=?", String.valueOf(id)).find(ControlSlider.class);
    }

    public List<ControlButton> getControlButtonList() {
        return LitePal.where("controlPanel_id=?", String.valueOf(id)).find(ControlButton.class);
    }

    public List<ControlSwitch> getControlSwitchList() {
        return LitePal.where("controlPanel_id=?", String.valueOf(id)).find(ControlSwitch.class);
    }

    public int delete() {
        LitePal.deleteAll(ControlSlider.class, "controlPanel_id = ?", String.valueOf(id));
        LitePal.deleteAll(ControlButton.class, "controlPanel_id = ?", String.valueOf(id));
        LitePal.deleteAll(ControlSwitch.class, "controlPanel_id = ?", String.valueOf(id));
        return super.delete();
    }
}