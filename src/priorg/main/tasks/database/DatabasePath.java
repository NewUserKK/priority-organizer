package priorg.main.tasks.database;


import java.io.File;

/**
 * @author Konstantin Kostin
 * */
public enum DatabasePath {

    TASKS("db/tasks.csv"),
    CATEGORIES("db/categories.csv");

    private String fullPath;
    private String directory;
    private String name;

    DatabasePath(String fullPath) {
        this.fullPath = fullPath;
        this.directory = parseDirectoryPath(fullPath);
        this.name = parseFileName(fullPath);
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
        return fullPath;
    }


    public String getDirectory() {
        return directory;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return fullPath;
    }
}
