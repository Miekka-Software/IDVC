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
    private final Pair<Integer,Integer> winSize = new Pair<>(600,600);
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
        carTex.add(new ImageView(new Image("file:./res/img/SimcarBlue.png", carSz.fst, carSz.snd, true, false)));
        carTex.add(new ImageView(new Image("file:./res/img/SimcarGreen.png", carSz.fst, carSz.snd, true, false)));

        CollisionMap layer1 = new CollisionMap();
        ArrayList<Vehicle> car = new ArrayList<>();
        car.add(new Vehicle(50,0,300,0,carSz,layer1));
        car.add(new Vehicle(50,600,300,180,carSz,layer1));
        car.add(new Vehicle(50,300,0,90,carSz,layer1));

        new AnimationTimer()
        {
            long lastTick = System.currentTimeMillis();
            public void handle(long currentNanoTime)
            {
                for(Vehicle C : car) {
                    C.updateState();
                }

                if(System.currentTimeMillis()-lastTick > 250) {
                    layer1.update();
                    System.out.print("Colliding figures: ");
                    for (SimObject O : layer1.checkCollisons()) {
                        System.out.print(O + ", ");
                    }
                    System.out.print("\n");
                    if(!layer1.checkCollisons().isEmpty()) {
                        this.stop();
                    }
                    lastTick = System.currentTimeMillis();
                }

                root.getChildren().clear();
                for(int i = 0; i < car.size(); i++) {
                    carTex.get(i).setX((car.get(i).getX() - carSz.fst / 2));
                    carTex.get(i).setY((car.get(i).getY() - carSz.snd / 2));
                    carTex.get(i).setRotate(car.get(i).getH());
                    root.getChildren().add(carTex.get(i));
                }
            }
        }.start();
        theStage.show();
    }
}