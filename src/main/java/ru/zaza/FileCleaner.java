package ru.zaza;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDeleter {
    public static void main(String[] args) {
        String filePath = "D:/forprogram";
        deleteFile(filePath);
    }

    private static void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
            System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.out.println("Failed to delete file: " + e.getMessage());
        }
    }
}