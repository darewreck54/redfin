import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

/**
 * Console Application used to find open food trucks in SF
 */
public class FoodTruckFinder {
	public static void main(String[] args) {
		SodaApiClient client = new SodaApiClient();
		FoodTruckFinder app = new FoodTruckFinder();
		
		// Determine the current time
		Calendar c = Calendar.getInstance();
		int dayOrder = SodaDataConvertor.convertToDayOfWeekStr(c.get(Calendar.DAY_OF_WEEK));

		String currentTime = c.get(Calendar.HOUR) + SodaDataConvertor.convertAmPm(c.get(Calendar.AM_PM));
		
		// Used to manage paging
		int previousOffset = 0;
		int currentOffset = 0;
		
		Scanner sc = new Scanner(System.in);
		String input;
		
		System.out.println("-------------------------------------------------------------");
		System.out.println("Food Truck Finder");
		System.out.println("-------------------------------------------------------------");

		do {
			previousOffset = currentOffset;
			List<FoodTruck> foodTrucks = client.getFoodTruck(currentOffset, 10, dayOrder, currentTime);
			System.out.println("\nDate: " + c.getTime());
			
			app.printFoodTruckList(foodTrucks, currentOffset);
			currentOffset += foodTrucks.size();
			
			System.out.println("Options: 'q' quit, 'p' previous list, 'n' next list");
			System.out.print("Please enter what you would like to do next: ");

			input = sc.nextLine();

			// only accepts valid inputs.  
			while (!input.equals("p") && !input.equals("n") && !input.equals("q")) {
				System.out.print(input + " is not a valid input.  Please try again: ");
				input = sc.nextLine();
			}

			if (input.equals("p")) {
				int prev = previousOffset - 10;
				currentOffset = (prev < 0) ? 0 : prev;
			} else if (input.equals("n")) {
				// do nothing since currentOffset has already been incremented
			}
		} while (!input.equals("q"));

		System.out.println("\nThank you for using Food Truck Finder.");
		sc.close();
	}

	private void printFoodTruckList(List<FoodTruck> list, int offset) {
		String format2 = String.format("%-3s %-40s %-8s", "", "Name", "Address");
		System.out.println(format2);
		System.out.println("-------------------------------------------------------------");

		if (list.size() == 0) {
			System.out.println("Sorry there is no more food trucks open at this time.");
		}

		for (FoodTruck f : list) {
			String format = String.format("%-3s %-40s %-8s", (offset++ + 1), f.getApplicant(),
					f.getLocation());
			System.out.println(format);
		}
		System.out.println("-------------------------------------------------------------");
	}

	

	
}