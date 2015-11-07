package com.miekka;

public class Main {

    public static void main(String[] args) {
        Vehicle car0 = new Vehicle(0,1.5);
        for(int i = 0; i <= 10; i++) {
            System.out.println("After " + i + " seconds, the vehicle is traveling: " + car0.V + " meters per second.");
            car0.updateVelocity();
        }
    }
}
