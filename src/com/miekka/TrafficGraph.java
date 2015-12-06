package com.miekka;

//Imports:
import com.miekka.helper.Node;
import com.miekka.helper.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TrafficGraph {
    public ArrayList<Node> NodeList;

    public TrafficGraph(ArrayList<Node> initialNodes) {
        this.NodeList = initialNodes;
    }

    public void addNewNode(String id, Pair<Double,Double> position, double[] velocity, double[] heading) {
        Node newNode = new Node(id, position, velocity, heading);
        NodeList.add(newNode);
    }

    public Node lookupNode(String id) {
        //Java 8 stream and lambda witchcraft. Filters the NodeList and returns all Nodes with the correct id,
        //then collects those values into a list, and finally, pulls the first element.
        return (NodeList.stream().filter(o -> o.getID().equals(id)).collect(Collectors.toList()).get(0));
    }

    public void updateNodeWithID(String id, Pair<Double,Double> newPosition, double[] newVelocity, double[] newHeading) {
        Node targetNode = lookupNode(id);
        targetNode.updateNode(newPosition,newVelocity,newHeading);
    }

    public void removeNode(String id) {
        Node targetNode = lookupNode(id);
        NodeList.remove(targetNode);
    }
}
