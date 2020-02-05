package com.example.fireapp;

public class Hall {
    private String hallName;
    private int row;
    private int column;

    public Hall(){}

    public Hall(String hallName, int row, int column) {
        this.hallName = hallName;
        this.row = row;
        this.column = column;
    }


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
