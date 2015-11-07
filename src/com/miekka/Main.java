package com.miekka;

public class Main {

    public static void main(String[] args) {
        Vehicle car = new Vehicle(0,1,-10,0);
        for(int i = 0; i <= 10; i++) {
            System.out.println("After " + i + " seconds, the vehicle is at: " + car.S.P.show());
            car.updateState();
        }
    }
}
