package com.miekka;

//Imports:
import com.miekka.helper.Pair;
import java.util.ArrayList;
import java.util.HashMap;

//Define the 'SimLayer' class.
//This class holds a HashMap of 'SimObjects', and associated Boolean values that indicate whether or
//not any given Vehicle is involved in a collision.
public class SimLayer {
    //Declare HashMap to keep track of all registered SimObjects.
    private HashMap<SimObject,Boolean> objMap;

    //Initialize HashMap
    public SimLayer() {
        objMap = new HashMap<>();
    }

    //Register a SimObject by adding it to the HashMap with the initial value of 'false'.
    public void register(SimObject obj) {
        objMap.put(obj,false);
    }

    //NOTE: This. is. hideous.
    //Also, eventually check for more than corners here.
    //This function checks all the corners of all of the figures in the simulation and tests
    //whether or not they are inside any of the other figures. If one object's corners intersects with
    //another objects's hit-box, change both objects' value to true. True indicates a collision.
    public void update() {
        objMap.replaceAll((k, v) -> false);
        for(SimObject obj1 : objMap.keySet()) {
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

    //This function filters through the whole HashMap and returns all SimObjects (the keys).
    public ArrayList<SimObject> members() {
        ArrayList<SimObject> mbrs = new ArrayList<>();
        for(SimObject obj : objMap.keySet()) {
            mbrs.add(obj);
        }
        return mbrs;
    }

    //This function filters through the whole HashMap and returns all SimObjects with a value of 'true'.
    public ArrayList<SimObject> checkCollisons(){
        ArrayList<SimObject> cObjs = new ArrayList<>();
        for(SimObject obj : members()) {
            if(objMap.get(obj)) {
                cObjs.add(obj);
            }
        }
        return cObjs;
    }
}
