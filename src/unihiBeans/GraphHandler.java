package unihiBeans;

public class GraphHandler {

	protected String graph = "";
	protected boolean go = false;
	
	public String getGraph()
	{
		String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		res += "<graphml>\n";
		res += "<graph id=\"G\" edgedefault=\"undirected\">\n";

		res += "<key id=\"label\" for=\"all\" attr.name=\"label\" attr.type=\"string\"/>\n";
		res += "<key id=\"color\" for=\"node\" attr.name=\"color\" attr.type=\"string\"/>\n";
		res += "<key id=\"type\" for=\"edge\" attr.name=\"type\" attr.type=\"string\"/>\n";
		
		if(!go)
		{
			res += "<node id=\"1\">\n";
			res += "<data key=\"label\">1</data>\n";
			res += "<data key=\"color\">yellow</data>\n";
			res += "</node>\n";
			res += "<node id=\"2\">\n";
			res += "<data key=\"label\">2</data>\n";
			res += "<data key=\"color\">red</data>\n";
			res += "</node>\n";
			res += "<node id=\"3\">\n";
			res += "<data key=\"label\">3</data>\n";
			res += "<data key=\"color\">grey</data>\n";
			res += "</node>\n";
		}
		else
		{
			res += "<node id=\"1\">\n";
			res += "<data key=\"label\">a</data>\n";
			res += "<data key=\"color\">yellow</data>\n";
			res += "</node>\n";
			res += "<node id=\"2\">\n";
			res += "<data key=\"label\">b</data>\n";
			res += "<data key=\"color\">red</data>\n";
			res += "</node>\n";
			res += "<node id=\"3\">\n";
			res += "<data key=\"label\">c</data>\n";
			res += "<data key=\"color\">grey</data>\n";
			res += "</node>\n";
		}
		
		res += "<edge source=\"1\" target=\"2\" id=\"1to2\">\n";
		res += "<data key=\"type\">physical</data>\n";
		res += "</edge>\n";
		res += "<edge source=\"2\" target=\"3\" id=\"2to3\">\n";
		res += "<data key=\"type\">physical</data>\n";
		res += "</edge>\n";
		res += "<edge source=\"3\" target=\"1\" id=\"3to1\" directed=\"true\">\n";
		res += "<data key=\"type\">regulatory</data>\n";
		res += "</edge>\n";
		
		res += "</graph>\n";
		res += "</graphml>";
		return res;
	}
	
	public void gnow()
	{
		this.go = true;
		System.out.println("NOW AND FOREVER");
	}
	
	public String getGraph_test()
	{
		String res = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		res += "<graphml>\n";
		res += "<graph id=\"G\" edgedefault=\"undirected\">\n";

		res += "<key id=\"label\" for=\"all\" attr.name=\"label\" attr.type=\"string\"/>\n";
		res += "<key id=\"color\" for=\"node\" attr.name=\"color\" attr.type=\"string\"/>\n";
		res += "<key id=\"type\" for=\"edge\" attr.name=\"type\" attr.type=\"string\"/>\n";
		
		res += "<node id=\"1\">\n";
		res += "<data key=\"label\">1</data>\n";
		res += "<data key=\"color\">yellow</data>\n";
		res += "</node>\n";
		res += "<node id=\"2\">\n";
		res += "<data key=\"label\">2</data>\n";
		res += "<data key=\"color\">red</data>\n";
		res += "</node>\n";
		res += "<node id=\"3\">\n";
		res += "<data key=\"label\">3</data>\n";
		res += "<data key=\"color\">grey</data>\n";
		res += "</node>\n";
		
		res += "<edge source=\"1\" target=\"2\" id=\"1to2\">\n";
		res += "<data key=\"type\">physical</data>\n";
		res += "</edge>\n";
		res += "<edge source=\"2\" target=\"3\" id=\"2to3\">\n";
		res += "<data key=\"type\">physical</data>\n";
		res += "</edge>\n";
		res += "<edge source=\"3\" target=\"1\" id=\"3to1\" directed=\"true\">\n";
		res += "<data key=\"type\">regulatory</data>\n";
		res += "</edge>\n";
		
		res += "</graph>\n";
		res += "</graphml>";
		return res;
	}
	
}
