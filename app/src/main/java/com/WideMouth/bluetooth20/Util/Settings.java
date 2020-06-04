package com.WideMouth.bluetooth20.Util;

import com.WideMouth.bluetooth20.R;

import org.litepal.crud.LitePalSupport;

public class Settings extends LitePalSupport {
    private boolean sound = true;
    private boolean message = true;
    private boolean broad = true;
    private boolean notice = true;
    private boolean save = true;
    private boolean createProject = true;
    private boolean scan = true;
    private boolean order = true;
    private boolean createPanel = true;
    private int period = 30;
    private int ImageRes = R.drawable.icon_svg_office_work;

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isBroad() {
        return broad;
    }

    public void setBroad(boolean broad) {
        this.broad = broad;
    }

    public boolean isNotice() {
        return notice;
    }

    public void setNotice(boolean notice) {
        this.notice = notice;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public boolean isCreateProject() {
        return createProject;
    }

    public void setCreateProject(boolean createProject) {
        this.createProject = createProject;
    }


    public boolean isCreatePanel() {
        return createPanel;
    }

    public void setCreatePanel(boolean createPanel) {
        this.createPanel = createPanel;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getImageRes() {
        return ImageRes;
    }

    public void setImageRes(int imageRes) {
        ImageRes = imageRes;
    }

    public boolean isScan() {
        return scan;
    }

    public void setScan(boolean scan) {
        this.scan = scan;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }
}