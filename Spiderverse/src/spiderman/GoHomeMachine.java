package spiderman;

import java.util.*;
/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the spiderverse
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {
    
    public static void main(String[] args) {

        if ( args.length < 5 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
        StdIn.setFile(args[0]); // dimension.in

        int num_dimensions = StdIn.readInt();
        DimensionClusters universe = new DimensionClusters(StdIn.readInt(), StdIn.readDouble());

        int index = 0;
        while (index < num_dimensions){
            universe.insert(new Dimension(StdIn.readInt(), StdIn.readInt(), StdIn.readInt()));
            index++;
        }

        StdIn.setFile(args[2]); //hub.in
        int hub = StdIn.readInt();


        StdIn.setFile(args[1]); // spiderverse.in
        int people = StdIn.readInt();

        ArrayList<Person> anomalies = new ArrayList<>();

        for (int i = 0; i < people; i++) {
            int dim = StdIn.readInt();
            String name = StdIn.readString();
            int signature = StdIn.readInt();

            Person p = new Person(dim, name, signature);
            if (dim != signature && dim != hub) anomalies.add(p);
            universe.addPerson(p);

        }

        HashMap<Integer, List<Dimension>> adjacency_list = universe.createAdjacencyList();

        Spiderverse spiderverse = new Spiderverse();

        Dimension source = new Dimension(0, 0, 0);
        //creating the graph with weighted edges using adjacency list
        for (Integer i : adjacency_list.keySet()){
            List<Dimension> curr = adjacency_list.get(i);
            Dimension zero = curr.get(0);
            if (zero.getNumber() == hub) source = zero;
            for (int j = 1; j < curr.size(); j++) {
                zero.addDestination(curr.get(j), zero.getWeight() + curr.get(j).getWeight());
            } 
            spiderverse.addVertex(zero);
            
        }

        spiderverse = spiderverse.calc_dijkstra(spiderverse, source);

        StdIn.setFile(args[3]);  //anomalies.in
        int num_anomalies = StdIn.readInt();

        StdOut.setFile(args[4]);

        for (int i = 0; i < num_anomalies; i++) {
            String name = StdIn.readString();
            int time = StdIn.readInt();

            Person anomaly = universe.getPerson(name);
            Dimension dimension = spiderverse.findVertex(anomaly.getSignature());
        
            if (dimension.getTimetoTravelPath() < time) {
                StdOut.println(dimension.getCanonEvents() + " " + name + " SUCCESS "+ dimension.getShortestPathString() + anomaly.getSignature());

            }
            else {
                dimension.setCanonEvents(dimension.getCanonEvents()-1);
                StdOut.println(dimension.getCanonEvents() + " " + name + " FAILED "+ dimension.getShortestPathString() + anomaly.getSignature());

            }

        }


        
    }
}
