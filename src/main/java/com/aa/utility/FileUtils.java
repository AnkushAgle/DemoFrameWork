package com.aa.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

    /**
     * Copy a file from source to destination
     */
    public static void copyFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File copied successfully!");
    }

    /**
     * Move a file from source to destination
     */
    public static void moveFile(String sourcePath, String destinationPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File moved successfully!");
    }

    /**
     * Delete a file
     */
    public static void deleteFile(String filePath) throws IOException {
        Path file = Paths.get(filePath);
        Files.deleteIfExists(file);
        System.out.println("File deleted successfully!");
    }

    /**
     * List all files in a given folder
     */
    public static List<String> listFilesInFolder(String folderPath) {
        List<String> fileList = new ArrayList<>();
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path!");
            return fileList;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getName());
            }
        }
        return fileList;
    }

    /**
     * Read the content of a file
     */
    public static String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            content.append(scanner.nextLine()).append("\n");
        }
        scanner.close();
        return content.toString();
    }

    /**
     * Write content to a file
     */
    public static void writeFileContent(String filePath, String content) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(content);
        writer.close();
        System.out.println("Content written to file successfully!");
    }

    /**
     * Check if a file exists
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Create a new file
     */
    public static boolean createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.createNewFile()) {
            System.out.println("File created: " + file.getName());
            return true;
        } else {
            System.out.println("File already exists.");
            return false;
        }
    }
}
