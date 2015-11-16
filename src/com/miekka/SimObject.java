package com.miekka;

import com.miekka.helper.Pair;
import java.util.ArrayList;
import java.util.Collections;

public class SimObject {
    public Pair<Double,Double> Sz;
    public Pair<Double,Double> P;
    protected double[] V;
    protected double[] H;

    public SimObject() {}

    /*TODO:
    * 1. Clean up this code. Abstract and try to eliminate repetition.
    * 2. Run lots more tests on 'containsPoint' stuff.
    * 3. Merge 'testing' into 'master'.
    * 4. After testing 'containsPoint', create a new 'collisionMap' class for the rest of collision handling.
    *   collisionMap class:
    *       * Add a collisionMap class that allows the user to "register" vehicles.
    *       * collisionMap.checkCollide() returns an array. Empty for no collisions, and otherwise contains
    *         all vehicles involved in the collision.
    *       * collisionMap is a hashMap datatype that stores all registered cars as the keys, and a boolean
    *         for collision as the value.
    *       * Perhaps several smaller local collisionMaps could be used in place of one large one for the
     *        sake of performance.
    */

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
            System.out.println("\n");
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
