package supermarket.inventory.api;

import java.time.LocalDate;

import org.hibernate.Session;

import supermarket.inventory.api.dao.ProductDAO;
import supermarket.inventory.api.database.DatabaseConnection;
import supermarket.inventory.api.model.Product;

public class TestHibernate {
    public static void main(String[] args) {
    	DatabaseConnection con = null;
        Session session = null;
        
        try {            
            Product product = new Product();
            product.setName("Cola");
            product.setMrp(40);
            product.setCount(100);
            product.setExpiryDate(LocalDate.parse("2025-10-31"));
            
            con = new DatabaseConnection();
            ProductDAO productDAO = new ProductDAO(con.getSessionFactory());
            product = productDAO.createProduct(product);
            
            System.out.printf("Starting read.\n");
            
            System.out.printf("%s.\n", product);
            
            System.out.printf("Starting update.\n");
            product.setName("Sprite");
            productDAO.updateProduct(product.getId(), product);
            
            System.out.printf("Starting delete. \n");
            productDAO.deleteProduct(product.getId());
            
        } catch (Exception e) {
            System.out.printf("Unsuccessful session.\n");
            e.printStackTrace();
        } finally {
            if (session != null) {
            	session.close();
            }
            if (con != null) {
            	con.close();
            }
            System.out.printf("Closed all.\n");
        }
    }
}