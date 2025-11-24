package DataManagment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesChecker {
    private static final String directory = "files/";

    public static void checkFile(String filename) {
        Path directory = Paths.get("files");
        try {
            if (Files.notExists(directory)) {
                Files.createDirectory(directory);
            }
            File f = new File("files/" + filename);
            f.createNewFile();

        } catch (IOException e) {
            System.out.println("Something went wrong with creating the folder: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getPath(String filename) {
        return directory + filename;
    }
}
