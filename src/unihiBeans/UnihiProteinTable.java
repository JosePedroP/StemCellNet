package unihiBeans;

import javax.naming.InitialContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import databaseAccess.accessers.DatabaseAccess;
import databaseAccess.accessers.ProteinDTO;

public class UnihiProteinTable {

	protected ProteinDTO[] pdto = new ProteinDTO[]{};
	
//	public UnihiProteinTable(String[] proteins) {
//		try {
//			super.startSession();
//		
//			ProteinsHome ph = new ProteinsHome();
//		
//			this.pdto = ph.multiQuerry(proteins);
//			
//			super.endSession();
//		} catch(Exception e)
//		{e.printStackTrace();}
//	}

	public void search(String[] proteins)
	{
		try {
			
			SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
			Session se = ob.openSession();
			
			DatabaseAccess ph = new DatabaseAccess();
			
			try{
				
				this.pdto = ph.multiQuerry(proteins, se);
				
			} finally
			{
				se.close();
			}
			
		} catch(Exception e)
		{e.printStackTrace();
		this.pdto = new ProteinDTO[]{};}
	}
	
	public ProteinDTO[] getPdto() {
		return pdto;
	}
}
