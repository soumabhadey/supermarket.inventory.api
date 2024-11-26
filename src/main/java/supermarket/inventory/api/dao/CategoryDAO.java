package supermarket.inventory.api.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import supermarket.inventory.api.dao.structure.ICategoryDAO;
import supermarket.inventory.api.model.Category;

public class CategoryDAO implements ICategoryDAO {

    private SessionFactory sessionFactory;

    public CategoryDAO() {
        super();
        sessionFactory = null;
    }

    public CategoryDAO(SessionFactory sessionFactory) {
        super();
        this.sessionFactory = sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Category createCategory(Category category) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.persist(category);

            transaction.commit();

            System.out.printf("success create category %s\n", category);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();
            System.out.printf("fail create category %s\n", category);
            category = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return category;
    }

    @Override
    public Category readCategory(int id) {
        Session session = null;
        Category category = null;

        try {
            session = sessionFactory.openSession();
            category = session.find(Category.class, id);
            if (category == null) {
                System.out.printf("not found category id %d\n", id);
            } else {
                System.out.printf("success read category %s\n", category);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("fail read category id %d\n", id);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return category;
    }

    @Override
    public List<Category> readAllCategories() {
        Session session = null;
        List<Category> categories = null;

        try {
            session = sessionFactory.openSession();
            categories = session.createQuery("from Category", Category.class).getResultList();
            
            if (categories == null) {
                System.out.printf("not found all categories\n");
            } else {
                System.out.printf("success read all categories %s\n", categories);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("fail read all categories\n");
            categories = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return categories;
    }

    @Override
    public Category updateCategory(int id, Category newCategory) {
        newCategory.setId(id);   	
    	
        Session session = null;
        Transaction transaction = null;
        Category oldCategory = null;

		try {
			session = sessionFactory.openSession();
			
			oldCategory = session.find(Category.class, id);
			
			if (oldCategory == null) {
				System.out.printf("not found category id %d\n", id);
				newCategory = null;
			} else {
				transaction = session.beginTransaction();
				session.merge(newCategory);
				transaction.commit();
				System.out.printf("success update category %s\n", newCategory);
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			
			e.printStackTrace();
			System.out.printf("fail update category id %d\n", id);
			newCategory = null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		
		return newCategory;
    }
    
    @Override
    public Category deleteCategory(int id) {
    	Session session = null;
    	Transaction transaction = null;
    	Category category = null;
    	
    	try {
    		session = sessionFactory.openSession();
    		
    		category = session.find(Category.class, id);
    		
    		if (category == null) {
    			System.out.printf("not found category id %d\n", id);
    		} else {    		    
    			transaction = session.beginTransaction();
    			session.remove(category);
    			transaction.commit();
    			System.out.printf("success delete category %s\n", category);
    		}
    	} catch (Exception e) {
    		if (transaction != null) {
    			transaction.rollback();
    		}
    		
    		e.printStackTrace();
    		System.out.printf("fail delete category id %d\n", id);
    		category = null;
    	} finally {
    		if (session != null) {
    			session.close();
    		}
    	}
    	
    	return category;
    }
}
