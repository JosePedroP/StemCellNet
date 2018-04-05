package unihiBeans;

import databaseAccess.accessers.InteractionDistributionDTO;
import databaseAccess.accessers.ProteinDTO;

public class PInteractedDetail extends PInteracted{
	
	private String genesymbol2;
	private String description2;
	private String uniprotid2;
	private String hprdid2;
	private String dipid2;
	private Integer entrezgeneid2;
	private Integer biogridid2;
	private Integer geneinfoid2;
	private String unigeneid2;
	private String ensembleid2;
	private String refseqid2;
	protected String hgncid2;
	protected Integer omimid2;
	private String[] cofScore2;
	private String[][] si;
	private String[][] goanotations1;
	private String[][] goanotations2;
	
	
	public PInteractedDetail()
	{
		super();
		
		super.genesymbol = "";
		super.description = "";
		super.uniprotid = "";
		super.hprdid = "";
		super.dipid = "";
		super.entrezgeneid = -1;
		super.geneinfoid = -1;
		super.unigeneid = "";
		super.ensembleid = "";
		super.refseqid = "";
		super.hgncid = "";
		super.omimid = -1;

		this.genesymbol2 = "";
		this.description2 = "";
		this.uniprotid2 = "";
		this.hprdid2 = "";
		this.dipid2 = "";
		this.entrezgeneid2 = -1;
		this.geneinfoid2 = -1;
		this.unigeneid2 = "";
		this.ensembleid2 = "";
		this.refseqid2 = "";
		this.hgncid2 = "";
		this.omimid2 = -1;
		
		this.goanotations1 = new String[][]{};
		this.goanotations2 = new String[][]{};
		
		this.cofScore2 = new String[]{};
		
		super.setCofScore(new String[][]{});
	}
	
	
	public PInteractedDetail(InteractionDistributionDTO ids)
	{
		super(ids);
		
		ProteinDTO protA = ids.getPridA();
		ProteinDTO protB = ids.getPridB();
		
		super.genesymbol = protA.getGenesymbol();
		super.description = protA.getDescription();
		super.uniprotid = protA.getUniprotid();
		super.hprdid = protA.getHprdid();
		super.dipid = protA.getDipid();
		super.entrezgeneid = protA.getEntrezgeneid();
		super.geneinfoid = protA.getGeneinfoid();
		super.unigeneid = protA.getUnigeneid();
		super.ensembleid = protA.getEnsembleid();
		super.refseqid = protA.getEnsembleid();
		super.hgncid = protA.getHgncid();
		super.omimid = protA.getOmimid();

		this.genesymbol2 = protB.getGenesymbol();
		this.description2 = protB.getDescription();
		this.uniprotid2 = protB.getUniprotid();
		this.hprdid2 = protB.getHprdid();
		this.dipid2 = protB.getDipid();
		this.entrezgeneid2 = protB.getEntrezgeneid();
		this.geneinfoid2 = protB.getGeneinfoid();
		this.unigeneid2 = protB.getUnigeneid();
		this.ensembleid2 = protB.getUnigeneid();
		this.refseqid2 = protB.getEnsembleid();
		this.hgncid2 = protB.getHgncid();
		this.omimid2 = protB.getOmimid();
		
		this.goanotations1 = protA.getGoanotations();
		this.goanotations2 = protB.getGoanotations();
		
		String[][] tcofScore = ids.getCofScore();
		
		String[][] cofScore1 = new String[][]{{"Spearman Correlation", "NaN"}, {"Quantile","NaN"}};
		
		this.cofScore2 = new String[]{"splMF", "splBP", "splCC", "mfQ", "bpQ", "ccQ"};
		
		for(int i=0;i<tcofScore.length;i++)
		{
			if(tcofScore[i][0].equals("corSpear")) cofScore1[0][1] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("corQuantile")) cofScore1[1][1] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("splMF")) cofScore2[0] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("splBP")) cofScore2[1] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("splCC")) cofScore2[2] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("mfQ")) cofScore2[3] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("bpQ")) cofScore2[4] = tcofScore[i][1];
			else if(tcofScore[i][0].equals("ccQ")) cofScore2[5] = tcofScore[i][1];
		}
		
		this.si = ids.getSi();
		
		super.setCofScore(cofScore1);
	}

	public String getGenesymbol2() {
		return genesymbol2;
	}

	public String getDescription2() {
		return description2;
	}

	public String getUniprotid2() {
		return uniprotid2;
	}
	
	public String getHprdid2() {
		return hprdid2;
	}

	public String getDipid2() {
		return dipid2;
	}

	public Integer getEntrezgeneid2() {
		return entrezgeneid2;
	}

	public Integer getBiogridid2() {
		return biogridid2;
	}

	public Integer getGeneinfoid2() {
		return geneinfoid2;
	}
	
	public String getGeneinfoid2S() {
		if(this.geneinfoid2==null || this.geneinfoid2.intValue()==0 || this.geneinfoid2.intValue()==-1) return "";
		return geneinfoid2.toString();
	}

	public String[] getCofScore2() {
		return cofScore2;
	}

	public void setCofScore2(String[] cofScore2) {
		this.cofScore2 = cofScore2;
	}

	public String[][] getSi() {
		return si;
	}

	public void setSi(String[][] si) {
		this.si = si;
	}

	public String getUnigeneid2() {
		return unigeneid2;
	}

	public String getEnsembleid2() {
		return ensembleid2;
	}

	public String getRefseqid2() {
		return refseqid2;
	}

	public String getHgncid2() {
		return hgncid2;
	}

	public Integer getOmimid2() {
		return omimid2;
	}
	
	public String getOmimid2S() {
		if(this.omimid2==null || this.omimid2.intValue()==0 || this.omimid2.intValue()==-1) return "";
		return omimid2.toString();
	}

	public String[][] getGoanotations1() {
		return goanotations1;
	}

	public String[][] getGoanotations2() {
		return goanotations2;
	}
	
}

//pInteractionsDetails.jsf?piid=6789