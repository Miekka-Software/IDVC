package com.miekka.helper;

//Imports:
import com.miekka.SimLayer;
import com.miekka.SimObject;
import com.miekka.Vehicle;

public class Msg {
    //  'Ripple' messages contain info that is pertinent to the whole group, and they
    //are spread to all IDVC contacts.
    //  'Request' messages contain only 'Tags' with no values. The recipient then sends
    //back an 'Update' message with the requested data.
    //  'Update' message contain info that is pertinent to the requester, and is sent
    //to select contacts (usually just the requester).
    public enum Type {
        Ripple, Request, Update
    }
    private Type type;
    //Tags describe how the associated data should be read.
    //  SensorData - Pair<Vehicle,Double>(sensedVehicle,distToVehicle).
    //  Velocity - Double[] = [currentV, targetV, deltaV].
    //  Heading - Double[] = [currentH, targetH, deltaH].
    public enum Tag {
        SensorData, Velocity, Heading
    }
    private Tag tag;
    private long timestamp; //Time the message was sent, in milliseconds.
    private String value; //Message content, stored as a string.
    private Vehicle sender; //Contains the object of the sender Vehicle.
    private SimLayer layer; //Keeps track of the SimLayer in which the message is sent.

    //Set all initial values, and take down the time.
    public Msg(Type type, Tag tag, String value, Vehicle sender, SimLayer layer) {
        this.type = type;
        this.tag = tag;
        this.value = value;
        this.sender = sender;
        this.layer = layer;
        timestamp = System.currentTimeMillis();
    }

    public Type getType() {
        return type;
    }

    public Tag getTag() {
        return tag;
    }

    public Vehicle getSender() {
        return sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getValue(){
        switch(tag){
            case SensorData:
                String[] vals = value.split(" ");
                SimObject obj = layer.lookup(vals[0]);
                double dist = Double.parseDouble(vals[1]);
                return (new Pair<>(obj,dist));
            case Velocity: case Heading:
                String[] sData = value.replace("[","").replace("]","").split(", ");
                Double[] rData = new Double[3];
                for(int i = 0; i < sData.length; i++) {
                    rData[i] = Double.parseDouble(sData[i]);
                }
                return rData;
            default:
                return null;
        }
    }

}
