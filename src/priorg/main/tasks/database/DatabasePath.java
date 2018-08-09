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

    private String fullPath;
    private String directory;
    private String name;

    /** Name for temporary file */
    private String tempName;

    DatabasePath(String fullPath) {
        this.fullPath = fullPath;
        this.directory = parseDirectoryPath(fullPath);
        this.name = parseFileName(fullPath);
        this.tempName = name + ".tmp";
    }

    private String parseDirectoryPath(String fullPath) {
        String[] pathSplit = fullPath.split(File.separator);
        StringBuilder dir = new StringBuilder();
        for (int i = 0; i < pathSplit.length - 1; i++) {
            dir.append(pathSplit[i]);
        }
        return dir.toString();
    }

    private String parseFileName(String fullPath) {
        String[] pathSplit = fullPath.split(File.separator);
        return pathSplit[pathSplit.length - 1];
    }

    public String getFullPath() {
        return getFullPath(false);
    }

    public String getFullPath(boolean tmp) {
        return (tmp ? getDirectory() + File.separator + getName(true): fullPath);
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
