package com.miekka;

//Imports:
import com.miekka.helper.Pair;
import java.util.ArrayList;
import java.util.HashMap;

//Define the 'CollisionMap' class.
//This class holds a HashMap of 'SimObjects', and associated Boolean values that indicate whether or
//not any given Vehicle is involved in a collision.
public class CollisionMap {
    HashMap<SimObject,Boolean> objMap;

    public CollisionMap() {
        objMap = new HashMap<>();
    }

    public void register(SimObject obj) {
        objMap.put(obj,false);
    }

    //This. is. hideous.
    //Also, eventually check for more than corners here.
    public void update() {
        for(SimObject obj1 : objMap.keySet()) {
            objMap.replace(obj1,false);
            for(SimObject obj2 : objMap.keySet()) {
                if(obj2 != obj1) {
                    for(Pair<Double,Double> obj2Pnts : obj2.corners()) {
                        if(obj1.containsPoint(obj2Pnts.fst, obj2Pnts.snd)) {
                            objMap.replace(obj1,true);
                            objMap.replace(obj2,true);
                        }
                    }
                }

            }
        }
    }

    public ArrayList<SimObject> checkCollisons(){
        ArrayList<SimObject> cObjs = new ArrayList<>();
        for(SimObject obj : objMap.keySet()) {
            if(objMap.get(obj)) {
                cObjs.add(obj);
            }
        }
        return cObjs;
    }

}
