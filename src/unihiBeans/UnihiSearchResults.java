package unihiBeans;

import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="uni")
@RequestScoped
public class UnihiSearchResults {

	private String[] proteins = new String[]{};
	
	private String[] proteinsGS = new String[]{};
	private String[] proteinsEntGID = new String[]{};
	
	private String[] proteinsName = new String[]{};
	private String[] proteinsIds = new String[]{};
	private UnihiProteinTable protTable = new UnihiProteinTable();
//	private UnihiPhysicalInteractions physTable = new UnihiPhysicalInteractions();
//	private UnihiRegulatoryInteractions reguTable = new UnihiRegulatoryInteractions();
	private GraphHandler ghand = new GraphHandler();

	public String getProteins() {
		String res = "";
		
		for(int i=0;i<this.proteins.length;i++)
		{
			if(i>0)
				res += "-<?>-";
			res += this.proteins[i]+","+this.proteinsGS[i]+","+this.proteinsEntGID[i];
		}
		
		return res;
	}
	
	public void setProteins(String proteins) {
		
		StringTokenizer st = new StringTokenizer(proteins, "-<?>-");
		
		this.proteins = new String[st.countTokens()];
		this.proteinsGS = new String[st.countTokens()];
		this.proteinsEntGID = new String[st.countTokens()];
		
		int z = 0;
		
//		System.out.println("Prot "+proteins);
		
		while(st.hasMoreTokens())
		{
			String temp = st.nextToken();
//			System.out.println("PS "+temp);
			
			String[] t2 = temp.split(",");
			
			this.proteins[z] = t2[0];
//			System.out.println("this.proteins[z] "+this.proteins[z]);
			this.proteinsGS[z] = t2[1];
//			System.out.println("this.proteinsGS[z] "+this.proteinsGS[z]);
			this.proteinsEntGID[z] = t2[2];
//			System.out.println("this.proteinsEntGID[z] "+this.proteinsEntGID[z]);
			z++;
		}
	}
	
	public String getProteinsIds() {
		String res = "";
		
		for(int i=0;i<this.proteinsIds.length;i++)
		{
			if(i>0)
				res += "-<?>-";
			res += this.proteinsIds[i];
		}
		
		return res;
	}

	public void setProteinsIds(String pds) {
		StringTokenizer st = new StringTokenizer(pds, "-<?>-");
		
		this.proteinsIds = new String[st.countTokens()];
		
		int z = 0;
		
		while(st.hasMoreTokens())
		{
			this.proteinsIds[z] = st.nextToken();
			z++;
		}
		
	}

	public UnihiProteinTable getProtTable()
	{
		return this.protTable;
	}
	
//	public UnihiPhysicalInteractions getPhysTable()
//	{
//		return physTable;
//	}
//	
//	public UnihiRegulatoryInteractions getReguTable()
//	{
//		return reguTable;
//	}

	public void getTable()
	{
		this.protTable.search(this.proteinsName);
		this.proteinsIds = new String[this.protTable.getPdto().length];
		
		for(int i=0;i<this.protTable.getPdto().length;i++)
		{
			this.proteinsIds[i] = this.protTable.getPdto()[i].getPrid().toString()+","+this.protTable.getPdto()[i].getGenesymbol()+","+this.protTable.getPdto()[i].getEntrezgeneid();
		}
	}

//	public void getPITable()
//	{
//		this.physTable.search(this.proteins, this.proteinsGS, this.proteinsEntGID);
//	}
//
//	public void getRITable()
//	{
//		this.reguTable.search(this.proteins, this.proteinsGS, this.proteinsEntGID);
//	}
	
	public void searchInteractions() {
		
	}
	
	public void setProteinsName(String proteins) {
		
		StringTokenizer st = new StringTokenizer(proteins, "-<?>-");
		
		this.proteinsName = new String[st.countTokens()];
		
		int z = 0;
		
		while(st.hasMoreTokens())
		{
			this.proteinsName[z] = st.nextToken();
			z++;
		}
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

	public GraphHandler getGhand() {
		return ghand;
	}
	
	public void gnow()
	{
		this.ghand.gnow();
	}
	
}
