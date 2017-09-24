package com.anhnt.kovidict.myapplication.entities;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import java.io.Serializable;

public class Meaning extends Model implements Serializable {
    private static final long serialVersionUID = 1;
    @Column(name = "meaning")
    public String meaning;

    public String toString() {
        return this.meaning;
    }
}