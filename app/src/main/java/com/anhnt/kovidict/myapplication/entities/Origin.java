package com.anhnt.kovidict.myapplication.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import java.io.Serializable;

public class Origin extends Model implements Serializable {
    private static final long serialVersionUID = 1;
    @Column(name = "favourite")
    public int favourite;
    @Column(name = "origin")
    public String origin;

    public String toString() {
        return this.origin;
    }
}