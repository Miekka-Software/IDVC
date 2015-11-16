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
    private final Pair<Integer,Integer> winSize = new Pair<>(500,500);
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

        ArrayList<Vehicle> car = new ArrayList<>();
        //car.add(new Vehicle(200,-675,-675,45,carSz));
        //car.add(new Vehicle(55,500,0,135,carSz));
        car.add(new Vehicle(100,500,250,180,carSz));
        car.add(new Vehicle(100,500,300,180,carSz));

        car.get(0).turn(-30, 1);

        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {
                for(Vehicle C : car) {
                    C.updateState();
                }
                for(Pair<Double,Double> P : car.get(1).corners()) {
                    if(car.get(0).containsPoint(P.fst,P.snd)){
                        this.stop();
                    }
                }
                for(Pair<Double,Double> P : car.get(0).corners()) {
                    if(car.get(1).containsPoint(P.fst,P.snd)){
                        this.stop();
                    }
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