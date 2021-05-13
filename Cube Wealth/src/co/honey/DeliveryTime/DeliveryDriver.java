package co.honey.DeliveryTime;

import java.util.Date;
import java.util.LinkedList;

public class DeliveryDriver{
	String DriverName;
	LinkedList<String> Product = new LinkedList<String>(); 
	LinkedList<Long> PickupTime = new LinkedList<Long>();
	LinkedList<Long> DeliveryTime = new LinkedList<Long>();
	
	static String[] FoodItems = {"Pizza"};
	static String[] FragileItems = {"GoPro Camera", "Telescope"};
	static String[] ElectronicItems = {"GoPro Camera", "Table Lamp", "Telescope", "Drone"};
	static String[] NonElectronicItems = {"Tennis Shoes", "Football", "Frisbee"};
	static String[] HeavyItems = {"Telescope"};
	
	public DeliveryDriver(String DriverName, LinkedList<String> Product, LinkedList<Long> PickupTime, LinkedList<Long> DeliveryTime){
		this.DriverName = DriverName;
		this.Product = Product;
		this.PickupTime = PickupTime;
		this.DeliveryTime = DeliveryTime;
	}
	
	public boolean isFoodItem(String Product){
		for(String item: FoodItems){
			if(Product.equals(item)){
				return true;
			}
		}
		return false;
	}
	public boolean isFragileItem(String Product){
		for(String item: FragileItems){
			if(Product.equals(item)){
				return true;
			}
		}
		return false;
	}
	public boolean isElectronicItem(String Product){
		for(String item: ElectronicItems){
			if(Product.equals(item)){
				return true;
			}
		}
		return false;
	}
	public boolean isNonElectronicItem(String Product){
		for(String item: NonElectronicItems){
			if(Product.equals(item)){
				return true;
			}
		}
		return false;
	}
	public boolean isHeavyItem(String Product){
		for(String item: HeavyItems){
			if(Product.equals(item)){
				return true;
			}
		}
		return false;
	}
	
	public void DisplayData(){
		
		System.out.println("\n Driver Name : "+ DriverName);
		System.out.println(" -----------------------------------------------------------------------------");
		System.out.println(" | PickUp Time                  | Delivery Time                | Product");
        System.out.println(" -----------------------------------------------------------------------------");
		for(int i =0; i<Product.size(); i++){
			Date PickUpDate = new Date(PickupTime.get(i));
			Date DeliveryUpDate = new Date(DeliveryTime.get(i));			
			System.out.println(" | "+PickUpDate+" | "+DeliveryUpDate +" | "+ Product.get(i));
		}
		System.out.println(" -----------------------------------------------------------------------------");
	}
	public int DeductionCalculator(String Product){
		int timeToBeDeducted = 0;
		if(isFoodItem(Product)){					
			timeToBeDeducted = 180000; //3 minutes
		}
		else {
			if(isFragileItem(Product) || isElectronicItem(Product)){						
				timeToBeDeducted = 420000; //7 minutes
			}
			else if(isHeavyItem(Product)){						
				timeToBeDeducted = 300000;  // 5 minutes
			}
			else if(isNonElectronicItem(Product)){						
				timeToBeDeducted = 60000;   // 1 minute
			}
			else{
				timeToBeDeducted = 120000;  // 2 minutes
			}
		}
		return timeToBeDeducted;
	}
	public int OverlapCalculator(Long PickupTimePrev, Long DeliveryTimePrev, Long PickupTimeNext, Long DeliveryTimeNext){
		if(PickupTimeNext > PickupTimePrev
		   && PickupTimeNext < DeliveryTimePrev
		   && DeliveryTimeNext > DeliveryTimePrev){
			return 0;	//Partial Overlap Condition
		}
		else if(PickupTimeNext > PickupTimePrev
				&& PickupTimeNext < DeliveryTimePrev
				&& DeliveryTimeNext < DeliveryTimePrev){		
			return 1;	//Complete Overlap Condition
		}
		else{
			return -1; 	//No Overlap Condition
		}	
	}
	
	public long FinalTravelTimeCalculator(){
		
		long travelTime = DeliveryTime.get(0) - PickupTime.get(0); 
		long timeToBeDeducted = 0;
		
		for(int i=1; i<PickupTime.size(); i++){			
			long overlapStatus = OverlapCalculator(PickupTime.get(i-1), DeliveryTime.get(i-1), PickupTime.get(i), DeliveryTime.get(i));
			if(overlapStatus == 0){
				travelTime = travelTime + DeliveryTime.get(i) - DeliveryTime.get(i-1);										
				
				// for first item(after delivery scenario)
				timeToBeDeducted = DeductionCalculator(Product.get(i-1));
				
				// for second item(before pickup scenario)
				timeToBeDeducted = timeToBeDeducted + DeductionCalculator(Product.get(i));
				
				travelTime = travelTime - timeToBeDeducted;	
			}
			else if(overlapStatus == 1){				
			
				timeToBeDeducted = DeductionCalculator(Product.get(i));

				travelTime = travelTime - timeToBeDeducted * 2;		
				
				/* 	when next value is getting overlapped by first 
					we set it to first values so that we can compare it in next iteration */
				PickupTime.set(i, PickupTime.get(i-1));
				DeliveryTime.set(i, DeliveryTime.get(i-1));
				Product.set(i, Product.get(i-1));
			}
			else{					
				travelTime = travelTime + DeliveryTime.get(i) - PickupTime.get(i);	
			}
		}
		travelTime = travelTime / 60000;	// Conversion in minutes
		
		return travelTime;

	}
}