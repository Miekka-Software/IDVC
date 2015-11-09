package com.miekka;

public class Vehicle {
    private double[] V;
    private double[] H;
    private Spatial S;

    public Vehicle(double velocity, double xPosition, double yPosition, double initHeading) {
        V = new double[]{velocity,velocity,0};
        H = new double[]{initHeading,initHeading,0};
        S = new Spatial(xPosition,yPosition);
    }

    private void adjustTo(double[] P) {
        if(P[0] != P[1]) {
            if(P[0] < P[1]) {
                P[0] += P[2];
                if(P[0] > P[1]) {
                    P[0] = P[1];
                }
            }
            if(P[0] > P[1]) {
                P[0] -= P[2];
                if(P[0] < P[1]) {
                    P[0] = P[1];
                }
            }
        }
        else {
            P[2] = 0;
        }
    }

    public void updateState() {
        adjustTo(V);
        adjustTo(H);
        double xv = V[0]/60 * Math.cos(Math.toRadians(H[0]));
        double yv = V[0]/60 * Math.sin(Math.toRadians(H[0]));
        S.move(xv,yv);
    }

    public void accelerate(double tv, double dv) {
        V[1] += tv;
        V[2] = dv/60;
    }

    public void stop(double dv) {
        V[1] = 0;
        V[2] = dv/60;
    }

    public void turn(double th, double dh) {
        H[1] += th;
        H[2] = dh/60;
    }

    public double getV() {
        return V[0];
    }

    public double getA() {
        return V[2]*60;
    }

    public double getH() {
        return H[0];
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
