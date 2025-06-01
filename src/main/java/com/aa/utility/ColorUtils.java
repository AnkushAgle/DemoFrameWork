package com.aa.utility;
import java.util.Random;

public class ColorUtils {

    // Generates a random hex color code
    public static String generateRandomHexColor() {
        Random random = new Random();
        
        // Generate random RGB values (each in range [0, 255])
        int r = random.nextInt(256);  // Red component
        int g = random.nextInt(256);  // Green component
        int b = random.nextInt(256);  // Blue component
        
        // Convert RGB to hex format and return
        return String.format("#%02X%02X%02X", r, g, b);
    }
}
