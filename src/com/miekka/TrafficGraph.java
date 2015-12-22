package com.miekka;

//Imports:
import com.miekka.helper.Node;
import com.miekka.helper.Pair;

import java.util.ArrayList;
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
        Node newNode = new Node(templateObj.getID(), position, templateObj.getV(), templateObj.getH());
        NodeList.add(newNode);
    }

    public void addNode(Node newNode) {
        NodeList.add(newNode);
    }

    public Node lookupNode(String id) {
        //Java 8 stream and lambda witchcraft. Filters the NodeList and returns all Nodes with the correct id,
        //then collects those values into a list, and finally, pulls the first element. If the list is empty
        //(meaning to matches were found), then catch the exception and return null.
        try {
            return (NodeList.stream().filter(o -> o.getID().equals(id)).collect(Collectors.toList()).get(0));
        }
        catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Node getOrigin() {
        Pair<Double,Double> origin = new Pair<>(0.0,0.0);
        return (NodeList.stream().filter(o -> o.getPosition().equals(origin)).collect(Collectors.toList()).get(0));
    }

    public ArrayList<Node> getNodeList() {
        ArrayList<Node> newNL = new ArrayList<>();
        for(Node n : NodeList) {
            newNL.add(new Node(n));
        }
        return newNL;
    }

    public void updateNodeStatus(String id, double[] newVelocity, double[] newHeading) {
        Node targetNode = lookupNode(id);
        if (targetNode != null) {
            targetNode.updateNode(targetNode.getPosition(), newVelocity, newHeading);
        }
        else {
            System.out.println("Lookup for node '" + id + "' failed.");
        }
    }

    public void updateNodePosition(String id, Pair<Double,Double> newPosition) {
        Node targetNode = lookupNode(id);
        if (targetNode != null) {
            targetNode.updateNode(newPosition, targetNode.getVelocity(), targetNode.getHeading());
        }
        else {
            System.out.println("Lookup for node '" + id + "' failed.");
        }
    }

    public void removeNode(String id) {
        Node targetNode = lookupNode(id);
        if (targetNode != null) {
            NodeList.remove(targetNode);
        }
        else {
            System.out.println("Lookup for node '" + id + "' failed.");
        }
    }

    public void shiftOrigin(String newOriginID) {
        Node newOrigin = this.lookupNode(newOriginID);
        if (newOrigin != null) {
            Pair<Double, Double> graphShift = newOrigin.getPosition();
            for (Node node : NodeList) {
                Pair<Double, Double> newPos =
                        new Pair<>(node.getPosition().fst - graphShift.fst, node.getPosition().snd - graphShift.snd);
                node.updateNode(newPos, node.getVelocity(), node.getHeading());
            }
        }
        else {
            System.out.println("Lookup for node '" + newOriginID + "' failed.");
        }
    }

    /*
    * This function needs to be finished, it merges newer changes from a remote TG into the local one
    * Write so it can handle failed lookups (new nodes in the remote). Also, modify all functions to handle failed
    * lookups with the null return of lookupNode. Null means no results found.
    */
    public void syncWith(TrafficGraph remoteTG) {
        for(Node remoteNode : remoteTG.getNodeList()) {
            Node localNode = this.lookupNode(remoteNode.getID());
            if(localNode != null) {
                if(remoteNode.getLastUpdate() > localNode.getLastUpdate()) {
                    localNode.updateNode(remoteNode.getPosition(), remoteNode.getVelocity(), remoteNode.getHeading());
                }
            }
            else {
                this.addNode(remoteNode);
            }
        }
    }
}
