package unihiBeans;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlSelectManyListbox;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;

import jsc.contingencytables.ContingencyTable2x2;
import jsc.contingencytables.FishersExactTest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import sun.misc.BASE64Decoder;
import databaseAccess.accessers.DatabaseAccess;
import databaseAccess.accessers.InteractionDistributionDTO;
import databaseAccess.accessers.ProteinDTO;

@ManagedBean
@RequestScoped
public class UnihiNetwork {

	
	private boolean a1 = true; //Hs_ESC_Oct4 targets_Boyer
	private boolean a2 = true; //Hs_ESC_Nanog Targets_Boyer
	private boolean a3 = true; //Hs_ESC_Sox2 targets_Boyer
	private boolean a4 = true; //Hs_ESC_Consensus_Assou
	private boolean a5 = true; //Hs_SC_Palmer
	private boolean a6 = true; //Hs_ESC_NOS targets_Boyer
	private boolean a7 = true; //Mm_ESC_Ivanova
	private boolean a8 = true; //Mm_NSC_Ivanova
	private boolean a9 = true; //Mm_HSC_Ivanova
	private boolean a10 = true; //Mm_ESC_Ramalho
	private boolean a11 = true; //Mm_NSC_Ramalho
	private boolean a12 = true; //Mm_HSC_Ramalho
	private boolean a13 = true; //Mm_ESC_Fortunel
	private boolean a14 = true; //Mm_NSC_Fortunel
	private boolean a15 = true; //Mm_RPC_Fortunel
	private boolean a16 = true; //Mm_ESC_Gasper
	private boolean a17 = true; //Hs_ESC_Chia
	private boolean a18 = true; //Mm_ESC_Ding
	private boolean a19 = true; //Mm_ESC_Hu
	private boolean a20 = true; //Mm_ESC_Wang

	private String[] proteinsIds = new String[]{};
	private String[] extraData = new String[]{};
	private HtmlSelectManyListbox method = new HtmlSelectManyListbox();
	private String fileData = new String();
	private boolean uploadComplete = false;
	private String uploadData = new String();
	private int nodeLimitSize = 1000;

	public String getNodeLimitSize() {
		return ""+nodeLimitSize;
	}

	public void setNodeLimitSize(String nodeLimitSize) {
		
		int sa = 1000;
		
		try {
			int sa1 = new Integer(nodeLimitSize).intValue();
			sa = sa1;
		} catch(Exception e)
		{}
		
		this.nodeLimitSize = sa;
	}

	public HtmlSelectManyListbox getMethod() {
		return method;
	}

	public void setMethod(HtmlSelectManyListbox method) {
		this.method = method;
	}
	
	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String getProteinsIds() {
		String res = "";
		
		for(int i=0;i<this.proteinsIds.length;i++)
		{
			if(i>0)
				res += "-<>-";
			res += this.proteinsIds[i];
		}
		
		return res;
	}

	public void setProteinsIds(String pds) {
		StringTokenizer st = new StringTokenizer(pds, "-<>-");
		
		this.proteinsIds = new String[st.countTokens()];
		
		int z = 0;
		
		while(st.hasMoreTokens())
		{
			this.proteinsIds[z] = st.nextToken();
			z++;
		}
		
	}

	public String getGraphBasicData() {
		if(this.proteinsIds.length>0)
		{
			String graphBasicData = "";
		
			String[] protnames = new String[this.proteinsIds.length];
			String[] protentrez = new String[this.proteinsIds.length];
		
			ArrayList<String> tempN = new ArrayList<String>();
			ArrayList<String> tempE = new ArrayList<String>();
			ArrayList<String> tempEid = new ArrayList<String>();
			ArrayList<Integer> tempEn = new ArrayList<Integer>();
			ArrayList<String> tempR = new ArrayList<String>();
			ArrayList<String> tempRid = new ArrayList<String>();
			ArrayList<Integer> tempRn = new ArrayList<Integer>();
		
			InteractionDistributionDTO[][] idss = new InteractionDistributionDTO[this.proteinsIds.length][];
			InteractionDistributionDTO[][] idsr = new InteractionDistributionDTO[this.proteinsIds.length][];
			
			HashMap<Integer,Integer> hel = new HashMap<Integer,Integer>();
			
			int numint = 0;
			int numintP = 0;
			int numintR = 0;
			
//			System.out.println("Starting edge count");
			
			Integer[] pids = new Integer[this.proteinsIds.length];
			
			for(int i=0;i<this.proteinsIds.length;i++)
				pids[i] = new Integer(this.proteinsIds[i]);
			
			DatabaseAccess idh = new DatabaseAccess();
			
			
			InteractionDistributionDTO[][][] idst = new InteractionDistributionDTO[0][][];
			
			DatabaseAccess p = new DatabaseAccess();

			
			
			try{
			
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
			
				try{
					idst = idh.findByMultiProteinId_Test(pids, se);
				} finally
				{
					se.close();
				}
			} catch(Exception e)
			{e.printStackTrace();
			this.extraData = new String[]{};}
			
			
			
			
			
			
			idss = idst[1];
			idsr = idst[0];
			
			for(int i=0;i<this.proteinsIds.length;i++)
			{
				numint += idss[i].length;
				numint += idsr[i].length;
				

				numintP += idss[i].length;
				numintR += idsr[i].length;
				
			}
			
			System.out.println("nodeLimitSize "+nodeLimitSize);
			
			int lim = this.nodeLimitSize;
			int min = 1;

			if(numint>lim)
			{
				int total = 0;
				int u = 2;
				
				do {

					for(int y=0;y<idss.length;y++)
					{
						for(int o=0;o<idss[y].length;o++)
						{
							if(idss[y][o].getSiNumber()>=u) total++;
						}
					}

					for(int y=0;y<idsr.length;y++)
					{
						for(int o=0;o<idsr[y].length;o++)
						{
							if(idsr[y][o].getSiNumber()>=u) total++;
						}
					}
					
					if(total<lim) u++;
					
				} while(total>lim);
				
//				System.out.println("E is "+u);
				
				min = u;
			}
			
			ArrayList<Integer> centralConnection = new ArrayList<Integer>();
			
//			System.out.println("Loadins data into temp");
			
			for(int i=0;i<this.proteinsIds.length;i++)
			{
//				InteractionDistributionHome idh = new InteractionDistributionHome();
//				InteractionDistributionDTO[] ids = idh.findByPsyProteinId(this.proteinsIds[i]);
				InteractionDistributionDTO[] ids = idss[i];
			
				for(int y=0;y<ids.length;y++)
				{
					
					if(ids[y].getSiNumber()>=min)
					{
					
						ProteinDTO pa = ids[y].getPridA();
						ProteinDTO pb = ids[y].getPridB();
					
						if(!pa.getPrid().equals(pb.getPrid()) && pa.getGenesymbol()!=null && pb.getGenesymbol()!=null)
						{
							if(protnames[i]==null)
							{
								if(this.proteinsIds[i].equals(pa.getPrid().toString()))
								{
									protnames[i] = pa.getGenesymbol();
									protentrez[i] = pa.getEntrezgeneid().toString();
								}
								else if(this.proteinsIds[i].equals(pb.getPrid().toString()))
								{
									protnames[i] = pb.getGenesymbol();
									protentrez[i] = pb.getEntrezgeneid().toString();
								}
							}
				
							
							
							boolean centrala = false;
							boolean centralb = false;
							
							for(int v=0;v<this.proteinsIds.length && (!centrala || !centralb);v++)
							{
								if(!centrala && this.proteinsIds[v].equals(pa.getPrid().toString())) centrala = true;
								else if(!centralb && this.proteinsIds[v].equals(pb.getPrid().toString())) centralb = true;
							}
							
							boolean doublecentral = centrala && centralb;
							
							
							if(!doublecentral && this.proteinsIds[i].equals(pb.getPrid().toString()))
							{
								pb = ids[y].getPridA();
								pa = ids[y].getPridB();
							}
							
							ProteinDTO pc;
				
							if(!this.proteinsIds[i].equals(pa.getPrid().toString()))
							{
								pc = pa;
							}
							else pc = pb;
				
							boolean nok = true; //cecking if central node
						
							for(int o=0;nok && o<this.proteinsIds.length;o++)
							{
								nok = !this.proteinsIds[o].equals(pc.getPrid().toString());
							}
						
							String tem;
							//2087
							if(nok) 
							{
								tem = pc.getPrid()+"-<,>-"+pc.getGenesymbol()+"-<,>-"+pc.getEntrezgeneid().toString();
				
								if(!tempN.contains(tem)) tempN.add(tem);
							}
						
							tem = pa.getPrid()+"-<,>-"+pb.getPrid();
						
							if(!tempE.contains(tem))
							{
								tempE.add(tem);
								tempEid.add(""+ids[y].getIntdisid());
								tempEn.add(new Integer(ids[y].getSiNumber()));
							}
						}	
					
					
					}
				}
				
//				ids = idh.findByRegProteinId(this.proteinsIds[i]);
				
				ids = idsr[i];
				
				for(int y=0;y<ids.length;y++)
				{
					
					if(ids[y].getSiNumber()>=min) //restrict the network edges to a minimal number of citations (in this case 1)
					{
					
						ProteinDTO pa = ids[y].getPridA();
						ProteinDTO pb = ids[y].getPridB();
					
						if(!pa.getPrid().equals(pb.getPrid()) && pa.getGenesymbol()!=null && pb.getGenesymbol()!=null)
						{
							if(protnames[i]==null)
							{
								if(this.proteinsIds[i].equals(pa.getPrid().toString()))
								{
									protnames[i] = pa.getGenesymbol();
									protentrez[i] = pa.getEntrezgeneid().toString();
								}
								else if(this.proteinsIds[i].equals(pb.getPrid().toString()))
								{
									protnames[i] = pb.getGenesymbol();
									protentrez[i] = pb.getEntrezgeneid().toString();
								}
							}
							

							if(this.proteinsIds[i].equals(pb.getPrid().toString()))
							{
								pb = ids[y].getPridA();
								pa = ids[y].getPridB();
							}
				
							ProteinDTO pc;
				
							if(!this.proteinsIds[i].equals(pa.getPrid().toString()))
							{
								pc = pa;
							}
							else pc = pb;
				
							boolean nok = true; //cecking if central node
						
							for(int o=0;nok && o<this.proteinsIds.length;o++)
							{
								nok = !this.proteinsIds[o].equals(pc.getPrid().toString());
							}
						
							String tem;
							//2087
							if(nok)
							{
								tem = pc.getPrid()+"-<,>-"+pc.getGenesymbol()+"-<,>-"+pc.getEntrezgeneid().toString();
				
								if(!tempN.contains(tem)) tempN.add(tem);
							}
						
							tem = pa.getPrid()+"-<,>-"+pb.getPrid();
						
							if(!tempR.contains(tem))
							{
								tempR.add(tem);
							
								String species;
								tempRid.add(""+ids[y].getIntdisid());
								tempRn.add(new Integer(ids[y].getSiNumber()));
							}
						}
					
					}
					
					
				}
			}
		
			
//			System.out.println("Creating data line");
		
			String neds = "";
		
			for(int i=0;i<tempE.size();i++)
			{
				if(i>0) neds += "-<>-";
				neds += tempE.get(i)+"-<,>-"+tempEid.get(i)+"-<,>-"+tempEn.get(i);
				
			}
			
			for(int i=0;i<this.proteinsIds.length;i++)
			{
				if(this.proteinsIds[i]!=null && protnames[i]!=null)
				{
					if(!graphBasicData.equals("")) graphBasicData += "-<>-";
					graphBasicData += this.proteinsIds[i]+"-<,>-"+protnames[i]+"-<,>-"+protentrez[i];
				}
			}
		
			graphBasicData += "-<cn>-"; //End of central nodes start of normal nodes
		
			for(int i=0;i<tempN.size();i++)
			{
				if(i>0) graphBasicData += "-<>-";
				graphBasicData += tempN.get(i);
			}
		
			graphBasicData += "-<ne>-"+neds; //End of normal nodes start of normal edges
			
			neds = "";
			
			for(int i=0;i<tempR.size();i++)
			{
				if(i>0) neds += "-<>-";
				neds += tempR.get(i)+"-<,>-"+tempRid.get(i)+"-<,>-"+tempRn.get(i);
			}
			
			graphBasicData += "-<er>-"+neds; //End of normal edges start of regulatory interaction edges
			
//			System.out.println(graphBasicData);
			
			return graphBasicData+"-<mi>-"+min;
		}
		return "";
	}

	public void setGraphBasicData(String graphBasicData) {
	}

	public String getExtraData() {
//		return "1372-<>-820-<>-4493-<>-4195-<>-2383-<>-4449-<>-4450";
		
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

	public void checkForDrugTargets()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
			
				
				DatabaseAccess p = new DatabaseAccess();

				this.extraData = new String[]{};
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
				
				try{
					
					this.extraData = p.getDrugTargetsIds(da, se);
					
				} finally
				{
					se.close();
				}
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	public void checkForSources()
	{
		
		String[] methods = (String[])this.method.getSelectedValues();
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for(int i=0;i<methods.length;i++)
		{
			if(methods[i].equals("mdc")) temp.add("MDC");
			else if(methods[i].equals("intAct")) temp.add("IntAct");
			else if(methods[i].equals("vidal"))
			{
				temp.add("Vidal");
				temp.add("Vidal_Y2H");
			}
			else if(methods[i].equals("hprd_Complex")) temp.add("HPRD_Complex");
			else if(methods[i].equals("hprd_binary")) temp.add("HPRD_Binary");
			else if(methods[i].equals("bind")) temp.add("BIND");
			else if(methods[i].equals("dip")) temp.add("DIP");
			else if(methods[i].equals("reactome")) temp.add("Reactome");
			else if(methods[i].equals("ramani")) temp.add("Ramani");
			else if(methods[i].equals("fraser")) temp.add("Fraser");
			else if(methods[i].equals("HomoMint")) temp.add("HomoMint");
			else if(methods[i].equals("ophid")) temp.add("ophid");
			else if(methods[i].equals("BioGrid")) temp.add("BioGrid");
			else if(methods[i].equals("Transfac")) temp.add("TRANSFAC");
			else if(methods[i].equals("miRTarBase")) temp.add("miRTarBase");
		}
		
		String[] da2 = temp.toArray(new String[]{});
		
		try {
			
			
			if(da2.length==0 || this.extraData.length==0)
			{
				this.extraData = new String[]{};
			}
			else
			{
	
				DatabaseAccess p = new DatabaseAccess();
	
				Integer[] da = new Integer[this.extraData.length-1];

//				Integer[] da2 = getExtraExtraData(this.extraData[0]);
			
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
				}
			
				
				this.extraData = new String[]{};
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();
				
				try{
					
					this.extraData = p.getProteinsBySource(da, da2, se);
					
				} finally
				{
					se.close();
				}
				
		
			}
		} catch(Exception e)
		{e.printStackTrace();}
		
	}
	
	public String getPDF()
	{
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] file = decoder.decodeBuffer(this.fileData);
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("application/pdf"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"network.pdf\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}
		
		return null;
	}
	
	public String getPNG()
	{
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] file = decoder.decodeBuffer(this.fileData);
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("image/png"); // Check http://www.iana.org/assignments/media-types for all types. Use if necessary ExternalContext#getMimeType() for auto-detection based on filename.
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"network.png\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}

		return null;
	}
	
	public String getText()
	{
		
		try {
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
			
				
				InteractionDistributionDTO[] ids = new InteractionDistributionDTO[]{};
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				DatabaseAccess p = new DatabaseAccess();
				
				try{
					
					ids = p.findByInteractionId(da, se);
					
				} finally
				{
					se.close();
				}
				
				
		
//				System.out.println("Lemmiwinks "+this.extraData.length+":"+ids.length);
		
				String res = "Entrez gene id A\tGene symbol A\tEntrez gene id B\tGene symbol B\tSources\tPubmed Ids\n";
				
				for(int y=0;y<ids.length;y++)
				{
					ProteinDTO pa = ids[y].getPridA();
					ProteinDTO pb = ids[y].getPridB();
					
					res += pa.getEntrezgeneid()+"\t"+pa.getGenesymbol()+"\t"+pb.getEntrezgeneid()+"\t"+pb.getGenesymbol()+"\t";
					
//					PInteracted pd =new PInteracted(ids[y]);
					
					boolean[] methodsFound= new boolean[16];
					
					for(int q=0;q<methodsFound.length;q++) methodsFound[q] = false;
					
					for(int q=0;q<ids[y].getInteractionProperties().length;q++)
					{
						if(ids[y].getInteractionProperties()[q].getName().equals("MDC")) methodsFound[0] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("Vidal")) methodsFound[1] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("Vidal_Y2H")) methodsFound[2] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("HPRD_Complex")) methodsFound[3] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("HPRD_Binary")) methodsFound[4] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("DIP")) methodsFound[5] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("Reactome")) methodsFound[6] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("Ramani")) methodsFound[7] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("Fraser")) methodsFound[8] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("HomoMint")) methodsFound[9] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("ophid")) methodsFound[10] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("BioGrid")) methodsFound[11] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("IntAct")) methodsFound[12] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("BIND")) methodsFound[13] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("TRANSFAC")) methodsFound[14] = true;
						else if(ids[y].getInteractionProperties()[q].getName().equals("miRTarBase")) methodsFound[15] = true;
					}
					
					if(methodsFound[0]) res += "MDC ";
					if(methodsFound[1]) res += "CCSB-LIT ";
					if(methodsFound[2]) res += "CCSB-Y2H ";
					if(methodsFound[3]) res += "HPRD-COMP ";
					if(methodsFound[4]) res += "HPRD-BIN ";
					if(methodsFound[5]) res += "DIP ";
					if(methodsFound[6]) res += "REACTOME ";
					if(methodsFound[7]) res += "COCIT ";
					if(methodsFound[8]) res += "ORTHO ";
					if(methodsFound[9]) res += "HOMOMINT ";
					if(methodsFound[10]) res += "OPHID ";
					if(methodsFound[11]) res += "BIOGRID ";
					if(methodsFound[12]) res += "INTACT ";
					if(methodsFound[13]) res += "BIND ";
					if(methodsFound[14]) res += "TRANSFAC ";
					if(methodsFound[15]) res += "miRTarBase ";
					
					
					res += "\t";
					
					String[][] si = ids[y].getSi();
					
					for(int s=0;s<si.length;s++)
					{
						if(s==0) res += si[s][1];
						else res += ";"+si[s][1];
					}
					
					res += "\n";
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
				
			}
		} catch(Exception e)
		{e.printStackTrace();}

		return null;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		
		try {
			UploadedFile file = event.getFile();
			
			if(file.getContentType().equals("text/plain"))
			{
				String decoded = new String(file.getContents(), "UTF-8");
			
//				System.out.println(decoded);
				
				StringTokenizer st = new StringTokenizer(decoded, "\r\n", false);
				
				int nt = st.countTokens();
				
				HashMap<String,ArrayList<String>> data = new HashMap<String,ArrayList<String>>();
				
				for(int i=0;i<nt;i++)
				{
					String lin = st.nextToken();
					
					if(!lin.equals(""))
					{
						StringTokenizer st2 = new StringTokenizer(lin, "\t");
						
						if(st2.countTokens()>=4)
						{
							String id = st2.nextToken();
							String change = new Double(st2.nextToken()).toString();
							String pvalue = new Double(st2.nextToken()).toString();
							String comparison = st2.nextToken();
							
							String str = id+"-<,>-"+change+"-<,>-"+pvalue;
							
							if(!data.containsKey(comparison))
							{
								data.put(comparison, new ArrayList<String>());
							}
							
							data.get(comparison).add(str);
							
//							String str = lin;
//							
//							if(!first) this.uploadData += "-<>-";
//							else first = false;
////							System.out.println("-"+str+"+");
//							this.uploadData += str;
						}
					}
				}
				
				boolean first = true;
				
				String[] keys = data.keySet().toArray(new String[]{});
				
				for(int i=0;i<keys.length;i++)
				{
					if(!first) this.uploadData += "-<>-";
					else first = false;
					String str = keys[i]+"-<:>-";
					ArrayList<String> liz = data.get(keys[i]);
					
					for(int z=0;z<liz.size();z++)
					{
						if(z>0) str += "-<;>-";
						str += liz.get(z);
					}
					
					this.uploadData += str;
				}
				
//				System.out.println(this.uploadData);
				
				this.uploadComplete=true;
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.uploadData="";}
	}

	public String getUploadData() {
		return uploadData;
	}

	public void setUploadData(String uploadData) {
		this.uploadData = uploadData;
	}

	public boolean isUploadComplete() {
		return uploadComplete;
	}

	public void checkForSpecies()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				String spc = this.extraData[0];
			
//				System.out.println(spc);
				
				Integer[] da = new Integer[this.extraData.length-1];
			
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
					
//					System.out.println(this.extraData[i]);
				}
			
//				InteractionSpeciesHome p = new InteractionSpeciesHome();
				DatabaseAccess p = new DatabaseAccess();
				
				this.extraData = new String[]{};

				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
//					this.extraData = p.getDrugTargetsIds(da, spc);
					this.extraData = p.getInterInSpecies(da, spc, se);
					
				} finally
				{
					se.close();
				}
				
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}

	public void checkForGO()
	{
		try {
	
			int six = 0;
			if(a1) six++;
			if(a2) six++;
			if(a3) six++;
			if(a4) six++;
			if(a5) six++;
			if(a6) six++;
			if(a7) six++;
			if(a8) six++;
			if(a9) six++;
			if(a10) six++;
			if(a11) six++;
			if(a12) six++;
			if(a13) six++;
			if(a14) six++;
			if(a15) six++;
			if(a16) six++;
			if(a17) six++;
			if(a18) six++;
			if(a19) six++;
			if(a20) six++;
			
			if(this.extraData.length>0 && six>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
	
				//TEMP
				
				int y=0;
				
				String[] gos = new String[six];
				
				if(a1)
				{
					gos[y] = "Hs_ESC_Oct4 targets_Boyer";
					y++;
				}
				if(a2)
				{
					gos[y] = "Hs_ESC_Nanog Targets_Boyer";
					y++;
				}
				if(a3)
				{
					gos[y] = "Hs_ESC_Sox2 targets_Boyer";
					y++;
				}
				if(a4)
				{
					gos[y] = "Hs_ESC_Consensus_Assou";
					y++;
				}
				if(a5)
				{
					gos[y] = "Hs_SC_Palmer";
					y++;
				}
				if(a6)
				{
					gos[y] = "Hs_ESC_NOS targets_Boyer";
					y++;
				}
				if(a7)
				{
					gos[y] = "Mm_ESC_Ivanova";
					y++;
				}
				if(a8)
				{
					gos[y] = "Mm_NSC_Ivanova";
					y++;
				}
				if(a9)
				{
					gos[y] = "Mm_HSC_Ivanova";
					y++;
				}
				if(a10)
				{
					gos[y] = "Mm_ESC_Ramalho";
					y++;
				}
				if(a11)
				{
					gos[y] = "Mm_NSC_Ramalho";
					y++;
				}
				if(a12)
				{
					gos[y] = "Mm_HSC_Ramalho";
					y++;
				}
				if(a13)
				{
					gos[y] = "Mm_ESC_Fortunel";
					y++;
				}
				if(a14)
				{
					gos[y] = "Mm_NSC_Fortunel";
					y++;
				}
				if(a15)
				{
					gos[y] = "Mm_RPC_Fortunel";
					y++;
				}
				if(a16)
				{
					gos[y] = "Mm_ESC_Gasper";
					y++;
				}
				if(a17)
				{
					gos[y] = "Hs_ESC_Chia";
					y++;
				}
				if(a18)
				{
					gos[y] = "Mm_ESC_Ding";
					y++;
				}
				if(a19)
				{
					gos[y] = "Mm_ESC_Hu";
					y++;
				}
				if(a20)
				{
					gos[y] = "Mm_ESC_Wang";
					y++;
				}
				
//				String[] gos = new String[]{"Hs_ESC_Oct4 targets_Boyer", "Hs_ESC_Sox2 targets_Boyer", "Hs_ESC_Consensus_Assou", "Hs_SC_Palmer",
//					"Hs_ESC_NOS targets_Boyer","Mm_ESC_Ivanova","Mm_NSC_Ivanova","Mm_HSC_Ivanova","Mm_ESC_Ramalho","Mm_NSC_Ramalho","Mm_HSC_Ramalho",
//					"Mm_ESC_Fortunel", "Mm_NSC_Fortunel", "Mm_RPC_Fortunel", "Mm_ESC_Gasper", "Hs_ESC_Chia", "Mm_ESC_Ding", "Mm_ESC_Hu", "Mm_ESC_Wang"};
				
				String[][] temp = new String[][]{};
				
				
				
				
//				ScreenHome p = new ScreenHome();				
				DatabaseAccess p = new DatabaseAccess();
				
				this.extraData = new String[]{};

				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					temp = p.countGOByNode(da, gos, se);
					
				} finally
				{
					se.close();
				}
				
				
				
				
				this.extraData = new String[temp.length];
				
				for(int i=0;i<this.extraData.length;i++)
				{
					this.extraData[i] = temp[i][0]+"-<,>-"+temp[i][1];
				}
		
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}

	public boolean isA1() {
		return a1;
	}

	public void setA1(boolean a1) {
		this.a1 = a1;
	}

	public boolean isA2() {
		return a2;
	}

	public void setA2(boolean a2) {
		this.a2 = a2;
	}

	public boolean isA3() {
		return a3;
	}

	public void setA3(boolean a3) {
		this.a3 = a3;
	}

	public boolean isA4() {
		return a4;
	}

	public void setA4(boolean a4) {
		this.a4 = a4;
	}

	public boolean isA5() {
		return a5;
	}

	public void setA5(boolean a5) {
		this.a5 = a5;
	}

	public boolean isA6() {
		return a6;
	}

	public void setA6(boolean a6) {
		this.a6 = a6;
	}

	public boolean isA7() {
		return a7;
	}

	public void setA7(boolean a7) {
		this.a7 = a7;
	}

	public boolean isA8() {
		return a8;
	}

	public void setA8(boolean a8) {
		this.a8 = a8;
	}

	public boolean isA9() {
		return a9;
	}

	public void setA9(boolean a9) {
		this.a9 = a9;
	}

	public boolean isA10() {
		return a10;
	}

	public void setA10(boolean a10) {
		this.a10 = a10;
	}

	public boolean isA11() {
		return a11;
	}

	public void setA11(boolean a11) {
		this.a11 = a11;
	}

	public boolean isA12() {
		return a12;
	}

	public void setA12(boolean a12) {
		this.a12 = a12;
	}

	public boolean isA13() {
		return a13;
	}

	public void setA13(boolean a13) {
		this.a13 = a13;
	}

	public boolean isA14() {
		return a14;
	}

	public void setA14(boolean a14) {
		this.a14 = a14;
	}

	public boolean isA15() {
		return a15;
	}

	public void setA15(boolean a15) {
		this.a15 = a15;
	}

	public boolean isA16() {
		return a16;
	}

	public void setA16(boolean a16) {
		this.a16 = a16;
	}

	public boolean isA17() {
		return a17;
	}

	public void setA17(boolean a17) {
		this.a17 = a17;
	}

	public boolean isA18() {
		return a18;
	}

	public void setA18(boolean a18) {
		this.a18 = a18;
	}

	public boolean isA19() {
		return a19;
	}

	public void setA19(boolean a19) {
		this.a19 = a19;
	}

	public boolean isA20() {
		return a20;
	}

	public void setA20(boolean a20) {
		this.a20 = a20;
	}
	
	public void getPvalues()
	{
		int numgofgenes = 17959;
		
		String[] pvalues = new String[20];
		
		try {
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
			
//				ScreenHome p = new ScreenHome();
				DatabaseAccess p = new DatabaseAccess();
	
				int[][] vals = new int[][]{};
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					vals = p.getForPvalue(da, se);
					
				} finally
				{
					se.close();
				}
				
				
				if(vals.length==0) pvalues = new String[]{};
				
				
				for(int i=0;i<vals.length;i++)
				{
					int a = vals[i][1]; //number of genes
					int b = this.extraData.length-vals[i][1];
					int c = vals[i][0]-vals[i][1];
					int d = (numgofgenes - vals[i][0])-(this.extraData.length-vals[i][1]);
					
					ContingencyTable2x2 tab = new ContingencyTable2x2(a, b, c, d);
					try {
						FishersExactTest fish = new FishersExactTest(tab);
						double pvalue = fish.getOneTailedSP();
//						System.out.println(i+" "+pvalue);
						
						if(pvalue>0.000009)
						{
							DecimalFormat df = new DecimalFormat("#.#####");
							pvalues[i] = df.format(pvalue)+"-<,>-"+a;
						}
						else
						{
							DecimalFormat df = new DecimalFormat("0.##E0");
							pvalues[i] = df.format(pvalue)+"-<,>-"+a;
						}
					} catch(IllegalArgumentException e)
					{
						pvalues[i] = "1-<,>-"+a;
//						System.out.println(i+" NaN");
					}
					
				}
				
				this.extraData = pvalues;
				
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	private InteractionDistributionDTO[][][] purgeExcess(InteractionDistributionDTO[][] idss, InteractionDistributionDTO[][] idsr, int lim, int minScore)
	{
		InteractionDistributionDTO[][] res1 = new InteractionDistributionDTO[idss.length][];
		InteractionDistributionDTO[][] res2 = new InteractionDistributionDTO[idsr.length][];
		
		ArrayList<Integer> found = new ArrayList<Integer>();
		
		for(int i=0;i<idss.length;i++)
		{
			ArrayList<InteractionDistributionDTO> temp = new ArrayList<InteractionDistributionDTO>();
			InteractionDistributionDTO[] ids = idss[i];
			
			for(int y=0;y<ids.length;y++)
			{
				if(minScore<=ids[y].getScore().intValue())
				{
					ProteinDTO pa = ids[y].getPridA();
					ProteinDTO pb = ids[y].getPridB();
					
					if(!found.contains(pa.getPrid()))
					{
						found.add(pa.getPrid());
					}
					
					if(!found.contains(pb.getPrid()))
					{
						found.add(pb.getPrid());
					}
					temp.add(ids[y]);
				}
			}
			
			res1[i] = temp.toArray(new InteractionDistributionDTO[]{});
			
			temp = new ArrayList<InteractionDistributionDTO>();
			ids = idsr[i];
			
			for(int y=0;y<ids.length;y++)
			{
				if(minScore<=ids[y].getScore().intValue())
				{
					ProteinDTO pa = ids[y].getPridA();
					ProteinDTO pb = ids[y].getPridB();
					
					if(!found.contains(pa.getPrid()))
					{
						found.add(pa.getPrid());
					}
					
					if(!found.contains(pb.getPrid()))
					{
						found.add(pb.getPrid());
					}
					temp.add(ids[y]);
				}
			}
			
			res1[i] = temp.toArray(new InteractionDistributionDTO[]{});
		}
		
//		System.out.println(found.size()+" : "+(minScore+1));
		
		if(found.size()<=lim)
		{
			InteractionDistributionDTO[][][] res = new InteractionDistributionDTO[2][][];
			
			res[0] = res1;
			res[1] = res2;
			
			return res;
		}
		else return purgeExcess(res1, res2, lim, minScore+1);
	}
	


	public void checkForScore()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer spc = new Integer(this.extraData[0]);
			
//				System.out.println(spc);
				
				Integer[] da = new Integer[this.extraData.length-1];
			
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
					
//					System.out.println(this.extraData[i]);
				}
			
//				InteractionDistributionHome idh = new InteractionDistributionHome();
				DatabaseAccess idh = new DatabaseAccess();
				
				this.extraData = new String[]{};
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					this.extraData = idh.findByInteractionScore(da, spc, se);
					
				} finally
				{
					se.close();
				}
				
				
				
				
		
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	public String getTabText()
	{
		
		try {
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				String res = "Gene set\tp-value\tNumber of genes\n";
				
				String[] gos = new String[]{"Hs_ESC_Oct4 targets_Boyer", "Hs_ESC_Nanog Targets_Boyer", "Hs_ESC_Sox2 targets_Boyer", 
						"Hs_ESC_Consensus_Assou", "Hs_SC_Palmer", "Hs_ESC_NOS targets_Boyer", "Mm_ESC_Ivanova", "Mm_NSC_Ivanova", 
						"Mm_HSC_Ivanova", "Mm_ESC_Ramalho", "Mm_NSC_Ramalho", "Mm_HSC_Ramalho", "Mm_ESC_Fortunel", "Mm_NSC_Fortunel", 
						"Mm_RPC_Fortunel", "Mm_ESC_Gasper", "Hs_ESC_Chia", "Mm_ESC_Ding", "Mm_ESC_Hu", "Mm_ESC_Wang"};
				
				for(int y=0;y<this.extraData.length;y++)
				{
					StringTokenizer st = new StringTokenizer(this.extraData[y], ";");
					
//					System.out.println(this.extraData[y]);
//					System.out.println(": "+st.nextToken());
					
					String num = st.nextToken();
//					System.out.println(": "+num);
					String pvalue = st.nextToken();
//					System.out.println("> "+pvalue);
					
					res += gos[y]+"\t"+pvalue+"\t"+num+"\n";
				}
				
//				System.out.println(res);
				
				byte[] file = res.getBytes();
				FacesContext fc = FacesContext.getCurrentInstance();
			    ExternalContext ec = fc.getExternalContext();
			    ec.responseReset();
			    ec.setResponseContentType("text/plain");
			    ec.setResponseContentLength(file.length);
			    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"pvaluetab.txt\"");
			    OutputStream output = ec.getResponseOutputStream();
			    output.write(file);
			    fc.responseComplete();
				
			}
		} catch(Exception e)
		{e.printStackTrace();}
		
		return null;
		
	}
	
	public void checkForSourceStem()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
				
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
					
//					System.out.println(this.extraData[i]);
				}
			
//				InteractionDistributionHome idh = new InteractionDistributionHome();
				DatabaseAccess idh = new DatabaseAccess();
				
				this.extraData = new String[]{};
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					this.extraData = idh.findIdsNotStem(da, se);
					
				} finally
				{
					se.close();
				}
				
				
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	
	public void getDBExpression()
	{
		try {
	
			if(this.extraData.length>1 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length-1];
			
				String set = this.extraData[0];
				
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
				}
			
				
				String ty = null;
				
				if(set.equals("Day 0 mESC-EB")) ty = "day0_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 1 mESC-EB")) ty = "day1_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 2 mESC-EB")) ty = "day2_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 3 mESC-EB")) ty = "day3_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 4 mESC-EB")) ty = "day4_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 5 mESC-EB")) ty = "day5_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 6 mESC-EB")) ty = "day6_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 7 mESC-EB")) ty = "day7_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 10 mESC-EB")) ty = "day10_vs_avgExp_other_days_Agapios_etal";
				else if(set.equals("Day 2 hiPSC-CM")) ty = "day2_vs_avgExp_d0-d5-d7-d9-d11_cardiomyocytes";
				else if(set.equals("Day 0 hiPSC-CM")) ty = "day0_vs_avgExp_d2-d5-d7-d9-d11_cardiomyocytes";
				else if(set.equals("Day 9 hiPSC-CM")) ty = "day9_vs_avgExp_d0-d2-d5-d7-d11_cardiomyocytes";
				else if(set.equals("Day 5 hiPSC-CM")) ty = "day5_vs_avgExp_d0-d2-d7-d9-d11_cardiomyocytes";
				else if(set.equals("Day 11 hiPSC-CM")) ty = "day11_vs_avgExp_d0-d2-d5-d7-d9_cardiomyocytes";
				else if(set.equals("Day 7 hiPSC-CM")) ty = "day7_vs_avgExp_d0-d2-d5-d9-d11_cardiomyocytes";
				else if(set.equals("Day 0 mES-XEN")) ty = "5G6GR5_Day0_vs_all_days";
				else if(set.equals("Day 1 mES-XEN")) ty = "5G6GR5_Day1_vs_all_days";
				else if(set.equals("Day 2 mES-XEN")) ty = "5G6GR5_Day2_vs_all_days";
				else if(set.equals("Day 3 mES-XEN")) ty = "5G6GR5_Day3_vs_all_days";
				else if(set.equals("Day 4 mES-XEN")) ty = "5G6GR5_Day4_vs_all_days";
				else if(set.equals("Day 5 mES-XEN")) ty = "5G6GR5_Day5_vs_all_days";
				else if(set.equals("Day 0 mES-trophoblast")) ty = "ZHBTc4_0hr_vs_all_hours";
				else if(set.equals("Day 1 mES-trophoblast")) ty = "ZHBTc4_24hr_vs_all_hours";
				else if(set.equals("Day 2 mES-trophoblast")) ty = "ZHBTc4_48hr_vs_all_hours";
				else if(set.equals("Day 3 mES-trophoblast")) ty = "ZHBTc4_72hr_vs_all_hours";
				else if(set.equals("Day 4 mES-trophoblast")) ty = "ZHBTc4_96hr_vs_all_hours";
				else if(set.equals("Day 5 mES-trophoblast")) ty = "ZHBTc4_120hr_vs_all_hours";
				else if(set.equals("Day 1 mES-neural ectoderm")) ty = "N2_d1_vs_all_days";
				else if(set.equals("Day 2 mES-neural ectoderm")) ty = "N2_d2_vs_all_days";
				else if(set.equals("Day 3 mES-neural ectoderm")) ty = "N2_d3_vs_all_days";
				else if(set.equals("Day 4 mES-neural ectoderm")) ty = "N2_d4_vs_all_days";
				else if(set.equals("Day 5 mES-neural ectoderm")) ty = "N2_d5_vs_all_days";
				else if(set.equals("Day 6 mES-neural ectoderm")) ty = "N2_d6_vs_all_days";
				else if(set.equals("Day 3 vs day 0 MEFs-iPSCs")) ty = "Prot_D3_vs_D0";
				else if(set.equals("Day 6 vs day 0 MEFs-iPSCs")) ty = "Prot_D6_vs_D0";
				else if(set.equals("Day 9 vs day 0 MEFs-iPSCs")) ty = "Prot_D9_vs_D0";
				else if(set.equals("Day 12 vs day 0 MEFs-iPSCs")) ty = "Prot_D12_vs_D0";
				else if(set.equals("iPS vs day 0 MEFs-iPSCs")) ty = "Prot_iPS_vs_D0";
				
				
				
				String[][] dat = new String[][]{};
				
				if(ty!=null)
				{
					
					
//					HdGeneExpressionHome hge = new HdGeneExpressionHome();
					DatabaseAccess hge = new DatabaseAccess();
					
					SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
					Session se = ob.openSession();

					try{
						
						dat = hge.getExpressionValuesByType(ty, da, se);
						
					} finally
					{
						se.close();
					}
					
					
				}
				
				
				
				
				
				
				this.extraData = new String[dat.length];
				
				for(int i=0;i<this.extraData.length;i++)
					this.extraData[i] = dat[i][0]+"-<,>-"+dat[i][1];
			}
			
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
	}
	
	public String getSetsList()
	{
		try {
	
			int six = 0;
			if(a1) six++;
			if(a2) six++;
			if(a3) six++;
			if(a4) six++;
			if(a5) six++;
			if(a6) six++;
			if(a7) six++;
			if(a8) six++;
			if(a9) six++;
			if(a10) six++;
			if(a11) six++;
			if(a12) six++;
			if(a13) six++;
			if(a14) six++;
			if(a15) six++;
			if(a16) six++;
			if(a17) six++;
			if(a18) six++;
			if(a19) six++;
			if(a20) six++;
			
			String res = "Gene symbol\tEntrez gene id\tNumber of Sets\tSets\n";
			
			if(this.extraData.length>0 && six>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
				String[] geneSym = new String[this.extraData.length];
				String[] entrezId = new String[this.extraData.length];
				
				
				for(int i=0;i<this.extraData.length;i++)
				{
					String[] ga = this.extraData[i].split("-<,>-");
					
					da[i] = new Integer(ga[0]);
					geneSym[i] = ga[1];
					entrezId[i] = ga[2];
				}
			
				
				int y=0;
				
				String[] gos = new String[six];
				
				if(a1)
				{
					gos[y] = "Hs_ESC_Oct4 targets_Boyer";
					y++;
				}
				if(a2)
				{
					gos[y] = "Hs_ESC_Nanog Targets_Boyer";
					y++;
				}
				if(a3)
				{
					gos[y] = "Hs_ESC_Sox2 targets_Boyer";
					y++;
				}
				if(a4)
				{
					gos[y] = "Hs_ESC_Consensus_Assou";
					y++;
				}
				if(a5)
				{
					gos[y] = "Hs_SC_Palmer";
					y++;
				}
				if(a6)
				{
					gos[y] = "Hs_ESC_NOS targets_Boyer";
					y++;
				}
				if(a7)
				{
					gos[y] = "Mm_ESC_Ivanova";
					y++;
				}
				if(a8)
				{
					gos[y] = "Mm_NSC_Ivanova";
					y++;
				}
				if(a9)
				{
					gos[y] = "Mm_HSC_Ivanova";
					y++;
				}
				if(a10)
				{
					gos[y] = "Mm_ESC_Ramalho";
					y++;
				}
				if(a11)
				{
					gos[y] = "Mm_NSC_Ramalho";
					y++;
				}
				if(a12)
				{
					gos[y] = "Mm_HSC_Ramalho";
					y++;
				}
				if(a13)
				{
					gos[y] = "Mm_ESC_Fortunel";
					y++;
				}
				if(a14)
				{
					gos[y] = "Mm_NSC_Fortunel";
					y++;
				}
				if(a15)
				{
					gos[y] = "Mm_RPC_Fortunel";
					y++;
				}
				if(a16)
				{
					gos[y] = "Mm_ESC_Gasper";
					y++;
				}
				if(a17)
				{
					gos[y] = "Hs_ESC_Chia";
					y++;
				}
				if(a18)
				{
					gos[y] = "Mm_ESC_Ding";
					y++;
				}
				if(a19)
				{
					gos[y] = "Mm_ESC_Hu";
					y++;
				}
				if(a20)
				{
					gos[y] = "Mm_ESC_Wang";
					y++;
				}
				
				
				
				
	
				
				HashMap<Integer,ArrayList<String>> temp = new HashMap<Integer,ArrayList<String>>();
				
				
//				ScreenHome p = new ScreenHome();
				DatabaseAccess p = new DatabaseAccess();
	
				this.extraData = new String[]{};
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					temp = p.getGOByNode(da, gos, se);
					
				} finally
				{
					se.close();
				}
				
				
				
				
				
				for(int i=0;i<da.length;i++)
				{
					if(temp.containsKey(da[i]))
					{
						ArrayList<String> li = temp.get(da[i]);
						
						String sts = "";
						
						for(int s=0;s<li.size();s++)
						{
							if(s==0) sts = li.get(s);
							else sts += ", "+li.get(s);
						}
						
						res += geneSym[i]+"\t"+entrezId[i]+"\t"+li.size()+"\t"+sts+"\n";
					}
				}
				
			}
			
			byte[] file = res.getBytes();
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("text/plain");
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"sets.txt\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}
		
		return null;
		
	}
	
	
	public String getScoreList()
	{
		try {
			
			String res = "Gene symbol\tEntrez gene id\tScore\n";
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				
				for(int i=0;i<this.extraData.length;i++)
				{
					String[] ga = this.extraData[i].split("-<,>-");
					res += ga[0]+"\t"+ga[1]+"\t"+ga[2]+"\n";
				}
				
			}
			
			byte[] file = res.getBytes();
			FacesContext fc = FacesContext.getCurrentInstance();
		    ExternalContext ec = fc.getExternalContext();
		    ec.responseReset();
		    ec.setResponseContentType("text/plain");
		    ec.setResponseContentLength(file.length);
		    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"scores.txt\"");
		    OutputStream output = ec.getResponseOutputStream();
		    output.write(file);
		    fc.responseComplete();
		} catch(Exception e)
		{e.printStackTrace();}
		
		return null;
		
	}
	
	public void checkForIntCof()
	{
		try {
	
			if(this.extraData.length>2 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length-2];

				double da2 = Double.parseDouble(this.extraData[0]);
			
				
				String typ = this.extraData[1];
				
				if(typ.equals("cs")) typ = "corSpear";
				else if(typ.equals("eb")) typ = "EB_Agapois_Day0_CorPear";
				else if(typ.equals("cm")) typ = "CM_hIPSC_Day0_CorQuantile";
				else if(typ.equals("rmi")) typ = "iPSCs_prot_ave_log2FC_D3_vs_D0_CorQuantile";
				else if(typ.equals("ne")) typ = "N2_d1_vs_all_days_CorQuantile";
				else if(typ.equals("t")) typ = "ZHBTc4_0hr_vs_all_hours_CorQuantile";
				else if(typ.equals("xen")) typ = "X5G6GR5_Day0_vs_all_days_CorQuantile";
				else typ = "All_CorPear";
				
				
				for(int i=2;i<this.extraData.length;i++)
				{
					da[i-2] = new Integer(this.extraData[i]);
				}
			
				
	
				
				String[][] gaf = new String[][]{};
	
//				InteractionConfscoreHome ic = new InteractionConfscoreHome();
				DatabaseAccess ic = new DatabaseAccess();
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					gaf = ic.findSpearByInteractionIds(da, da2, typ, se);
					
				} finally
				{
					se.close();
				}
				
				
				
				
				
				
				ArrayList<String> lar = new ArrayList<String>();
				
				for(int i=1;i<da.length;i++)
				{
					boolean found = false;
					
					String sta = da[i].toString();
					
					for(int d=0;d<gaf[0].length && !found;d++)
					{
//						System.out.println(gaf[0][d]+":"+sta);
						
						if(gaf[0][d].equals(sta)) found = true;
					}
					
					for(int d=0;d<gaf[1].length && !found;d++)
					{
						if(gaf[1][d].equals(sta)) found = true;
					}
					
					if(!found)
					{
						lar.add(sta);
					}
				}
				
				String[] fad = new String[gaf[0].length+lar.size()];
				
				int y = 0;
				
				for(int i=0;i<gaf[0].length;i++)
				{
					fad[y] = gaf[0][i];
					y++;
				}
				
				for(int i=0;i<lar.size();i++)
				{
					fad[y] = lar.get(i);
					y++;
				}
				
				this.extraData = fad;
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	public void getCofVals()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				String typ = this.extraData[0];
				
				Integer[] da = new Integer[this.extraData.length-1];

				if(typ.equals("cs")) typ = "corSpear";
				else if(typ.equals("eb")) typ = "EB_Agapois_Day0_CorPear";
				else if(typ.equals("cm")) typ = "CM_hIPSC_Day0_CorQuantile";
				else if(typ.equals("rmi")) typ = "iPSCs_prot_ave_log2FC_D3_vs_D0_CorQuantile";
				else if(typ.equals("ne")) typ = "N2_d1_vs_all_days_CorQuantile";
				else if(typ.equals("t")) typ = "ZHBTc4_0hr_vs_all_hours_CorQuantile";
				else if(typ.equals("xen")) typ = "X5G6GR5_Day0_vs_all_days_CorQuantile";
				else typ = "All_CorPear";
				
//				Integer[] da2 = getExtraExtraData(this.extraData[0]);
			
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
				}
			
				
	
				
				String[][] dat = new String[][]{};
				
//				InteractionConfscoreHome ic = new InteractionConfscoreHome();
				DatabaseAccess ic = new DatabaseAccess();
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					dat = ic.findSpearAndIds(da, typ, se);
					
				} finally
				{
					se.close();
				}
				
				
				
				
				
	
				
				
				this.extraData = new String[dat.length];
				
				for(int i=0;i<dat.length;i++)
				{
					

					double x = new Double(dat[i][1]).doubleValue();
					
					double normed = 6 + Math.round(10*x);
					
					if(normed<0) normed = 1;
					
					this.extraData[i] = dat[i][0]+"-<,>-"+normed;
					
				}
				
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	public void getCofValsForFilter()
	{
		try {
	
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				String typ = this.extraData[0];
				
				Integer[] da = new Integer[this.extraData.length-1];

				if(typ.equals("cs")) typ = "corSpear";
				else if(typ.equals("eb")) typ = "EB_Agapois_Day0_CorPear";
				else if(typ.equals("cm")) typ = "CM_hIPSC_Day0_CorQuantile";
				else if(typ.equals("rmi")) typ = "iPSCs_prot_ave_log2FC_D3_vs_D0_CorQuantile";
				else if(typ.equals("ne")) typ = "N2_d1_vs_all_days_CorQuantile";
				else if(typ.equals("t")) typ = "ZHBTc4_0hr_vs_all_hours_CorQuantile";
				else if(typ.equals("xen")) typ = "X5G6GR5_Day0_vs_all_days_CorQuantile";
				else typ = "All_CorPear";
				
//				Integer[] da2 = getExtraExtraData(this.extraData[0]);
			
				for(int i=1;i<this.extraData.length;i++)
				{
					da[i-1] = new Integer(this.extraData[i]);
				}
				
				String[][] dat = new String[][]{};
				
//				InteractionConfscoreHome ic = new InteractionConfscoreHome();
				DatabaseAccess ic = new DatabaseAccess();
				
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				try{
					
					dat = ic.findSpearAndIds(da, typ, se);
					
				} finally
				{
					se.close();
				}
				
				this.extraData = new String[dat.length];
				
				for(int i=0;i<dat.length;i++)
				{
					

					double x = new Double(dat[i][1]).doubleValue();
					
					double normed = Math.round(10*x);
					
					if(normed<0) normed = 0;
					
//					System.out.println(x+" => "+normed);
					
					this.extraData[i] = dat[i][0]+"-<,>-"+normed;
					
				}
				
				
			}
		} catch(Exception e)
		{e.printStackTrace();
		this.extraData = new String[]{};}
		
	}
	
	public String getXGMML()
	{
		
		try {
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
			
				
				InteractionDistributionDTO[] ids = new InteractionDistributionDTO[]{};
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				DatabaseAccess p = new DatabaseAccess();
				
				String res = "";
				
				try{
					ids = p.findByInteractionId(da, se, true);
					res = XGMMLMaker.getXGMML(ids);
					
				} finally
				{
					se.close();
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
				
			}
		} catch(Exception e)
		{e.printStackTrace();}

		return null;
	}
	
	/*
	public String getXGMML()
	{
		
		try {
			
			if(this.extraData.length>0 && !this.extraData[0].equals(""))
			{
				Integer[] da = new Integer[this.extraData.length];
			
				for(int i=0;i<this.extraData.length;i++)
				{
					da[i] = new Integer(this.extraData[i]);
				}
			
				
				InteractionDistributionDTO[] ids = new InteractionDistributionDTO[]{};
				SessionFactory ob = (SessionFactory)new InitialContext().lookup("SessionFactory");
				Session se = ob.openSession();

				DatabaseAccess p = new DatabaseAccess();
				
				String res = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
				
				res += "<graph label=\"Network\" directed=\"1\" cy:documentVersion=\"3.0\" " +
					"xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
					"xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:cy=\"http://www.cytoscape.org\" " +
					"xmlns=\"http://www.cs.rpi.edu/XGMML\">\n";
				
				res += "  <att name=\"networkMetadata\">\n";
				res += "    <rdf:RDF>\n";
				res += "      <rdf:Description rdf:about=\"http://www.cytoscape.org/\">\n";
				res += "        <dc:type>Protein-Protein Interaction</dc:type>\n";
				res += "        <dc:description>N/A</dc:description>\n";
				res += "        <dc:identifier>N/A</dc:identifier>\n";
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				res += "        <dc:date>"+dateFormat.format(date)+"</dc:date>\n";
				res += "        <dc:title>Network</dc:title>\n";
				res += "        <dc:source>http://www.cytoscape.org/</dc:source>\n";
				res += "        <dc:format>Cytoscape-XGMML</dc:format>\n";
				res += "      </rdf:Description>\n";
				res += "    </rdf:RDF>\n";
				res += "  </att>\n";
				
				HashMap<Integer,Integer> newNodIds = new HashMap<Integer,Integer>();
				HashMap<Integer,String> nodTempName = new HashMap<Integer,String>();
				int newIds = 1;
				
				try{
					
					ids = p.findByInteractionId(da, se, true);
					
					for(int y=0;y<ids.length;y++)
					{
						ProteinDTO pa = ids[y].getPridA();
						ProteinDTO pb = ids[y].getPridB();
						
						Integer prid = pa.getPrid();
						
						if(!newNodIds.containsKey(prid))
						{
							newNodIds.put(prid, new Integer(newIds));
							
							String name = "";
							
							if(pa.getGenesymbol()==null) name = pa.getEntrezgeneid().toString();
							else name = pa.getGenesymbol();
								
							res += "  <node id=\""+newIds+"\" label=\""+name+"\">\n";
							res += "    <att name=\"name\" value=\""+name+"\" type=\"string\" cy:type=\"String\"/>\n";
							res += "    <att name=\"Stemness sets\" value=\""+pa.getSourcesT()+"\" type=\"string\" cy:type=\"String\"/>\n";
							res += "  </node>\n";
						
							nodTempName.put(prid, name);
							
							newIds++;
						}
						
						prid = pb.getPrid();
						
						if(!newNodIds.containsKey(prid))
						{
							newNodIds.put(prid, new Integer(newIds));
							
							String name = "";
							
							if(pb.getGenesymbol()==null) name = pb.getEntrezgeneid().toString();
							else name = pb.getGenesymbol();
								
							res += "  <node id=\""+newIds+"\" label=\""+name+"\">\n";
							res += "    <att name=\"name\" value=\""+name+"\" type=\"string\" cy:type=\"String\"/>\n";
							res += "    <att name=\"Stemness sets\" value=\""+pb.getSourcesT()+"\" type=\"string\" cy:type=\"String\"/>\n";
							res += "  </node>\n";
						
							nodTempName.put(prid, name);
							
							newIds++;
						}
						
					}
					
					
					
					for(int y=0;y<ids.length;y++)
					{
						boolean[] methodsFound= new boolean[16];
						
						for(int q=0;q<ids[y].getInteractionProperties().length;q++)
						{
							if(ids[y].getInteractionProperties()[q].getName().equals("MDC")) methodsFound[0] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("Vidal")) methodsFound[1] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("Vidal_Y2H")) methodsFound[2] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("HPRD_Complex")) methodsFound[3] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("HPRD_Binary")) methodsFound[4] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("DIP")) methodsFound[5] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("Reactome")) methodsFound[6] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("Ramani")) methodsFound[7] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("Fraser")) methodsFound[8] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("HomoMint")) methodsFound[9] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("ophid")) methodsFound[10] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("BioGrid")) methodsFound[11] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("IntAct")) methodsFound[12] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("BIND")) methodsFound[13] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("TRANSFAC")) methodsFound[14] = true;
							else if(ids[y].getInteractionProperties()[q].getName().equals("miRTarBase")) methodsFound[15] = true;
						}
						
						String methods = "";
						
						if(methodsFound[0]) methods += "MDC";
						if(methodsFound[1]) 
						{
							if(!methods.equals("")) methods += ",";
							methods += "CCSB-LIT";
						}
						if(methodsFound[2])
						{
							if(!methods.equals("")) methods += ",";
							methods += "CCSB-Y2H";
						}
						if(methodsFound[3])
						{
							if(!methods.equals("")) methods += ",";
							methods += "HPRD-COMP";
						}
						if(methodsFound[4])
						{
							if(!methods.equals("")) methods += ",";
							methods += "HPRD-BIN";
						}
						if(methodsFound[5])
						{
							if(!methods.equals("")) methods += ",";
							methods += "DIP";
						}
						if(methodsFound[6])
						{
							if(!methods.equals("")) methods += ",";
							methods += "REACTOME";
						}
						if(methodsFound[7])
						{
							if(!methods.equals("")) methods += ",";
							methods += "COCIT";
						}
						if(methodsFound[8])
						{
							if(!methods.equals("")) methods += ",";
							methods += "ORTHO";
						}
						if(methodsFound[9])
						{
							if(!methods.equals("")) methods += ",";
							methods += "HOMOMINT";
						}
						if(methodsFound[10])
						{
							if(!methods.equals("")) methods += ",";
							methods += "OPHID";
						}
						if(methodsFound[11])
						{
							if(!methods.equals("")) methods += ",";
							methods += "BIOGRID";
						}
						if(methodsFound[12])
						{
							if(!methods.equals("")) methods += ",";
							methods += "INTACT";
						}
						if(methodsFound[13])
						{
							if(!methods.equals("")) methods += ",";
							methods += "BIND";
						}
						if(methodsFound[14])
						{
							if(!methods.equals("")) methods += ",";
							methods += "TRANSFAC";
						}
						if(methodsFound[15])
						{
							if(!methods.equals("")) methods += ",";
							methods += "miRTarBase";
						}
						
//						newNodIds.put(prid, new Integer(newIds));
//						
//						nodTempName.put(prid, name);
						
//						  <edge id="64" label="Node 1 (interacts with) Node 2" source="63" target="62" cy:directed="1">


						ProteinDTO pa = ids[y].getPridA();
						ProteinDTO pb = ids[y].getPridB();
						
						
						res += "  <edge id=\""+newIds+"\" label=\""+nodTempName.get(pa.getPrid())+" (interacts with) "+
							nodTempName.get(pa.getPrid())+"\" source=\""+newNodIds.get(pb.getPrid())+
							"\" target=\""+newNodIds.get(pb.getPrid())+"\">\n";
						
						
						res += "    <att name=\"Methods\" type=\"string\" value=\""+methods+"\"/>\n";
						
						String ag = "1";
						
						if(ids[y].equals("N")) ag = "0";
						
						
						res += "    <att name=\"Regulatory\" value=\""+ag+"\" type=\"boolean\"/>\n";
						
						res += "  </edge>\n";
					
						
						newIds++;
						
					}
					
					
					
				} finally
				{
					se.close();
				}
				
				res += "</graph>";
		
//				System.out.println("Lemmiwinks "+this.extraData.length+":"+ids.length);
		
				
				
				
				
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
				
			}
		} catch(Exception e)
		{e.printStackTrace();}

		return null;
	}
	*/
}
