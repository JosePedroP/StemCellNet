package unihiBeans;

import javax.naming.InitialContext;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//
//import databaseAccess.InteractionDistributionDTO;
//import databaseAccess.InteractionDistributionHome;

public class UnihiRegulatoryInteractions {
/*
	private Prot[] prots = new Prot[]{};

	public Prot[] getProts() {
		return prots;
	}
	
	public void search(String[] proteins, String[] proteinsGS, String[] proteinsEntGID)
	{
		this.prots = new Prot[proteins.length];
		
		
		try{

			SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
			Session se = ob.openSession();
			
			try{
				
				for(int i=0;i<proteins.length;i++)
				{
					this.prots[i] = new Prot();
					this.prots[i].setProteins(proteins[i]);
					this.prots[i].setProteinsName(proteinsGS[i]);
					this.prots[i].setProteinsEid(proteinsEntGID[i]);
					
					
					InteractionDistributionDTO[] ids = new InteractionDistributionDTO[]{};
					InteractionDistributionHome idh = new InteractionDistributionHome();
					
					ids = idh.findByRegProteinId(proteins[i]);
							
					this.prots[i].setIds(ids);
					
				}
				
			} finally
			{
				se.close();
			}
			
		} catch(Exception e)
		{e.printStackTrace(); this.prots=new Prot[]{};}
		
		
		
	}
	*/
}
