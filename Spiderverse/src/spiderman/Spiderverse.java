package spiderman;
import java.util.*;

public class Spiderverse {

    private Set<Dimension> vertices = new HashSet<>();
    
    public void addVertex(Dimension v) {
        vertices.add(v);
    }

    public Spiderverse calc_dijkstra(Spiderverse graph, Dimension root) {

        root.setDistance(0);
        HashSet<Dimension> not_visited = new HashSet<>();

        HashSet<Dimension> visited = new HashSet<>();
        not_visited.add(root);

        while (not_visited.size() != 0) {
            Dimension curr = getfarthest(not_visited);
            not_visited.remove(curr);
            for (Map.Entry<Dimension, Integer> pair : curr.getAdjacentNodes().entrySet()) {
                Dimension adjacent = pair.getKey();
                Integer w = pair.getValue();

                if (!visited.contains(adjacent)) {
                    getMinDistance(w, adjacent, curr);
                    not_visited.add(adjacent);
                }
            }
            visited.add(curr);
        }
        return graph;
    }

    public Dimension findVertex( int r_dimension){
        for (Dimension dimension : vertices) 
            if (dimension.getNumber() == r_dimension)
                return dimension;

        return null;

    }

    public Set<Dimension> getVertices() {
        return vertices;
    }

    private static Dimension getfarthest(HashSet<Dimension> not_visited) {
        Dimension farthest = null;
        int lowest = Integer.MAX_VALUE;
        for (Dimension n : not_visited) {
            int dist = n.getDistance();
            if (lowest > dist) {
                lowest = dist;
                farthest = n;
            }
        }
        return farthest;
    }

    private static void getMinDistance(Integer w, Dimension ev, Dimension root) {
        Integer rootDistance = root.getDistance();
        if (rootDistance + w < ev.getDistance()) {
            ev.setDistance(rootDistance + w);
            LinkedList<Dimension> shortestPath = new LinkedList<>(root.getShortestPath());
            shortestPath.add(root);
            ev.setShortestPath(shortestPath);
        }
    }

   

}