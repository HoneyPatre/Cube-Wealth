import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import co.honey.DeliveryTime.*;

public class Main {
	public static long TimeManager(String date){
        Date dateTime = Date.from(Instant.parse( date ));
        return dateTime.getTime();
	}
	public static LinkedList[] SortData(LinkedList<String> Product, LinkedList<Long> PickupTime, LinkedList<Long> DeliveryTime){
		int n = PickupTime.size();
		LinkedList[] result = new LinkedList[3];
        for (int i = 0; i < n-1; i++)
            for (int j = 0; j < n-i-1; j++)
                if (PickupTime.get(j) > PickupTime.get(j+1)){
                	
                    long PickupSwap = PickupTime.get(j);
                    PickupTime.set(j, PickupTime.get(j+1));
                    PickupTime.set(j+1, PickupSwap);
                    
                    long DeliverSwap = DeliveryTime.get(j);
                    DeliveryTime.set(j, DeliveryTime.get(j+1));
                    DeliveryTime.set(j+1, DeliverSwap);
                    
                    String ProductSwap = Product.get(j);
                    Product.set(j, Product.get(j+1));
                    Product.set(j+1, ProductSwap);

                }
        result[0] = Product;
        result[1] = PickupTime;
        result[2] = DeliveryTime;
        return result;        
	}
	public static void main(String args[]) throws IOException{
		ArrayList<String[]> TableData = new ArrayList<String[]>();
		HashSet<String> DeliveryPersons = new HashSet<String>();
		LinkedList[] result = new LinkedList[3];
		
		String line = "";  
		String splitBy = ",";  
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\HP\\Desktop\\Cube Wealth\\Cube-Wealth.csv"));
		
		while((line = br.readLine()) != null){
			String[] entry = line.split(splitBy);
			entry[0] = entry[0].substring(1,entry[0].length());
			entry[4] = entry[4].substring(0,entry[4].length()-1);
			TableData.add(entry);
			DeliveryPersons.add(entry[1]);
		}
		DeliveryPersons.remove("Delivery Person");	
		
		for(String person: DeliveryPersons){
			LinkedList<String> Product = new LinkedList<String>();
			LinkedList<Long> PickupTime = new LinkedList<Long>();
			LinkedList<Long> DeliveryTime = new LinkedList<Long>();
			
			for(int j=1; j<TableData.size(); j++){
				if(person.equals(TableData.get(j)[1])){					
					Product.add(TableData.get(j)[2]);
					PickupTime.add(TimeManager(TableData.get(j)[3]));
					DeliveryTime.add(TimeManager(TableData.get(j)[4]));
				}
			}
			
			result = SortData(Product, PickupTime, DeliveryTime);	// Sorting of data based on Pickup Time
			DeliveryDriver driver = new DeliveryDriver(person, result[0], result[1], result[2]);	
			driver.DisplayData();
			System.out.println(" Driver's Travel Time: "+driver.FinalTravelTimeCalculator()+" minutes\n");			
		}

		br.close();
	}	
}
