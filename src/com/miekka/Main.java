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
 * 1. Outline and implement communication between Vehicles:
 *      * Use queues for pending IO
 *      * If a Vehicle_1's sensor can "see" another Vehicle (Vehicle_2), it can be pinged. If Vehicle_2 pings back,
 *        Vehicle_2 is added is added to Vehicle_1's 'idvcContacts' and visa-versa.
 *      * If Vehicle_2 leaves Vehicle_1's sensor range, Vehicle_2 is deleted from Vehicle_1's contacts.
 *      * When further communication is required, the Vehicles can communicate directly via their contacts.
 *      * If a sensed object fails to respond to the ping, it is added to a separate 'trackedObjects' list. Land
 *        obstacles, traffic control objects (road cones, etc.), and non-IDVC Vehicles all fall into this category.
 *      * When information is pertinent to all Vehicles in traffic, a 'rippleMessage' is sent out. Ripple messages
 *        have priority over all other messages, and every vehicle that receives this message sends a copy of it to
 *        all of its IDVC contacts. The information ripples through the network, spreading at an exponential rate
 *        outwards from the source (hence the name).
 *      * Every vehicle stores its received messages in memory for future reference.
 *      * If a vehicle receives a duplicate ripple message, it ignores it and does not forward it a second time. This
 *        way, ripple messages don't flood the network, and once everyone has received the message, it dies out.
 */

//Main application class, opens a JavaFX window and manages animation.
public class Main extends Application
{
    //Define constants for window size and car size.
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
        theStage.setTitle("IDVC (Intelligent Distributed Vehicle Control)");
        Group root = new Group();
        Scene theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setWidth(winSize.fst);
        theStage.setHeight(winSize.snd);

        //Create an 'ArrayList' containing all of the car sprites. Sprites are stored in JavaFX 'ImageView'.
        //The images are resized to match 'carSz'.
        ArrayList<Image> carTex = new ArrayList<>();
        carTex.add(new Image("img/SimcarGreen.png", carSz.fst, carSz.snd, false, false));
        carTex.add(new Image("img/SimcarBlue.png", carSz.fst, carSz.snd, false, false));
        carTex.add(new Image("img/SimcarRed.png", carSz.fst, carSz.snd, false, false));

        //Create a collision layer to keep track of colliding objects.
        SimLayer layer1 = new SimLayer();

        //Create an 'ArrayList' of Vehicles, then add all of the Vehicle objects.
        ArrayList<Vehicle> car = new ArrayList<>();
        car.add(new Vehicle(40,0,0,45,carSz,layer1));
        car.add(new Vehicle(40,300,0,90,carSz,layer1));
        car.add(new Vehicle(40,600,0,135,carSz,layer1));
        car.add(new Vehicle(60,600,300,180,carSz,layer1));
        car.add(new Vehicle(40,600,600,-135,carSz,layer1));
        car.add(new Vehicle(40,300,600,-90,carSz,layer1));
        car.add(new Vehicle(40,0,600,-45,carSz,layer1));
        car.add(new Vehicle(60,0,300,0,carSz,layer1));

        for(Vehicle C : car){
            C.stop(2.5);
        }

        //Create a new 'AnimationTimer' (because 'AnimationTimer' is an abstract class, we extend it inline).
        //In 'AnimationTimer' we implement 'handle' which runs 60 times a second, and animates the Stage.
        new AnimationTimer()
        {
            //Animation loop
            public void handle(long currentNanoTime)
            {
                //Update the state of all of the Vehicles in the simulation.
                for(Vehicle C : car) {
                    C.nextTick();
                }

                //Update the collision layer.
                layer1.update();

                //Freeze all colliding objects.
                for (SimObject O : layer1.checkCollisons()) {
                    O.freeze();
                }

                //Clear the scene (wipe the screen).
                root.getChildren().clear();

                //Iterate through all of the Vehicles in the simulation drawing them with the respective sprite.
                //This loop re-adds all of the children to the cleared scene.
                for(Vehicle C : car) {
                    ImageView cT = new ImageView(carTex.get(C.Tex));
                    cT.setX((C.getX() - carSz.fst / 2));
                    cT.setY((C.getY() - carSz.snd / 2));
                    cT.setRotate(C.getH());
                    root.getChildren().add(cT);
                }
            }
        }.start();

        //Show the stage (print to the screen).
        theStage.show();
    }
}