package com.miekka.helper;

//Define the polymorphic 'Pair' class.
//This class simply bundles values into a 2-tuple.
public class Pair<F,S> {
    //Define the first and second values of the specified type.
    //Important: 'Pair' allows direct access to these values.
    public F fst;
    public S snd;

    //Set the two values to those given in the constructor.
    public Pair(F f, S s) {
        fst = f;
        snd = s;
    }

    //Convert class values to a formatted pair; return as a string.
    public String show() {
        return "("+fst+","+snd+")";
    }

}
