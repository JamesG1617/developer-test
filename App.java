package test;

import java.io.FileWriter;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.opencsv.CSVWriter;

/**
 * Application used to implement an API query and transform this data into a CSV
 * file.
 * 
 * @author James
 *
 */
public class App {

	// Constant
	private static final String COMMA = ",";

	// String Variables
	String cityName, name, type;
	// int Variable
	int id, incorrectValue;
	// double Variable
	double latitude, longitude;

	/**
	 * start() method - Used in AppDriver - scans and identifies user input,
	 * writes the ID, name, type, latitude and longitude to the CSV file
	 */
	public void run() {

		Scanner scanner = new Scanner(System.in);

		do {
			System.out.println("Search By City - Please Enter The City");
			cityName = scanner.nextLine();

		} while (cityName == null || cityName.trim().equals(""));

		String endpoint = "http://api.goeuro.com/api/v2/position/suggest/en/" + cityName + " ";

		try {
			CSVWriter writer = new CSVWriter(new FileWriter(cityName + ".csv"), '\t', CSVWriter.NO_QUOTE_CHARACTER);
			String connectionHandler = Jsoup.connect(endpoint).timeout(30000).ignoreContentType(true).execute().body();
			JSONArray array = new JSONArray(connectionHandler);

			System.out.println("ID\tName\t\t\tType\t\tLatitude\tLongitude");
			String[] header = { "ID", COMMA, "NAME", COMMA, "TYPE", COMMA, "LATITUDE", COMMA, "LONGITUDE" };
			writer.writeNext(header);

			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				id = object.getInt("_id");
				name = object.getString("name");
				type = object.getString("type");
				JSONObject object2 = object.getJSONObject("geo_position");
				latitude = object2.getDouble("latitude");
				longitude = object2.getDouble("longitude");

				String[] strings = { String.valueOf(id), COMMA, name, COMMA, type, COMMA, String.valueOf(latitude),
						COMMA, String.valueOf(longitude) };

				System.out.printf(id + "\t" + name + "\t\t\t" + "type" + "\t\t" + latitude + "\t" + longitude);

				System.out.println();
				// With more time, header would have been added to CSV file.

				writer.writeNext(strings);

			}

			writer.close();
			if (!writer.checkError()) {
				System.out.println("Search Results Completed - See CSV File.");
			}

		} catch (Exception exception) {
			System.out.println("Error caused by : " + exception);
			scanner.close();
		}

	}

}
