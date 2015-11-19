package com.miekka;

//Imports:
import com.miekka.helper.Pair;

//Define the Vehicle class.
//It extends the 'SimObject' class, and adds functionality for driving the vehicle.
public class Vehicle extends SimObject {

    //'Vehicle' constructor:
    //  1. Sets an initial velocity, position, and heading.
    //  2. Set the size of the vehicle.
    //  3. Register the new vehicle in the given 'CollisionMap' so its
    //  collisions can be tracked
    public Vehicle(double velocity, double xPosition, double yPosition, double initHeading, Pair<Double,Double> sz, CollisionMap layer) {
        V = new double[]{velocity,velocity,0};
        H = new double[]{initHeading,initHeading,0};
        P = new Pair<>(xPosition,yPosition);
        Sz = sz;
        layer.register(this);
    }

    //Takes an array of 3, [currentValue,targetValue,delta], and gradually moves the current value
    //towards the target by a rate of 'delta' on every call.
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

    //This function updates the "state" of a vehicle. It adjusts the velocity and heading, then moves the vehicle.
    public void updateState() {
        adjustTo(V);
        adjustTo(H);
        double xv = V[0]/60 * Math.cos(Math.toRadians(H[0]));
        double yv = V[0]/60 * Math.sin(Math.toRadians(H[0]));
        move(xv,yv);
    }

    //This function takes a target velocity and a delta, then sets these values in the velocity array.
    //Delta is divided by 60 because this function is called 60 times per second in 'AnimationTimer'.
    public void accelerate(double tv, double dv) {
        V[1] += tv;
        V[2] = dv/60;
    }

    //This is a shortcut function for stopping the vehicle.
    public void stop(double dv) {
        accelerate(-getV(),dv);
    }

    //This function takes a target heading and a delta, then sets these values in the heading array.
    //Delta is divided by 60 because this function is called 60 times per second in 'AnimationTimer'.
    public void turn(double th, double dh) {
        H[1] += th;
        H[2] = dh/60;
    }

    //Simple functions for returning information about the vehicle:

    //Velocity
    public double getV() {
        return V[0];
    }
    //Acceleration
    public double getA() {
        return V[2]*60;
    }
    //Heading
    public double getH() {
        return H[0];
    }
    //X coordinate
    public double getX() {
        return P.fst;
    }
    //Y coordinate
    public double getY() {
        return P.snd;
    }
    //Returns: "(X,Y)"
    public String showPos() {
        return P.show();
    }

}
