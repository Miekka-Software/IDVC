package com.miekka;

import com.miekka.helper.Pair;
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
    private final Pair<Double,Double> carSz = new Pair<>(44.0,26.0);

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
        carTex.add(new ImageView(new Image("file:./res/img/SimcarRed.png", carSz.fst, carSz.snd, true, false)));
        //carTex.add(new ImageView(new Image("file:./res/img/SimcarBlue.png", carL, carW, true, false)));
        //carTex.add(new ImageView(new Image("file:./res/img/SimcarGreen.png", carL, carW, true, false)));

        ArrayList<Vehicle> car = new ArrayList<>();
        car.add(new Vehicle(200,500,50,0,carSz));
        //car.add(new Vehicle(200,0,250,0));
        //car.add(new Vehicle(200,0,270,0));

        car.get(0).turn(1080, 60);
        //car.get(1).stop(50);
        //car.get(2).stop(25);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                for(Vehicle C : car) {
                    C.updateState();
                    System.out.println(C.containsPoint(500,50));
                }

                root.getChildren().clear();
                for(int i = 0; i < car.size(); i++) {
                    carTex.get(i).setX((car.get(i).getX() - carSz.fst / 2) % winSize.fst);
                    carTex.get(i).setY((car.get(i).getY() - carSz.snd / 2) % winSize.snd);
                    carTex.get(i).setRotate(car.get(i).getH());
                    root.getChildren().add(carTex.get(i));
                }
            }
        }.start();

        theStage.show();
    }
}