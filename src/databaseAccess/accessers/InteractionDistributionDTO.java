package databaseAccess.accessers;

import java.util.ArrayList;

import databaseAccess.Goannotation;
import databaseAccess.Interaction;
import databaseAccess.Screen;

public class InteractionDistributionDTO {

	private Integer intdisid;
	private ProteinDTO pridA;
	private ProteinDTO pridB;
	private Integer score;
	private Integer numofsource;
	private InteractionPropertiesDTO[] interactionProperties;
	private String[][] cofScore;
	private String[][] si;
	private int siNumber;
	private String regulatory;
	
	public InteractionDistributionDTO(Interaction id) {
		this.intdisid = id.getId();
		this.pridA = new ProteinDTO(id.getProteinsByPridA());
		this.pridB = new ProteinDTO(id.getProteinsByPridB());
		this.score = id.getPubmedrefs().size();
		this.numofsource = id.getMethods().size();
		
		Object[] arr = id.getProteinsByPridA().getGoannotations().toArray();
		
		String[][] gos = new String[arr.length][3];
		
		for(int v=0;v<arr.length;v++)
		{
			Goannotation g = (Goannotation)arr[v];
			
			gos[v][0] = g.getCategory();
			gos[v][1] = g.getTitle();
			gos[v][2] = g.getGoid();
		}
		
		this.pridA.setGoanotations(gos);
		
		arr = id.getProteinsByPridB().getGoannotations().toArray();
		
		gos = new String[arr.length][3];
		
		for(int v=0;v<arr.length;v++)
		{
			Goannotation g = (Goannotation)arr[v];
			
			gos[v][0] = g.getCategory();
			gos[v][1] = g.getTitle();
			gos[v][2] = g.getGoid();
		}
		
		this.pridB.setGoanotations(gos);
		
		this.siNumber = -1;
		
		this.regulatory = id.getRegulatory();
		
	}
	
	public InteractionDistributionDTO(Interaction id, boolean screendata) {
		this(id);
		
		if(screendata)
		{
			Object[] arr = id.getProteinsByPridA().getScreens().toArray();
			
			ArrayList<String> temp = new ArrayList<String>();
			
			for(int v=0;v<arr.length;v++)
			{
				Screen sc = (Screen)arr[v];
				
				String sor = sc.getSource();
				
				if(!temp.contains(sor))
					temp.add(sor);
			}
			
			this.pridA.setSources(temp.toArray(new String[]{}));
			
			
			arr = id.getProteinsByPridB().getScreens().toArray();
			
			temp = new ArrayList<String>();
			
			for(int v=0;v<arr.length;v++)
			{
				Screen sc = (Screen)arr[v];
				
				String sor = sc.getSource();
				
				if(!temp.contains(sor))
					temp.add(sor);
			}
			
			this.pridB.setSources(temp.toArray(new String[]{}));
		}
		
	}

	public Integer getIntdisid() {
		return intdisid;
	}

	public ProteinDTO getPridA() {
		return pridA;
	}

	public ProteinDTO getPridB() {
		return pridB;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getNumofsource() {
		return numofsource;
	}

	public InteractionPropertiesDTO[] getInteractionProperties() {
		return interactionProperties;
	}

	public void setInteractionProperties(
			InteractionPropertiesDTO[] interactionProperties) {
		this.interactionProperties = interactionProperties;
	}

	public String[][] getCofScore() {
		return cofScore;
	}

	public void setCofScore(String[][] cofScore) {
		this.cofScore = cofScore;
	}

	public String[][] getSi() {
		return si;
	}

	public void setSi(String[][] si) {
		this.si = si;
	}

	public int getSiNumber() {
		return siNumber;
	}

	public void setSiNumber(int siNumber) {
		this.siNumber = siNumber;
	}

	public String getRegulatory() {
		return regulatory;
	}
	
}
