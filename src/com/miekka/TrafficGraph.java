package com.miekka;

//Imports:
import com.miekka.helper.Node;
import com.miekka.helper.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrafficGraph {
    private ArrayList<Node> NodeList;

    public TrafficGraph(ArrayList<Node> initialNodes) {
        this.NodeList = initialNodes;
    }

    public TrafficGraph() {
        this.NodeList = new ArrayList<>();
    }

    public void addNode(String id, Pair<Double,Double> position, double[] velocity, double[] heading) {
        Node newNode = new Node(id, position, velocity, heading);
        NodeList.add(newNode);
    }

    public void addNode(SimObject templateObj, Pair<Double,Double> position) {
        Node newNode = new Node(templateObj.getID(), position, templateObj.V, templateObj.H);
        NodeList.add(newNode);
    }

    public void addNode(Node newNode) {
        NodeList.add(newNode);
    }

    public List<Node> lookupNode(String id) {
        //Java 8 stream and lambda witchcraft. Filters the NodeList and returns all Nodes with the correct id,
        //then collects those values into a list, and finally, pulls the first element.
        return (NodeList.stream().filter(o -> o.getID().equals(id)).collect(Collectors.toList()));
    }

    public Node getOrigin() {
        Pair<Double,Double> origin = new Pair<>(0.0,0.0);
        return (NodeList.stream().filter(o -> o.getPosition().equals(origin)).collect(Collectors.toList()).get(0));
    }

    public ArrayList<Node> getNodeList() {
        return NodeList;
    }

    public void updateNodeWithID(String id, Pair<Double,Double> newPosition, double[] newVelocity, double[] newHeading) {
        Node targetNode = lookupNode(id).get(0);
        targetNode.updateNode(newPosition,newVelocity,newHeading);
    }

    public void removeNode(String id) {
        Node targetNode = lookupNode(id).get(0);
        NodeList.remove(targetNode);
    }

    public void shiftOrigin(String newOriginID) {
        Pair<Double,Double> graphShift = this.lookupNode(newOriginID).get(0).getPosition();
        for(Node node : NodeList) {
            Pair<Double,Double> newPos =
                new Pair<>(node.getPosition().fst - graphShift.fst, node.getPosition().snd - graphShift.snd);
            node.updateNode(newPos, node.getVelocity(), node.getHeading());
        }
    }

    /*
    * This function needs to be finished, it merges newer changes from a remote TG into the local one
    * Write so it can handle failed lookups (new nodes in the remote). Also, modify all functions to handle failed
    * lookups with the new List<Node> return type of lookupNode. Empty list means no results found.

    public void syncWith(TrafficGraph remoteTG) {
        for(Node remoteNode : remoteTG.getNodeList()) {
            if(remoteNode.)
        }
    }*/
}
