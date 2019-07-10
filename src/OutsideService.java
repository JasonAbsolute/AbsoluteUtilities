import java.util.ArrayList;

/**
 * this will help with adding the out side services like:
 * heat treat, grinding, wielding, laser, plating, misc/oddballs
 */
public class OutsideService {
    private String service;
    private ArrayList amount;
    private String quantity0;
    private ArrayList<Double> cost = new ArrayList();

    //TODO
    //when i get back add this where
    //send a string to constructer
    //seperate eveything by ","
    // add to list
    // make them print out to the chart

    public OutsideService(String service, ArrayList amount){
        this.service = service;
        this.amount = amount;
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
    public ArrayList getCost(){
        return cost;
    }


    public void outsideServiceQoutes(String textBoxOfCosts){
        String[] result = textBoxOfCosts.split(",");
        for (int i = 0; i < result.length; i++) {
            System.out.println(result[i]);
            // Fetch the item, trim it and put it back in
            result[i] = result[i].trim();
            cost.add((Double.parseDouble(result[i])));

        }

    }
}
