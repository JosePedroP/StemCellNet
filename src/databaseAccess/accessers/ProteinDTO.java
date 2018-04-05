package databaseAccess.accessers;

import java.util.ArrayList;

import databaseAccess.Proteins;
import databaseAccess.Proteinsalias;

public class ProteinDTO {

	private Integer prid;
//	private Humanexp humanexp;
	private Integer entrezgeneid;
	private String genesymbol;
	private String uniprotid;
	private Integer geneinfoid;
	private String ensembleid;
	private String unigeneid;
	private String hprdid;
	private Integer omimid;
	private String dipid;
	private String refseqid;
	private String description;
	private Integer score;
	private Integer biogridid;
	private String alias;
	private String hgncid;
	private String transfacid;
	private String intactid;
//	private AltSpeciesDTO[] aliases;
	private String[] synonyms;
	private PathwaydistributionDTO[] paths = new PathwaydistributionDTO[]{};
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
	private String[][] goanotations;
	private String[][] drugs;
	private String[] sources;
	
	public ProteinDTO() {
		this.prid = -1;
//		this.humanexp = null;
		this.entrezgeneid = -1;
		this.genesymbol = "";
		
		this.uniprotid = "";
		this.geneinfoid = -1;
		this.ensembleid = "";
		this.unigeneid = "";
		this.hprdid = "";
		this.omimid = -1;
		this.dipid = "";
		this.refseqid = "";
		
		this.description = "";
		this.score = -1;
		
		this.biogridid = -1;
		this.alias = "";
		this.hgncid = "";
		this.transfacid = "";
		
		this.intactid = "";
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
		this.goanotations = new String[][]{{}};
		this.drugs = new String[][]{{}};
		this.sources = new String[]{};
	}
	
	public ProteinDTO(Proteins p) {
		this.prid = p.getPrid();
//		this.humanexp = p.getHumanexp();
		this.entrezgeneid = new Integer(p.getEntrezgeneid());
		this.genesymbol = p.getGenesymbol();
		
		
		
		this.uniprotid = "";
		this.geneinfoid = -1;
		this.ensembleid = "";
		this.unigeneid = "";
		this.hprdid = "";
		this.omimid = -1;
		this.dipid = "";
		this.refseqid = "";
		
		this.biogridid = -1;
		this.alias = "";
		this.hgncid = "";
		this.transfacid = "";
		
		
		Object[] arr = p.getProteinsaliases().toArray();
		
		
		for(int i=0;i<arr.length;i++)
		{
			Proteinsalias pa = (Proteinsalias)arr[i];
			
			String type = pa.getType();
			
			if(type.equals("uniprotid")) this.uniprotid = pa.getAlias();
			else if(type.equals("geneinfoid")) this.geneinfoid = new Integer(pa.getAlias());
			else if(type.equals("unigeneid")) this.unigeneid = pa.getAlias();
			else if(type.equals("ensembleid")) this.ensembleid = pa.getAlias();
			else if(type.equals("hprdid")) this.hprdid = pa.getAlias();
			else if(type.equals("omimid")) this.omimid = new Integer(pa.getAlias());
			else if(type.equals("refseqid")) this.refseqid = pa.getAlias();
			else if(type.equals("biogridid")) this.biogridid = new Integer(pa.getAlias());
			else if(type.equals("stemcellnet_alias")) this.alias = pa.getAlias();
			else if(type.equals("hgncid")) this.hgncid = pa.getAlias();
			else if(type.equals("dipid")) this.dipid = pa.getAlias();
			else if(type.equals("transfacid")) this.transfacid = pa.getAlias();

		}
		
		
//		this.uniprotid = p.getUniprotid();
//		this.geneinfoid = p.getGeneinfoid();
//		this.ensembleid = p.getEnsembleid();
//		this.unigeneid = p.getUnigeneid();
//		this.hprdid = p.getHprdid();
//		this.omimid = p.getOmimid();
//		this.dipid = p.getDipid();
//		this.refseqid = p.getRefseqid();
		this.description = p.getDescription();
//		this.score = p.getScore();
//		this.biogridid = p.getBiogridid();
//		this.alias = p.getAlias();
//		this.hgncid = p.getHgncid();
//		this.transfacid = p.getTransfacid();
//		this.intactid = p.getIntactid();
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
		this.goanotations = new String[][]{{}};
		this.drugs = new String[][]{{}};
		this.sources = new String[]{};
	}

	public Integer getPrid() {
		return prid;
	}
	public void setPrid(Integer prid) {
		this.prid = prid;
	}
//	public Humanexp getHumanexp() {
//		return humanexp;
//	}
//	public void setHumanexp(Humanexp humanexp) {
//		this.humanexp = humanexp;
//	}
	public Integer getEntrezgeneid() {
		if(this.entrezgeneid==null || this.entrezgeneid.intValue()==0) return -1;
		return entrezgeneid;
	}
	public void setEntrezgeneid(Integer entrezgeneid) {
		this.entrezgeneid = entrezgeneid;
	}
	public String getGenesymbol() {
		return genesymbol;
	}
	public void setGenesymbol(String genesymbol) {
		this.genesymbol = genesymbol;
	}
	public String getUniprotid() {
		if(this.uniprotid==null) return "-1";
		return uniprotid;
	}
	public void setUniprotid(String uniprotid) {
		this.uniprotid = uniprotid;
	}
	public Integer getGeneinfoid() {
		if(this.geneinfoid==null || this.geneinfoid.intValue()==0) return -1;
		return geneinfoid;
	}
	public void setGeneinfoid(Integer geneinfoid) {
		this.geneinfoid = geneinfoid;
	}
	public String getEnsembleid() {
		if(this.ensembleid==null) return "-1";
		return ensembleid;
	}
	public void setEnsembleid(String ensembleid) {
		this.ensembleid = ensembleid;
	}
	public String getUnigeneid() {
		if(this.unigeneid==null) return "-1";
		return unigeneid;
	}
	public void setUnigeneid(String unigeneid) {
		this.unigeneid = unigeneid;
	}
	public String getHprdid() {
		return hprdid;
	}
	public void setHprdid(String hprdid) {
		this.hprdid = hprdid;
	}
	public Integer getOmimid() {
		if(this.omimid==null || this.omimid.intValue()==0) return -1;
		return omimid;
	}
	public void setOmimid(Integer omimid) {
		this.omimid = omimid;
	}
	public String getDipid() {
		return dipid;
	}
	public void setDipid(String dipid) {
		this.dipid = dipid;
	}
	public String getRefseqid() {
		if(this.refseqid==null) return "-1";
		return refseqid;
	}
	public void setRefseqid(String refseqid) {
		this.refseqid = refseqid;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getBiogridid() {
		return biogridid;
	}
	public void setBiogridid(Integer biogridid) {
		this.biogridid = biogridid;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getHgncid() {
		return hgncid;
	}
	public void setHgncid(String hgncid) {
		this.hgncid = hgncid;
	}
	public String getTransfacid() {
		return transfacid;
	}
	public void setTransfacid(String transfacid) {
		this.transfacid = transfacid;
	}
	public String getIntactid() {
		return intactid;
	}
	public void setIntactid(String intactid) {
		this.intactid = intactid;
	}
//	public AltSpeciesDTO[] getAliases() {
//		return aliases;
//	}
//	public void setAliases(AltSpeciesDTO[] aliases) {
//		this.aliases = aliases;
//	}
	public PathwaydistributionDTO[] getPaths() {
		return paths;
	}
	public void setPaths(PathwaydistributionDTO[] paths) {
		this.paths = paths;
	}
	public int getPathssize() {
		return this.paths.length;
	}
	
	public String getSynonyms() {
		
		String res;
		
		if(this.synonyms== null || this.synonyms.length==0) res = "None";
		else
		{
			res = this.synonyms[0];
			
			for(int i=1;i<this.synonyms.length;i++)
				res += ", "+this.synonyms[i];
		}
		
		return res;
	}
	public void setSynonyms(String[] synonyms) {
		this.synonyms = synonyms;
	}
	public void setSynonyms(ArrayList<String> synonyms) {
		this.synonyms = new String[synonyms.size()];
		
		for(int i=0;i<synonyms.size();i++) this.synonyms[i] = synonyms.get(i);
		
	}

	public int getMdc() {
		return mdc;
	}

	public void setMdc(int mdc) {
		this.mdc = mdc;
	}

	public int getVidal() {
		return vidal;
	}

	public void setVidal(int vidal) {
		this.vidal = vidal;
	}

	public int getVidal_y2h() {
		return vidal_y2h;
	}

	public void setVidal_y2h(int vidal_y2h) {
		this.vidal_y2h = vidal_y2h;
	}

	public int getHprd_binary() {
		return hprd_binary;
	}

	public void setHprd_binary(int hprd_binary) {
		this.hprd_binary = hprd_binary;
	}

	public int getHprd_complex() {
		return hprd_complex;
	}

	public void setHprd_complex(int hprd_complex) {
		this.hprd_complex = hprd_complex;
	}

	public int getBind() {
		return bind;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}

	public int getDip() {
		return dip;
	}

	public void setDip(int dip) {
		this.dip = dip;
	}

	public int getReactome() {
		return reactome;
	}

	public void setReactome(int reactome) {
		this.reactome = reactome;
	}

	public int getRamani() {
		return ramani;
	}

	public void setRamani(int ramani) {
		this.ramani = ramani;
	}

	public int getFraser() {
		return fraser;
	}

	public void setFraser(int fraser) {
		this.fraser = fraser;
	}

	
	
	
	
	
	/*public int getHomomit() {
		
		int res = -40;
		
		if(this.uniprotid.equals("-1") && this.homomit==1) res = 2;
		else res = homomit;
		
		System.out.println("GAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+res);
		
		return res;
	}

	public void setHomomit(int homomit) {
		this.homomit = homomit;
	}*/
	
	
	

	public int getHomomit() {
		int res = homomit;
		
		if(this.uniprotid!=null && this.uniprotid.equals("-1") && this.homomit==1) res = 2;
		
		return res;
	}

	public void setHomomit(int homomit) {
		this.homomit = homomit;
	}

	public int getOphid() {
		return ophid;
	}

	public void setOphid(int ophid) {
		this.ophid = ophid;
	}

	public int getBiogrid() {
		return biogrid;
	}

	public void setBiogrid(int biogrid) {
		this.biogrid = biogrid;
	}

	public int getIntact() {
		return intact;
	}

	public void setIntact(int intact) {
		this.intact = intact;
	}

	public int getTransfac() {
		return transfac;
	}

	public void setTransfac(int transfac) {
		this.transfac = transfac;
	}

	public int getMirtarbase() {
		return mirtarbase;
	}

	public void setMirtarbase(int mirtarbase) {
		this.mirtarbase = mirtarbase;
	}

	public String[][] getGoanotations() {
		return goanotations;
	}

	public void setGoanotations(String[][] goanotations) {
		this.goanotations = goanotations;
	}

	public String[][] getDrugs() {
		return drugs;
	}

	public void setDrugs(String[][] drugs) {
		this.drugs = drugs;
	}

	public int getHtridb() {
		return htridb;
	}

	public void setHtridb(int htridb) {
		this.htridb = htridb;
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

	public String[] getSources() {
		return sources;
	}

	public void setSources(String[] sources) {
		this.sources = sources;
	}

	public String getSourcesT() {
		
		String res;
		
		if(this.sources.length==0) res = "None";
		else
		{
			res = "";
			
			for(int i=0;i<this.sources.length;i++)
			{
				if(i>0) res += ", "+this.sources[i].replace("_", " ");
				else res = this.sources[i].replace("_", " ");
			}
		}
		
		return res;
	}

	public void setSourcesT(String sourcesT) {
	}
	
}
