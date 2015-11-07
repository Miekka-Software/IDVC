package com.miekka;

public class Pair<F,S> {
    public F fst;
    public S snd;

    public Pair(F f, S s) {
        this.fst = f;
        this.snd = s;
    }

    public String show() {
        return "("+fst+","+snd+")";
    }

}
