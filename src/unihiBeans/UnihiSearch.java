package unihiBeans;

import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import databaseAccess.accessers.DatabaseAccess;
import databaseAccess.accessers.ProteinDTO;

@ManagedBean
@RequestScoped
public class UnihiSearch {

	protected String searchData = "";
//	protected String[] organisms = new String[]{"HUMAN", "YEAST", "WORM", "FLY", "MOUSE"};
	protected String[] organisms = new String[]{"HUMAN"};
	protected String organism = "HUMAN";
	protected String[] proteins = new String[]{};
	protected String[] proteinsName = new String[]{};
	protected String[] proteinsEid = new String[]{};
	protected ProteinDTO[] pdto;
	
	public ProteinDTO[] getPdto() {
		return pdto;
	}
	
	public String getProteinsName() {
		String res = "";
		
		for(int i=0;i<this.proteinsName.length;i++)
		{
			if(i>0)
				res += "-<?>-";
			res += this.proteinsName[i];
		}
		
		return res;
	}

	public void setProteinsName(String proteins) {
		
		StringTokenizer st = new StringTokenizer(proteins, "-<?>-");
		
		this.proteinsEid = new String[st.countTokens()];
		
		int z = 0;
		
		while(st.hasMoreTokens())
		{
			this.proteinsEid[z] = st.nextToken();
			z++;
		}
	}

	public String getProteinsEid() {
		String res = "";
		
		for(int i=0;i<this.proteinsEid.length;i++)
		{
			if(i>0)
				res += "-<?>-";
			res += this.proteinsEid[i];
		}
		
		return res;
	}

	public void setProteinsEid(String proteins) {
		
		StringTokenizer st = new StringTokenizer(proteins, "-<?>-");
		
		this.proteinsName = new String[st.countTokens()];
		
		int z = 0;
		
		while(st.hasMoreTokens())
		{
			this.proteinsName[z] = st.nextToken();
			z++;
		}
	}

	public void setProteins(String i) {
		
		StringTokenizer st = new StringTokenizer(i, "-<?>-");
		this.proteins = new String[st.countTokens()];
		
		int u = 0;
		while(st.hasMoreTokens())
		{
			this.proteins[u] = st.nextToken();
			u++;
		}
		
	}
	
	public String getProteins() {
		
		String res = "";
		
		if(this.proteins==null) return "";
		
		for(int i=0;i<this.proteins.length;i++)
		{
			if(i>0)
				res += "-<?>-";
			res += this.proteins[i];
		}
		
		return res;
	}

	public String getSearchData() {
		return searchData;
	}

	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}

	public String[] getOrganisms() {
		return organisms;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}
	
//	public String searchAction(){
//		
//		String[] proteins = this.breakQuerry(this.searchData);
//		
//		try {
//			super.startSession();
//			
//			ProteinsHome ph = new ProteinsHome();
//			
//			this.pdto = ph.multiQuerry(proteins);
//			
//			super.endSession();
//
//			this.proteins = new String[this.pdto.length];
//			this.proteinsName = new String[this.pdto.length];
//			this.proteinsEid = new String[this.pdto.length];
//			
//			for(int i=0;i<this.proteins.length;i++)
//			{
//				this.proteins[i] = this.pdto[i].getPrid().toString();
//				this.proteinsName[i] = this.pdto[i].getGenesymbol();
//				this.proteinsEid[i] = this.pdto[i].getEntrezgeneid().toString();
//			}
//			
//		} catch(Exception e)
//		{e.printStackTrace();}
//
//		
//		return "unihiSearchResult?faces-redirect=true&weseeyou=1";
//	}
	
	public String searchAction(){
		
		String[] proteins = this.breakQuerry(this.searchData);
		
		String isOne = "";
		
		int z = 1;
		
		if(proteins.length==0) return null;
		
		try {
			
			String[] prots = new String[]{};
			
			
			
			DatabaseAccess ph = new DatabaseAccess();
			
			SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
			Session se = ob.openSession();

			try{
				
				prots = ph.basicMultiQuerry(proteins, se);
				
			} finally
			{
				se.close();
			}
			
			
			
			
			for(int i=0;i<prots.length;i++)
			{
				if(i>0) isOne += "&";
				isOne += "prot"+z+"="+URLEncoder.encode(prots[i], "UTF-8"); //probably unnecessery but just in case
				z++;
			}
			
//			isOne = URLEncoder.encode(isOne, "UTF-8");
		} catch(Exception e)
		{e.printStackTrace();}

		
		
		if(isOne.equals("")) return "stemCellNetNotFound";
		else return "stemCellNetNetworkResult?faces-redirect=true&"+isOne;
//		else return "unihiSearchResult?faces-redirect=true&"+isOne;
	}
	
	protected String[] breakQuerry(String querry)
	{
		StringTokenizer st = new StringTokenizer(querry, " \t\n\r\f,");
		String[] res = new String[st.countTokens()];
		
		int u = 0;
		while(st.hasMoreTokens())
		{
			res[u] = st.nextToken();
			u++;
		}
		
		return res;
	}
	
}
