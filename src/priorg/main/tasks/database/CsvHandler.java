package priorg.main.tasks.database;

import priorg.main.id.Id;
import priorg.main.id.Identifiable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Konstantin Kostin
 * */
public abstract class CsvHandler<T extends Identifiable> implements AutoCloseable {

    private int itemCounter = 0;
    private int idCounter = 0;

    private CsvDatabaseFile dbFile;
    private Map<Id, T> items;

    protected CsvHandler(DatabasePath dbPath) {
        this.dbFile = new CsvDatabaseFile(dbPath);
        openDbFile(CsvDatabaseFile.READ);
    }

    private void openDbFile(String mode) {
        try {
            dbFile.open(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * ==============================
     * | Converting db into the map |
     * ==============================
     * */

    private void buildMap() {
        openDbFile(CsvDatabaseFile.READ);
        this.items = new HashMap<>();

        try {
            dbFile.skip(1);

            String[] line;
            while ((line = dbFile.readNext()) != null) {
                T parsedItem = parseItem(line);
                items.put(parsedItem.getId(), parsedItem);
                itemCounter++;
                idCounter++;
            }

        } catch (IOException e) {
            System.err.println("Error while loading tree from db");
            e.printStackTrace();
        }
    }

    public T parseItem(String[] line) {
        return parseItemImpl(line);
    }

    protected abstract T parseItemImpl(String[] line);

    public Map<Id, T> getItemsMap() {
        if (items == null) {
            buildMap();
        }
        return items;
    }

    public T getItemById(Id id) {
        return getItemsMap().get(id);
    }

    public int getItemCounter() {
        return itemCounter;
    }

    public int getNextAvailableId() {
        return idCounter;
    }

    /**
     * =================
     * | Item renaming |
     * =================
     * */

    public void renameItem(T item, String newName) {
//        checkDuplicates(item);
        openDbFile(CsvDatabaseFile.READ);

//        String oldName = item.getName();
    }

    /**
     * =================
     * | Item addition |
     * =================
     * */

    public void addEntry(T newItem) {
//        checkDuplicates(newItem);
        openDbFile(CsvDatabaseFile.APPEND);
    }


    public void removeEntry(T item) throws IOException {
        openDbFile(CsvDatabaseFile.EDIT);
        Id itemId = item.getId();

        try {
            //TODO: remove from subchildren
            String[] line;
            while ((line = dbFile.readNext()) != null) {
                if (line[0].equals(String.valueOf(itemId.getValue()))) {
                    getItemsMap().remove(itemId);
                    itemCounter--;
                    continue;
                }
                dbFile.writeNext(line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            dbFile.close();
        }
    }

    /**
     * =========
     * | Utils |
     * =========
     * */


    @Override
    public void close() throws IOException {
        dbFile.close();
    }
}
