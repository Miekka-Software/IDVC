package com.miekka;

public class Vehicle {
    public double V;
    public double A;
    public Spatial S;

    public Vehicle(double velocity, double acceleration, double xPosition, double yPosition) {
        this.V = velocity;
        this.A = acceleration;
        this.S = new Spatial(xPosition,yPosition);
    }

    public void updateState() {
        this.V += this.A;
        this.S.move(this.V,0);
        //Replace 0 with some trig function using heading and forward velocity.
    }

}
