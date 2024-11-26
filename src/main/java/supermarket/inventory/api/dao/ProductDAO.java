package supermarket.inventory.api.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import supermarket.inventory.api.dao.structure.IProductDAO;
import supermarket.inventory.api.model.Product;

public class ProductDAO implements IProductDAO {

    private SessionFactory sessionFactory;

    public ProductDAO() {
        super();
        sessionFactory = null;
    }

    public ProductDAO(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Product createProduct(Product product) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.persist(product);

            transaction.commit();
            
            System.out.printf("success create product %s\n", product);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.printf("fail create product %s\n", product);
            product = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return product;
    }
    
    @Override
    public Product readProduct(int id) {
        Session session = null;
        Product product = null;
        
        try {
            session = sessionFactory.openSession();
            product = session.find(Product.class, id);
            if (product == null) {
                System.out.printf("not found product id %d\n", id);
            } else {
                System.out.printf("success read product %s\n", product);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("fail read product id %d\n", id);
            product = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        return product;
    }
    
    @Override
    public List<Product> readAllProducts() {
        Session session = null;
        List<Product> products = null;

        try {
            session = sessionFactory.openSession();
            products = session.createQuery("from Product", Product.class).getResultList();
            if (products == null) {
                System.out.printf("not found all products\n");
            } else {
                System.out.printf("success read all products %s\n", products);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("fail read all products\n");
            products = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return products;
    }
    
    @Override
    public Product updateProduct(int id, Product newProduct) {
        newProduct.setId(id);
        
        Session session = null;
        Transaction transaction = null;
        Product oldProduct = null;

        try {
            session = sessionFactory.openSession();

            oldProduct = session.find(Product.class, id);

            if (oldProduct == null) {
                System.out.printf("not found product id %d\n", id);
                newProduct = null;
            } else {
                transaction = session.beginTransaction();
                session.merge(newProduct);
                transaction.commit();
                System.out.printf("success update product %s\n", newProduct);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.printf("fail update product id %d\n", id);
            newProduct = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return newProduct;
    }
    
    @Override
    public Product deleteProduct(int id) {
        Session session = null;
        Transaction transaction = null;
        Product product = null;

        try {
            session = sessionFactory.openSession();
            product = session.find(Product.class, id);
            if (product == null) {
                System.out.printf("not found product id %d\n", id);
            } else {
                transaction = session.beginTransaction();
                session.remove(product);
                transaction.commit();
                System.out.printf("success delete product %s\n", product);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.out.printf("fail delete product id %d\n", id);
            product = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        
        return product;
    }
}
