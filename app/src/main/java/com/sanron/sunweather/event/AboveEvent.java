package com.sanron.sunweather.event;


public class AboveEvent {
    private int type;
    private Object obj;
    public static final int TYPE_TOP_MENU_CLICK = 1;
    public static final int TYPE_CENTER_CLICK = 2;
    public static final int TYPE_BOTTOM_STATE_CHANGE = 3;

    public AboveEvent(int type, Object obj) {
        this.setType(type);
        this.setObj(obj);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
