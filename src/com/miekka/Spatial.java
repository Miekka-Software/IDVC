package com.miekka;

public class Spatial {
    public Pair<Double,Double> P;

    public Spatial(double xp, double yp) {
        this.P = new Pair<>(xp,yp);
    }

    public void move(double mx, double my) {
        this.P.fst += mx;
        this.P.snd += my;
    }

}
