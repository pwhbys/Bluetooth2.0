package com.WideMouth.bluetooth20.ControlGroup;

import android.text.InputType;

import org.litepal.crud.LitePalSupport;

public class ControlButton extends LitePalSupport implements ControlItem {
    private int id;
    private int controlPanel_id;
    private String onText;
    private String offText;
    private String onMessage;
    private String offMessage;
    private boolean isOn;

    public String getOnText() {
        return onText;
    }

    public void setOnText(String onText) {
        this.onText = onText;
    }

    public String getOffText() {
        return offText;
    }

    public void setOffText(String offText) {
        this.offText = offText;
    }

    public String getOnMessage() {
        return onMessage;
    }

    public void setOnMessage(String onMessage) {
        this.onMessage = onMessage;
    }

    public String getOffMessage() {
        return offMessage;
    }

    public void setOffMessage(String offMessage) {
        this.offMessage = offMessage;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }


    @Override
    public void setAttr(String s, int index) {
        switch (index) {
            case 0:
                onText = s;
                break;
            case 1:
                onMessage = s;
                break;
            case 2:
                offText = s;
                break;
            case 3:
                offMessage = s;
                break;
        }
    }

    @Override
    public String getAttr(int index) {
        switch (index) {
            case 0:
                return onText;
            case 1:
                return onMessage;
            case 2:
                return offText;
            case 3:
                return offMessage;
        }
        return null;
    }

    @Override
    public int getInputType(int index) {
        return InputType.TYPE_CLASS_TEXT;
    }

    @Override
    public String getAttrName(int index) {
        switch (index) {
            case 0:
                return "文本（on）";
            case 1:
                return "消息（on）";
            case 2:
                return "文本（off）";
            case 3:
                return "消息（off）";
        }
        return null;
    }

    @Override
    public void saveByLitePal() {
        save();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getControlPanel_id() {
        return controlPanel_id;
    }

    public void setControlPanel_id(int controlPanel_id) {
        this.controlPanel_id = controlPanel_id;
    }
}