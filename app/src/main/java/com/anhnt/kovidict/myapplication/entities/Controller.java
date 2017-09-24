package com.anhnt.kovidict.myapplication.entities;

import com.activeandroid.query.Select;
import java.util.ArrayList;

public class Controller {
    private Meaning meaning;
    private Origin origin;

    public Controller(int type_dict) {
        switch (type_dict) {
            case 0:
                this.origin = new KVOrigin();
                this.meaning = new KVMeaning();
            default:
        }
    }

    /**
     * SELECT origin,favourite FROM kv_origin ORDER BY origin ASC LIMIT 30;
     */
    public ArrayList<Origin> getOrigins() {
        return (ArrayList) new Select().from(this.origin.getClass()).orderBy("origin asc limit 30").execute();
    }

    /**
     * SELECT origin,favourite FROM kv_origin WHERE favourite=1 ORDER BY origin ASC;
     */
    public ArrayList<Origin> getFavourites() {
        return (ArrayList) new Select().from(this.origin.getClass()).where("favourite=1").orderBy("origin asc").execute();
    }

    /**
     * SELECT origin,favourite FROM kv_origin WHERE origin LIKE 'nội dung%' ORDER BY origin ASC LIMIT 30;
     */
    public ArrayList<Origin> search(String query) {
        return (ArrayList) new Select().from(this.origin.getClass()).where("origin like '" + query + "%'").orderBy("origin asc limit 30").execute();
    }

    public ArrayList<Origin> search(String query, int from, int end) {
        return (ArrayList) new Select().from(this.origin.getClass()).where("origin like '" + query + "%'").orderBy("origin asc limit " + from + "," + end).execute();
    }

    /**
     * SELECT meaning FROM kv_meaning WHERE Id = (Long.valueOf(id));
     */
    public Meaning getMeaning(long id) {
        return (Meaning) new Select().from(this.meaning.getClass()).where("Id = ?", Long.valueOf(id)).executeSingle();
    }
}