package restaurant;

public class myLinkedList {
    private MenuNode head;

    public void addFirst (MenuNode node){
        if (head == null){
            head = node;
        }
        else{
            node.setNextMenuNode(head);
            head = node;
        }
    }
    public MenuNode getFirst(){
        return head;
    }
}
