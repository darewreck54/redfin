import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.FoodTruck;

public class FoodTruckFinder {
	public static void main(String[] args) {
		FoodTruckFinder instance = new FoodTruckFinder();
		Calendar c = Calendar.getInstance();
		int dayOrder = instance.convertToDayOfWeekStr(c.get(Calendar.DAY_OF_WEEK));
		
		String currentTime = c.get(Calendar.HOUR) + instance.convertAmPm(c.get(Calendar.AM_PM));
		String input;
		int previousOffset = 0;
		int currentOffset = 0;
		Scanner sc = new Scanner(System.in);
		
		System.out.println("-------------------------------------------------------------");
		System.out.println("Food Truck Finder");
		System.out.println("-------------------------------------------------------------");
		
		do {
			previousOffset = currentOffset;
			List<FoodTruck> ft = instance.getFoodTruck(currentOffset, 10, dayOrder, currentTime);
			System.out.println("Date: " +  c.getTime());
		
			String format2 =  String.format( "%-3s %-40s %-8s", "", "Name", "Address" );
			System.out.println(format2);
			System.out.println("-------------------------------------------------------------");
				
			if(ft.size() == 0) {
				System.out.println("Sorry there is no more food trucks open at this time.");
			} 
		
			for(FoodTruck f: ft) {
				String format =  String.format( "%-3s %-40s %-8s", ((currentOffset++) + 1), f.getApplicant(), f.getLocation() );
				System.out.println(format);
			}
			System.out.println("-------------------------------------------------------------");
			System.out.println("Options: 'q' quit, 'p' previous list, 'n' next list");
			System.out.print("Please enter what you would like to do next:");
		
			input = sc.nextLine();
			
			while(!input.equals("p") && !input.equals("n") && !input.equals("q")) {
				System.out.print(input + " is not a valid input.  Please try again:");
				input = sc.nextLine();
			}
			
			if(input.equals("p")) {
				int prev = previousOffset - 10;
				currentOffset = (prev < 0) ? 0:prev;
			} else if(input.equals("n")) {
				//do nothing since currentOffset has already been incremented
			}
		} while(!input.equals("q")); 
		
		System.out.println("Thank you for using Food Truck Finder.");
		sc.close();
	}
	private String convertAmPm(int amPm) {
		return (amPm == Calendar.AM) ? "AM" : "PM";
	}
	private int convertToDayOfWeekStr(int dayOfWeek) {
		int dayOrder = -1;
		switch(dayOfWeek) {
			case Calendar.MONDAY: {
				dayOrder = 1;
				break;
			}
			case Calendar.TUESDAY: {
				dayOrder = 2;
				break;
			}
			case Calendar.WEDNESDAY: {
				dayOrder = 3;
				break;
			}
			case Calendar.THURSDAY: {
				dayOrder = 4;
				break;
			}
			case Calendar.FRIDAY: {
				dayOrder = 5;
				break;
			}
			case Calendar.SATURDAY: {
				dayOrder = 6;
				break;
			}
			case Calendar.SUNDAY: {
				dayOrder = 0;
				break;
			}
		}
		
		return dayOrder;
	}
	
	private Map<String, List<FoodTruck>> cache = new HashMap<>();
	private int currentDayOrder = -1;
	private String currentTime = null;
	
	private List<FoodTruck> getFoodTruck(int offset, int limit, int dayOrder, String time) {
		if(dayOrder != currentDayOrder || !currentTime.equals(time) ) {
			cache = new HashMap<>();
		}
		
		if(cache.containsKey(offset + time)) {
			return cache.get(offset + time);
		}
		
		List<FoodTruck> foodTrcks = new ArrayList<>();
		try {
			StringBuilder result = new StringBuilder();
			String BASE_URL = "http://data.sfgov.org/resource/bbb8-hzi6.json";
			StringBuilder querySb = new StringBuilder();
			querySb.append("$offset=" + offset);
			querySb.append("&$limit=" + limit);
			String whereClause = "starttime < '"+time+"'" + "AND endtime >'" + time +"'";
			querySb.append("&$where="+ URLEncoder.encode(whereClause, "UTF-8"));
			querySb.append("&$$exclude_system_fields=true");
			querySb.append("&$order=applicant");
			querySb.append("&dayorder=" + dayOrder);
		
			URL url = new URL(BASE_URL + "?" + querySb.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			if(responseCode == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				rd.close();
				ObjectMapper objectMapper = new ObjectMapper();
				foodTrcks = objectMapper.readValue(result.toString(), new TypeReference<List<FoodTruck>>(){});
			} else if( responseCode == 202) {
				System.out.println("You can retry your request, and when it’s complete, you’ll get a 200 instead");
			} else if( responseCode == 400) {
				System.out.println("Probably your request was malformed. See the error message in the body for details");
				
			} else if( responseCode == 401) {
				System.out.println("You attempted to authenticate but something went wrong. Make sure follow the instructions to authenticate properly");
			} else if( responseCode == 428) {
				System.out.println("Your client is currently being rate limited. Make sure you’re using an app token");
			} else if( responseCode == 403) {
				System.out.println("You’re not authorized to access this resource. Make sure you authenticate to access private datasets");
			} else if( responseCode == 500) {
				System.out.println("Our bad! Something has gone wrong with Socrta’s platform. Please let us know if you encounter a 500 error");
			} else {
				System.out.println("failed");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return foodTrcks;
	}
}