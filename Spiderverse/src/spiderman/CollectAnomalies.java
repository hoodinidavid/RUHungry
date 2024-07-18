package spiderman;

import java.util.*;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
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
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separentated) followed by 
 *    the Dimension number for each Dimension in the route (space separentated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
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

        
        
        StdIn.setFile(args[2]); // hub.in
        int hub = StdIn.readInt();


        StdIn.setFile(args[1]); // spiderverse.in
        int people = StdIn.readInt();

        ArrayList<Person> spiders = new ArrayList<>();
        ArrayList<Person> anomalies = new ArrayList<>();

        for (int i = 0; i < people; i++) {
            int dim = StdIn.readInt();
            String name = StdIn.readString();
            int signature = StdIn.readInt();

            Person p = new Person(dim, name, signature);
            if (dim != hub && dim != signature) 
                anomalies.add(p);
            if (dim == signature)  
                spiders.add(p);
                universe.addPerson(p);

        }

        HashMap<Integer, List<Dimension>> adjacency_list = universe.createAdjacencyList();
        
      
        HashMap<Integer,Integer> parent = new HashMap<>();
        for (Integer i : adjacency_list.keySet()){
            parent.put(i, -1);                    
        }


        HashMap<Integer,Integer> distance = new HashMap<>();
        for (Integer i : adjacency_list.keySet()){
            distance.put(i, Integer.MAX_VALUE);       
        }
        Queue<Integer> queue = new LinkedList<>();
        distance.put(hub, 0);
        queue.add(hub);

        while (!queue.isEmpty()) {
            int remove = queue.poll();

            for (int i = 0; i < adjacency_list.get(remove).size(); i++) {
                int neighbor = adjacency_list.get(remove).get(i).getNumber();
                if (distance.get(neighbor) == Integer.MAX_VALUE) {
                    parent.put(neighbor, remove);
                    distance.put(neighbor, 1 + distance.get(remove));
                    queue.add(neighbor);
                }
            }
        }


        StdOut.setFile(args[3]);
        for (Person p: anomalies) {
            String spider = ""; 
            for (Person s: spiders) {
                if (s.getDimension() == p.getDimension()) {
                    spider = s.getName();
                    break;
                }
            }

            // the shortest route
            List<Integer> route = new ArrayList<>();
            int curr = p.getDimension();
            route.add(p.getDimension());
            while (parent.get(curr) != -1) {
                route.add(parent.get(curr));
                curr = parent.get(curr);
            }

            if (spider.isEmpty()){
                StdOut.print(p.getName() + " " );
                for (int i = route.size() - 1; i > 0; i--)
                    StdOut.print(route.get(i) + " ");
            }
            else {
                StdOut.print(p.getName() + " " + spider + " ");
            }
            for (int i = 0; i < route.size(); i++)
                StdOut.print(route.get(i) + " ");
            StdOut.println();

        }
        
    }
}
