package com.miekka;

public class Spatial {
    public Pair<Double,Double> P;

    public Spatial(double xp, double yp) {
        P = new Pair<>(xp,yp);
    }

    public void move(double mx, double my) {
        P.fst += mx;
        P.snd += my;
    }
}
