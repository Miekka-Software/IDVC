package com.miekka;

//Imports:
import com.miekka.helper.Pair;

import java.util.UUID;

//Define the Vehicle class.
//It extends the 'SimObject' class, and adds functionality for driving the vehicle.
public class Vehicle extends SimObject {

    //'Vehicle' constructor:
    //  1. Sets an initial velocity, position, and heading.
    //  2. Set the size of the vehicle.
    //  3. Set initial texture to texture 0.
    //  4. Defaults the 'IsAnimated' value to true.
    //  5. Generate a random UUID used to identify the Vehicle.
    //  6. Register the new vehicle in the given 'SimLayer' so its collisions can be tracked.
    public Vehicle(double velocity, double xPosition, double yPosition, double initHeading, Pair<Double,Double> size, SimLayer layer) {
        V = new double[]{velocity,velocity,0};
        H = new double[]{initHeading,initHeading,0};
        P = new Pair<>(xPosition,yPosition);
        Sz = size;
        Tex = 0;
        IsAnimated = true;
        ID = UUID.randomUUID().toString();
        Layer = layer;
        Layer.register(this);
    }

    //Takes an array of 3, [currentValue,targetValue,delta], and gradually moves the current value
    //towards the target by a rate of 'delta' on every call.
    //Returns 'true' if Vehicle is adjusting, and 'false' otherwise.
    private boolean adjustTo(double[] P) {
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
            return false;
        }
        return true;
    }

    //This function updates the state of a vehicle. It adjusts the velocity and heading, then moves the vehicle.
    //Also changes texture if the Vehicle is turning or accelerating.
    public void nextTick() {
        if(IsAnimated) {
            boolean dvp = adjustTo(V);
            boolean dhp = adjustTo(H);
            if (dvp || dhp) {
                Tex = 1; //2 = "Changing State" texture.
            } else {
                Tex = 0; //0 = "Default" texture.
            }
            double xv = getVelocity() / 60 * Math.cos(Math.toRadians(getHeading()));
            double yv = getVelocity() / 60 * Math.sin(Math.toRadians(getHeading()));
            move(xv, yv);
        }
    }

    //'senseDist' returns the distance to the nearest SimObject at the relative angle 'angle'.
    //This function works by moving a point along a sensor line until it either goes out of range,
    //or collides with another SimObject in this Layer. The function then returns the distance from
    //the center of this SimObject to the point.
    public double senseDist(double angle, double precision, double maxRange) {
        //Local variables:
        double distance = 0; //Distance from the point to the center of this SimObject.
        double adjAngle = H[0] + angle; //Absolute sensor heading.
        boolean detected = false; //Is the point colliding with another figure?

        //If 'detected' is false, and distance has not exceeded its max, move the point and test for collisions.
        while(!detected && distance < maxRange) {

            //Update distance and find the new point.
            distance += precision;
            double pointX = (Math.cos(Math.toRadians(adjAngle)) * distance) + P.fst;
            double pointY = (Math.sin(Math.toRadians(adjAngle)) * distance) + P.snd;

            //Check all SimObjects (excluding this one) for collisions with the point.
            //If there is one, update 'detected' to 'true'.
            for(SimObject obj : Layer.members()) {
                if(obj != this) {
                    if (obj.containsPoint(pointX, pointY)) {
                        detected = true;
                    }
                }
            }
        }
        //Return the accumulated distance.
        return distance;
    }

    //This function takes a target velocity and a delta, then sets these values in the velocity array.
    //Delta is divided by 60 because this function is called 60 times per second in 'AnimationTimer'.
    public void accelerate(double tv, double dv) {
        V[1] += tv;
        V[2] = dv/60;
    }

    //This is a shortcut function for stopping the vehicle.
    public void stop(double dv) {
        accelerate(-getVelocity(),dv);
    }

    //This function takes a target heading and a delta, then sets these values in the heading array.
    //Delta is divided by 60 because this function is called 60 times per second in 'AnimationTimer'.
    public void turn(double th, double dh) {
        H[1] += th;
        H[2] = dh/60;
    }

    //Simple functions for returning information about the vehicle:

    //Velocity
    public double getVelocity() {
        return V[0];
    }
    //Acceleration
    public double getAcceleration() {
        return V[2]*60;
    }
    //Heading
    public double getHeading() {
        return H[0];
    }
    //X screen-coordinate
    public double getX() {
        return P.fst;
    }
    //Y screen-coordinate
    public double getY() {
        return P.snd;
    }
}
