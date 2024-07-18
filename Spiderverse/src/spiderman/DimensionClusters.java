package spiderman;
import java.util.*;

public class DimensionClusters {
    private List<List<Dimension>> dimTable;
    private HashMap<Integer, List<Dimension>> adjacency_list;
    private ArrayList<Person> people;

    private int dimensionsAdded;
    private int size;
    private double threshold;
    

    public DimensionClusters(int initial_size, double threshold) {
        this.size = initial_size;
        this.threshold = threshold;
        dimTable = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            dimTable.add(new ArrayList<>());
        }
        dimensionsAdded = 0;

        people = new ArrayList<>();
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public void addPerson(Person p) {
        people.add(p);
    }

    public Person getPerson(String name) {
        for (Person p : people){
            if( p.getName().equals(name))
                return p;
        }
        return null;
    }

    public void clusters() {
        for (int i = 0; i < dimTable.size(); i++) {
            List<Dimension> second = dimTable.get((i - 1 + size) % size);
            List<Dimension> last = dimTable.get((i - 2 + size) % size);

            List<Dimension> cluster = dimTable.get(i);

            if (!second.isEmpty()) {
                cluster.add(second.get(0));
            }
            if (!last.isEmpty()) {
                cluster.add(last.get(0));
            }
        }
    }
    public HashMap<Integer, List<Dimension>> createAdjacencyList() {

        for (int i = 0; i < dimTable.size(); i++) {
            List<Dimension> second = dimTable.get((i - 1 + size) % size);
            List<Dimension> last = dimTable.get((i - 2 + size) % size);

            List<Dimension> cluster = dimTable.get(i);

            if (!second.isEmpty()) {
                cluster.add(second.get(0));
            }
            if (!last.isEmpty()) {
                cluster.add(last.get(0));
            }
        }
        
        adjacency_list = new HashMap<>();

        for (int i = 0; i < dimTable.size(); i++) {
            for (int j = 0; j< dimTable.get(i).size(); j++){
                Dimension d = dimTable.get(i).get(j);
             
                if (!adjacency_list.containsKey(d.getNumber())){
                    adjacency_list.put(d.getNumber(), new ArrayList<>());
                    adjacency_list.get(d.getNumber()).add(d);
                }
                if (j != 0) {
                    adjacency_list.get(d.getNumber()).add(dimTable.get(i).get(0));
                    adjacency_list.get(dimTable.get(i).get(0).getNumber()).add(d);
                }
               

            }
        }

        return adjacency_list;
    }

    public List<List<Dimension>> getdimTable() {
        return dimTable;
    }

    private void rehash() {
        int double_size = size * 2;

        List<List<Dimension>> newdimTable = new ArrayList<>(double_size);

        for (int i = 0; i < double_size; i++) {
            newdimTable.add(new ArrayList<>());
        }
        for (List<Dimension> cluster : dimTable) {
            for (Dimension dimension : cluster) {
                int newIndex = dimension.getNumber() % double_size;
                newdimTable.get(newIndex).add(0, dimension);
            }
        }
        dimTable = newdimTable;
        size = double_size;
    }

    public void insert(Dimension dimension) {
        int index = dimension.getNumber() % size;
        dimTable.get(index).add(0, dimension);
        dimensionsAdded++;
        if ((double) dimensionsAdded / size >= threshold) {
            rehash();
        }
    }
   
 

   

}
