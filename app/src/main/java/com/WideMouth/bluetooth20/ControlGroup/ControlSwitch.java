package com.WideMouth.bluetooth20.ControlGroup;

import android.text.InputType;

import org.litepal.crud.LitePalSupport;

public class ControlSwitch extends LitePalSupport implements ControlItem {
    private int id;
    private int controlPanel_id;
    private String text;
    private String onMessage;
    private String offMessage;
    private boolean isOn;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
                text = s;
                break;
            case 1:
                onMessage = s;
                break;
            case 2:
                offMessage = s;
                break;
        }
    }

    @Override
    public String getAttr(int index) {
        switch (index) {
            case 0:
                return text;
            case 1:
                return onMessage;
            case 2:
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
                return "文本";
            case 1:
                return "消息（on）";
            case 2:
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