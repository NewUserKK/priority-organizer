package priorg.main.tasks.database;

import java.io.*;
import java.util.Arrays;


/**
 * @author Konstantin Kostin
 * */
public class DatabaseFile implements Closeable {

    public static final String CLOSED = "c";
    public static final String READ = "r";
    public static final String WRITE = "w";
    public static final String APPEND = "a";

    private BufferedReader dbReader;
    private PrintWriter dbWriter;
    private String dbPath;
    private String currentMode = CLOSED;

    public DatabaseFile(String dbPath) {
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
            dbReader = new BufferedReader(new InputStreamReader(new FileInputStream(dbPath)));
            currentMode = READ;

        } catch (FileNotFoundException noDbFoundException) {
            System.out.println("Failed to open existing db on path \"" + dbPath + "\"");
            System.out.println("Attempting to create new db file on \"" + DatabasePaths.OLD_DB_FULL_PATH + "\"");

            File dbDir = new File(DatabasePaths.DIRECTORY.toString());
            try {
                createDirectory(dbDir);
            } catch (IOException e) {
                System.err.println(e.getMessage() + ", aborting");
                System.exit(1);
            }

            File dbFile = new File(DatabasePaths.OLD_DB_FULL_PATH.toString());
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
            dbWriter = new PrintWriter(dbPath);
            currentMode = WRITE;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open or create db file: " + dbPath);
        }
    }

    private void openToAppend() throws IOException {
        try {
            dbWriter = new PrintWriter(new BufferedWriter(new FileWriter(dbPath, true)));
            currentMode = APPEND;

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to open db file: " + dbPath);
        }
    }

    public String readLine() throws IOException {
        ensureMode(READ);
        return dbReader.readLine();
    }


    public void println(String s) {
        ensureMode(WRITE, APPEND);
        dbWriter.println(s);
        dbWriter.flush();
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
