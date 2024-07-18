package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Menu {


        

    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        String inputFile = args[0];
        String outputFile = args[1];
	
        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();

	// 3. Call the menu() method to read the menu
        rh.menu(inputFile);

	// 4. Set output file
	// Option to remove this line if you want to print directly to the screen
        StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();

        rh.createStockHashTable("stock.in");
        rh.printRestaurant();
        
        rh.updatePriceAndProfit();
        rh.printRestaurant();

       /* */ StdIn.setFile("order1.in");
        
        int numOrders = StdIn.readInt();
        StdIn.readChar(); 

        for (int i = 0; i < numOrders; i++){ 
            int quantity = StdIn.readInt();
            StdIn.readChar();  
            String dishName = StdIn.readLine();
            
            
        rh.order(dishName, quantity);
        }

        rh.printRestaurant();

        /*StdIn.setFile("donate1.in");

       int amount = Integer.parseInt(StdIn.readLine()); 

       for (int i = 0; i < amount; i++){ 
           int quantity = StdIn.readInt();
           StdIn.readChar();  
           String item = StdIn.readLine();
           rh.donation(item, quantity);
       }

       rh.printRestaurant(); */

       StdIn.setFile("restock1.in");

       int count = Integer.parseInt(StdIn.readLine()); 

       for (int i = 0; i < count; i++){ 
           int quantity = StdIn.readInt();
           StdIn.readChar();  
           String x = StdIn.readLine();
           rh.restock(x, quantity);
       }

       rh.printRestaurant(); 

       

       
}
}
