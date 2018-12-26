package priorg.main.tasks.database;


import java.io.File;

/**
 * Enumeration of database paths in a filesystem.
 * Allows to extract file name and parent directory from the full path.
 * Also besides actual name you can get name of a temporary object in the same directory
 * */
public enum DatabasePath {

    /** Path to tasks db */
    TASKS("db/tasks.csv"),

    /** Path to categories db */
    CATEGORIES("db/categories.csv");

    // escaping for non-unix systems
    private String SEPARATOR;

    private String fullPath;
    private String directory;
    private String name;

    /** Name for temporary file */
    private String tempName;

    DatabasePath(String fullPath) {
        SEPARATOR = File.separator.replace("\\", "\\\\");
        this.fullPath = fullPath;
        this.directory = parseDirectoryPath(fullPath);
        this.name = parseFileName(fullPath);
        this.tempName = name + ".tmp";
    }

    private String parseDirectoryPath(String fullPath) {
        String[] pathSplit = fullPath.split(SEPARATOR);
        StringBuilder dir = new StringBuilder();
        for (int i = 0; i < pathSplit.length - 1; i++) {
            dir.append(pathSplit[i]).append(SEPARATOR);
        }
        return dir.toString();
    }

    private String parseFileName(String fullPath) {
        String[] pathSplit = fullPath.split(SEPARATOR);
        return pathSplit[pathSplit.length - 1];
    }

    public String getFullPath() {
        return getFullPath(false);
    }

    public String getFullPath(boolean tmp) {
        return (tmp ? getDirectory() + SEPARATOR + getName(true): fullPath);
    }

    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return getName(false);
    }

    public String getName(boolean tmp) {
        return (tmp ? tempName : name);
    }

    @Override
    public String toString() {
        return fullPath;
    }
}
