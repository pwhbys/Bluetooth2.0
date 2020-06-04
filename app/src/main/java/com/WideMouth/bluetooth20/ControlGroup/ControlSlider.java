package com.WideMouth.bluetooth20.ControlGroup;

import android.text.InputType;

import org.litepal.crud.LitePalSupport;

public class ControlSlider extends LitePalSupport implements ControlItem {
    private int id;
    private int controlPanel_id;
    private String text;
    private int minProgress;
    private int maxProgress;
    private int progress;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMinProgress() {
        return minProgress;
    }

    public void setMinProgress(int minProgress) {
        this.minProgress = minProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }


    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void setAttr(String s, int index) {
        switch (index) {
            case 0:
                text = s;
                break;
            case 1:
                try {
                    minProgress = Integer.parseInt(s);
                } catch (Exception e) {

                }
                break;
            case 2:
                try {
                    maxProgress = Integer.parseInt(s);
                } catch (Exception e) {

                }
                break;
        }
    }

    @Override
    public String getAttr(int index) {
        switch (index) {
            case 0:
                return text;
            case 1:
                return String.valueOf(minProgress);
            case 2:
                return String.valueOf(maxProgress);
        }
        return null;
    }

    @Override
    public int getInputType(int index) {
        switch (index) {
            case 0:
                return InputType.TYPE_CLASS_TEXT;
            case 1:
            case 2:
                return InputType.TYPE_CLASS_NUMBER;
        }
        return InputType.TYPE_CLASS_TEXT;
    }

    @Override
    public String getAttrName(int index) {
        switch (index) {
            case 0:
                return "文本";
            case 1:
                return "最小值";
            case 2:
                return "最大值";
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