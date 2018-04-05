package unihiBeans;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.naming.InitialContext;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

@ManagedBean(eager=true)
@ApplicationScoped
public class StartUp {
	
	public StartUp()
	{	
		super();
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			configuration.buildSessionFactory(serviceRegistry);
			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			new InitialContext().rebind("SessionFactory", sessionFactory);
		} catch(Exception e)
		{e.printStackTrace();}
	}
	
}
