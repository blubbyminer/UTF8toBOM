import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Converter {

    private static ArrayList<File> ymlFiles = new ArrayList<>();
    private static ArrayList<File> txtFiles = new ArrayList<>();

    private static final byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    public enum Status {
        success,
        emptyPath,
        notAMod,
        unknownError
    }

    /**
     * The magic happens here
     * @param path Project path
     * @return Status code so the user can see if the world is burning
     */
    public static Status processProject(String path) {
        // Some checks required
        if (path == null) return Status.emptyPath;

        // Here we check if the path is a Stellaris project... I have no idea how to do that...
        try {
            try {
                final File project = new File(path);

                boolean hasModDescription = false;

                for (File file : project.listFiles()) {
                    if (FilenameUtils.isExtension(file.getName(), "mod")) {
                        hasModDescription = true;
                    }
                }

                // Abort if a .mod file cannot be found
                if (!hasModDescription) return Status.notAMod;


                // Iterate over every file in the directory, just adding them to the ToDO-list
                getFileNames(project);
                // Just debugging some info
                System.out.println("Count yml files: " + ymlFiles.size());
                System.out.println("Count txt files: " + txtFiles.size());

                int bomifiedFiles = 0;
                for (File yml : ymlFiles) {
                    try (FileInputStream stream = new FileInputStream(yml)) {
                        String content = IOUtils.toString(stream, StandardCharsets.UTF_8);

                        if (isBOMified(yml) == 0) {
                            writeBomFile(yml, content);
                            bomifiedFiles++;
                        }
                    }
                }
                System.out.println("~~~~");
                System.out.println("Count BOMified files: " + bomifiedFiles);


                return Status.success;
            } catch (NullPointerException e) {
                return Status.emptyPath;
            }
        } catch (Exception e) {
            return Status.unknownError;
        }
    }

    /**
     * Recursive function to sort through all project files.
     * Note that everything besides .yml and .txt files is ignored
     * @param file the parent directory to loop over
     */
    private static void getFileNames(File file) {
        if (file.isDirectory()) {
            for (File temp : file.listFiles()) {
                getFileNames(temp);
            }
        } else if (FilenameUtils.isExtension(file.getName(), "yml")) {
            ymlFiles.add(file);

        } else if (FilenameUtils.isExtension(file.getName(), "txt")) {
            txtFiles.add(file);
        }
    }

    /**
     * Check if a file is already UTF-8 BOM
     * @param file The file to check
     * @return -1: IOException - something went wrong <br>
     * 0: File is not BOMified<br>
     * 1: File is already UTF-8 BOM encoded
     */
    private static int isBOMified(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());

            byte[] checkForBOM = new byte[3];

            checkForBOM[0] = fileContent[0];
            checkForBOM[1] = fileContent[1];
            checkForBOM[2] = fileContent[2];

            if (Arrays.equals(checkForBOM, BOM)) {
                System.out.println("File " + file.getName() + " already BOMified.");
                return 1;
            }
        } catch (IOException e) {
            return -1;
        }


        System.out.println("File " + file.getName() + " needs to be BOMified.");
        return 0;
    }


    /**
     * Copied shamelessly from <a href = https://mkyong.com/java/java-how-to-add-and-remove-bom-from-utf-8-file/> Mkyong</a>.
     * Adds the necessary BOM bytes to a UTF-8 file
     * @param path File to BOMifiy
     * @param content Content of the file, to be appended after the BOM bytes
     */
    private static void writeBomFile(File path, String content) {
        try (FileOutputStream fos = new FileOutputStream(path)) {

            byte[] BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

            fos.write(BOM);
            fos.write(content.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
