package com.miekka;

//Imports:
import com.miekka.helper.Pair;
import java.util.ArrayList;
import java.util.Collections;

//Define the 'SimObject' class.
//This class holds information pertaining to objects in the simulation; storing things like:
//  * Position
//  * Velocity
//  * Heading (what direction its facing)
//This class also contains the crucial 'containsPoint' function for testing whether or not a point
//is inside of the 'SimObject'. This is the basis for collision detection and Vehicle sensors.
public class SimObject {
    public Pair<Double,Double> Sz;
    public Pair<Double,Double> P;
    protected double[] V;
    protected double[] H;

    public SimObject() {}

    public ArrayList<Pair<Double,Double>> corners() {
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
        return (ln.snd ? y <= ln.fst[0] * (x - ln.fst[1]) + ln.fst[2] : y >= ln.fst[0] * (x - ln.fst[1]) + ln.fst[2]);
    }

    public void move(double mx, double my) {
        P.fst += mx;
        P.snd += my;
    }

    public boolean containsPoint(double x, double y) {
        ArrayList<Pair<Double, Double>> cs = corners();
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
}
