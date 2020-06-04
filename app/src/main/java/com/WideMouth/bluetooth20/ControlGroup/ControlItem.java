package com.WideMouth.bluetooth20.ControlGroup;

public interface ControlItem {

    void setAttr(String s,int index);

    String getAttr(int index);

    int getInputType(int index);

    String getAttrName(int index);

    void saveByLitePal();
}