package com.miekka;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application
{
    private final String Title = "IDVC";
    private final Pair<Integer,Integer> winSize = new Pair<>(1000,500);
    private final int carL = 44;
    private final int carW = 26;

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage theStage)
    {
        theStage.setTitle(Title);
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setWidth(winSize.fst);
        theStage.setHeight(winSize.snd);

        ArrayList<ImageView> carTex = new ArrayList<>();
        carTex.add(new ImageView(new Image("file:///home/brady/Documents/Programming/Java/IDVC/res/img/SimcarRed.png", carL, carW, true, false)));
        carTex.add(new ImageView(new Image("file:///home/brady/Documents/Programming/Java/IDVC/res/img/SimcarBlue.png", carL, carW, true, false)));
        carTex.add(new ImageView(new Image("file:///home/brady/Documents/Programming/Java/IDVC/res/img/SimcarGreen.png", carL, carW, true, false)));

        ArrayList<Vehicle> car = new ArrayList<>();
        car.add(new Vehicle(0,1.00,0,220,0));
        car.add(new Vehicle(0,1.01,0,250,0));
        car.add(new Vehicle(0,0.99,0,280,0));

        new AnimationTimer()
        {
            long lastTick = System.nanoTime();
            public void handle(long currentNanoTime)
            {
                double timeSinceLastTick = (currentNanoTime - lastTick) / 10000000.0; //Divide into 100ths of seconds
                if(timeSinceLastTick>1) {
                    for(Vehicle C : car) {
                        C.updateState();
                    }
                    lastTick = System.nanoTime();
                }

                root.getChildren().clear();
                for(int i = 0; i < car.size(); i++) {
                    carTex.get(i).setX((car.get(i).getX() - carL / 2) % winSize.fst);
                    carTex.get(i).setY((car.get(i).getY() - carW / 2) % winSize.snd);
                    carTex.get(i).setRotate(car.get(i).getH());
                    root.getChildren().add(carTex.get(i));
                }
            }
        }.start();

        theStage.show();
    }
}