package priorg.main.tasks.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import priorg.main.tasks.Category;

import java.io.*;
import java.util.Arrays;
import java.util.List;


/**
 * @author Konstantin Kostin
 * */
public class CsvDatabaseFile<T> implements Closeable {

    public static final String READ = "r";
    public static final String WRITE = "w";
    public static final String APPEND = "a";
    public static final String EDIT = "e";

    private static final String CLOSED = "c";

    private CSVReader dbReader;
    private CSVWriter dbWriter;
    private CSVWriter tempDbWriter;
    private DatabasePath dbPath;

    private String currentMode = CLOSED;

    public CsvDatabaseFile(DatabasePath dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * Open database file. Available modes:
     * "r" - read
     * "w" - write
     * "a" - append
     * */
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

    private void openToWrite(boolean tmp) throws IOException {
        try {
            dbWriter = new CSVWriter(new BufferedWriter(new FileWriter(dbPath.getFullPath(tmp))));
            currentMode = WRITE;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open or create db file: " + dbPath.getFullPath(tmp));
        }
    }

    private void openToAppend() throws IOException {
        try {
            dbWriter = new CSVWriter(new BufferedWriter(new FileWriter(dbPath.getFullPath(), true)));
            currentMode = APPEND;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open db file: " + dbPath.getFullPath());
        }
    }

    private void openToEdit() throws IOException {
        openToRead();
        openToWrite(true);
        currentMode = EDIT;
    }

    /**
     * ================
     * | Read methods |
     * ================
     * */

    public String[] readNext() throws IOException {
        ensureMode(READ, EDIT);
        return dbReader.readNext();

    }

    public List<String[]> readAll() throws IOException {
        ensureMode(READ, EDIT);
        return dbReader.readAll();
    }

    public void skip(int numberOfLines) throws IOException {
        ensureMode(READ, EDIT);
        dbReader.skip(numberOfLines);
    }

    /**
     * =================
     * | Write methods |
     * =================
     * */

    public void writeNext(String[] s) throws IOException {
        ensureMode(WRITE, APPEND, EDIT);
        dbWriter.writeNext(s);
        dbWriter.flush();
    }

    /**
     * =========
     * | Utils |
     * =========
     * */

    private void ensureMode(String ... modes) {
        for (String mode: modes) {
            if (currentMode.equals(mode)) {
                return;
            }
        }
        throw new IllegalStateException("File is in illegal mode state!\n" +
                "Current: " + currentMode + ", needed one of those: " + Arrays.toString(modes));
    }

    private void createDirectory(File dir) throws IOException {
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                System.out.println("Created directory " + dir.getName());
            } else {
                throw new IOException("Failed to create directory on " + dir.getName());
            }
        }
    }

    private void createFile(File file) throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("Created file " + file.getName());
            } else {
                throw new IOException("Failed to create file " + file.getName());
            }
        }
    }

    private void removeFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getName() + " not found");
        }
        if (!file.delete()) {
            throw new IOException("Failed to remove file: " + file.getName());
        }
    }

    private void renameFile(File file, String newName) throws IOException {
        if (!file.renameTo(new File(file.getParent(), newName))) {
            throw new IOException("Failed to rename file: " + file.getName() + " to: " + newName);
        }
    }

    @Override
    public void close() throws IOException {
        if (dbReader != null) {
            dbReader.close();
        }
        if (dbWriter != null) {
            dbWriter.close();
        }
        if (currentMode.equals(EDIT)) {
            renameFile(new File(dbPath.getFullPath(true)), dbPath.getName());
        }
        currentMode = CLOSED;
    }
}
