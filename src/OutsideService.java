import java.util.ArrayList;

/**
 * this will help with adding the out side services like:
 * heat treat, grinding, wielding, laser, plating, misc/oddballs
 */
public class OutsideService {
    private String service;
    private ArrayList amount;
    private String quantity0;
    private ArrayList<Double> cost = new ArrayList<Double>();
    private int internalCounter;

    public OutsideService(String service, ArrayList amount){
        this.service = service;
        this.amount = amount;
        this.internalCounter = 0;

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

    public double getNumberToReturn(){
        System.out.println("NUMBER BEING RETURNED");
        double numToReturn;
        if(internalCounter == amount.size()-1){
            numToReturn = cost.get(internalCounter);
            internalCounter = 0;
        }else{
            numToReturn = cost.get(internalCounter);
            internalCounter++;
        }
        return numToReturn;
    }


    public ArrayList<Double> getCost(){
        return cost;
    }

    public String getCost(int i){
        return Double.toString(cost.get(i));
    }

    public void outsideServiceQoutes(String textBoxOfCosts){
        String[] result = textBoxOfCosts.split(",");
        for (int i = 0; i < result.length; i++) {
            // Fetch the item, trim it and put it back in
            result[i] = result[i].trim();
            cost.add((Double.parseDouble(result[i])));
        }
    }
}
