package com.miekka;

import com.miekka.helper.Pair;
import java.util.ArrayList;

public class SimObject {
    public Pair<Double,Double> Sz;
    public Pair<Double,Double> P;
    protected double[] V;
    protected double[] H;

    public SimObject() {}

    //I am pretty sure I will never need this, but it's going to stick around just a little while longer.
    /*public ArrayList<Pair<Double,Double>> unRotatedCorners() {
        ArrayList<Pair<Double,Double>> urc = new ArrayList<>(4);
        urc.add(new Pair<>(P.fst - (Sz.fst/2),P.snd + (Sz.snd/2)));
        urc.add(new Pair<>(P.fst + (Sz.fst/2),P.snd + (Sz.snd/2)));
        urc.add(new Pair<>(P.fst + (Sz.fst/2),P.snd - (Sz.snd/2)));
        urc.add(new Pair<>(P.fst - (Sz.fst/2),P.snd - (Sz.snd/2)));
        return urc;
    }*/

    public ArrayList<Pair<Double,Double>> corners() {
        double L = Sz.fst;
        double W = Sz.snd;
        double hyp = Math.sqrt(Math.pow(L/2,2) + Math.pow(W/2,2));
        double aa = Math.toDegrees(Math.atan((W/2)/(L/2)));
        double ab = Math.toDegrees(Math.atan((W/2)/(-L/2))) + 180;
        double ac = Math.toDegrees(Math.atan((-W/2)/(-L/2))) + 180;
        double ad = Math.toDegrees(Math.atan((-W/2)/(L/2))) + 360;

        ArrayList<Pair<Double,Double>> c = new ArrayList<>(4);
        c.add(new Pair<>((Math.cos(Math.toRadians(ab - H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ab - H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(aa - H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(aa - H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(ad - H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ad - H[0])) * hyp) + P.snd));
        c.add(new Pair<>((Math.cos(Math.toRadians(ac - H[0])) * hyp) + P.fst, (Math.sin(Math.toRadians(ac - H[0])) * hyp) + P.snd));
        return c;
    }

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

    private boolean inequality(Pair<double[],Boolean> ln, double x, double y) {
        return (ln.snd ? y < ln.fst[0] * (x - ln.fst[1]) + ln.fst[2] : y > ln.fst[0] * (x - ln.fst[1]) + ln.fst[2]);
    }

    public void move(double mx, double my) {
        P.fst += mx;
        P.snd += my;
    }

    public boolean containsPoint(double x, double y) {
        if(H[0] != 0 && H[0] % 90 != 0) {
            ArrayList<Pair<Double, Double>> cs = corners();
            Pair<double[], Boolean> ln1 = pntsToLn(cs.get(0), cs.get(1));
            Pair<double[], Boolean> ln2 = pntsToLn(cs.get(1), cs.get(2));
            Pair<double[], Boolean> ln3 = pntsToLn(cs.get(2), cs.get(3));
            Pair<double[], Boolean> ln4 = pntsToLn(cs.get(3), cs.get(0));
            return (inequality(ln1, x, y) && inequality(ln2, x, y) && inequality(ln3, x, y) && inequality(ln4, x, y));
        }
        else {
            //TODO: handle all non-rotated collisions here
            return true;
        }
    }
}
