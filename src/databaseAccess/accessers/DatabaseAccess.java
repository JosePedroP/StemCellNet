package databaseAccess.accessers;

// Generated 30/Mai/2013 10:06:00 by Hibernate Tools 3.4.0.CR1

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import databaseAccess.Expression;
import databaseAccess.Goannotation;
import databaseAccess.Interaction;
import databaseAccess.InteractionConfscore;
import databaseAccess.Method;
import databaseAccess.Metname;
import databaseAccess.Pathway;
import databaseAccess.ProtToPath;
import databaseAccess.ProteinDrug;
import databaseAccess.Proteins;
import databaseAccess.Proteinsalias;
import databaseAccess.Proteinsource;
import databaseAccess.Pubmedref;
import databaseAccess.Screen;

public class DatabaseAccess {

	private static final Log log = LogFactory.getLog(DatabaseAccess.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}
	
	
	// This function handles the first query used in the web site, it searches though the database for all the proteins which match the user's input
	public String[] basicMultiQuerry(String[] ids, Session s) {
		log.debug("Testing...");
		
//		Session s = sessionFactory.getCurrentSession();

		Transaction tx = s.beginTransaction();
		try {
			
//			String qu = "(";
//			
//			for(int u=0;u<ids.length && u<500;u++)
//			{
//				if(u>0) qu += " or "+this.multiQuerryAux(ids[u]);
//				else qu += this.multiQuerryAux(ids[u]);
//			}
//			
//			qu += ")";
//			
//			String queryString = "select prot from Proteins as prot where " + qu;
			

			String queryString = "select distinct prot from Proteins as prot join prot.proteinsaliases as alias where prot.entrezgeneid in (:id) or " +
				"prot.genesymbol in (:id) or (alias.type != 'old_stemcellnet' and alias.alias in (:id))";
			
//			System.out.println(queryString);
			
			Query queryObject = s.createQuery(queryString).setParameterList("id", ids);
			
			List l = queryObject.list();
			
			ArrayList<String> tres = new ArrayList<String>();
			
			for(int i=0;i<l.size();i++)
			{
				Proteins prot = ((Proteins)l.get(i));
				
				String tag = prot.getGenesymbol(); 
				
				if(!tres.contains(tag))
					tres.add(tag);
			}
			
			String[] res = new String[tres.size()];
			
			for(int i=0;i<tres.size();i++)
			{
				res[i] = tres.get(i);
			}
			
			tx.commit();
			
			return res;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	
	
	
	
	
	public ProteinDTO[] multiQuerry(String[] ids, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			String queryString = "select prot from Proteins as prot where (prot.genesymbol in (:id) or prot.entrezgeneid in (:id) or prot.uniprotid in (:id) " +
//				"or prot.ensembleid in (:id) or prot.unigeneid in (:id) or prot.dipid in (:id) or prot.refseqid in (:id) or prot.transfacid in (:id) " +
//				"or prot.intactid in (:id) or prot.hgncid in (:id))";
			
			String queryString = "select distinct prot from Proteins as prot join prot.proteinsaliases as alias where prot.entrezgeneid in (:id) or " +
				"prot.genesymbol in (:id) or (alias.type != 'old_stemcellnet' and alias.alias in (:id))";
			
//			System.out.println(queryString);
			
			Query queryObject = s.createQuery(queryString).setParameterList("id", ids);
			
			List l = queryObject.list();
			
			ProteinDTO[] res = new ProteinDTO[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				Proteins prot = ((Proteins)l.get(i));
				res[i] = new ProteinDTO(prot);
				
				Object[] arr = prot.getProtToPaths().toArray();
				
				ArrayList<Pathway> temppt = new ArrayList<Pathway>();
				
				for(int v=0;v<arr.length;v++)
				{
					Pathway pat = ((ProtToPath)arr[v]).getPathway();
					
					boolean add = true;
					
					for(int j=0;j<temppt.size() && !add;j++)
					{
						if(temppt.get(j).getId() == pat.getId())
						{
							add = false;
						}
					}
					
					if(add) temppt.add(pat);
					
//					pt[v] = new PathwaydistributionDTO((Pathwaydistribution)arr[v]);
				}
				
				PathwaydistributionDTO[] pt = new PathwaydistributionDTO[temppt.size()];
				
				for(int v=0;v<temppt.size();v++)
				{
					pt[v] = new PathwaydistributionDTO(temppt.get(v));
				}
				
				res[i].setPaths(pt);
				
				arr = prot.getProteinsaliases().toArray();
				
				ArrayList<String> synonyms = new ArrayList<String>();
				
				for(int v=0;v<arr.length;v++)
				{
					
					String alias = ((Proteinsalias)arr[v]).getAlias();
					
					String type = ((Proteinsalias)arr[v]).getType();
					
					if(!type.equals("old_stemcellnet"))
					{
						synonyms.add(alias);
					}
				}
				
				res[i].setSynonyms(synonyms);
				
				arr = prot.getProteinsources().toArray();
				
				
//				arr = prot.getProteinsource().toArray();
				
				for(int v=0;v<arr.length;v++)
				{
					String type = ((Proteinsource)arr[v]).getName();
					
					if(type.equals("MDC")) res[i].setMdc(1);
					else if(type.equals("Vidal")) res[i].setVidal(1);
					else if(type.equals("Vidal_Y2H")) res[i].setVidal_y2h(1);
					else if(type.equals("HPRD_Binary")) res[i].setHprd_binary(1);
					else if(type.equals("HPRD_Complex")) res[i].setHprd_complex(1);
					else if(type.equals("BIND")) res[i].setBind(1);
					else if(type.equals("DIP")) res[i].setDip(1);
					else if(type.equals("Reactome")) res[i].setReactome(1);
					else if(type.equals("Ramani")) res[i].setRamani(1);
					else if(type.equals("Fraser")) res[i].setFraser(1);
					else if(type.equals("HomoMint")) res[i].setHomomit(1);
					else if(type.equals("ophid")) res[i].setOphid(1);
					else if(type.equals("BioGrid")) res[i].setBiogrid(1);
					else if(type.equals("IntAct")) res[i].setIntact(1);
					else if(type.equals("TRANSFAC")) res[i].setTransfac(1);
					else if(type.equals("miRTarBase")) res[i].setMirtarbase(1);
					else if(type.equals("htridb")) res[i].setHtridb(1);
					else if(type.equals("mESC-HTP")) res[i].setmESCHTP(1);
					else if(type.equals("mES-ChIP")) res[i].setmESChIP(1);
				}
				
				
			}
			
			tx.commit();
			
			return res;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	//This function returns a string with the information about each of the inputed interactions separated by newlines, 
	//it is used to create the ASCII file returned by the download button in the search result page
	public String findByNodeIds_reduced(Integer[] ids, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			System.out.println("1.1.1");
			
			String queryString = "select idis from Interaction as idis where " +
				"(idis.proteinsByPridA.prid in (:ids) or idis.proteinsByPridB.prid in (:ids)) " +
				"and idis.proteinsByPridA.prid!=idis.proteinsByPridB.prid";
			
			
			
			//use multiple classe qith an equal query
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
//			System.out.println("1.1.2");
			
			List l = queryObject.list();
			
//			System.out.println("1.1.3");
			
			String res = "";
			
//			System.out.println("1.1.4");
			
			for(int i=0;i<l.size();i++)
			{
				
				Interaction id = (Interaction)l.get(i);
				
				String pres = "";
				
				boolean add = false;
				
				try {
					String entrezA = id.getProteinsByPridA().getEntrezgeneid();
					String genesA = id.getProteinsByPridA().getGenesymbol();
					String entrezB = id.getProteinsByPridB().getEntrezgeneid();
					String genesB = id.getProteinsByPridB().getGenesymbol();
					
					if(entrezA!=null && genesA!=null && entrezB!=null && genesB!=null)
					{
						pres += entrezA+"\t"+genesA+"\t"+entrezB+"\t"+genesB+"\t";
						
						Object[] arr = id.getMethods().toArray();
						
						int mdc = 0;
						int vidal = 0;
						int vidal_y2h = 0;
						int hprd_binary = 0;
						int hprd_complex = 0;
						int bind = 0;
						int dip = 0;
						int reactome = 0;
						int ramani = 0;
						int fraser = 0;
						int homomit = 0;
						int ophid = 0;
						int biogrid = 0;
						int intact = 0;
						int transfac = 0;
						int mirtarbase = 0;
						int htridb = 0;
						int mESCHTP = 0;
						int mESChIP = 0;
						
						for(int z=0;z<arr.length;z++)
						{
							
							String type = ((Method)arr[z]).getName();
							
							if(type.equals("MDC"))  mdc = 1;
							else if(type.equals("Vidal")) vidal = 1;
							else if(type.equals("Vidal_Y2H")) vidal_y2h = 1;
							else if(type.equals("HPRD_Binary")) hprd_binary = 1;
							else if(type.equals("HPRD_Complex")) hprd_complex = 1;
							else if(type.equals("BIND")) bind = 1;
							else if(type.equals("DIP")) dip = 1;
							else if(type.equals("Reactome")) reactome = 1;
							else if(type.equals("Ramani")) ramani = 1;
							else if(type.equals("Fraser")) fraser = 1;
							else if(type.equals("HomoMint")) homomit = 1;
							else if(type.equals("ophid")) ophid = 1;
							else if(type.equals("BioGrid")) biogrid = 1;
							else if(type.equals("IntAct")) intact = 1;
							else if(type.equals("TRANSFAC")) transfac = 1;
							else if(type.equals("miRTarBase")) mirtarbase = 1;
							else if(type.equals("htridb")) htridb = 1;
							else if(type.equals("mESC-HTP")) mESCHTP = 1;
							else if(type.equals("mES-ChIP")) mESChIP = 1;
						}
						
						
						if(mdc>0) pres += "MDC ";
						if(vidal>0) pres += "CCSB-LIT ";
						if(vidal_y2h>0) pres += "CCSB-Y2H ";
						if(hprd_complex>0) pres += "HPRD-COMP ";
						if(hprd_binary>0) pres += "HPRD-BIN ";
						if(dip>0) pres += "DIP ";
						if(reactome>0) pres += "REACTOME ";
						if(ramani>0) pres += "COCIT ";
						if(fraser>0) pres += "ORTHO ";
						if(homomit>0) pres += "HOMOMINT ";
						if(ophid>0) pres += "OPHID ";
						if(biogrid>0) pres += "BIOGRID ";
						if(intact>0) pres += "INTACT ";
						if(bind>0) pres += "BIND ";
						if(transfac>0) pres += "TRANSFAC ";
						if(mirtarbase>0) pres += "miRTarBase ";
						if(htridb>0) pres += "HTRIdb ";
						if(mESCHTP>0) pres += "mES-PPI ";
						if(mESChIP>0) pres += "mES-ChIP ";
						
						pres += "\t";
						
						
						arr = id.getPubmedrefs().toArray();
						
						ArrayList<String> temp1 = new ArrayList<String>();
						
						for(int z=0;z<arr.length;z++)
						{
							Pubmedref e = (Pubmedref)arr[z];
							
//							e.getMetnames()
							
							
							Object[] metas = e.getMetnames().toArray();
						
							ArrayList<String> tempmeta = new ArrayList<String>();
							
							
							for(int v=0;v<metas.length;v++)
							{
								Metname mn = (Metname)metas[v];
								
								if(mn.getShortLabel()!=null && !tempmeta.contains(mn.getShortLabel()))
									tempmeta.add(mn.getShortLabel());
								
							}
							
							String conc = "";
							
							if(tempmeta.size()==0) conc = "Unknown";
							else
							{
								conc = tempmeta.get(0);
								for(int v=1;v<tempmeta.size();v++) conc +=","+tempmeta.get(v);
							}
							
							temp1.add(""+e.getPubmedid()+":"+conc);
							
							
//						for(int v=0;v<metas.length;v++)
//						{
//							Metname mn = (Metname)metas[v];
//							
//							temp1.add(new String[]{mn.getShortLabel(), pubid});
//						}
							
							
//							
//							Object[] arr2 = e.getXrefset().toArray();
//							
//							for(int v=0;v<arr2.length;v++)
//							{
//								Xrefset xr = (Xrefset)arr2[v];
//								
//								if(xr.getId()!=null && !temp1.contains(xr.getId()))
//									temp1.add(xr.getId());
//							}
							
						}
						
						for(int d=0;d<temp1.size();d++)
						{
							if(d==0) pres += temp1.get(d);
							else pres += ";"+temp1.get(d);
						}
						
						if(id.getRegulatory().equals("N")) pres +="\tPhysical";
						else pres +="\tRegulatory";
						

						pres += "\n";
					}
					
					
					
					add = true;
				} catch(Exception e)
				{add=false;}
				
				if(add)
				{
					res += pres;
				}
			}
			
			tx.commit();
			
//			System.out.println("1.1.5");
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	//??? still not sure where this is used - I think it is the function called to get the data to create the network
	public InteractionDistributionDTO[][][] findByMultiProteinId_Test(Integer[] ids, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			String queryString = "select idis, ris " +
//				"from Interaction as idis left join idis.regulatoryinteractions as ris where " +
//				"((idis.pridA.prid in (:ids) and (ris is null or ris.deprid=idis.pridA.prid)) or " +
//				"(idis.pridB.prid in (:ids) and (ris is null or ris.deprid=idis.pridB.prid))) " +
//				"and idis.numofsource is not null " +
//				"and idis.pridA.prid!=idis.pridB.prid";
			

			
			String queryString = "select idis " +
				"from Interaction as idis where " +
				"((idis.regulatory = 'N' and " +
				"(idis.proteinsByPridA.prid in (:ids) or idis.proteinsByPridB.prid in (:ids))) or " +
				"(idis.regulatory = 'Y' and idis.proteinsByPridB.prid in (:ids))) and " +
				"idis.methods is not empty";
			
			
			
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			ArrayList<InteractionDistributionDTO>[] tresr = new ArrayList[ids.length];
			ArrayList<InteractionDistributionDTO>[] tresp = new ArrayList[ids.length];
			
			for(int i=0;i<ids.length;i++)
			{
				tresr[i] = new ArrayList<InteractionDistributionDTO>();
				tresp[i] = new ArrayList<InteractionDistributionDTO>();
			}
			
			for(int i=0;i<l.size();i++)
			{
//				Object[] lis = (Object[])l.get(i);
//				

//				System.out.println("====================");
//				
//				System.out.println(lis[0]!=null);
//				System.out.println(lis[1]!=null);
//				System.out.println(lis[1]);
				
//				Interaction id= (Interaction)lis[0];
				
				Interaction id= (Interaction)l.get(i);
				
				InteractionDistributionDTO idt = null;
				
				try {
						idt = new InteractionDistributionDTO(id);
						Object[] arr = id.getMethods().toArray();
				
						InteractionPropertiesDTO[] ipdto = new InteractionPropertiesDTO[arr.length]; 
				
						for(int z=0;z<arr.length;z++)
						{
							Method ip =(Method)arr[z];
							ipdto[z] = new InteractionPropertiesDTO(ip);
						}
				
						idt.setInteractionProperties(ipdto);
				
						arr = id.getPubmedrefs().toArray();
				
						ArrayList<String> temp1 = new ArrayList<String>();
				
						for(int z=0;z<arr.length;z++)
						{
							Pubmedref e = (Pubmedref)arr[z];
					
							if(!temp1.contains(""+e.getPubmedid()))
								temp1.add(""+e.getPubmedid());
							
//							Object[] arr2 = e.getXrefset().toArray();
//					
//							for(int v=0;v<arr2.length;v++)
//							{
//								Xrefset xr = (Xrefset)arr2[v];
//						
//								if(xr.getId()!=null && !temp1.contains(xr.getId()))
//									temp1.add(xr.getId());
//							}
					
						}
				
						idt.setSiNumber(temp1.size());

				} catch(Exception e)
				{idt=null;}
				
				if(idt!=null)
				{
					ProteinDTO pa = idt.getPridA();
					ProteinDTO pb = idt.getPridB();
					
					
					boolean found = false;
					
					for(int z=0;z<ids.length && !found;z++)
					{
						//trying to solve the bug
						
						if(pa.getPrid().equals(ids[z]) || pb.getPrid().equals(ids[z]))
						{
//							found = true;
							
							/*
							old version
							
							if(id.getRegulatory().equals("Y"))
							{
								tresr[z].add(idt);
							}
							else
							{
								tresp[z].add(idt);
							}
							
							*/
							
							if(id.getRegulatory().equals("Y") && pb.getPrid().equals(ids[z]))
							{
								tresr[z].add(idt);
							}
							else if(!id.getRegulatory().equals("Y"))
							{
								tresp[z].add(idt);
							}
							
							
							
						}
						
					}
					
					
				}
				
			}
			
			InteractionDistributionDTO[][] resr = new InteractionDistributionDTO[tresr.length][];
			
			for(int i=0;i<tresr.length;i++)
			{
				resr[i] = new InteractionDistributionDTO[tresr[i].size()];
				for(int z=0;z<tresr[i].size();z++)
				{
					resr[i][z] = tresr[i].get(z);
				}
			}
			
			InteractionDistributionDTO[][] resp = new InteractionDistributionDTO[tresp.length][];
			
			for(int i=0;i<tresp.length;i++)
			{
				resp[i] = new InteractionDistributionDTO[tresp[i].size()];
				for(int z=0;z<tresp[i].size();z++)
				{
					resp[i][z] = tresp[i].get(z);
				}
			}
			
			tx.commit();
			
			InteractionDistributionDTO[][][] res = new InteractionDistributionDTO[][][]{resr, resp};
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	//takes a list of database protein ids and returns the ones which are drug targets
	public String[] getDrugTargetsIds(Integer[] ids, Session s)
	{
		String[] res = new String[]{};
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			String queryString = "select distinct prots.prid from ProteinDrug as protd join protd.proteins as prots where prots.prid in (:id)";
			String queryString = "select distinct prots.prid from ProteinDrug as protd join protd.proteins as prots where prots.prid in (:id)";
			
			Query queryObject = s.createQuery(queryString).setParameterList("id", ids);
			
			List l = queryObject.list();
			
			res = new String[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				Integer protd = ((Integer)l.get(i));
				
				res[i] = ""+protd.intValue();
				
//				System.out.println("Jar "+i+" "+res[i]);
				
			}
			
			tx.commit();
			
		} catch (RuntimeException re) {
			log.error("drug target search error", re);
			tx.rollback();
			throw re;
		}
		
		return res;
	}
	
	
	//checkes from a list of inputed protein ids which ones are associated with the inputed sources
	public String[] getProteinsBySource(Integer[] proteins, String[] numsource, Session s)
	{
		String[] res = new String[]{};
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select distinct protss.pridFk from Proteinsource as protss where protss.proteins in (:pid) and protss.name in (:sid)";
			
			Query queryObject = s.createQuery(queryString).setParameterList("pid", proteins).setParameterList("sid", numsource);
			
			List l = queryObject.list();
			
			res = new String[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				Integer protd = ((Integer)l.get(i));
				
				res[i] = ""+protd.intValue();
				
//				System.out.println("Jar "+i+" "+res[i]);
				
			}
			
			tx.commit();
			
		} catch (RuntimeException re) {
			log.error("drug target search error", re);
			tx.rollback();
			throw re;
		}
		
		return res;
	}
	
	
	//returns the interactions with the associated ids.
	
	
	public InteractionDistributionDTO[] findByInteractionId(Integer[] ids, Session s) {
//		
		return findByInteractionId(ids, s, false);
	}
	
	public InteractionDistributionDTO[] findByInteractionId(Integer[] ids, Session s, boolean screenData) {
//		Session s = sessionFactory.getCurrentSession();
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select idis from Interaction as idis where idis.id in (:ids)";
			
			//use multiple classe qith an equal query
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			ArrayList<InteractionDistributionDTO> tres = new ArrayList<InteractionDistributionDTO>();
			
			for(int i=0;i<l.size();i++)
			{
				Interaction id = (Interaction)l.get(i);
				
				InteractionDistributionDTO idt = null;
				
				//interaction_properties data went to methods table
				
				try {
					idt = new InteractionDistributionDTO(id, screenData);
					Object[] arr = id.getMethods().toArray();
					
					InteractionPropertiesDTO[] ipdto = new InteractionPropertiesDTO[arr.length]; 
					
					for(int z=0;z<arr.length;z++)
					{
						Method ip =(Method)arr[z];
						ipdto[z] = new InteractionPropertiesDTO(ip);
					}
					
					idt.setInteractionProperties(ipdto);
					
					
					
//					arr = id.getExpdescriptions().toArray();
					
					arr = id.getPubmedrefs().toArray();
					
					ArrayList<String[]> temp1 = new ArrayList<String[]>();
//					ArrayList<String> temp2 = new ArrayList<String>();
					
					for(int z=0;z<arr.length;z++)
					{
						Pubmedref e = (Pubmedref)arr[z];
						
						String pubid = ""+e.getPubmedid();
						
						Object[] metas = e.getMetnames().toArray();
						
						for(int v=0;v<metas.length;v++)
						{
							Metname mn = (Metname)metas[v];
							
							temp1.add(new String[]{mn.getShortLabel(), pubid});
						}
						
						
//						Object[] arr3 = e.getMetnames().toArray();
//						
//						for(int v=0;v<arr3.length;v++)
//						{
//							Metname mn = (Metname)arr3[v];
//							
						
//						if(!temp1.contains(pubid)) temp1.add(pubid);
						
						
					
					}
					
					String[][] si = new String[temp1.size()][];
					
					for(int a=0;a<temp1.size();a++)
					{
						si[a] = temp1.get(a);
					}
					
					idt.setSi(si);
					
				} catch(Exception e)
				{idt=null;}
				
				if(idt!=null)
				{
					tres.add(idt);
				}
			}
			
			InteractionDistributionDTO[] res = new InteractionDistributionDTO[tres.size()];
			
			for(int i=0;i<tres.size();i++)
			{
				res[i] = tres.get(i);
			}
			
			tx.commit();
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	//checkes which of the inputed interactions are a assciated with the selected species
	public String[] getInterInSpecies(Integer[] ids, String species, Session s)
	{
		String[] res = new String[]{};
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		
		try {
			String queryString = "select int.id from Interaction as int join int.interactionSpecieses as isp where " +
				"int.id in (:id) and isp.species = '"+species+"'";

			Query queryObject = s.createQuery(queryString).setParameterList("id", ids);
			
			List l = queryObject.list();
			
			res = new String[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				res[i] = l.get(i).toString();
			}
			
			tx.commit();
			
		} catch (RuntimeException re) {
			log.error("drug target search error", re);
			tx.rollback();
			throw re;
		}
		
		return res;
	}
	
	
	
	
	public String[][] countGOByNode(Integer[] ids, String[] gos, Session s) {
		
		HashMap<String,Integer> temp = new HashMap<String,Integer>();
		String[][] res;

//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
//			String queryString = "select screen from Screen as screen where screen.proteins_prid in (:ids) and screen.source in (:gos)";
			String queryString = "select screen from Screen as screen join screen.proteins as prots " +
				"where prots.prid in (:ids) and screen.source in (:gos)";
			
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids).setParameterList("gos", gos);
			
			List l = queryObject.list();
			
			for(int i=0;i<l.size();i++)
			{
				Screen scr = ((Screen)l.get(i));
				
				String key = ""+scr.getProteins().getPrid();
						
				int nz;
				
				if(temp.containsKey(key)) nz = temp.get(key).intValue();
				else nz = 0;
				
				nz++;
				
				temp.put(key, new Integer(nz));
			}
			
			tx.commit();
			
			String[] keys = temp.keySet().toArray(new String[]{});
			
			res=new String[keys.length][2];
			
			for(int i=0;i<keys.length;i++)
			{
				String nod = keys[i];
				String value = temp.get(nod).toString();
				res[i][0] = nod;
				res[i][1] = value;
			}
			
		} catch(Exception e)
		{e.printStackTrace();
		tx.rollback();
		res=new String[][]{};}
		
		return res;
	}
	
	//returns an matrix with the total number of protein interactions asosociated with each source and the number of matches which where found.
	public int[][] getForPvalue(Integer[] ids, Session s) {
		int[][] res = new int[20][2];
		
		String[] gos = new String[]{"Hs_ESC_Oct4 targets_Boyer", "Hs_ESC_Nanog Targets_Boyer", "Hs_ESC_Sox2 targets_Boyer", 
			"Hs_ESC_Consensus_Assou", "Hs_SC_Palmer", "Hs_ESC_NOS targets_Boyer", "Mm_ESC_Ivanova", "Mm_NSC_Ivanova", 
			"Mm_HSC_Ivanova", "Mm_ESC_Ramalho", "Mm_NSC_Ramalho", "Mm_HSC_Ramalho", "Mm_ESC_Fortunel", "Mm_NSC_Fortunel", 
			"Mm_RPC_Fortunel", "Mm_ESC_Gasper", "Hs_ESC_Chia", "Mm_ESC_Ding", "Mm_ESC_Hu", "Mm_ESC_Wang"};

//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		
		try {
			
			for(int i=0;i<res.length;i++)
			{
//				String queryString = "select distinct screen.pridFk from Screen as screen where screen.source='"+gos[i]+"'";
				String queryString = "select distinct prots.prid from Screen as screen join screen.proteins as prots " +
					"where screen.source = '"+gos[i]+"'";
			
//				Query queryObject = s.createQuery(queryString).setParameterList("ids", new Integer[]{new Integer(5469)});
				Query queryObject = s.createQuery(queryString);
			
				List l = queryObject.list();
			
//				System.out.println(gos[i]);
				
//				System.out.println("Set size "+l.size());
				
				res[i][0] = l.size();
				
//				queryString = "select distinct screen.pridFk from Screen as screen where screen.pridFk in (:ids) and screen.source='"+gos[i]+"'";
				queryString = "select distinct prots.prid from Screen as screen join screen.proteins as prots " +
					"where prots.prid in (:ids) and screen.source = '"+gos[i]+"'";
				
				
				queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
				l = queryObject.list();
				
				res[i][1] = l.size();
			
//				System.out.println("Found "+l.size());
			}
			
			tx.commit();
			
		} catch(Exception e)
		{e.printStackTrace();
		tx.rollback();}
		
		return res;
	}
	
	
	public String[] findByInteractionScore(Integer[] ids, Integer score, Session s) {
		String[] res = new String[]{};
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			String queryString = "select idis from InteractionDistribution as idis where idis.intdisid in (:ids) and idis.score < "+score;
			String queryString = "select idis from Interaction as idis where idis.id in (:ids) and idis.pubmedrefs.size < "+score;
			
			//use multiple classe qith an equal query
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			res = new String[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				Interaction id = (Interaction)l.get(i);
				
				res[i] = id.getId().toString();
			}
			
			tx.commit();
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	
	public String[] findIdsNotStem(Integer[] ids, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			String queryString = "select idis from InteractionDistribution as idis where idis.intdisid in (:ids)";
			
			String queryString = "select distinct idis.id from Interaction as idis join idis.methods as mets where " +
					"idis.id in (:ids) and mets.name != 'mESC-HTP' and mets.name != 'mES-ChIP'";
			
			//use multiple classe qith an equal query
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			String[] res = new String[l.size()];
			
			for(int i=0;i<l.size();i++)
			{
				
				res[i] = l.get(i).toString();
				
			}
			
			tx.commit();
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	public String[][] getExpressionValuesByType(String type, Integer[] proteins, Session s)
	{
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			String queryString = "select exp from Expression as exp where exp.type = '"+type+
				"' and exp.proteins.prid in (:pid)";
					
			Query queryObject = s.createQuery(queryString).setParameterList("pid", proteins);
			
			List l = queryObject.list();
			
			HashMap<Integer,String> temp = new HashMap<Integer,String>();
			
			for(int i=0;i<l.size();i++)
			{
				Expression hge = ((Expression)l.get(i));
				
				if(temp.containsKey(hge.getProteins().getPrid())) temp.put(hge.getProteins().getPrid(), null);
				else temp.put(hge.getProteins().getPrid(), hge.getLogfoldchange());
				
//				System.out.println(hge.getGenesymbol()+" "+hge.getProteins().getPrid());
			}
			
			tx.commit();
			
			ArrayList<String[]> tempa = new ArrayList<String[]>();
			
			Integer[] keys = temp.keySet().toArray(new Integer[]{});
			
			for(int i=0;i<keys.length;i++)
			{
				Integer key = keys[i];
				
				String f = temp.get(key);
				
				if(f!=null)
				{
					tempa.add(new String[]{key.toString(), f});
				}
			}
			
			return tempa.toArray(new String[][]{});
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	public HashMap<Integer,ArrayList<String>> getGOByNode(Integer[] ids, String[] gos, Session s) {
		
		HashMap<Integer,ArrayList<String>> res = new HashMap<Integer,ArrayList<String>>();

//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select screen from Screen as screen where screen.proteins.prid in (:ids) and screen.source in (:gos)";
			
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids).setParameterList("gos", gos);
			
			List l = queryObject.list();
			
			for(int i=0;i<l.size();i++)
			{
				Screen scr = ((Screen)l.get(i));
				
				Integer key = scr.getProteins().getPrid();
				
				ArrayList<String> liz;
				
				if(res.containsKey(key)) liz = res.get(key);
				else liz = new ArrayList<String>();
				
				liz.add(scr.getSource());
				
				res.put(key, liz);
			}
			
			tx.commit();
			
		} catch(Exception e)
		{e.printStackTrace();
		tx.rollback();
		res=new HashMap<Integer,ArrayList<String>>();}
		
		return res;
	}
	

	public String[][] findSpearByInteractionIds(Integer[] ids, double min, String type, Session s)
	{
		
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();
		
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select ics from InteractionConfscore as ics where ics.interaction.id in (:ids) and ics.type = '"+type+"'";
			
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();

			for(int i=0;i<l.size();i++)
			{
				InteractionConfscore ic = (InteractionConfscore)l.get(i);
				
				String val = ic.getValue();
				
				String it = null;
				
				try {
					double yu = Double.parseDouble(val);
					
					if(yu<min)
					{
						it = ""+ic.getInteraction().getId();
//						System.out.println(ic.getIntdisidFk()+": "+yu+"<"+min);
					}
					else temp2.add(""+ic.getInteraction().getId());
				} catch(NumberFormatException e)
				{temp.add(""+ic.getInteraction().getId());}
				
				if(it!=null)
				{
					temp.add(it);
				}
			}
			
			tx.commit();
			
		} catch (RuntimeException re) {
			tx.rollback();
			throw re;
		}
		
		String[][] res = new String[2][];
		
		res[0] = new String[temp.size()];
		
		for(int i=0;i<temp.size();i++) res[0][i] = temp.get(i);
		
		res[1] = new String[temp2.size()];
		
		for(int i=0;i<temp2.size();i++) res[1][i] = temp2.get(i);
		
		
		return res;
		
	}
	
	public String[][] findSpearAndIds(Integer[] ids, String type, Session s)
	{
		String[][] res = new String[][]{};
		
		ArrayList<String[]> temp = new ArrayList<String[]>();
		
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select ics from InteractionConfscore as ics where ics.interaction.id in (:ids) and ics.type = '"+type+"'";
			
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			for(int i=0;i<l.size();i++)
			{
				InteractionConfscore ic = (InteractionConfscore)l.get(i);
				
				try {
					Double.parseDouble(ic.getValue());
					temp.add(new String[]{""+ic.getInteraction().getId(), ""+ic.getValue()});
				} catch(NumberFormatException e) {}
			}
			
			tx.commit();
			
		} catch (RuntimeException re) {
			tx.rollback();
			throw re;
		}
		
		res = new String[temp.size()][];
		
		for(int i=0;i<temp.size();i++)
			res[i] = temp.get(i);
		
		return res;
		
	}
	
	public ProteinDTO getOneProt(String id, Session s) {
		log.debug("Testing...");
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			Proteins prot = (Proteins) sessionFactory.getCurrentSession().get("databaseAccess.Proteins", new Integer(id));
			Proteins prot = (Proteins)s.get("databaseAccess.Proteins", new Integer(id));
			
			ProteinDTO res = new ProteinDTO(prot);
			
			Object[] arr = prot.getProteinsaliases().toArray();
			
			ArrayList<String> tsynonyms = new ArrayList<String>();
			
			for(int v=0;v<arr.length;v++)
			{
				
				if(!((Proteinsalias)arr[v]).getType().equals("old_stemcellnet"))
					tsynonyms.add(((Proteinsalias)arr[v]).getAlias());
//				synonyms[v] = ((Proteinsalias)arr[v]).getAlias();
			}
			
			String[] synonyms = new String[tsynonyms.size()];
			
			for(int v=0;v<tsynonyms.size();v++) synonyms[v] = tsynonyms.get(v);
			
			res.setSynonyms(synonyms);
			
			arr = prot.getGoannotations().toArray();
			
			String[][] gos = new String[arr.length][3];
			
			for(int v=0;v<arr.length;v++)
			{
				Goannotation g = (Goannotation)arr[v];
				
				gos[v][0] = g.getCategory();
				gos[v][1] = g.getTitle();
				gos[v][2] = g.getGoid();
			}
			
			res.setGoanotations(gos);
			
			tx.commit();
			
			return res;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	
	public ProteinDTO getOneProtWithDBID(String id, Session s) {
		log.debug("Testing...");
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			Proteins prot = (Proteins) sessionFactory.getCurrentSession().get("databaseAccess.Proteins", new Integer(id));
			Proteins prot = (Proteins)s.get("databaseAccess.Proteins", new Integer(id));
			
			ProteinDTO res = new ProteinDTO(prot);
			
			Object[] arr = prot.getProteinsaliases().toArray();
			
			
			
			
			
			
			ArrayList<String> tsynonyms = new ArrayList<String>();
			
			for(int v=0;v<arr.length;v++)
			{
				
				if(!((Proteinsalias)arr[v]).getType().equals("old_stemcellnet"))
					tsynonyms.add(((Proteinsalias)arr[v]).getAlias());
//				synonyms[v] = ((Proteinsalias)arr[v]).getAlias();
			}
			
			String[] synonyms = new String[tsynonyms.size()];
			
			for(int v=0;v<tsynonyms.size();v++) synonyms[v] = tsynonyms.get(v);
			
//			String[] synonyms = new String[arr.length];
//			
//			for(int v=0;v<arr.length;v++)
//			{
//				synonyms[v] = ((Proteinsalias)arr[v]).getAlias();
//			}
			
			res.setSynonyms(synonyms);
			
			arr = prot.getGoannotations().toArray();
			
			String[][] gos = new String[arr.length][3];
			
			for(int v=0;v<arr.length;v++)
			{
				Goannotation g = (Goannotation)arr[v];
				
				gos[v][0] = g.getCategory();
				gos[v][1] = g.getTitle();
				gos[v][2] = g.getGoid();
			}
			
			res.setGoanotations(gos);
			
			arr = prot.getProteinDrugs().toArray();
			
			String[][] drug = new String[arr.length][3];
			
			for(int v=0;v<arr.length;v++)
			{
				ProteinDrug pd = (ProteinDrug)arr[v];
				
				drug[v][0] = pd.getDrugbankid();
				drug[v][1] = pd.getName();
				drug[v][2] = pd.getMoa();
				
			}
			
			res.setDrugs(drug);
			
			arr = prot.getScreens().toArray();
			
			ArrayList<String> temp = new ArrayList<String>();
			
			for(int v=0;v<arr.length;v++)
			{
				Screen sc = (Screen)arr[v];
				
				String sor = sc.getSource();
				
				if(!temp.contains(sor))
					temp.add(sor);
			}
			
			System.out.println(temp.size());
			
			res.setSources(temp.toArray(new String[]{}));
			
			tx.commit();
			
			return res;
			
		} catch (RuntimeException re) {
			log.error("get failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	public InteractionDistributionDTO findByOneInteraction(String id, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		
		Transaction tx = s.beginTransaction();
		try {
			
//			Interaction inte = (Interaction)sessionFactory.getCurrentSession().get("databaseAccess.Interaction", new Integer(id));
			Interaction inte = (Interaction)s.get("databaseAccess.Interaction", new Integer(id));
			
			InteractionDistributionDTO idt = new InteractionDistributionDTO(inte);
			
			Object[] arr = inte.getMethods().toArray();
			
			InteractionPropertiesDTO[] ipdto = new InteractionPropertiesDTO[arr.length];
			
			for(int z=0;z<arr.length;z++)
			{
				Method ip =(Method)arr[z];
				ipdto[z] = new InteractionPropertiesDTO(ip);
			}
			
			idt.setInteractionProperties(ipdto);
			
			arr = inte.getInteractionConfscores().toArray();
			
			String[][] icfs = new String[arr.length][2];
			
			for(int z=0;z<arr.length;z++)
			{
				InteractionConfscore ic = (InteractionConfscore)arr[z];
				icfs[z][0] = ic.getType();
				icfs[z][1] = ic.getValue();
			}
			
			idt.setCofScore(icfs);
			
			arr = inte.getPubmedrefs().toArray();
			
			HashMap<String,ArrayList<String>> temph = new HashMap<String,ArrayList<String>>();
			
			for(int z=0;z<arr.length;z++)
			{

//				ArrayList<String> temp1 = new ArrayList<String>();
				
				Pubmedref e = (Pubmedref)arr[z];
				
//				si[z][0] = e.getId().toString();
//				si[z][1] = "";
				
				String key = ""+e.getPubmedid();
				
				if(!temph.containsKey(key))
					temph.put(key, new ArrayList<String>());
					
				
				Object[] arr3 = e.getMetnames().toArray();
				
				for(int v=0;v<arr3.length;v++)
				{
					Metname mn = (Metname)arr3[v];
					
					String sl = mn.getShortLabel();
					
					if(sl!=null)
					{
						ArrayList<String> da = temph.get(key);
						
						if(!da.contains(sl)) da.add(sl);
					
						temph.put(key, da);
					}
				}
				
				/*temp1.add(key);
					
				Object[] arr3 = e.getMetnames().toArray();
				
				for(int v=0;v<arr3.length;v++)
				{
					Metname mn = (Metname)arr3[v];
					
					String sl = mn.getShortLabel();
					
					if(sl!=null)
					{
						for(int m=0;m<temp1.size();m++)
						{
							String key = temp1.get(m);
							
							ArrayList<String> da = temph.get(key);
						
							if(!da.contains(sl)) da.add(sl);
						
							temph.put(key, da);
						}
					}
//					si[z][1] += mn.getShortLabel()+" ";
				}*/
				
			}
			
			String[] keys = temph.keySet().toArray(new String[]{});
			
			String[][] si = new String[keys.length][2];
			
			for(int z=0;z<keys.length;z++)
			{
				ArrayList<String> da = temph.get(keys[z]);
				
				String gk = "Unknown";
				
				if(da.size()>0)
				{
					gk = da.get(0);
					for(int j=1;j<da.size();j++)
					{
						gk += ", "+da.get(j);
					}
				}
				
				si[z][0]=gk;
				si[z][1]=keys[z];
			}
			
			idt.setSi(si);
			
			tx.commit();
			
			return idt;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	public InteractionDistributionDTO[] findByNodeIds(Integer[] ids, Session s) {
//		Session s = sessionFactory.getCurrentSession();
		Transaction tx = s.beginTransaction();
		try {
			
			String queryString = "select idis from Interaction as idis where " +
				"(idis.proteinsByPridA.prid in (:ids) or idis.proteinsByPridB.prid in (:ids)) " +
				"and idis.proteinsByPridA.prid!=idis.proteinsByPridB.prid";
			
			//use multiple classe qith an equal query
			Query queryObject = s.createQuery(queryString).setParameterList("ids", ids);
			
			List l = queryObject.list();
			
			ArrayList<InteractionDistributionDTO> tres = new ArrayList<InteractionDistributionDTO>();
			
			for(int i=0;i<l.size();i++)
			{
				Interaction id = (Interaction)l.get(i);
				
				InteractionDistributionDTO idt = null;
				
				//interaction_properties data went to methods table
				
				try {
					idt = new InteractionDistributionDTO(id, true);
					Object[] arr = id.getMethods().toArray();
					
					InteractionPropertiesDTO[] ipdto = new InteractionPropertiesDTO[arr.length]; 
					
					for(int z=0;z<arr.length;z++)
					{
						Method ip =(Method)arr[z];
						ipdto[z] = new InteractionPropertiesDTO(ip);
					}
					
					idt.setInteractionProperties(ipdto);
					
					
					
//					arr = id.getExpdescriptions().toArray();
					
					arr = id.getPubmedrefs().toArray();
					
					ArrayList<String[]> temp1 = new ArrayList<String[]>();
//					ArrayList<String> temp2 = new ArrayList<String>();
					
					for(int z=0;z<arr.length;z++)
					{
						Pubmedref e = (Pubmedref)arr[z];
						
						String pubid = ""+e.getPubmedid();
						
						Object[] metas = e.getMetnames().toArray();
						
						for(int v=0;v<metas.length;v++)
						{
							Metname mn = (Metname)metas[v];
							
							temp1.add(new String[]{mn.getShortLabel(), pubid});
						}
						
						
//						Object[] arr3 = e.getMetnames().toArray();
//						
//						for(int v=0;v<arr3.length;v++)
//						{
//							Metname mn = (Metname)arr3[v];
//							
						
//						if(!temp1.contains(pubid)) temp1.add(pubid);
						
						
					
					}
					
					String[][] si = new String[temp1.size()][];
					
					for(int a=0;a<temp1.size();a++)
					{
						si[a] = temp1.get(a);
					}
					
					idt.setSi(si);
					
				} catch(Exception e)
				{idt=null;}
				
				if(idt!=null)
				{
					tres.add(idt);
				}
			}
			
			InteractionDistributionDTO[] res = new InteractionDistributionDTO[tres.size()];
			
			for(int i=0;i<tres.size();i++)
			{
				res[i] = tres.get(i);
			}
			
			tx.commit();
			
			return res;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			tx.rollback();
			throw re;
		}
	}
	
	
	
}
