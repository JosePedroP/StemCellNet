package unihiBeans;

import databaseAccess.accessers.InteractionDistributionDTO;
import databaseAccess.accessers.InteractionPropertiesDTO;
import databaseAccess.accessers.ProteinDTO;

public class PInteracted {

	protected Integer id;
	protected String genesymbol;
	protected String description;
	protected String uniprotid;
	protected String hprdid;
	protected String dipid;
	protected Integer entrezgeneid;
	protected Integer biogridid;
	protected Integer geneinfoid;
	protected String unigeneid;
	protected String ensembleid;
	protected String refseqid;
	protected String hgncid;
	protected Integer omimid;
	private String[][] cofScore;
	private int mdc;
	private int vidal;
	private int vidal_y2h;
	private int hprd_binary;
	private int hprd_complex;
	private int bind;
	private int dip;
	private int reactome;
	private int ramani;
	private int fraser;
	private int homomit;
	private int ophid;
	private int biogrid;
	private int intact;
	private int transfac;
	private int mirtarbase;
	private int htridb;
	private int mESCHTP;
	private int mESChIP;
	
	
	public PInteracted() {
		this.genesymbol = "";
		this.description = "";
		this.uniprotid = "";
		this.hprdid = "";
		this.dipid = "";
		this.entrezgeneid = -1;
		this.geneinfoid = -1;
		this.mdc = 0;
		this.vidal = 0;
		this.vidal_y2h = 0;
		this.hprd_binary = 0;
		this.hprd_complex = 0;
		this.bind = 0;
		this.dip = 0;
		this.reactome = 0;
		this.ramani = 0;
		this.fraser = 0;
		this.homomit = 0;
		this.ophid = 0;
		this.biogrid = 0;
		this.intact = 0;
		this.transfac = 0;
		this.mirtarbase = 0;
		
	}
	
	
	
	public PInteracted(InteractionDistributionDTO ids, String primaryId) {
		ProteinDTO secprot;
		
		if(ids.getPridA().getPrid().intValue() == (new Integer(primaryId)).intValue())
			secprot = ids.getPridB();
		else
			secprot = ids.getPridA();
		
		this.id = ids.getIntdisid();
		this.genesymbol = secprot.getGenesymbol();
		this.description = secprot.getDescription();
		this.uniprotid = secprot.getUniprotid();
		this.hprdid = secprot.getHprdid();
		this.dipid = secprot.getDipid();
		this.entrezgeneid = secprot.getEntrezgeneid();
		this.geneinfoid = secprot.getGeneinfoid();
		this.unigeneid = secprot.getUnigeneid();
		this.cofScore = new String[][]{};
		this.mdc = 0;
		this.vidal = 0;
		this.vidal_y2h = 0;
		this.hprd_binary = 0;
		this.hprd_complex = 0;
		this.bind = 0;
		this.dip = 0;
		this.reactome = 0;
		this.ramani = 0;
		this.fraser = 0;
		this.homomit = 0;
		this.ophid = 0;
		this.biogrid = 0;
		this.intact = 0;
		this.transfac = 0;
		this.mirtarbase = 0;
		this.htridb = 0;
		this.mESCHTP = 0;
		this.mESChIP = 0;
		
		InteractionPropertiesDTO[]  ips = ids.getInteractionProperties();
		
		for(int i=0;i<ips.length;i++)
		{
			if(ips[i].getName()!=null)
			{
				String source = ips[i].getName();
			
				if(source.equals("MDC")) this.mdc = 1;
				else if(source.equals("Vidal")) this.vidal = 1;
				else if(source.equals("Vidal_Y2H")) this.vidal_y2h = 1;
				else if(source.equals("HPRD_Binary")) this.hprd_binary = 1;
				else if(source.equals("HPRD_Complex")) this.hprd_complex = 1;
				else if(source.equals("BIND")) this.bind = 1;
				else if(source.equals("DIP")) this.dip = 1;
				else if(source.equals("Reactome")) this.reactome = 1;
				else if(source.equals("Ramani")) this.ramani = 1;
				else if(source.equals("Fraser")) this.fraser = 1;
				else if(source.equals("HomoMint")) this.homomit = 1;
				else if(source.equals("ophid")) this.ophid = 1;
				else if(source.equals("BioGrid")) this.biogrid = 1;
				else if(source.equals("IntAct")) this.intact = 1;
				else if(source.equals("TRANSFAC")) this.transfac = 1;
				else if(source.equals("miRTarBase")) this.mirtarbase = 1;
				else if(source.equals("htridb")) this.htridb = 1;
				else if(source.equals("mESC-HTP")) this.mESCHTP = 1;
				else if(source.equals("mES-ChIP")) this.mESChIP = 1;
			}
			
		}
	}
	
	public PInteracted(InteractionDistributionDTO ids) {
		this.genesymbol = null;
		this.description = null;
		this.uniprotid = null;
		this.hprdid = null;
		this.dipid = null;
		this.entrezgeneid = null;
		this.geneinfoid = null;
		this.mdc = 0;
		this.vidal = 0;
		this.vidal_y2h = 0;
		this.hprd_binary = 0;
		this.hprd_complex = 0;
		this.bind = 0;
		this.dip = 0;
		this.reactome = 0;
		this.ramani = 0;
		this.fraser = 0;
		this.homomit = 0;
		this.ophid = 0;
		this.biogrid = 0;
		this.intact = 0;
		this.transfac = 0;
		this.mirtarbase = 0;
		
		InteractionPropertiesDTO[]  ips = ids.getInteractionProperties();
		
//		System.out.println(">>> "+ips.length);
		
		for(int i=0;i<ips.length;i++)
		{
//			System.out.println("1: "+i+": "+(ips[i]!=null));
//			System.out.println("2: "+i+": "+(ips[i].getSource()!=null));
//			System.out.println("3: "+i+": "+(ips[i].getSource()));
			
			if(ips[i].getName()!=null)
			{
				String source = ips[i].getName();
			
				if(source.equals("MDC")) this.mdc = 1;
				else if(source.equals("Vidal")) this.vidal = 1;
				else if(source.equals("Vidal_Y2H")) this.vidal_y2h = 1;
				else if(source.equals("HPRD_Binary")) this.hprd_binary = 1;
				else if(source.equals("HPRD_Complex")) this.hprd_complex = 1;
				else if(source.equals("BIND")) this.bind = 1;
				else if(source.equals("DIP")) this.dip = 1;
				else if(source.equals("Reactome")) this.reactome = 1;
				else if(source.equals("Ramani")) this.ramani = 1;
				else if(source.equals("Fraser")) this.fraser = 1;
				else if(source.equals("HomoMint")) this.homomit = 1;
				else if(source.equals("ophid")) this.ophid = 1;
				else if(source.equals("BioGrid")) this.biogrid = 1;
				else if(source.equals("IntAct")) this.intact = 1;
				else if(source.equals("TRANSFAC")) this.transfac = 1;
				else if(source.equals("miRTarBase")) this.mirtarbase = 1;
				else if(source.equals("htridb")) this.htridb = 1;
				else if(source.equals("mESC-HTP")) this.mESCHTP = 1;
				else if(source.equals("mES-ChIP")) this.mESChIP = 1;
			}
		}
	}

	public String getGenesymbol() {
		return genesymbol;
	}

	public void setGenesymbol(String genesymbol) {
		this.genesymbol = genesymbol;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMdc() {
		return mdc;
	}

	public int getVidal() {
		return vidal;
	}

	public int getVidal_y2h() {
		return vidal_y2h;
	}

	public int getHprd_binary() {
		return hprd_binary;
	}

	public int getHprd_complex() {
		return hprd_complex;
	}

	public int getBind() {
		return bind;
	}

	public int getDip() {
		return dip;
	}

	public int getReactome() {
		return reactome;
	}

	public int getRamani() {
		return ramani;
	}

	public int getFraser() {
		return fraser;
	}

	public int getHomomit() {
		if((this.uniprotid==null || this.uniprotid.equals("-1")) && this.homomit==1) return 2;
		else return homomit;
	}

	public int getOphid() {
		return ophid;
	}

	public int getBiogrid() {
		return biogrid;
	}

	public int getIntact() {
		return intact;
	}

	public int getTransfac() {
		return transfac;
	}

	public int getMirtarbase() {
		return mirtarbase;
	}

	public String getUniprotid() {
		return uniprotid;
	}

	public String getHprdid() {
		return hprdid;
	}
	public String getDipid() {
		return dipid;
	}
	public Integer getEntrezgeneid() {
		if(this.entrezgeneid==null || this.entrezgeneid.intValue()==0) return -1;
		return entrezgeneid;
	}
	public Integer getBiogridid() {
		return biogridid;
	}
	public Integer getGeneinfoid() {
		if(this.geneinfoid==null || this.geneinfoid.intValue()==0) return -1;
		return geneinfoid;
	}
	
	

	public String getGeneinfoidS() {
		if(this.geneinfoid==null || this.geneinfoid.intValue()==0 || this.geneinfoid.intValue()==-1) return "";
		return geneinfoid.toString();
	}

	public Integer getId() {
		return id;
	}

	public String[][] getCofScore() {
		return cofScore;
	}

	public void setCofScore(String[][] cofScore) {
		this.cofScore = cofScore;
	}

	public String getUnigeneid() {
		return unigeneid;
	}

	public String getEnsembleid() {
		return ensembleid;
	}

	public String getRefseqid() {
		return refseqid;
	}

	public String getHgncid() {
		return hgncid;
	}

	public Integer getOmimid() {
		return omimid;
	}

	public String getOmimidS() {
		if(this.omimid==null || this.omimid.intValue()==0 || this.omimid.intValue()==-1) return "";
		return omimid.toString();
	}

	public int getmESCHTP() {
		return mESCHTP;
	}

	public void setmESCHTP(int mESCHTP) {
		this.mESCHTP = mESCHTP;
	}

	public int getmESChIP() {
		return mESChIP;
	}

	public void setmESChIP(int mESChIP) {
		this.mESChIP = mESChIP;
	}

	public int getHtridb() {
		return htridb;
	}

	public void setHtridb(int htridb) {
		this.htridb = htridb;
	}
	
}
