package com.example.fireapp;

public class Hall {
    private String hallName;
    private String row;
    private String column;

    public Hall(){}

    public Hall(String hallName, String row, String column) {
        this.hallName = hallName;
        this.row = row;
        this.column = column;
    }


    public String getHallName() {
        return hallName;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }
}
