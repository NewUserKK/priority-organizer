package priorg.main.tasks;

import priorg.main.Config;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;


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
            throw new IllegalArgumentException("Unknown mode");
        }
    }

    private void openToRead() throws IOException {
        try {
            dbReader = new BufferedReader(new InputStreamReader(new FileInputStream(dbPath)));
            currentMode = READ;

        } catch (FileNotFoundException noDbFoundException) {
            System.out.println("Failed to open existing db on path \"" + dbPath + "\", creating new");
            System.out.println("New db file created on " + Config.TASK_DB_PATH);
            File dbFile = new File(Config.TASK_DB_PATH.toString());
            try {
                if (!dbFile.createNewFile()) {
                    System.err.println("Failed to create db, aborting");
                    System.exit(1);
                }
            } catch (IOException fileCreationException) {
                fileCreationException.printStackTrace();
                System.err.println("Failed to create db, aborting");
                System.exit(1);
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
