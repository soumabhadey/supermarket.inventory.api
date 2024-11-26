package supermarket.inventory.api.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseConnection implements AutoCloseable {
	private SessionFactory sessionFactory;

	public DatabaseConnection() {
		sessionFactory = generateSessionFactory();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	private static SessionFactory generateSessionFactory() {
		SessionFactory sessionFactory = null;
		
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			System.out.printf("Session Factory created successfully.\n");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.printf("%s\n", e.getMessage());
			System.out.printf("Session Factory creation unsusccessful.\n");
		}
		
		return sessionFactory;
	}
	
	@Override
	public void close() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
			System.out.printf("Session Factory closed successfully.\n");
		}
	}
}
