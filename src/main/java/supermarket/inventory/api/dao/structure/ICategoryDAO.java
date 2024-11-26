package supermarket.inventory.api.dao.structure;

import java.util.List;

import supermarket.inventory.api.model.Category;

public interface ICategoryDAO {
    Category createCategory(Category category);
    Category readCategory(int id);
    List<Category> readAllCategories();
    Category updateCategory(int id, Category newCategory);
    Category deleteCategory(int id);
}
