package com.miekka;

//Imports:
import com.miekka.helper.Pair;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.ArrayList;

/*TODO:
 * 1. Document! Comment every block of code and explain its function.
 * 2. AFTER documentation, work on adding sensors to the 'Vehicle' class.
 * 3. Revise turning so the heading and wheel angle are unique values.
 */

//Main application class, opens a JavaFX window and manages animation.
public class Main extends Application
{
    //Define constants for window title, window size and car size.
    private final String Title = "IDVC";
    private final Pair<Integer,Integer> winSize = new Pair<>(600,600);
    private final Pair<Double,Double> carSz = new Pair<>(44.0,26.0);

    //Launch app via the 'start' function.
    public static void main(String[] args)
    {
        launch(args);
    }

    //The 'start' function, builds and animates the Stage.
    //JavaFX organizes the GUI into a hierarchy and the Stage is the "root" of the hierarchy
    //and contains all of the other GUI components.
    public void start(Stage theStage)
    {
        //Set the stage with a Scene, and set window attributes.
        theStage.setTitle(Title);
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setWidth(winSize.fst);
        theStage.setHeight(winSize.snd);

        //Create an 'ArrayList' containing all of the car sprites. Sprites are stored in JavaFX 'ImageView'.
        //The images are resized to match 'carSz'.
        ArrayList<ImageView> carTex = new ArrayList<>();
        carTex.add(new ImageView(new Image("file:./res/img/SimcarRed.png", carSz.fst, carSz.snd, true, false)));
        carTex.add(new ImageView(new Image("file:./res/img/SimcarBlue.png", carSz.fst, carSz.snd, true, false)));
        carTex.add(new ImageView(new Image("file:./res/img/SimcarGreen.png", carSz.fst, carSz.snd, true, false)));

        //Create a collision layer to keep track of colliding objects.
        CollisionMap layer1 = new CollisionMap();

        //Create an 'ArrayList' of Vehicles, then add all of the Vehicle objects.
        ArrayList<Vehicle> car = new ArrayList<>();
        car.add(new Vehicle(50,0,300,0,carSz,layer1));
        car.add(new Vehicle(50,600,300,180,carSz,layer1));
        car.add(new Vehicle(50,300,0,90,carSz,layer1));

        //Create a new 'AnimationTimer' (because 'AnimationTimer' is an abstract class, we extend it inline).
        //In 'AnimationTimer' we implement 'handle' which runs 60 times a second, and animates the Stage.
        new AnimationTimer()
        {
            //Get the current time (starting time) in milliseconds.
            long lastTick = System.currentTimeMillis();

            //Animation loop
            public void handle(long currentNanoTime)
            {
                //Update the state of all of the Vehicles in the simulation.
                for(Vehicle C : car) {
                    C.updateState();
                }

                //If the last tick was more than a 4th of a second ago, it is time for the next tick.
                if(System.currentTimeMillis()-lastTick > 250) {
                    //Update the collision layer
                    layer1.update();

                    //Print all colliding objects
                    System.out.print("Colliding figures: ");
                    for (SimObject O : layer1.checkCollisons()) {
                        System.out.print(O + ", ");
                    }
                    System.out.print("\n");

                    //If any objects are colliding, stop the simulation.
                    if(!layer1.checkCollisons().isEmpty()) {
                        this.stop();
                    }
                    //Update the time of the last tick to now.
                    lastTick = System.currentTimeMillis();
                }

                //Clear the scene (wipe the screen)
                root.getChildren().clear();

                //Iterate through all of the Vehicles in the simulation drawing them with the respective sprite.
                //This loop re-adds all of the children to the cleared scene.
                for(int i = 0; i < car.size(); i++) {
                    carTex.get(i % carTex.size()).setX((car.get(i).getX() - carSz.fst / 2));
                    carTex.get(i % carTex.size()).setY((car.get(i).getY() - carSz.snd / 2));
                    carTex.get(i % carTex.size()).setRotate(car.get(i).getH());
                    root.getChildren().add(carTex.get(i));
                }
            }
        }.start();

        //Show the stage (print to the screen).
        theStage.show();
    }
}