package com.miekka;

public class Vehicle {
    public double V;
    public double A;

    public Vehicle(double Velocity, double Acceleration) {
        this.V = Velocity;
        this.A = Acceleration;
    }

    public void updateVelocity() {
        this.V += this.A;
    }
}
