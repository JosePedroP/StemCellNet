package unihiBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import databaseAccess.accessers.DatabaseAccess;

@ManagedBean(name="pInteractionsDetails")
@RequestScoped
public class PInteractionsDetails {

	private String piid;
	private PInteractedDetail pd = null;

	public String getPiid() {
		return piid;
	}

	public void setPiid(String piid) {
		this.piid = piid;
	}

	public PInteractedDetail getPd() {
		
		
		if(this.pd==null)
		{
			
			try {
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
				
//				InteractionDistributionHome idh = new InteractionDistributionHome();
				DatabaseAccess idh = new DatabaseAccess();
				
				try{
					
					this.pd=new PInteractedDetail(idh.findByOneInteraction(this.piid, se));
					
				} finally
				{
					se.close();
				}
				
			} catch(Exception e)
			{e.printStackTrace();
			this.pd = new PInteractedDetail();}
			
			
		}
		
		return pd;
	}

	public void setPd(PInteractedDetail pd) {
		this.pd = pd;
	}
	
}
