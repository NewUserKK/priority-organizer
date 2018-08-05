package priorg.main.tasks.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.util.Arrays;
import java.util.List;


/**
 * @author Konstantin Kostin
 * */
public class CsvDatabaseFile implements Closeable {

    public static final String READ = "r";
    public static final String WRITE = "w";
    public static final String APPEND = "a";

    private static final String CLOSED = "c";

    private CSVReader dbReader;
    private CSVWriter dbWriter;
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

        if (mode.equals("r")) {
            openToRead();
        } else if (mode.equals("w")) {
            openToWrite();
        } else if (mode.equals("a")) {
            openToAppend();
        } else {
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

    private void createDirectory(File dbDir) throws IOException {
        if (!dbDir.exists()) {
            if (dbDir.mkdirs()) {
                System.out.println("Created directory " + dbDir.getName());
            } else {
                throw new IOException("Failed to create directory on " + dbDir.getName());
            }
        }
    }

    private void createFile(File dbFile) throws IOException {
        if (!dbFile.exists()) {
            if (dbFile.createNewFile()) {
                System.out.println("Created db file " + dbFile.getName());
            } else {
                throw new IOException("Failed to create db file " + dbFile.getName());
            }
        }
    }

    private void openToWrite() throws IOException {
        try {
            dbWriter = new CSVWriter(new BufferedWriter(new FileWriter(dbPath.getFullPath())));
            currentMode = WRITE;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open or create db file: " + dbPath.getFullPath());
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

    /**
     * ================
     * | Read methods |
     * ================
     * */

    public String[] readNext() throws IOException {
        ensureMode(READ);
        return dbReader.readNext();
    }

    public List<String[]> readAll() throws IOException {
        ensureMode(READ);
        return dbReader.readAll();
    }

    public void skip(int numberOfLines) throws IOException {
        ensureMode(READ);
        dbReader.skip(numberOfLines);
    }

    /**
     * =================
     * | Write methods |
     * =================
     * */

    public void writeNext(String[] s) {
        ensureMode(WRITE, APPEND);
        dbWriter.writeNext(s);
        try {
            dbWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureMode(String ... modes) {
        for (String mode: modes) {
            if (currentMode.equals(mode)) {
                return;
            }
        }
        throw new IllegalStateException("File is in illegal mode state!\n" +
                "Current: " + currentMode + ", needed one of those: " + Arrays.toString(modes));
    }

    @Override
    public void close() throws IOException {
        if (dbReader != null) {
            dbReader.close();
        }
        if (dbWriter != null) {
            dbWriter.close();
        }
        currentMode = CLOSED;
    }
}
