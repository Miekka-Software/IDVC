package com.miekka;

public class Vehicle {
    private double V;
    private double A;
    private double H;
    private Spatial S;

    public Vehicle(double velocity, double acceleration, double xPosition, double yPosition, double initHeading) {
        this.V = velocity/100; //Translate to milliseconds
        this.A = acceleration/100; //Translate to milliseconds
        this.H = initHeading % 360;
        this.S = new Spatial(xPosition,yPosition);
    }

    public void updateState() {
        this.V += this.A;
        double xv = V * Math.cos(Math.toRadians(H));
        double yv = V * Math.sin(Math.toRadians(H));
        this.S.move(xv,yv);
    }

    public double getV() {
        return V*100;
    }

    public double getA() {
        return A*100;
    }

    public double getH() {
        return H;
    }

    public double getX() {
        return S.P.fst;
    }

    public double getY() {
        return S.P.snd;
    }

    public String showPos() {
        return S.P.show();
    }

}
