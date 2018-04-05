package unihiBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import databaseAccess.accessers.DatabaseAccess;
import databaseAccess.accessers.ProteinDTO;

@ManagedBean(name="proteinsDetails")
@RequestScoped
public class ProteinsDetails {

	private String prid;
	private String prid2;
	private ProteinDTO prot = null;

	public String getPrid() {
		return prid;
	}

	public void setPrid(String prid) {
		this.prid = prid;
	}
	
	public String getPrid2() {
		return prid2;
	}

	public void setPrid2(String prid2) {
		this.prid2 = prid2;
	}

	public ProteinDTO getProt() {
		
//		System.out.print("1 ");
//		System.out.println(prid==null);
//		System.out.print("2 ");
//		System.out.println(prid2==null);
		
		if(this.prot==null)
		{
			this.prot = new ProteinDTO();
			
			if(prid!=null)
			{
				try {

					DatabaseAccess ph = new DatabaseAccess();
					
					SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
					Session se = ob.openSession();
					
					try{
						
						this.prot = ph.getOneProt(this.prid, se);
						
					} finally
					{
						se.close();
					}
					
				} catch(Exception e)
				{e.printStackTrace();}
				
		
				return prot;
			}
			else 
			{
				try {

					DatabaseAccess ph = new DatabaseAccess();
					
					SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
					Session se = ob.openSession();
					
					try{
						
						this.prot = ph.getOneProtWithDBID(this.prid2, se);
						
					} finally
					{
						se.close();
					}
					
				} catch(Exception e)
				{e.printStackTrace();}
				
				return prot;
			}
		}
		else return prot;
	}

	public void setProt(ProteinDTO prot) {
		this.prot = prot;
	}
	
}
