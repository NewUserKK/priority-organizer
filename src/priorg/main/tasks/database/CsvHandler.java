package priorg.main.tasks.database;

import priorg.main.id.Id;
import priorg.main.id.Identifiable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Handler for abstract CSV database of objects.
 * Performs basic operations such as transforming CSV file to {@link Map},
 * adding or removing entries and so on.
 *
 * Objects in the database must have unique ID.
 *
 * @param <T> type of {@link Identifiable} objects that database stores
 * */
public abstract class CsvHandler<T extends Identifiable> implements AutoCloseable {

    /** Counter for items that database has */
    private int itemCounter = 0;

    /** Counter representing the smallest not used yet ID */
    private int idCounter = 0;

    /** CSV database file */
    private CsvDatabaseFile dbFile;

    /** Map of items */
    private Map<Id, T> items;


    /**
     * Constructor for handler. Takes {@link DatabasePath} object representing the path to a db file
     *
     * @param dbPath path to db file
     */
    CsvHandler(DatabasePath dbPath) {
        this.dbFile = new CsvDatabaseFile(dbPath);
    }

    private void openDbFile(String mode) {
        try {
            dbFile.open(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * ==============================
     * | Converting db into the map |
     * ==============================
     * */

    /**
     * Build map from CSV file.
     */
    private void buildMap() {
        openDbFile(CsvDatabaseFile.READ);
        this.items = new HashMap<>();

        try {
            dbFile.skip(1);  // skip header

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

    /**
     * Parse item from CSV line.
     *
     * @param line line from CSV file representing an object
     * @return instance of object with parsed properties
     */
    public T parseItem(String[] line) {
        return parseItemImpl(line);
    }

    protected abstract T parseItemImpl(String[] line);

    /**
     * Return map of items in db with its ID as keys and objects as values.
     * If map is not exist yet, it is built first.
     *
     * @return map with db items
     */
    public Map<Id, T> getItemsMap() {
        if (items == null) {
            buildMap();
        }
        return items;
    }

    /**
     * Return an object by ID
     *
     * @param id ID of an object
     * @return object that have requested ID
     */
    public T getItemById(Id id) {
        return getItemsMap().get(id);
    }

    /**
     * Return how much items database has
     *
     * @return current size of database
     */
    public int getItemCounter() {
        return itemCounter;
    }

    /**
     * Return next smallest available ID to use
     *
     * @return next free ID
     */
    public int getAvailableId() {
        return idCounter;
    }


    /*
     * =================
     * | Item addition |
     * =================
     * */

    /**
     * Add entry to the database file
     *
     * @param newItem new item to add in the database
     */
    public void addEntry(T newItem) {
        openDbFile(CsvDatabaseFile.APPEND);
    }

    /**
     * Remove existing entry from the database.
     * Make sure to remove its occurrence from any other databases connected with it as well.
     *
     * @param item item to remove
     * @throws IOException if an I/O exception occurs
     */
    public void removeEntry(T item) throws IOException {
        openDbFile(CsvDatabaseFile.EDIT);
        Id itemId = item.getId();

        try {
            //TODO: remove from subchildren
            String[] line;
            while ((line = dbFile.readNext()) != null) {
                if (line[0].equals(String.valueOf(itemId.getValue()))) {
                    getItemsMap().remove(itemId);
                    removeEntryImpl(item);
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

    protected abstract void removeEntryImpl(T item);

    /**
     * =========
     * | Utils |
     * =========
     * */

    /**
     * Close database file
     *
     * @throws IOException if an I/O exception occurs
     */
    @Override
    public void close() throws IOException {
        dbFile.close();
    }
}
