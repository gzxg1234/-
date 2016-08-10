package com.sanron.sunweather.event;


public class CityEvent {
    private int position;
    private int type;
    public static final int TYPE_DEL = 1;
    public static final int TYPE_REFRESH = 2;
    public static final int TYPE_PRIMARY = 3;
    public static final int TYPE_SELECT = 4;
    public static final int TYPE_ADD = 5;

    public CityEvent(int type, int position) {
        this.type = type;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
