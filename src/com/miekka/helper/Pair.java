package com.miekka.helper;

public class Pair<F,S> {
    public F fst;
    public S snd;

    public Pair(F f, S s) {
        fst = f;
        snd = s;
    }

    public String show() {
        return "("+fst+","+snd+")";
    }

}
