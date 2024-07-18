package spiderman;
import java.util.*;

public class Dimension {
    
    private int number;
    private int canonEvents;
    private int weight;

    
    private List<Dimension> shortestPath = new LinkedList<>();
    
    private Integer distance = Integer.MAX_VALUE;
    
    HashMap<Dimension, Integer> adjacentNodes = new HashMap<>();

    public Dimension(int number, int canonEvents, int weight) {
        this.number = number;
        this.canonEvents = canonEvents;
        this.weight = weight;
    }

    public int getNumber() {
        return number;
    }

    public int getCanonEvents(){
        return canonEvents;
    }

    public void setCanonEvents(int c){
        canonEvents = c;
    }

   

    public int getWeight(){
        return weight;
    }

    public String toString() {
        return number + " " + canonEvents + " " + weight;
    }



    public void addDestination(Dimension destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public void setDistance(int dis){
        this.distance = dis;
    }

    public int getDistance(){
        return distance;
    }

    public void setShortestPath(LinkedList<Dimension> path){
        shortestPath = path;
    }

    public List<Dimension> getShortestPath(){
        return shortestPath;
    }

    public String getShortestPathString(){
        String n = "";
        for (Dimension d : shortestPath){
            n += d.getNumber() + " ";
        }

        return n;
    }

    public int getTimetoTravelPath(){
        int n = weight;
        for (int i = 0 ; i < shortestPath.size()-1; i++){
            n += shortestPath.get(i).getWeight() + shortestPath.get(i+1).getWeight();
        }
        n += shortestPath.get(shortestPath.size()-1).getWeight();
        return n ;
    }

    public HashMap<Dimension, Integer> getAdjacentNodes(){
        return adjacentNodes;
    }

}