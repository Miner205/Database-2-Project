package com.project.artconnect.model;

public class Dimension {
    private double length;
    private double width;
    private double depth;

    public Dimension() {
    }

    public Dimension(double length, double width, double depth) {
        this.length = length;
        this.width = width;
        this.depth = depth;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        if (this.length == 0 && this.width== 0 && this.depth == 0) {
            return "digital or no data";
        }
        if (this.depth == 0) {
            return length + " x " + width;
        }
        return length + " x " + width + " x " + depth;
    }

}
