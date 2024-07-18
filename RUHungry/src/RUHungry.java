package restaurant;

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * RUHungry is a fictitious restaurant. 
 * You will be running RUHungry for a day by seating guests, 
 * taking orders, donation requests and restocking the pantry as necessary.
 * 
 * Compiling and executing:
 * 1. use the run or debug function to run the driver and test your methods 
 * 
 * @author Mary Buist
 * @author Kushi Sharma
*/

public class RUHungry {
    
    /*
     * Instance variables
     */

    // Menu: two parallel arrays. The index in one corresponds to the same index in the other.
    private   String[] categoryVar; // array where containing the name of menu categories (e.g. Appetizer, Dessert).
    private MenuNode[] menuVar;     // array of lists of MenuNodes where each index is a category.
    
    // Stock: hashtable using chaining to resolve collisions.
    private StockNode[] stockVar;  // array of linked lists of StockNodes (use hashfunction to organize Nodes: id % stockVarSize)
    private int stockVarSize;

    // Transactions: orders, donations, restock transactions are recorded 
    private TransactionNode transactionVar; // refers to the first front node in linked list

    // Queue keeps track of parties that left the restaurant
    private Queue<Party> leftQueueVar;  

    // Tables Information - parallel arrays
    // If tableSeats[i] has 3 seats then parties with at most 3 people can sit at tables[i]
    private Party[] tables;      // Parties currently occupying the tables
    private   int[] tableSeats;  // The number of seats at each table


    /*
     * Default constructor
     */

     public void printStockRoomAndMenu(){
        System.out.println("Stock:");

        for (int i = 0; i < stockVarSize; i++){
            System.out.println("Index " + i);

            StockNode current = stockVar[i] ;

            while (current != null){
                Ingredient ingredient = current.getIngredient();

                System.out.println(ingredient.getName() + "ID: " + ingredient.getID() + " Price: " + ingredient.getCost() + " Stock Level: " + ingredient.getStockLevel());
                current = current.getNextStockNode();
            }
        }
    
        printStockRoomAndMenu(); 
     }

    public RUHungry () {
        categoryVar    = null;
        menuVar        = null;
        stockVar       = null;
        stockVarSize   = 0;
        transactionVar = null;
        leftQueueVar   = null;
        tableSeats     = null;
        tables         = null;
    }

    /*
     * Getter and Setter methods
     */
    public MenuNode[] getMenu() { return menuVar; }
    public String[] getCategoryArray() { return categoryVar;}
    public StockNode[] getStockVar() { return stockVar; } 
    public TransactionNode getFrontTransactionNode() { return transactionVar; } 
    public TransactionNode resetFrontNode() {return transactionVar = null;} // method to reset the transactions for a new day
    public Queue<Party> getLeftQueueVar() { return leftQueueVar; } 
    public Party[] getTables() { return tables; }
    public int[] getTableSeats() { return tableSeats; }

    /*
     * Menu methods
     */

    /**
     * 
     * This method populates the two parallel arrays menuVar and categoryVar.
     * 
     * Each index of menuVar corresponds to the same index in categoryVar (a menu category like Appetizers).
     * If index 0 at categoryVar is Appetizers then menuVar at index 0 contains MenuNodes of appetizer dishes.
     * 
     * 1. read the input file:
     *      a) the first number corresponds to the number of categories (aka length of menuVar and categoryVar)
     *      b) the next line states the name of the category (populate CategoryVar as you read each category name)
     *      c) the next number represents how many dishes are in that category - this will be the size of the linked list in menuVar for this category
     *      d) the next line states the name of the dish
     *      e) the first number in the next line represents how many ingredient IDs there are
     *      f) the next few numbers (all in the 100s) are each the ingredient ID
     * 
     * 2. As you read through the input file:
     *      a) populate the categoryVar array
     *      b) populate menuVar depending on which index (aka which category) you are in
     *          i) make a dish object (with filled parameters -- don't worry about "price" and "profit" in the dish object for right now)
     *          ii) create menuNode and insert at the front of menuVar (NOTE! there will be multiple menuNodes in one index)
     * 
     * @param inputFile - use menu.in file which contains all the dishes
     */

    public void menu(String inputFile) {

        StdIn.setFile(inputFile); // opens the inputFile to be read

        int numCategories = StdIn.readInt(); 
        StdIn.readLine();
        categoryVar = new String[numCategories];
        menuVar = new MenuNode [numCategories];


        for (int i = 0; i <numCategories; i++){

            categoryVar[i] = StdIn.readLine();
            int dishesInCategory = StdIn.readInt(); 
            StdIn.readLine(); 

            MenuNode headNode = null; 

            for (int j = 0; j < dishesInCategory; j++){

                String dishName  = StdIn.readLine();

                int numIngredients = StdIn.readInt(); 
    
                int [] categoryIngredientIDs = new int [numIngredients]; 

                for ( int k = 0; k < numIngredients; k++){
                    categoryIngredientIDs[k] = StdIn.readInt();
                }

                StdIn.readLine(); 

                Dish newDish = new Dish (categoryVar[i], dishName, categoryIngredientIDs);
                MenuNode newNode = new MenuNode (newDish, headNode); 

                headNode = newNode;

            }

            menuVar[i] = headNode;

        }

        printRestaurant();

    }

    /** 
     * Find and return the MenuNode that contains the dish with dishName in the menuVar.
     * 
     *      ** GIVEN METHOD **
     *      ** DO NOT EDIT **
     * 
     * @param dishName - the name of the dish
     * @return the dish object corresponding to searched dish, null if dishName is not found.
     */

    public MenuNode findDish ( String dishName ) {

        MenuNode menuNode = null;

        // Search all categories since we don't know which category dishName is at
        for ( int category = 0; category < menuVar.length; category++ ) {

            MenuNode ptr = menuVar[category]; // set ptr at the front (first menuNode)
            
            while ( ptr != null ) { // while loop that searches the LL of the category to find the itemOrdered
                if ( ptr.getDish().getName().equalsIgnoreCase(dishName) ) {
                    return ptr;
                } else{
                    ptr = ptr.getNextMenuNode();
                }
            }
        }
        return menuNode;
    }

    /**
     * Find integer that corresponds to the index in menuVar and categoryVar arrays that has that category
     *              
     *      ** GIVEN METHOD **
     *      ** DO NOT EDIT **
     *
     * @param category - the category name
     * @return index of category in categoryVar
     */

    public int findCategoryIndex ( String category ) {
        int index = 0;
        for ( int i = 0; i < categoryVar.length; i++ ){
            if ( category.equalsIgnoreCase(categoryVar[i]) ) {
                index = i;
                break;
            }
        }
        return index;
    }

    /*
     * Stockroom methods
     */

    /**
     * PICK UP LINE OF THE METHOD:
     * *can I insert myself into your life? cuz you always help me sort 
     * out my problems and bring stability to my mine*
     * 
     * ***********
     * This method adds a StockNode into the stockVar hashtable.
     * 
     * 1. get the id of the given newNode and use a hash function to get the index at which the
     *    newNode is being inserted.
     * 
     * HASH FUNCTION: id % stockVarSize
     * 
     * 2. insert at the front of the linked list at the specific index
     * 
     * @param newNode - StockNode that needs to be inserted into StockVar
     */

    public void addStockNode ( StockNode newNode ) {

        int ingredientID = newNode.getIngredient().getID();

        int index = ingredientID % stockVarSize; 

        //newNode.setNextStockNode(stockVar[index]);
        //stockVar[index] = newNode; 

        if (stockVar[index] == null) {
            stockVar[index] = newNode;
        } 
        else {
            newNode.setNextStockNode(stockVar[index]);
            stockVar[index] = newNode;
        }
    }

    /**
     * This method finds an ingredient from StockVar (given the ingredientID)
     * 
     * 1. find the node based upon the ingredient ID (you can go to the specific index using the hash function!)
     *      (a) this is an efficient search as it looks only at the linked list which the key hash to
     * 2. find and return the node
     *  
     * @param ingredientID - the ID of the ingredient
     * @return the StockNode corresponding to the ingredientID, null otherwise
     */
   
    public StockNode findStockNode (int ingredientID) {

	int index = ingredientID % stockVarSize; 

    StockNode cur = stockVar[index]; 

    while (cur != null){
        if (cur.getIngredient().getID() == ingredientID){
            return cur;
        }
        cur = cur.getNextStockNode();
    }
        return null; 
    }

    /**
     * This method is to find an ingredient from StockVar (given the ingredient name).
     * 
     *      ** GIVEN METHOD **
     *      ** DO NOT EDIT **
     * 
     * @param ingredientName - the name of the ingredient
     * @return the specific ingredient StockNode, null otherwise
     */

    public StockNode findStockNode (String ingredientName) {
        
        StockNode stockNode = null;
        
        for ( int index = 0; index < stockVar.length; index ++ ){
            
            StockNode ptr = stockVar[index];
            
            while ( ptr != null ){
                if ( ptr.getIngredient().getName().equalsIgnoreCase(ingredientName) ){
                    return ptr;
                } else {  
                    ptr = ptr.getNextStockNode();
                }
            }
        }
        return stockNode;
    }

    /**
     * This method updates the stock amount of an ingredient.
     * 
     * 1. you will be given the ingredientName **OR** the ingredientID:
     *      a) the ingredientName is NOT null: find the ingredient and add the given stock amount to the
     *         current stock amount
     *      b) the ingredientID is NOT -1: find the ingredient and add the given stock amount to the
     *         current stock amount
     * 
     * (FOR FUTURE USE) SOMETIMES THE STOCK AMOUNT TO ADD MAY BE NEGATIVE (TO REMOVE STOCK)
     * 
     * @param ingredientName - the name of the ingredient
     * @param ingredientID - the id of the ingredient
     * @param stockAmountToAdd - the amount to add to the current stock amount
     */
    
    public void updateStock (String ingredientName, int ingredientID, int stockAmountToAdd) {

        StockNode stockNode = null; 

        if (ingredientName == null && ingredientID != -1){
            stockNode = findStockNode(ingredientID);
        }
        else if (ingredientName != null && ingredientID == -1 ){
            stockNode = findStockNode(ingredientName);
        }

        if (stockNode != null){
            int updatedStock = stockNode.getIngredient().getStockLevel();
            stockNode.getIngredient().setStockLevel(updatedStock + stockAmountToAdd);
}

    }

    /**
     * PICK UP LINE OF THE METHOD:
     * *are you a single ‘for’ loop? cuz i only have i’s for you*
     * 
     * ***********
     * This method goes over menuVar to update the price and profit of each dish,
     * using the stockVar hashtable to lookup for ingredient's costs.
     * 
     * 1. For each dish in menuVar, add up the cost for each ingredient (found in stockVar), 
     *    and multiply the total by 1.2 to get the final price.
     *      a) update the price of each dish
     *  HINT! --> you can use the methods you've previously made!
     * 
     * 2. Calculate the profit of each dish by getting the totalPrice of ingredients and subtracting from 
     *    the price of the dish itself.
     * 
     * @return void
     */

    public void updatePriceAndProfit() {

        for (MenuNode menuNode : menuVar){
            while ( menuNode != null){
                Dish dish = menuNode.getDish();
                double dishPrice = 0.0;

                int [] ingredientIDs = dish.getStockID();

                for ( int ingredientID : ingredientIDs){
                    StockNode stockNode = findStockNode(ingredientID);
                    if (stockNode != null){
                        dishPrice += stockNode.getIngredient().getCost();
                    }
                }

                double newPrice = dishPrice * 1.2; 
                double dishProfit = newPrice - dishPrice; 

                dish.setPriceOfDish(newPrice);
                dish.setProfit(dishProfit);

                menuNode = menuNode.getNextMenuNode();
            }

            
        } 

    }

    /**
     * PICK UP LINE OF THE METHOD:
     * *are you a decimal? cuz the thought of you 
     * always floats in my head and the two of use would make double*
     * 
     * ************
     * This method initializes and populates stockVar which is a hashtable where each index contains a 
     * linked list with StockNodes.
     * 
     * 1. set and read the inputFile (stock.in):
     *      a) first integer (on line 1) is the size of StockVar *** update stockVarSize AND create the stockVar array ***
     *      b) first integer of next line represents the ingredientID
     *          i) example: 101 on line 2
     *      c) use StdIn.readChar() to get rid of the space between the id and the name
     *      d) the string that follows is the ingredient name (NOTE! --> there are spaces between certain strings)
     *          i) example: Lettuce
     *      e) the double on the next line corresponds to the ingredient's cost
     *          i) example: 3.12 on line 3
     *      f) the next integer is the stock amount for that ingredient
     *          i) example: 30 on line 3
     * 
     * 2. create a Ingredient object followed by a StockNode then add to stockVar
     *      HINT! --> you may use previous methods written to help you!
     * 
     * @param inputFile - the input file with the ingredients and all their information (stock.in)
     */

    public void createStockHashTable (String inputFile){
        
        StdIn.setFile(inputFile); 

        stockVarSize = StdIn.readInt(); 
        stockVar = new StockNode[stockVarSize]; 

        while (!StdIn.isEmpty()){
            int ingredientID = StdIn.readInt(); 
            StdIn.readChar(); 
            String ingredientName = StdIn.readLine();
            double ingredientCost = StdIn.readDouble(); 
            int stockAmount = StdIn.readInt(); 


            Ingredient ingredient = new Ingredient(ingredientID, ingredientName, stockAmount, ingredientCost); 

            StockNode newNode = new StockNode (ingredient, null); 

            addStockNode(newNode);

        }

        printRestaurant();

    }

    /*
     * Transaction methods
     */

    /**
     * This method adds a TransactionNode to the END of the transactions linked list.
     * The front of the list is transactionVar.
     *
     * 1. create a new TransactionNode with the TransactionData paramenter.
     * 2. add the TransactionNode at the end of the linked list transactionVar.
     * 
     * @param data - TransactionData node to be added to transactionVar
     */

    public void addTransactionNode ( TransactionData data ) { // method adds new transactionNode to the end of LL

        TransactionNode newTransNode = new TransactionNode(data,null); 

        if (transactionVar == null){
            transactionVar = newTransNode; 
            return;
        }
        else {
            TransactionNode cur = transactionVar; 
            while (cur.getNext() != null){
                cur = cur.getNext();
            }
            cur.setNext(newTransNode); 
        }
	
    }



    /**
     * PICK UP LINE OF THE METHOD:
     * *are you the break command? cuz everything else stops when I see you*
     * 
     * *************
     * This method checks if there's enough in stock to prepare a dish.
     * 
     * 1. use findDish() method to find the menuNode node for dishName
     * 
     * 2. retrieve the Dish, then traverse ingredient array within the Dish
     * 
     * 3. return boolean based on whether you can sell the dish or not
     * HINT! --> once you determine you can't sell the dish, break and return
     * 
     * @param dishName - String of dish that's being requested
     * @param numberOfDishes - int of how many of that dish is being ordered
     * @return boolean
     */

    public boolean checkDishAvailability (String dishName, int numberOfDishes){

        MenuNode menuNode = findDish(dishName);

        if (menuNode == null){return false;}

        Dish thisDish = menuNode.getDish(); 
        int [] ingredientsRequired = thisDish.getStockID(); 
        
        for ( int id : ingredientsRequired){
            StockNode used = findStockNode(id); 

            if (used == null || used.getIngredient().getStockLevel() < numberOfDishes){return false;}
        }

        return true; 
    }

    /**
     * PICK UP LINE OF THE METHOD:
     * *if you were a while loop and I were a boolean, we could run 
     * together forever because I’ll always stay true to you*
     * 
     * ***************
     * This method simulates a customer ordering a dish. Use the checkDishAvailability() method to check whether the dish can be ordered.
     * If the dish cannot be prepared
     *      - create a TransactionData object of type "order" where the item is the dishName, the amount is the quantity being ordered, and profit is 0 (zero).
     *      - then add the transaction as an UNsuccessful transaction and,
     *      - simulate the customer trying to order other dishes in the same category linked list:
     *          - if the dish that comes right after the dishName can be prepared, great. If not, try the next one and so on.
     *          - you might have to traverse through the entire category searching for a dish that can be prepared. If you reach the end of the list, start from the beginning until you have visited EVERY dish in the category.
     *          - It is possible that no dish in the entire category can be prepared.
     *          - Note: the next dish the customer chooses is always the one that comes right after the one that could not be prepared. 
     * 
     * @param dishName - String of dish that's been ordered
     * @param quantity - int of how many of that dish has been ordered
     */

    public void order (String dishName, int quantity){

        MenuNode menuNode = findDish(dishName);
        MenuNode first = menuVar[findCategoryIndex(menuNode.getDish().getCategory())];

        if (checkDishAvailability(dishName, quantity)){
            //MenuNode menuNode = findDish(dishName);
            Dish menuDish = menuNode.getDish();
            double profit = menuDish.getProfit() * quantity; 

            TransactionData validTrans = new TransactionData("order", dishName, quantity, profit, true); 
            addTransactionNode(validTrans);

            int [] ing = menuDish.getStockID();
            for (int ingredientID : ing){
                updateStock(null, ingredientID, -quantity);
            }
            return; 
        }
        else {
            TransactionData invalidTrans = new TransactionData("order", dishName, quantity, 0, false);
            addTransactionNode(invalidTrans); 

            MenuNode cur = findDish(dishName).getNextMenuNode();

            if (cur == null){
                cur = first;
            }

            while( cur != null && !cur.getDish().getName().equals(dishName)){
                if (checkDishAvailability(cur.getDish().getName(), quantity)){
                    double diffProfit = cur.getDish().getProfit() * quantity; 
                    TransactionData diffTrans = new TransactionData("order", cur.getDish().getName(), quantity, diffProfit,true);
                    addTransactionNode(diffTrans);
                    

                    int [] ing = cur.getDish().getStockID();
                    for (int ingredientID : ing){
                        updateStock(null, ingredientID, -quantity);
                    }
                    break;
                }
                else{//u dont need this else block 
                    TransactionData t = new TransactionData("order", cur.getDish().getName(),quantity,0.0,false);
                        addTransactionNode(t);
                }

                cur = cur.getNextMenuNode();

                if (cur == null){
                    cur = first;
                }
            }
                cur = cur.getNextMenuNode();
            }
            printRestaurant();
        }


    /*public void orderTest (String inputFile){

        StdIn.setFile(inputFile); 
        
        int numOrders = StdIn.readInt();
        StdIn.readChar(); 

        for (int i = 0; i < numOrders; i++){ 
            int quantity = StdIn.readInt();
            StdIn.readChar();  
            String dishName = StdIn.readLine(); 

            order(dishName, quantity); 
        }
    }*/

    /**
     * This method returns the total profit for the day
     *
     * The profit is computed by traversing the transaction linked list (transactionVar) 
     * adding up all the profits for the day
     * 
     * @return profit - double value of the total profit for the day
     */

    public double profit () {

	double totalProfit = 0.0; 
    TransactionNode curNode = transactionVar; 

    while (curNode != null){
        totalProfit += curNode.getData().getProfit(); 
        curNode = curNode.getNext(); 
    }
    return totalProfit; 
    }

    /**
     * This method simulates donation requests, successful or not.
     * 
     * 1. check whether the profit is > 50 and whether there's enough ingredients in stock.
     * 
     * 2. add transaction to transactionVar
     * 
     * @param ingredientName - String of ingredient that's been requested
     * @param quantity - int of how many of that ingredient has been ordered
     * @return void
     */

    public void donation (String ingredientName, int quantity){

	double totalProfit = profit();
    

    if (totalProfit > 50){

        double newNodeValue = findStockNode(ingredientName).getIngredient().getStockLevel();

        if ( newNodeValue < quantity){

            TransactionData newTransaction = new TransactionData("donation", ingredientName, quantity, 0,false);

            addTransactionNode(newTransaction);


        }
        else{
            TransactionData newTransaction = new TransactionData("donation", ingredientName, quantity, 0,true);

            addTransactionNode(newTransaction);

            updateStock(ingredientName, -1, -quantity);             
        }  
    }

    else {
        TransactionData notTransaction = new TransactionData("donation", ingredientName, quantity, 0,false);

        addTransactionNode(notTransaction);
        
    }  

    }
    

    /**
     * This method simulates restock orders
     * 
     * 1. check whether the profit is sufficient to pay for the total cost of ingredient
     *      a) (how much each ingredient costs) * (quantity)
     *      b) if there is enough profit, adjust stock and profit accordingly
     * 
     * 2. add transaction to transactionVar
     * 
     * @param ingredientName - ingredient that's been requested
     * @param quantity - how many of that ingredient needs to be ordered
     */

    public void restock (String ingredientName, int quantity){

        double totalUnit = findStockNode(ingredientName).getIngredient().getCost(); 
        double costOfRestocking = totalUnit * quantity; 
        double profit = profit(); 

        if (profit > costOfRestocking){
            updateStock(ingredientName, -1, quantity);
            TransactionData can = new TransactionData("restock", ingredientName, quantity, -costOfRestocking, true);
            addTransactionNode(can);}
        else {
            TransactionData cant = new TransactionData("restock", ingredientName, quantity, 0, false);
            addTransactionNode(cant);}


    }


   /*
    * Seat guests/customers methods
    */

    /**
     * Method to populate tables (which is a 1D integer array) based upon input file
     * 
     * The input file is formatted as follows:
     * - an integer t contains the number of tables
     * - t lines containing number of rows * seats per row for each table
     * 
     * @param inputFile - tables1.in (contains all the tables in the RUHungry restaurant)
     * @return void (aka nothing)
     */

    public void createTables ( String inputFile ) { 

        StdIn.setFile(inputFile);
	
        int numberOfTables = StdIn.readInt();
        tableSeats = new int[numberOfTables];
        tables     = new Party[numberOfTables];

        for ( int t = 0; t < numberOfTables; t++ ) {
            tableSeats[t] = StdIn.readInt() * StdIn.readInt();
        }
    }

    /**
     * PICK UP LINE OF THE METHOD:
     * *are you a linked list? cuz nothing could stock up to you and 
     * you’re pretty queue(te)*
     * 
     * ***************
     * This method simulates seating guests at tables. You are guaranteed to be able to sit everyone from the waitingQueue eventually.
     * 
     * 1. initialize a tables array for party that are currently sitting
     * 
     * 2. initialize leftQueueVar a Party queue that represents the people that have left the restaurant
     * 
     * 3. while there are parties waiting to be sat:
     *      - Starting from index 0 (zero), seat the next party in the first available table that fits their party.
     *      - If there is no available table for the next party, kick a party out from the tables array:
     *          1. starting at index 0 (zero), find the first table big enough to hold the next party in line.
     *          2. remove the current party, add them to the leftQueueVar.
     *          3. seat the next party in line.
     *
     * Parallel arrays: tableSeats[i] refers to tables[i]. If tableSeats[i] is 3 then a party with 3 or less people can sit at tables[i].
     * tableSeats contains the number of seats per table.
     * tables contains the Party object currently at the table.
     * 
     * Note: After everyone has been seated (waitingQueue is empty), remove all the parties from tables and add then to the leftQueueVar.
     * 
     * @param waitingQueue - queue containing parties waiting to be seated (each element in queue is a Party <-- you are given this class!)
     */

    public void seatAllGuests ( Queue<Party> waitingQueue ) {

	// WRITE YOUR CODE HERE

    }

    /**
     * Prints all states of the restaurant.
     * 
     * Edit this method if you wish.
     */
    public void printRestaurant() {
        // 1. Print out menu
        StdOut.println("Menu:");
        if (categoryVar != null) {
            for (int i=0; i < categoryVar.length; i++) {
                StdOut.print(categoryVar[i] + ":");
                StdOut.println();

                MenuNode ptr = menuVar[i];
                while (ptr != null) {
                    StdOut.print(ptr.getDish().getName() + "  Price: $" +
                    ((Math.round(ptr.getDish().getPriceOfDish() * 100.0)) / 100.0) + " Profit: $" + ((Math.round(ptr.getDish().getProfit() * 100.0)) / 100.0));
                    StdOut.println();

                    ptr = ptr.getNextMenuNode();
                }
                StdOut.println();
            }
        }
        else {
            StdOut.println("Empty - categoryVar is null.");
        }
        // 2. Print out stock
        StdOut.println("Stock:");
        if (stockVar != null) {
            for (int i=0; i < 10; i++) {
                StdOut.println("Index " + i);
                StockNode ptr = stockVar[i];
                while (ptr != null) {
                    StdOut.print(ptr.getIngredient().getName() + "  ID: " + ptr.getIngredient().getID() + " Price: " +
                    ((Math.round(ptr.getIngredient().getCost() *100.0)) / 100.0) + " Stock Level: " + ptr.getIngredient().getStockLevel());
                    StdOut.println();
    
                    ptr = ptr.getNextStockNode();
                }
    
                StdOut.println();
            }
        }
        else {
            StdOut.println("Empty - stockVar is null.");
        }
        // 3. Print out transactions
        StdOut.println("Transactions:");
        if (transactionVar != null) {
            TransactionNode ptr = transactionVar;
            int successes = 0;
            int failures = 0;
            while (ptr != null) {
                String type = ptr.getData().getType();
                String item = ptr.getData().getItem();
                int amount = ptr.getData().getAmount();
                double profit = ptr.getData().getProfit();
                boolean success = ptr.getData().getSuccess();
                if (success == true){
                    successes += 1;
                }
                else if (success == false){
                    failures += 1;
                }

                StdOut.println("Type: " + type + ", Name: " + item + ", Amount: " + amount + ", Profit: $" + ((Math.round(profit * 100.0)) / 100.0) + ", Was it a Success? " + success);
                
                ptr = ptr.getNext();
            }
            StdOut.println("Total number of successful transactions: " + successes);
            StdOut.println("Total number of unsuccessful transactions: " + failures);
            StdOut.println("Total profit remaining: $" + ((Math.round(profit() * 100.0)) / 100.0));
        }
        else {
            StdOut.println("Empty - transactionVar is null.");
        }
        // 4. Print out tables
        StdOut.println("Tables and Parties:");
        restaurant.Queue<Party> leftQueue = leftQueueVar;
        if (leftQueueVar != null) {
            StdOut.println(("Parties in order of leaving:"));
            int counter = 0;
            while (!leftQueue.isEmpty()) {
                Party removed = leftQueue.dequeue();
                counter += 1;
                StdOut.println(counter + ": " + removed.getName());
            }
        }
        else {
            StdOut.println("Empty -- leftQueueVar is empty");
        }
    }
}
