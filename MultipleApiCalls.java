import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ApiExample {
    public static void main(String[] args) throws Exception {
        // Define two API endpoints
        String api1 = "https://jsonplaceholder.typicode.com/posts/1"; // Example API
        String api2 = "https://jsonplaceholder.typicode.com/users/1"; // Example API

        // Fetch responses
        String response1 = fetchApiResponse(api1);
        String response2 = fetchApiResponse(api2);

        // Parse JSON responses into HashMaps
        HashMap<String, Object> map1 = parseJsonToHashMap(response1);
        HashMap<String, Object> map2 = parseJsonToHashMap(response2);

        // Perform some arbitrary processing
        // Example: Combine data from both maps
        System.out.println("Data from API 1: " + map1);
        System.out.println("Data from API 2: " + map2);

        String combinedResult = "Post Title: " + map1.get("title") +
                                " | User Name: " + map2.get("name");
        System.out.println("Combined Result: " + combinedResult);
    }

    // Method to fetch API response
    private static String fetchApiResponse(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }

    // Method to parse JSON string into a HashMap
    private static HashMap<String, Object> parseJsonToHashMap(String json) {
        JsonReader reader = Json.createReader(new InputStreamReader(new java.io.ByteArrayInputStream(json.getBytes())));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        HashMap<String, Object> map = new HashMap<>();
        jsonObject.forEach((key, value) -> {
            switch (value.getValueType()) {
                case STRING:
                    map.put(key, jsonObject.getString(key));
                    break;
                case NUMBER:
                    map.put(key, jsonObject.getJsonNumber(key).numberValue());
                    break;
                case TRUE:
                case FALSE:
                    map.put(key, value == javax.json.JsonValue.TRUE);
                    break;
                case NULL:
                    map.put(key, null);
                    break;
                default:
                    map.put(key, value.toString()); // Handle as string for complex types
            }
        });
        return map;
    }
}
