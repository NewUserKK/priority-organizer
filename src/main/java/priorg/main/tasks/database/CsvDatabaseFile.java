package priorg.main.tasks.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import priorg.main.tasks.Category;

import java.io.*;
import java.util.Arrays;
import java.util.List;


/**
 * Class representing single csv file.
 * It's intended to simplify interactions with I/O streams and encapsulate them in a single file.
 * File can be opened in multiple modes:
 *   1. Read mode - you can only read from file
 *   2. Write mode - clears all previous content from file and allows to write into it
 *   3. Append mode - allows to append existing file from the end
 *   4. Edit mode - like a write mode with ability to read file in parallel
 *      Basically this mode opens reader from existing file and writer to %existing file name%.tmp
 *      and, when closed, replaces old file with new
 *
 * */
public class CsvDatabaseFile implements Closeable {

    public static final String READ = "r";
    public static final String WRITE = "w";
    public static final String APPEND = "a";
    public static final String EDIT = "e";

    private static final String CLOSED = "c";

    private CSVReader dbReader;
    private CSVWriter dbWriter;
    private DatabasePath dbPath;

    private String currentMode = CLOSED;

    public CsvDatabaseFile(DatabasePath dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * Open file in a certain mode
     *
     * @param mode mode in which file opens
     * @throws IOException in case I/O exception occurs
     */
    public void open(String mode) throws IOException {
        if (mode.equals(currentMode)) {
            return;
        }

        try {
            this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (mode) {
            case READ:
                openToRead();
                break;
            case WRITE:
                openToWrite();
                break;
            case APPEND:
                openToAppend();
                break;
            case EDIT:
                openToEdit();
                break;
            default:
                throw new IllegalArgumentException("Unknown file mode");
        }
    }

    /**
     * Open file in read mode.
     * If the file is not exist, will attempt to create it and, if it fails, abort the program
     */
    private void openToRead() {
        try {
            dbReader = new CSVReader(new InputStreamReader(new FileInputStream(dbPath.getFullPath())));
            currentMode = READ;

        } catch (FileNotFoundException noDbFoundException) {
            System.out.println("Failed to open existing db on path \"" + dbPath.getFullPath() + "\"");
            System.out.println("Attempting to create new db file on \"" + dbPath.getFullPath() + "\"");

            File dbDir = new File(dbPath.getDirectory());
            try {
                createDirectory(dbDir);
            } catch (IOException e) {
                System.err.println(e.getMessage() + ", aborting");
                System.exit(1);
            }

            File dbFile = new File(dbPath.getFullPath());
            try {
                createFile(dbFile);
            } catch (IOException e) {
                System.err.println(e.getMessage() + ", aborting");
                System.exit(1);
            }
        }
    }

    private void openToWrite() throws IOException {
        openToWrite(false);
    }

    /**
     * Open file in write mode.
     * If tmp is true, then a path to the temporary file will be used
     *
     * @param tmp determines if a path to the temporary file will be used
     * @throws IOException if failed to open or create file
     */
    private void openToWrite(boolean tmp) throws IOException {
        try {
            dbWriter = new CSVWriter(new BufferedWriter(new FileWriter(dbPath.getFullPath(tmp))));
            currentMode = WRITE;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open or create db file: " + dbPath.getFullPath(tmp));
        }
    }

    /**
     * Open file in append mode.
     *
     * @throws IOException if failed to open or create file
     */
    private void openToAppend() throws IOException {
        try {
            dbWriter = new CSVWriter(new BufferedWriter(new FileWriter(dbPath.getFullPath(), true)));
            currentMode = APPEND;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open db file: " + dbPath.getFullPath());
        }
    }

    /**
     * Open file in edit mode.
     *
     * @throws IOException if failed to open or create file
     */
    private void openToEdit() throws IOException {
        openToRead();
        openToWrite(true);
        currentMode = EDIT;
    }


    /*
     * ================
     * | Read methods |
     * ================
     * */

    /**
     * Read next line from file.
     *
     * @return array of String with values of CSV file row
     * @throws IOException if an I/O exception occurs
     *
     * @see CSVReader
     */
    public String[] readNext() throws IOException {
        ensureMode(READ, EDIT);
        return dbReader.readNext();

    }

    /**
     * Read all lines from CSV file in a list of String
     *
     * @return list of arrays of String with all CSV lines
     * @throws IOException if an I/O exception occurs
     *
     * @see CSVReader
     */
    public List<String[]> readAll() throws IOException {
        ensureMode(READ, EDIT);
        return dbReader.readAll();
    }

    /**
     * Skip next lines
     *
     * @param numberOfLines number of lines to skip
     * @throws IOException if an I/O exception occurs
     *
     * @see CSVReader
     */
    public void skip(int numberOfLines) throws IOException {
        ensureMode(READ, EDIT);
        dbReader.skip(numberOfLines);
    }


    /*
     * =================
     * | Write methods |
     * =================
     * */

    /**
     * Write line to the file
     *
     * @param line array of String to write as CSV row
     * @throws IOException if an I/O exception occurs
     *
     * @see CSVWriter
     */
    public void writeNext(String[] line) throws IOException {
        ensureMode(WRITE, APPEND, EDIT);
        dbWriter.writeNext(line);
        dbWriter.flush();
    }


    /*
     * =========
     * | Utils |
     * =========
     * */

    /**
     * Check if current file mode is correct to do operation with it
     *
     * @param modes list of correct modes
     * @throws IllegalStateException if no modes is correct
     */
    private void ensureMode(String ... modes) {
        for (String mode: modes) {
            if (currentMode.equals(mode)) {
                return;
            }
        }
        throw new IllegalStateException("File is in illegal mode state!\n" +
                "Current: " + currentMode + ", needed one of those: " + Arrays.toString(modes));
    }

    /**
     * Create directory in a filesystem
     *
     * @param dir File object representing path to the directory
     * @throws IOException if fails to create the directory
     */
    private void createDirectory(File dir) throws IOException {
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory " + dir.getName());
            } else {
                throw new IOException("Failed to create directory on " + dir.getName());
            }
        }
    }

    /**
     * Create file in a filesystem
     *
     * @param file File object representing path to the file
     * @throws IOException if fails to create the file
     */
    private void createFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("Created file " + file.getName());
            } else {
                throw new IOException("Failed to create file " + file.getName());
            }
        }
    }

    /**
     * Remove file from the filesystem
     *
     * @param file File object representing path to the file
     * @throws IOException if fails to remove the file
     * @throws FileNotFoundException if file is not exist
     */
    private void removeFile(File file) throws FileNotFoundException, IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getName() + " not found");
        }
        if (!file.delete()) {
            throw new IOException("Failed to remove file: " + file.getName());
        }
    }

    /**
     * Rename file in a filesystem
     *
     * @param file File object representing path to the file
     * @param newName new file name
     * @throws IOException if fails to rename the file
     */
    private void renameFile(File file, String newName) throws IOException {
        File newFile = new File(file.getParent(), newName);
        if (newFile.exists()) {
            throw new IOException("File with name " + newFile.getName() + " already exists!");
        }
        if (!file.renameTo(newFile)) {
            throw new IOException("Failed to rename file: " + file.getName() + " to: " + newName);
        }
    }

    /**
     * Close all readers and writers.
     * If file is in edit mode, renames tmp file to match the old db file
     *
     * @throws IOException if an I/O exception occurs
     */
    @Override
    public void close() throws IOException {
        if (dbReader != null) {
            dbReader.close();
        }
        if (dbWriter != null) {
            dbWriter.close();
        }
        if (currentMode.equals(EDIT)) {

            try {
                removeFile(new File(dbPath.getFullPath()));
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }

            renameFile(new File(dbPath.getFullPath(true)), dbPath.getName());
        }
        currentMode = CLOSED;
    }
}
