package com.miekka;

//Imports:
import com.miekka.helper.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

//Define the 'SimObject' class.
//This class holds information pertaining to objects in the simulation; storing things like:
//  * Position
//  * Velocity
//  * Heading (what direction its facing)
//This class also contains the crucial 'containsPoint' function for testing whether or not a point
//is inside of the 'SimObject'. This is the basis for collision detection and Vehicle sensors.
public class SimObject {

    //A unique identifier that can be used to look up the SimObject in the SimLayer.
    protected String ID;

    //Stores the index of a textural representation for the SimObject.
    //As of 2015.11.20: 0 = SimcarGreen (Default), 1 = SimcarBlue (Changing State), 2 = SimcarRed (Frozen).
    protected int Tex;

    //The layer in which this SimObject resides.
    protected SimLayer Layer;

    //Boolean that determines whether or not an object should be updated.
    protected boolean IsAnimated;

    //Boolean that determines whether or not an object is IDVC compatible
    public boolean IDVCCompat;

    //Other global class variables:
    protected Pair<Double,Double> Sz; //Size (xSize,ySize)
    protected Pair<Double,Double> P; //Position (xPos,yPos)
    public double[] V; //Velocity [currentV, targetV, deltaV]
    public double[] H; //Heading  [currentH, targetH, deltaH]

    protected SimObject() {}

    //The 'corners' function returns the 4 corners of the SimObject. It uses trigonometry
    //to rotate all of the rectangles corners around the center of the figure.
    protected ArrayList<Pair<Double,Double>> corners() {
        double L = Sz.fst;
        double W = Sz.snd;
        double hyp = Math.sqrt(Math.pow(L/2,2) + Math.pow(W/2,2));
        double aa = Math.toDegrees(Math.atan((W/2)/(L/2)));
        double ab = Math.toDegrees(Math.atan((W/2)/(-L/2))) + 180;
        double ac = Math.toDegrees(Math.atan((-W/2)/(-L/2))) + 180;
        double ad = Math.toDegrees(Math.atan((-W/2)/(L/2))) + 360;

        ArrayList<Pair<Double,Double>> c = new ArrayList<>(4);
        c.add(new Pair<>((Math.cos(Math.toRadians(ab + H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ab + H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(aa + H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(aa + H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(ad + H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ad + H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(ac + H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ac + H[0])) * hyp) + P.snd));
        return c;
    }

    //Takes two points and returns an equation for a line in point-slope form.
    //Note: The "Line" is returned in 3 parts, and is assembled into: y = P1*(x-P2)+P3
    //Also, the Boolean, if true, means y < line, if false, y > line.
    private Pair<double[],Boolean> pntsToLn(Pair<Double,Double> A,Pair<Double,Double> B) {
        double[] ln = new double[3];
        ln[0] = (A.snd - B.snd) / (A.fst - B.fst);
        ln[1] = A.fst;
        ln[2] = A.snd;
        double abM = (A.snd + B.snd) / 2;
        return (new Pair<>(ln,abM > P.snd));
    }

    //Takes a line and a point and tests whether or not the point satisfies the inequality.
    private boolean inequality(Pair<double[],Boolean> ln, double x, double y) {
        return (ln.snd ? y <= ln.fst[0] * (x - ln.fst[1]) + ln.fst[2] : y >= ln.fst[0] * (x - ln.fst[1]) + ln.fst[2]);
    }

    //The all important 'containsPoint' function, determines if the given point is inside of outside of the SimObject
    public boolean containsPoint(double x, double y) {
        ArrayList<Pair<Double, Double>> cs = corners();
        //If the heading of the vehicle is not 0, 90, 180, 270, 360... use the rotated collision detection algorithm
        //Otherwise use the traditional Axis-Aligned Bounding Box collision detection method.
        if(H[0] != 0 && H[0] % 90 != 0) {
            Pair<double[], Boolean> ln1 = pntsToLn(cs.get(0), cs.get(1));
            Pair<double[], Boolean> ln2 = pntsToLn(cs.get(1), cs.get(2));
            Pair<double[], Boolean> ln3 = pntsToLn(cs.get(2), cs.get(3));
            Pair<double[], Boolean> ln4 = pntsToLn(cs.get(3), cs.get(0));
            return (inequality(ln1, x, y) && inequality(ln2, x, y) && inequality(ln3, x, y) && inequality(ln4, x, y));
        }
        else {
            ArrayList<Double> cxs = new ArrayList<>(4);
            ArrayList<Double> cys = new ArrayList<>(4);
            for(Pair<Double,Double> C : cs) {
                cxs.add(C.fst);
                cys.add(C.snd);
            }
            boolean inX = Collections.max(cxs) >= x && x >= Collections.min(cxs);
            boolean inY = Collections.max(cys) >= y && y >= Collections.min(cys);
            return (inX && inY);
        }
    }

    //Returns the UUID of the SimObject
    public String getID() {
        return ID;
    }

    public double[] getV() {
        return V.clone();
    }

    public double[] getH() {
        return H.clone();
    }

    //Moves the SimObject by the specified x and y.
    public void move(double mx, double my) {
        P.fst += mx;
        P.snd += my;
    }

    //Sets the 'IsAnimated' value to false "freezing" the object and preventing it from updating.
    public void freeze() {
        IsAnimated = false;
        Tex = 2; //2 = "Frozen" texture.
    }
}
