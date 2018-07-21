package priorg.main.tasks;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Konstantin Kostin
 */
public class TaskParser {

    private Category root;
    private Map<String, Category> categories;

    public TaskParser(Category root) {
        this.root = root;
        this.categories = new HashMap<>();
        categories.put("root", root);
    }

    public void parse(String fileName) {
        try (BufferedReader taskReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName)))) {

            String line;
            while ((line = taskReader.readLine()) != null) {
                if (line.startsWith("#") || line.equals("")) {
                    continue;
                }

                String[] categoriesSplit = line.split(">");
                parseCategories(categoriesSplit);

                String[] taskSplit = categoriesSplit[categoriesSplit.length - 1].split("\\|");
                parseTask(taskSplit, categories.get(categoriesSplit[categoriesSplit.length - 2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseCategories(String[] categoriesSplit) {
        for (int i = 0; i < categoriesSplit.length - 1; i++) {
            String categoryName = categoriesSplit[i];
            if (!categories.containsKey(categoryName)) {
                Category newCategory = new Category(categoryName);
                Category parent = (i == 0 ? root : categories.get(categoriesSplit[i - 1]));
                newCategory.setParent(parent);
                parent.addItem(newCategory);
                categories.put(categoryName, newCategory);
            }
        }
    }

    private void parseTask(String[] taskSplit, Category parentCategory) {
        Task task = new Task(taskSplit[0]);
        parentCategory.addItem(task);
        task.setParent(parentCategory);
        task.setPriority(Integer.parseInt(taskSplit[1]));
        task.setDescription(taskSplit[2]);
        // TODO: proper date reading
        task.setDeadline(new Date(0));

    }
}
