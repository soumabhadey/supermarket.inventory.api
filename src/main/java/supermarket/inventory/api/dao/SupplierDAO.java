package supermarket.inventory.api.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import supermarket.inventory.api.dao.structure.ISupplierDAO;
import supermarket.inventory.api.model.Supplier;

public class SupplierDAO implements ISupplierDAO {
	private SessionFactory sessionFactory;
	
	public SupplierDAO() {
		super();
		sessionFactory = null;
	}
	
	public SupplierDAO(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Supplier createSupplier(Supplier supplier) {
		Session session = null;
		Transaction transaction = null;
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.persist(supplier);
			transaction.commit();
			System.out.printf("success create supplier %s\n", supplier);
		} catch (Exception e) {
			if (transaction != null) {
			    transaction.rollback();
			}
			
			e.printStackTrace();
			System.out.printf("fail create supplier %s\n", supplier);
			supplier = null;
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
		
		return supplier;
	}
	
	@Override
	public Supplier readSupplier(int id) {
	    Session session = null;
	    Supplier supplier = null;
	    
	    try {
	        session = sessionFactory.openSession();
	        supplier = session.find(Supplier.class, id);
	        if (supplier == null) {
	            System.out.printf("not found supplier id %d\n", id);
	        } else {
	            System.out.printf("success read supplier %s\n", supplier);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.printf("fail read supplier id %d\n", supplier);
	        supplier = null;
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    
	    return supplier;
	}
	
	@Override
	public List<Supplier> readAllSuppliers() {
	    Session session = null;
	    List<Supplier> suppliers = null;
	    
	    try {
	        session = sessionFactory.openSession();
	        suppliers = session.createQuery("from Supplier", Supplier.class).getResultList();
	        System.out.printf("success read all suppliers %s\n", suppliers);
	    } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("fail read all suppliers\n");
            suppliers = null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
	    
	    return suppliers;
	}
	
	@Override
	public Supplier updateSupplier(int id, Supplier newSupplier) {
	    newSupplier.setId(id);
	    
	    Session session = null;
	    Transaction transaction = null;
	    Supplier oldSupplier = null;
	    
	    try {
	        session = sessionFactory.openSession();
	        oldSupplier = session.find(Supplier.class, id);
	        
	        if (oldSupplier == null) {
	            System.out.printf("not found supplier id %d\n", id);
	            newSupplier = null;
	        } else {
	            transaction = session.beginTransaction();
	            session.merge(newSupplier);
	            transaction.commit();
	            System.out.printf("success update supplier %s\n", newSupplier);
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        System.out.printf("fail update supplier id %d\n", id);
	        newSupplier = null;
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    
	    return newSupplier;
	}
	
	@Override
	public Supplier deleteSupplier(int id) {
	    Session session = null;
	    Transaction transaction = null;
	    Supplier supplier = null;
	    
	    try {
	        session = sessionFactory.openSession();
	        supplier = session.find(Supplier.class, id);
	        if (supplier == null) {
	            System.out.printf("not found supplier id %d\n", id);
	        } else {
	            transaction = session.beginTransaction();
	            session.remove(supplier);
	            transaction.commit();
	            System.out.printf("success delete supplier %s\n", supplier);
	        }
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace();
	        System.out.printf("fail delete supplier id %d\n", id);
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    
	    return supplier;
	}
}
