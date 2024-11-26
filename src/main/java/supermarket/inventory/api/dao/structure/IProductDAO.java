package supermarket.inventory.api.dao.structure;

import java.util.List;

import supermarket.inventory.api.model.Product;

public interface IProductDAO {
    Product createProduct(Product product);
    Product readProduct(int id);
    List<Product> readAllProducts();
    Product updateProduct(int id, Product newProduct);
    Product deleteProduct(int id);
}
