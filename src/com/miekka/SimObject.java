package com.miekka;

import com.miekka.helper.Pair;

public class SimObject {
    public Pair<Double,Double> Sz;
    public Pair<Double,Double> P;
    protected double[] V;
    protected double[] H;

    public SimObject() {}

    /*private Pair<Double,Double> TL() {
        return (new Pair<>(P.fst - (Sz.fst/2),P.snd + (Sz.snd/2)));
    }

    private Pair<Double,Double> BR() {
        return (new Pair<>(P.fst + (Sz.fst/2),P.snd - (Sz.snd/2)));
    }*/

    public void move(double mx, double my) {
        P.fst += mx;
        P.snd += my;
    }

    public boolean containsPoint(double x, double y) {
        return false;
    }
}
