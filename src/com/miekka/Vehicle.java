package com.miekka;

public class Vehicle {
    private double V;
    private double A;
    private double H;
    private Spatial S;

    /*TODO:
    * 1. Change all attributes, V, H, etc. to arrays of 3 where: [current, target, delta].
    * 2. Once that is in place, there will be no need for A, revise getA to take the 'delta' from V.
    */

    public Vehicle(double velocity, double acceleration, double xPosition, double yPosition, double initHeading) {
        this.V = velocity/60; //Translate to 60fps
        this.A = acceleration/60; //Translate to 60fps
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
        return V*60;
    }

    public double getA() {
        return A*60;
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
