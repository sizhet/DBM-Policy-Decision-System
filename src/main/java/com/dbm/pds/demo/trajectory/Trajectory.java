package com.dbm.pds.demo.trajectory;

public class Trajectory {

    private final String name;
    private final int length;
    private final double risk;

    public Trajectory(String name, int length, double risk) {
        this.name = name;
        this.length = length;
        this.risk = risk;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public double getRisk() {
        return risk;
    }

    @Override
    public String toString() {
        return name + "(len=" + length + ", risk=" + risk + ")";
    }
}