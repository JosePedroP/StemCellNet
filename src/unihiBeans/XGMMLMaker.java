package unihiBeans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import databaseAccess.accessers.InteractionDistributionDTO;
import databaseAccess.accessers.ProteinDTO;

public class XGMMLMaker {

	public static String getXGMML(InteractionDistributionDTO[] ids) throws Exception
	{
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
			
			ProteinDTO pa = ids[y].getPridA();
			ProteinDTO pb = ids[y].getPridB();
									
			res += "  <edge id=\""+newIds+"\" label=\""+nodTempName.get(pa.getPrid())+" (interacts with) "+
				nodTempName.get(pb.getPrid())+"\" source=\""+newNodIds.get(pa.getPrid())+
				"\" target=\""+newNodIds.get(pb.getPrid())+"\">\n";
						
						
			res += "    <att name=\"Methods\" type=\"string\" value=\""+methods+"\"/>\n";
						
			String ag = "1";
						
			if(ids[y].getRegulatory().equals("N")) ag = "0";
						
			res += "    <att name=\"Regulatory\" value=\""+ag+"\" type=\"boolean\"/>\n";
						
			res += "  </edge>\n";
			
			newIds++;
						
		}
				
		res += "</graph>";

		return res;
	}
	
}
