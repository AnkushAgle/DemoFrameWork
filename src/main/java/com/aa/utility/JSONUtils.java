package com.aa.utility;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.testng.Reporter;

public class JSONUtils {
    private static final Gson gson = new Gson();

    private static final Gson gsonwrite = new GsonBuilder().setPrettyPrinting().create();

    public static void setDataInJSON(String jsonFilePath, JsonObject data) {
        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            // Write JSON object to file
        	gsonwrite.toJson(data, writer);
            System.out.println("Data has been written to " + jsonFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getValueFromJSON(String jsonFilePath, String key) {
        try {
            // Create a FileReader
            Reader reader = new FileReader(jsonFilePath);

            // Parse JSON using Gson
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // Fetch value from JSON using key
            String value = jsonObject.get(key).getAsString();

            // Close the reader
            reader.close();

            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Map<String, Integer> findDuplicateValues(String jsonFilePath) {
      
    	System.out.println(jsonFilePath);
    	System.out.println("");
    	System.out.println("");
    	
    	Map<String, Integer> frequencyMap = new HashMap<>();

        try (FileReader reader = new FileReader(jsonFilePath)) {
            // Parse JSON using Gson
            JsonElement jsonElement = JsonParser.parseReader(reader);
            processJsonElement(jsonElement, frequencyMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Filter duplicate values
        Map<String, Integer> duplicates = new HashMap<>();
        Set<Map.Entry<String, Integer>> entrySet = frequencyMap.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicates;
    }

    private static void processJsonElement(JsonElement jsonElement, Map<String, Integer> frequencyMap) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                processJsonElement(entry.getValue(), frequencyMap);
            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                processJsonElement(element, frequencyMap);
            }
        } else if (jsonElement.isJsonPrimitive()) {
            String value = jsonElement.getAsString();
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
    }
    
    
    public static String getTokenValueFromJSON(String jsonFilePath) {
        try (Reader reader = new FileReader(jsonFilePath)) {
            // Parse JSON using Gson
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // Navigate through the JSON structure to get the token value
            JsonObject dataObject = jsonObject.getAsJsonObject("data");
            JsonObject tokenObject = dataObject.getAsJsonObject("token");
            String tokenValue = tokenObject.get("value").getAsString();

            return tokenValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public static void main(String[] args) {

	  String loginFilePath = "D:\\TakeBKP\\ANKUSH\\NEW_HORIZON_PROD\\loginresponse.json";
      String tokenValue = JSONUtils.getTokenValueFromJSON(loginFilePath);

      System.out.println(tokenValue);
	}	
   

   
}

