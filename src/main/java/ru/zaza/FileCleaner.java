package ru.zaza;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FileCleaner {
    private static List<String> pathsNotToClean = new ArrayList<>(List.of(
            "D:\\Games\\Epic Games\\Fortnite", "D:\\Games\\Epic Games\\DirectXRedist",
            "D:\\Games\\Epic Games\\GenshinImpact", "D:\\Games\\Epic Games\\Launcher",
            "D:\\Games\\Steam\\steamapps\\common\\Grand Theft Auto V",
            "D:\\Games\\Steam\\steamapps\\common\\dota 2 beta",
            "D:\\Games\\Steam\\steamapps\\common\\Counter-Strike Global Offensive",
            "D:\\Games\\Steam\\steamapps\\common\\PUBG",
            "D:\\Games\\Steam\\steamapps\\common\\Apex Legends",
            "D:\\Games\\Steam\\steamapps\\common\\Call of Duty HQ"));

    public static void main(String[] args) {
        cleanChromeDownloads();
        cleanWorkshopMaps();
        cleanSteamWorkshopContent();
        cleanSteamDownloadingContent();
        cleanAllUselessFiles();
    }

    private static void cleanChromeDownloads() {
        File directory = new File("D:/ChromeDownloads");
        cleanDirectory(directory);
    }

    private static void cleanWorkshopMaps() {
        File directory =
                new File("D:/Games/Steam/steamapps/common/Counter-Strike Global Offensive/csgo/maps/workshop");
        cleanDirectory(directory);
    }

    private static void cleanSteamWorkshopContent() {
        File directory =
                new File("D:/Games/Steam/steamapps/workshop/content/570");
        cleanDirectory(directory);
    }

    private static void cleanSteamDownloadingContent() {
        File directory =
                new File("D:/Games/Steam/steamapps/downloading");
        cleanDirectory(directory);
    }

    private static void cleanAllUselessFiles() {
        String[] pathsToClean = {"D:/Games/Epic Games", "D:/Games/Steam/steamapps/common"};

        for (String path:
             pathsToClean) {
            File directory = new File(path);
            for (File file : directory.listFiles()) {
                if (pathsNotToClean.contains(file.getAbsolutePath())) continue;
                if (file.isDirectory() & isFileObsolete(file)) {
                    cleanDirectory(file);
                    deleteDirectory(file);
                }
            }
        }
    }

    private static void cleanDirectory(File directory) {
        LinkedList<File> directories = new LinkedList<>();
        directories.addLast(directory);
        while (!directories.isEmpty()) {
            File dir = directories.removeFirst();
            if (!dir.isDirectory()) {
                continue;
            }
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    directories.addLast(file);
                } else {
                    if (file.delete()) {
                        System.out.println("File deleted: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private static void deleteDirectory(File directory) {
        LinkedList<File> directories = new LinkedList<>();
        directories.addLast(directory);
        while (!directories.isEmpty()) {
            File dir = directories.removeFirst();
            if (!dir.isDirectory()) {
                continue;
            }
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    directories.addLast(file);
                } else {
                    if (file.delete()) {
                        System.out.println("File deleted: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
                if (file.isDirectory() & file.listFiles().length == 0) {
                    if (file.delete()) {
                        System.out.println("Directory deleted: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to delete directory: " + file.getAbsolutePath());
                    }
                }
            }
        }
        if (directory.delete()) {
            System.out.println("Directory deleted: " + directory.getAbsolutePath());
        } else {
            System.out.println("Failed to delete directory: " + directory.getAbsolutePath());
        }
    }

    private static boolean isFileObsolete(File file) {
        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime lastAccessTime = attr.lastAccessTime();
            long fileAgeInDays = (System.currentTimeMillis() - lastAccessTime.toMillis()) / (24 * 60 * 60 * 1000);
            long maxFileAgeInDays = 15; // задаем максимальный возраст файла в днях
            return fileAgeInDays > maxFileAgeInDays || file.length() == 0; // проверяем условия ненужности файла
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}