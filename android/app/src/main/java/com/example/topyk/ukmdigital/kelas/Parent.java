package com.example.topyk.ukmdigital.kelas;

import java.util.ArrayList;

/**
 * Created by topyk on 7/13/2017.
 */

public class Parent {
    private int _icon_ID = 0;
    private String _Title_parent = "";
    private ArrayList<Child> _Children = null;

    public int get_icon_ID() {
        return _icon_ID;
    }

    public void set_icon_ID(int _icon_ID) {
        this._icon_ID = _icon_ID;
    }

    public String get_Title_parent() {
        return _Title_parent;
    }

    public void set_Title_parent(String _Title_parent) {
        this._Title_parent = _Title_parent;
    }

    public ArrayList<Child> get_Children() {
        return _Children;
    }

    public void set_Children(ArrayList<Child> _Children) {
        this._Children = _Children;
    }
}
