package com.miekka.helper;

public class Node {
    private String ID;
    private Pair<Double,Double> Position;
    private long LastUpdate;
    private double[] Velocity;
    private double[] Heading;
    //Add approx GPS position here.

    public Node(String id, Pair<Double,Double> pos, double[] v, double[] h) {
        this.ID = id;
        this.Position = pos;
        this.Velocity = v;
        this.Heading = h;
        this.LastUpdate = System.nanoTime();
    }

    public String getID() {
        return ID;
    }

    public Pair<Double,Double> getPosition() {
        return Position;
    }

    public long getLastUpdate() {
        return LastUpdate;
    }

    public double[] getVelocity() {
        return Velocity;
    }

    public double[] getHeading() {
        return Heading;
    }

    public void updateNode(Pair<Double,Double> newPosition, double[] newVelocity, double[] newHeading) {
        this.Position = newPosition;
        this.Velocity = newVelocity;
        this.Heading = newHeading;
        this.LastUpdate = System.nanoTime();
    }

    public String show() {
        return (String.join("\n",
                            "Information for node " + this.ID + ":",
                            "\tPosition: " + this.Position.show(),
                            "\tTime of last update (nanos): " + this.LastUpdate,
                            "\tCurrent Velocity: " + this.Velocity[0],
                            "\tTarget Velocity: " + this.Velocity[1],
                            "\tDelta Velocity (Acceleration): " + this.Velocity[2],
                            "\tCurrent Heading: " + this.Heading[0],
                            "\tTarget Heading: " + this.Heading[1],
                            "\tDelta Heading (Turning Speed): " + this.Heading[2]));
    }

}
