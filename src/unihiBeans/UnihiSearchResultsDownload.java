package unihiBeans;

import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import databaseAccess.accessers.DatabaseAccess;

@ManagedBean(name="dow")
@RequestScoped
public class UnihiSearchResultsDownload {

	private String[] extraData = new String[]{};

	public String getExtraData() {
		String res = "";
		
		for(int i=0;i<this.extraData.length;i++)
		{
			if(i>0)
				res += "-<>-"+this.extraData[i];
			else res = this.extraData[i];
		}
		
		return res;
	}

	public void setExtraData(String extraData) {
		
		this.extraData = extraData.split("-<>-");
		
	}
	
	public String getText()
	{
		try {
			String res = "Entrez gene id A\tGene symbol A\tEntrez gene id B\tGene symbol B\tSources\tPubmed Ids\tType\n";
			
//			System.out.println("I can not believe.");
			
//			System.out.println("1");
			
			if(this.extraData.length>0)
			{
				Integer[] da = new Integer[this.extraData.length];
				
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
				
//				System.out.println("1.1");
				
				String qtext = "";
				
				
				DatabaseAccess p = new DatabaseAccess();
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
				
				try{
					
					qtext = p.findByNodeIds_reduced(da, se);
					
				} 
				catch(Exception e)
				{
					qtext = "";
				}
				finally
				{
					se.close();
				}
				
//				System.out.println("1.2");
				
//				System.out.println("I've never felt so free.");
				
				
				res += qtext;

				
//				System.out.println("1.3");
				
//				System.out.println("This is an incredible story about me");
				
			}
			
			byte[] file = res.getBytes();
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("text/plain");
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"network.txt\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}

		
		return null;
	}
	
	public String getXGMML()
	{
		try {
			String res = "";
			
//			System.out.println("I can not believe.");
			
//			System.out.println("1");
			
			if(this.extraData.length>0)
			{
				Integer[] da = new Integer[this.extraData.length];
				
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
				
//				System.out.println("1.1");
				
				DatabaseAccess p = new DatabaseAccess();
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
				
				try{
					res = XGMMLMaker.getXGMML(p.findByNodeIds(da, se));
				} 
				catch(Exception e)
				{
					res = "";
				}
				finally
				{
					se.close();
				}
				
			}
			
			byte[] file = res.getBytes();
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("text/plain");
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"network.xgmml\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}

		
		return null;
	}
}
