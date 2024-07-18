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
 *      i.    The dimension they are spot_currly at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The spot_curring dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visit_nodes (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        
        StdIn.setFile(args[0]); 

        int n = StdIn.readInt();
        // the first line with three numbers:
        // i.    a (int): number of dimensions in the graph
        // ii.   b (int): the initial size of the cluster table prior to rehashing
        // iii.  c (double): the capacity(threshold) used to rehash the cluster table 
        DimensionClusters universe = new DimensionClusters(StdIn.readInt(), StdIn.readDouble());

        for (int i = 0 ; i < n; i++){
            universe.insert(new Dimension(StdIn.readInt(), StdIn.readInt(), StdIn.readInt()));
        }
        StdIn.setFile(args[2]); 
        int spot_curr = StdIn.readInt();
        int spot_dest = StdIn.readInt();


        HashMap<Integer, List<Dimension>> adjacency_list = universe.createAdjacencyList();

        HashMap<Integer, Boolean> dfs = new HashMap<>();
        ArrayList<Integer> stack = new ArrayList<>();

        Iterator<Integer> iterator = adjacency_list.keySet().iterator();
        while (iterator.hasNext()) {
            Integer d = iterator.next();
            dfs.put(d, false);
        }
        

        List<Integer> visit_nodes = new ArrayList<>();

        stack.add(spot_curr);
        while (!stack.isEmpty()) {
            spot_curr = stack.remove(0);
            if (!dfs.get(spot_curr)){
                visit_nodes.add(spot_curr);
                dfs.put(spot_curr, true);

                List<Dimension> neighbors = adjacency_list.get(spot_curr);
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    Dimension node = neighbors.get(i);
                    if (!dfs.get(node.getNumber()))
                        stack.add(0,node.getNumber()); // Add at the beginning (top of the stack)
                }

            }
        }

        StdOut.setFile(args[3]); 

        for (Integer v : visit_nodes){
            if (v == spot_dest) 
                break;
            StdOut.print(v + " ");
        }
        StdOut.print(spot_dest);

    }

}
