import java.util.ArrayList;

/**
 * this will help with adding the out side services like:
 * heat treat, grinding, wielding, ;aser, plating, misc/oddballs
 */
public class OutsideService {
    private String service;
    private ArrayList amount;
    private String quantity0;
    private ArrayList cost;

    //TODO
    //when i get back add this where
    //send a string to constructer
    //seperate eveything by ","
    // add to list
    // make them print out to the chart

    public OutsideService(String service, ArrayList amount,String textBoxOfCosts){
        this.service = service;
        this.amount = amount;
        this.quantity0 = textBoxOfCosts;
    }
    public String getService(){
        return service;
    }
    public ArrayList getAmount(){
        return amount;
    }
    public String getQuantity0(){
        return quantity0;
    }
}
