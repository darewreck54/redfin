import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SodaApiClient {
	private static final String BASE_URL = "http://data.sfgov.org/resource/bbb8-hzi6.json";
	
	private Map<String, List<FoodTruck>> cache = new HashMap<>();
	private int currentDayOrder = -1;
	private String currentTime = null;
	
	/**
	 * This method is used to make a request to get food truck info
	 * 
	 * Note:
	 * 1) This will retrieve by sorted order of the food truck's name
	 * 2) Only request used data fields.  Limit the data size of the response
	 * 3) Cache results when possible to optimize network traffic
	 * 
	 * @param offset - offset where to start
	 * @param limit - size limit of each page
	 * @param dayOrder - the day of the week.  Needs to be mapped to Soda's mapping.  Ex. 3 = Thursday
	 * @param time - the hour the request is made (ex. 3PM).  Needs to be in that format
	 * @return List of FoodTruck
	 */
	public List<FoodTruck> getFoodTruck(int offset, int limit, int dayOrder, String time) {
		if (dayOrder != currentDayOrder || !currentTime.equals(time)) {
			cache = new HashMap<>();
		}

		if (cache.containsKey(offset + time)) {
			return cache.get(offset + time);
		}

		List<FoodTruck> foodTrcks = new ArrayList<>();
		try {
			StringBuilder result = new StringBuilder();
			StringBuilder querySb = new StringBuilder();
			querySb.append("$offset=" + offset);
			querySb.append("&$limit=" + limit);
			String whereClause = "starttime < '" + time + "'" + "AND endtime >'" + time + "'";
			querySb.append("&$where=" + URLEncoder.encode(whereClause, "UTF-8"));
			querySb.append("&$$exclude_system_fields=true");
			querySb.append("&$order=applicant");
			querySb.append("&dayorder=" + dayOrder);
			querySb.append("&$select=location,end24,endtime,start24,dayofweekstr,dayorder,applicant,optionaltext");

			URL url = new URL(BASE_URL + "?" + querySb.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				rd.close();
				ObjectMapper objectMapper = new ObjectMapper();
				foodTrcks = objectMapper.readValue(result.toString(), new TypeReference<List<FoodTruck>>() {
				});
			} else if (responseCode == 202) {
				System.out.println("You can retry your request, and when it’s complete, you’ll get a 200 instead");
			} else if (responseCode == 400) {
				System.out
						.println("Probably your request was malformed. See the error message in the body for details");

			} else if (responseCode == 401) {
				System.out.println(
						"You attempted to authenticate but something went wrong. Make sure follow the instructions to authenticate properly");
			} else if (responseCode == 428) {
				System.out.println("Your client is currently being rate limited. Make sure you’re using an app token");
			} else if (responseCode == 403) {
				System.out.println(
						"You’re not authorized to access this resource. Make sure you authenticate to access private datasets");
			} else if (responseCode == 500) {
				System.out.println(
						"Our bad! Something has gone wrong with Socrta’s platform. Please let us know if you encounter a 500 error");
			} else {
				System.out.println("failed");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return foodTrcks;
	}
}
