package com.example.planetmovieapp.Objects;

import java.io.Serializable;

/*Hall class represents the Hall in our App and in our DB as table name*/
public class Hall implements Serializable {

    private String hallId;
    private String hallName;
    private int row;
    private int column;

    public Hall(){}

    public Hall(String hallId, String hallName, int row, int column) {
        this.hallId = hallId;
        this.hallName = hallName;
        this.row = row;
        this.column = column;
    }

    public String getHallId() { return hallId; }

    public String getHallName() {
        return hallName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
