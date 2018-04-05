var vis;
var filteredNodes;
var filteredEdges;
var activeFilter;
var arr1;
var arr2;
var bypassl;
var bypass2c;
var bypassShape;
var bypassColor;
var bypassCurrent;
var doneStartup;

function startup()
{
	var size = "1000";
	
	bypassl = new Array();
	bypass2c = new Array();
	bypassShape = null;
	bypassColor = null;
	bypassCurrent = null;
	
	doneStartup = false;
	
	activeFilter = 0;
	var prots = "";
	var params = document.URL.split("?");
	params = params[1].split("&");
	
	for(var i=0;i<params.length;i++)
	{
		var p = params[i].split("=");
		if(p[0]=="c")
		{
			if(prots=="")
			{
				prots = p[1];
			}
			else
			{
				prots += "-<>-"+p[1];
			}
		}
		else if(p[0]=="s")
		{
			size = p[1];
		}
	}
	
	document.getElementById("form:hiddenMaxNodeSize").value = size;
	
	saveProts(prots);
	document.getElementById('form:update1').click();
	setTimeout(function(){startTimer()}, 500);
	
	document.getElementById("deeplink").innerHTML = "<br/>"+document.URL;

	
}

function countParts()
{
	var size = 0;
	var phys = 0;
	var regi = 0;
	
	var edg = vis.edges();
	
	for(var i=0;i<edg.length;i++)
	{
		if(edg[i].visible)
		{
			if(edg[i].data.type=="physical") phys++;
			else regi++;
		}
		
		
	}
	
	var nods = vis.nodes();
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible) size++;
	}
	
	$("div#numberofparts1").text("Gene: "+size);
	$("div#numberofparts2").text("Physical interactions: "+phys);
	$("div#numberofparts3").text("Regulatory interactions: "+regi);
}

function saveProts(prots)
{
	document.getElementById("form:hiddenProteinsId").value = prots;
}

function getGraph()
{
	var data = document.getElementById("form:hiddenData").value;
	
	var div_id = "cytoscapeweb";
	
	var options = {
		swfPath: "swf/CytoscapeWeb",
		flashInstallerPath: "swf/playerProductInstall"
	};

	vis = new org.cytoscapeweb.Visualization(div_id, options);
	
	vis.ready(function() {
		// add a listener for when nodes and edges are clicked
		vis.addListener("click", "nodes", function(event) {
			handle_click_node(event);
		})
		.addListener("click", "edges", function(event) {
			handle_click_edge(event);
		});
        
		function handle_click_node(event) {
			var target = event.target;
			window.open ("nodeDetails.jsf?prid="+target.data["id"], "_blank","location=1,status=1,scrollbars=1");
		}
        
		function handle_click_edge(event) {
			
			var target = event.target;
			
//			alert("GZ: "+target.data["id"]+" "+target.data["type"]);
			
			window.open ("pInteractionsDetails.jsf?piid="+(target.data["id"].replace("e","")), "_blank","location=1,status=1,scrollbars=1");
		}
        
		function clear() {
			document.getElementById("note").innerHTML = "";
		}
    
		function print(msg) {
			document.getElementById("note").innerHTML += "<p>" + msg + "</p>";
		}
	});
	
	
	var visual_style = {
			global: {
				backgroundColor: "#ABCFD6"
			},
			nodes: {
				selectionOpacity  : 4.0,
				selectionGlowBlur :16,
				borderWidth: 3,
				borderColor: "#ffffff",
				
				size: {
					discreteMapper: {
						attrName: "type",
						entries: [                                   
						    { attrValue: "central", value: 40 },        //75
						    { attrValue: "normal", value: 40 }
						]
					}
				},
				color: {
					discreteMapper: {
						attrName: "color",
						entries: [                                   
						    { attrValue: "grey", value: "#CCCCCC" },
						    { attrValue: "yellow", value: "#FFFF00" },
							{ attrValue: "red", value: "#FF0000" }
						]
					}
				},
				labelHorizontalAnchor: "center"
			},
			
			edges: {
				width: 4,
				color: {
					discreteMapper: {
						attrName: "type",
						entries: [
						    { attrValue: "physical", value: "#0B94B1" },
						    { attrValue: "regulatory", value: "#FF0000" }
						]
					}
				},
				targetArrowShape :{
					discreteMapper: {
						attrName: "type",
						entries: [
						    { attrValue: "physical", value: "NONE" },
						    { attrValue: "regulatory", value: "ARROW" }
						]
					}
				}
			}
		};
	
	
	var lis = {
			dataSchema: {
				nodes: [ { name: "label", type: "string" }, { name: "color", type: "string" }, { name: "type", type: "string" }, { name: "entrezid", type: "string" }],
				edges: [ { name: "label", type: "string" }, { name: "type", type: "string" }, { name: "pubnum", type: "number" } ]
			},
			data: {
				nodes: [ { id: "1", label: "1", color: "grey", type: "central" }, 
				         { id: "2", label: "2", color: "yellow", type: "normal" },
				         { id: "3", label: "3", color: "red", type: "normal"} ],
				edges: [ { id: "2to1", target: "1", source: "2", label: "2to1", type: "regulatory" },
				         { id: "1to2", target: "1", source: "3", label: "1to2", type: "physical" },
				         { id: "2to3", target: "2", source: "3", label: "2to3", type: "physical" } ]
			}
		};

	//alert("za1: "+lis.data.nodes.length);

	lis.data.nodes = new Array();
	lis.data.edges = new Array();
	
	var p = data.split("-<cn>-");
	var rest = p[1];
	var cnodes = p[0].split("-<>-");
	
	var z = 0;
	
	if(!(cnodes.length==1 && cnodes[0]==""))
	{
		for(var i=0;i<cnodes.length;i++)
		{
		//		alert(cnodes[i]);
			x = cnodes[i].split("-<,>-");
			lis.data.nodes[z] = { id: x[0], label: x[1], color: "grey", type: "central", entrezid: x[2] };
			z++;
		}
	}
	p = rest.split("-<ne>-");
	rest = p[1];
	cnodes = p[0].split("-<>-");

	if(!(cnodes.length==1 && cnodes[0]==""))
	{
		for(var i=0;i<cnodes.length;i++)
		{
			x = cnodes[i].split("-<,>-");
			lis.data.nodes[z] = { id: x[0], label: x[1], color: "yellow", type: "normal", entrezid: x[2] };
			z++;
		}
	}

	var size = z;
	
	p = rest.split("-<er>-");
	rest = p[1];
	cnodes = p[0].split("-<>-");
	
	var phys = 0;
	var regi = 0;
	
	z = 0;
	if(!(cnodes.length==1 && cnodes[0]==""))
	{
		for(var i=0;i<cnodes.length;i++)
		{
			x = cnodes[i].split("-<,>-");
//			lis.data.edges[z] = { id: "p"+x[0]+"to"+x[1], target: x[0], source: x[1], label: "", type: "physical" };
			lis.data.edges[z] = { id: "e"+x[2], target: x[0], source: x[1], label: "", type: "physical", pubnum: parseInt(x[3]) };
			z++;
			phys++;
		}
	}
	
	p = rest.split("-<mi>-");
	rest = p[1];
	cnodes = p[0].split("-<>-");
	
	if(!(cnodes.length==1 && cnodes[0]==""))
	{
		for(var i=0;i<cnodes.length;i++)
		{
			x = cnodes[i].split("-<,>-");
//			lis.data.edges[z] = { id: "r"+x[0]+"to"+x[1], target: x[0], source: x[1], label: "", type: "regulatory" };
			lis.data.edges[z] = { id: "e"+x[2], target: x[0], source: x[1], label: "", type: "regulatory", pubnum: parseInt(x[3]) };
			z++;
			regi++;
		}
	}
	
	if(size>400)
	{
		vis.draw({ 
			network: lis,
			visualStyle: visual_style,
			edgeLabelsVisible: true,
			layout: "Radial"});
	}
	else
	{
		vis.draw({ 
			network: lis,
			visualStyle: visual_style,
			edgeLabelsVisible: true,
			layout: "ForceDirected"});
	}
	
	filteredNodes = new Array();
	filteredEdges = new Array();
	
	if(parseInt(rest)>1) alert("This network contains too many edges, only interactions with more than "+rest+" associated pubmed ids will be shown");
	
	if(z>500)
	{
		$ = jQuery;
		$("#bpdf").hide();
	}
	

	$ = jQuery;
	
	$("div#numberofparts1").text("Gene: "+size);
	$("div#numberofparts2").text("Physical interactions: "+phys);
	$("div#numberofparts3").text("Regulatory interactions: "+regi);
	
}

function defaultColor()
{
	bypassl = new Array();
	bypass2c = new Array();
	bypassColor = null;
	vis.visualStyleBypass(null);
}

function defaultShape()
{
	bypassShape = null;
	vis.visualStyleBypass(null);
}

function removeNodes()
{
	var idNodes = getTargetNodes();
	
	var nods = vis.nodes();
	
	var y=filteredNodes.length;
	
	for(var i=0;i<nods.length;i++)
	{
		var rm = true;
		
		if(nods[i].data.type=="central") rm = false;
		
		for(var a=0;a<idNodes.length && rm==true;a++)
		{
			if(idNodes[a]==nods[i].data.id) rm = false;
		}
		
		if(rm)
		{
			//vis.removeNode(nods[i]);
			filteredNodes[y] = nods[i].data.id;
			y++;
		}
	}

	setFilter();
	
}

function getTargetNodes()
{
	var res = new Array();
	
	var data = document.getElementById("form:hiddenExtraData").value;
	
	sdata = data.split("-<>-");
	
	if(!(sdata.length==1 && sdata[0]==""))
	{
		for(var i=0;i<sdata.length;i++)
		{
			res[i] = sdata[i];
		}
	}
	
	return res;
}

function setNodeData()
{
	disableEverything();
	purgeFileData();
	var data = "";
	
	var nods = vis.nodes();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			if(i>0) data += "-<>-"+nods[i].data.id;
			else data = nods[i].data.id;
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}

function setEdgeDataHs()
{
	disableEverything();
	
	purgeFileData();
	var data = "Hs";
	
	var nods = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			data += "-<>-"+nods[i].data.id.replace("e","");
			y++;
		}
	}
	
//	alert(data);
	
	document.getElementById("form:hiddenExtraData").value = data;
}

function setEdgeDataMm()
{
	disableEverything();
	
	purgeFileData();
	var data = "Mm";
	
	var nods = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			data += "-<>-"+nods[i].data.id.replace("e","");
			y++;
		}
	}
	
//	alert(data);
	
	document.getElementById("form:hiddenExtraData").value = data;
}

function setEdgeData()
{
	disableEverything();
	
	purgeFileData();
	var data = "";
	
	var edgs = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			if(data!="") data += "-<>-"+edgs[i].data.id.replace("e","");
			else data = edgs[i].data.id.replace("e","");
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}


function setEdgeDataNum()
{
	
	var data = document.getElementById("score").value;
	
	if(!isNaN(data[1]))
	{
		purgeFileData();
		var nods = vis.edges();
	
		var y = 0;
	
		for(var i=0;i<nods.length;i++)
		{
			if(nods[i].visible)
			{
				data += "-<>-"+nods[i].data.id.replace("e","");
				y++;
			}
		}
	
//		alert(data);
	
		document.getElementById("form:hiddenExtraData").value = data;
	}
}


function removeNonCentralNodes()
{
	var nods = vis.nodes();
	
	var y=filteredNodes.length;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible && nods[i].data.type!="central")
		{
			filteredNodes[y] = nods[i].data.id;
			y++;
		}
	}
	
	setFilter();
}

function setFilter()
{
	vis.filter(null, function(node) {
		
		var res = true;
		
		if(node.group=="nodes")
		{
			for(var i=0;res && i<filteredNodes.length;i++)
			{
				if(filteredNodes[i]==node.data.id) res = false;
			}
		}
		else
		{
			for(var i=0;res && i<filteredEdges.length;i++)
			{
				if(filteredEdges[i]==node.data.id) res = false;
			}
		}
		
	    return res;
	}, true);
	
	countParts();
	
//	vis.filter("nodes", function(node) {
//		var res = true;
//		
//		for(var i=0;res && i<filteredNodes.length;i++)
//		{
//			if(filteredNodes[i]==node.data.id) res = false;
//		}
//		
//	    return res;
//	}, true);
	
	
//	vis.filter(null, function(node) {
//	
//	var res = true;
//	
//		if(node.group!="edges")
//		{
//			for(var i=0;res && i<filteredNodes.length;i++)
//			{
//				if(filteredNodes[i]==node.data.id) res = false;
//			}
//		}
//		return res;
//	}, true);
	
}


function resetFilter()
{
	filteredNodes = new Array();
	filteredEdges = new Array();
	edgeFilterArg = "none";
	vis.removeFilter();
}

function reLayout()
{
	var size = 0;

	var nods = vis.nodes();
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			size++;
		}
	}
	
	if(size>400) vis.layout("Radial");
	else vis.layout("ForceDirected");
	
	countParts();
}

function restNetwork()
{
	var nods = vis.nodes();

	$("#f1,#f2,#f3,#f4,#f5,#f6").css('color', 'black');
	
	resetFilter();
	defaultColor();
	defaultShape();
	bypassCurrent = null;
	reLayout();
}

function removeNonSharedNodes()
{
	var nods = vis.nodes();
	var temp = new Array();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		
		if(nods[i].visible && nods[i].data.type=="central")
		{
//			alert("kra "+nods[i].data.id);
			var dof = new Array();
			dof[0] = nods[i].data.id;
			var da = vis.firstNeighbors(dof,true);
			
			var subtemp = new Array();
			
			for(var f=0;f<da.neighbors.length;f++)
			{
				subtemp[f] = da.neighbors[f].data.id;
			}
			
			temp[y] = subtemp;
			
			y++;
			
//			alert(da.neighbors.length);
//			alert(da.neighbors[0].data.id);
		}
		
	}
	
	var gtk=filteredNodes.length;
	
	if(temp.length<2)
		removeNonCentralNodes();
	else
	{
		for(var i=0;i<nods.length;i++)
		{
			if(nods[i].visible && nods[i].data.type!="central")
			{
				var cn = 0;
				
				for(var x=0;x<temp.length && cn<2;x++)
				{
					var found = false;
					
					for(var g=0;g<temp[x].length && !found;g++)
					{
						if(nods[i].data.id==temp[x][g])
						{
							found = true;
							cn++;
						}
					}
					
				}
				
				if(cn<2)
				{
					filteredNodes[gtk] = nods[i].data.id;
					gtk++;
				}
			}
		}
		
		
	}
	

	setFilter();
}

function hideDivs(z)
{
	activeFilter = z;
	
	$ = jQuery;
	
	if(z==1)
	{
		$("#drmi1").show();
		$("#drmi2,#drmi3,#drmi4,#drmi5,#drmi6").hide();
	}
	else if(z==2)
	{
		$("#drmi2").show();
		$("#drmi1,#drmi3,#drmi4,#drmi5,#drmi6").hide();
	}
	else if(z==3)
	{
		$("#drmi3").show();
		$("#drmi1,#drmi2,#drmi4,#drmi5,#drmi6").hide();
	}
	else if(z==4)
	{
		$("#drmi4").show();
		$("#drmi1,#drmi2,#drmi3,#drmi5,#drmi6").hide();
	}
	else if(z==5)
	{
		$("#drmi5").show();
		$("#drmi1,#drmi2,#drmi3,#drmi4,#drmi6").hide();
	}
	else if(z==6)
	{
		$("#drmi6").show();
		$("#drmi1,#drmi2,#drmi3,#drmi4,#drmi5").hide();
	}
}

function runFilter1()
{
	if(activeFilter==1)
	{
		var e = document.getElementById("form:accord:showIp");
	
		if(e.options[e.selectedIndex].value=="2")
		{
			removeNonCentralNodes();
		}
		else if(e.options[e.selectedIndex].value=="1")
		{
			removeNonSharedNodes();
		}
	}
	else if(activeFilter==2)
	{
		
	}
}

function purgeFileData()
{
	document.getElementById("form:fileData").value = "";
}

function savePdf()
{
	var nods = vis.nodes();
	
	var edgs = vis.edges();
	
	document.getElementById("form:fileData").value = vis.pdf([1010, 1004]);
	document.getElementById('form:giveMeAPDF').click();
}

function savePng()
{
	var nods = vis.nodes();
	document.getElementById("form:fileData").value = vis.png();
	document.getElementById('form:giveMeAPNG').click();
}

function saveTxt()
{
//	var nods = vis.nodes();
	setEdgeData();
	enableEverything();
	document.getElementById('form:giveMeATXT').click();
}

function saveXgmml()
{
//	var nods = vis.nodes();
	setEdgeData();
	enableEverything();
	document.getElementById('form:giveMeAXGMML').click();
}

function afterUpload()
{
	arr1 = new Array();
	arr2 = new Array();
	
	var dat = document.getElementById("form:uploadData").value;
	
	document.getElementById("form:uploadData").value="";
	
	var nods = vis.nodes();
	
//	var select = document.getElementById("drop");
	
	var select = ClearOptionsFast("drop");
	
//	alert(dat);
	
	sdata = dat.split("-<>-");
	
	for(var i=0;i<sdata.length;i++)
	{
		var split1 = sdata[i].split("-<:>-");
		
		var opt = document.createElement('option');
		opt.value = split1[0];
		opt.id = split1[0];
	    opt.innerHTML = split1[0];
	    select.appendChild(opt);
	    
	    arr1[i] = split1[0];
	    arr2[i] = new Array();
	    var split2 = split1[1].split("-<;>-");
	    var u = 0;
	    
	    for(var f=0;f<split2.length;f++)
	    {
	    	var split3 = split2[f].split("-<,>-");
	    	if(!isNaN(split3[1]) && !isNaN(split3[2]))
	    	{    		
    			arr2[i][u] = new Array();
    			arr2[i][u][0] = split3[0];
    			arr2[i][u][1] = parseFloat(split3[1]);
    			arr2[i][u][2] = parseFloat(split3[2]);
    			u++;
	    	}
	    }
	}
	
	$ = jQuery;
	
	$("#drop,#drop2,#cutoff,#mapb,#mapf").show();
	
//	paintByGSandEntrez();
}

function ClearOptionsFast(id)
{
	var selectObj = document.getElementById(id);
	var selectParentNode = selectObj.parentNode;
	var newSelectObj = selectObj.cloneNode(false); // Make a shallow copy
	selectParentNode.replaceChild(newSelectObj, selectObj);
	return newSelectObj;
}

function paintByGSandEntrez()
{
	var nods = vis.nodes();
	
	var val = document.getElementById("cutoff").value.replace(",", ".");
	
	if(!isNaN(val))
	{		
		defaultColor();
		
		var nids = getTargetNodesGE(parseFloat(val));
		
		paint2C(nids);
	}

}

function paintAndCutByGSandEntrez()
{
	var nods = vis.nodes();
	
	var val = document.getElementById("cutoff").value.replace(",", ".");
	
	if(!isNaN(val))
	{
		defaultColor();
		
		var nids = getTargetNodesGE(parseFloat(val));
		
		paint2C(nids);
		
		filter2C(nids);
	}

}

function filter2C(nids)
{
	var v=filteredNodes.length;
	
	var nods = vis.nodes();
	
	for(var i=0;i<nods.length;i++)
	{
		var remove = true;
		
		if(nods[i].data.type=="central") remove = false;
		
		for(var y=0;y<nids[0].length && remove;y++)
		{
			if(nids[0][y]==nods[i].data.id)
			{
				remove = false;
			}
		}
		
		for(var y=0;y<nids[1].length && remove;y++)
		{
			if(nids[1][y]==nods[i].data.id)
			{
				remove = false;
			}
		}
		
		if(remove)
		{
			filteredNodes[v] = nods[i].data.id;
			v++;
		}
		
	}
	
	setFilter();
}


function getTargetNodesGE(val)
{
	var res = new Array();
	
	res[0] = new Array();
	res[1] = new Array();
	
	var x=document.getElementById("drop").selectedIndex;
	var y=document.getElementById("drop").options;
	
	var x1 =  y[x].text;
	
	x=document.getElementById("drop2").selectedIndex;
	y=document.getElementById("drop2").options;
	
	
	var x2 =  y[x].text;
	
	var method1 = false;
	
	if(x2=="p-value")
	{
		method1 = true;
		if(val<0) val = 0-val;
	}
	
	var stop = false;
	
	var nods = vis.nodes();
	
	var t1=0;
	var t2=0;
	
	for(var i=0;i<arr1.length && !stop;i++)
	{
		if(x1==arr1[i])
		{
			stop = true;
			for(var y=0;y<arr2[i].length;y++)
			{
				var found = false;
				
				for(var s=0;s<nods.length && !found;s++)
				{
					if(nods[s].visible && (nods[s].data.label.toLowerCase()==arr2[i][y][0].toLowerCase() || nods[s].data.entrezid==arr2[i][y][0]))
					{
						found = true;
						
						if(!method1) //change
						{
							if(arr2[i][y][1]<val)
							{
								res[0][t1] = nods[s].data.id;
								t1++;
							}
							else if(arr2[i][y][1]>val)
							{
								res[1][t2] = nods[s].data.id;
								t2++;
							}
						}
						else //p-value
						{
							var val2 = arr2[i][y][2];
							if(val2<0) val2 = 0-val2;
							
							if(val2<val)
							{

								if(arr2[i][y][1]<0)
								{
									res[0][t1] = nods[s].data.id;
									t1++;
								}
								else
								{
									res[1][t2] = nods[s].data.id;
									t2++;
								}
							}
						}
					}
				}
			}
		}
	}
	
	return res;
	
}

function removeEdges()
{
	var idEdges = getTargetNodes();
	
	var y=filteredEdges.length;
	
//	alert("GOING");
	
	for(var i=0;i<idEdges.length;i++)
	{
		filteredEdges[y] = "e"+idEdges[i];
		y++;
		
//		if(idEdges[i]=="4697")
//			alert("BUG");
//		if(idEdges[i]=="4694")
//			alert("ANOTHER BUG");
		
	}
	
	
//	alert(filteredEdges.length);
//	alert(filteredEdges[0]);
	
	var nods = vis.nodes();
	
	var visNods = new Array();
	var visNum = new Array();
	
	y=0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			visNods[i] = nods[i].data.id;
			visNum[i] = 0;
		}
	}
	
	
	var edgs = vis.edges();

	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			var found = false;
			
			for(var g=0;g<idEdges.length && !found;g++)
			{
				if(edgs[i].data.id=="e"+idEdges[g]) found = true;
			}
			
			if(!found)
			{
				var idx = edgs[i].data.source;
				var idy = edgs[i].data.target;
				var xfound = false;
				var yfound = false;
				
				for(var g=0;g<visNods.length && !(xfound && yfound);g++)
				{
					if(visNods[g]==idx)
					{
						xfound = true;
						visNum[g]++;
					}
					else if(visNods[g]==idy)
					{
						yfound = true;
						visNum[g]++;
					}
				}
				
			}
		}
	}
	
	y=filteredNodes.length;
	
//	alert(visNods.length);
	
	for(var g=0;g<visNods.length;g++)
	{
		if(visNum[g]==0)
		{
			filteredNodes[y] = visNods[g];
			y++;
		}
	}

	setFilter();
	
	enableEverything();
}

function printExtra()
{
	enableEverything();
	alert(document.getElementById("form:hiddenExtraData").value);
}

function sizeBypass()
{
	var nods = vis.nodes();
	defaultShape();
	
	var status = 1;
	
//	if(bypass2c.length>0) status = 3;
//	else if(bypassl.length>0) status = 2;
	
	var idNodes = getTargetNodes();
	
	var bypass = {
	        nodes: {
	        },
	        edges: {
	         }
	};
	
	
	for(var i=0;i<nods.length;i++)
	{
		if(bypass["nodes"][nods[i].data.id]==null)
			bypass["nodes"][nods[i].data.id] = { size: 20, shape: "RECTANGLE" };
		else
		{
			bypass["nodes"][nods[i].data.id].size = 20;
			bypass["nodes"][nods[i].data.id].shape = "RECTANGLE";
		}
		
	}
	
	for(var i=0;i<idNodes.length;i++)
	{
		var nd = idNodes[i].split("-<,>-");
		
		var fd = parseInt(nd[1]);
		
		var siz = 20+fd*10;
		
		var lfs = 11;
		
		if(fd>3) lfs = 11 + (fd-1);
		
		
		
		bypass["nodes"][nd[0]].size = siz;
		bypass["nodes"][nd[0]].labelFontSize = lfs;
		bypass["nodes"][nd[0]].shape = "ELLIPSE";

	}
	
	bypassShape = bypass;
	
	if(bypassColor!=null)
	{
		bypass = bypassColor;
		
		for(var i=0;i<nods.length;i++)
		{
			if(bypass["nodes"][nods[i].data.id]==null)
				bypass["nodes"][nods[i].data.id] = { size: 20, shape: "RECTANGLE" };
			else
			{
				bypass["nodes"][nods[i].data.id].size = 20;
				bypass["nodes"][nods[i].data.id].shape = "RECTANGLE";
			}
			
		}
		
		for(var i=0;i<idNodes.length;i++)
		{
			var nd = idNodes[i].split("-<,>-");
			
			var fd = parseInt(nd[1]);
			
			var siz = 20+fd*10;
			
			var lfs = 11;
			
			if(fd>3) lfs = 11 + (fd-1);
			
			
			
			bypass["nodes"][nd[0]].size = siz;
			bypass["nodes"][nd[0]].labelFontSize = lfs;
			bypass["nodes"][nd[0]].shape = "ELLIPSE";

		}
	}
	
	if(bypassCurrent!=null)
		bypass["edges"] = bypassCurrent["edges"];
	

	bypassCurrent = bypass;
	
	vis.visualStyleBypass(bypass);
	
	enableEverything();
}

function paint()
{
	var nods = vis.nodes();
	
	defaultColor();
	
	var idNodes = getTargetNodes();
	
	var bypass = {
	        nodes: {
	        },
	        edges: {
	         }
	};
	
	var y = 0;
	
	for(var i=0;i<idNodes.length;i++)
	{
		bypass["nodes"][idNodes[i]] = { color: "#FF0000" };
		bypassl[y] = idNodes[i];
		y++;
	}
	
	bypassColor = bypass;
	
	if(bypassShape!=null)
	{
		bypass = bypassShape;
		for(var i=0;i<idNodes.length;i++)
		{
//			bypass["nodes"][idNodes[i]] = { color: "#FF0000" };
			bypass["nodes"][idNodes[i]].color = "#FF0000";
			y++;
		}
	}

	

	if(bypassCurrent!=null)
		bypass["edges"] = bypassCurrent["edges"];

	bypassCurrent = bypass;
	
	vis.visualStyleBypass(bypass);
}

function paint2C(nids)
{
	var nods = vis.nodes();

	var bypass = {
		nodes: {
		},
		edges: {
		}
	};
	
	var y = 0;
	var z = 0;
	
	
	for(var i=0;i<nids[0].length;i++)
	{
		bypass["nodes"][nids[0][i]] = { color: "#00FF00" };
		bypass2c[y] = nids[0][i];
		y++;
	}

	for(var i=0;i<nids[1].length;i++)
	{
		bypass["nodes"][nids[1][i]] = { color: "#FF0000" };
		bypassl[z] = nids[1][i];
		z++;
	}
	
	bypassColor = bypass;
	
	if(bypassShape!=null)
	{
		bypass = bypassShape;
		for(var i=0;i<nids[0].length;i++)
		{
			bypass["nodes"][nids[0][i]].color = "#00FF00";
			bypass2c[y] = nids[0][i];
			y++;
		}
	
		for(var i=0;i<nids[1].length;i++)
		{
			bypass["nodes"][nids[1][i]].color = "#FF0000";
			bypassl[z] = nids[1][i];
			z++;
		}
	}
	

	if(bypassCurrent!=null)
		bypass["edges"] = bypassCurrent["edges"];
	

	bypassCurrent = bypass;
	
	vis.visualStyleBypass(bypass);
}

function serachForNode(arr,nod)
{
	var found = false;
	for(var i=0;i<arr.length && !found;i++)
	{
		if(arr[i]==nod) found = true;
	}
	
	return found;
}

function handelp()
{
	var pvalues = getTargetNodes();
	
	var vals = pvalues[0].split("-<,>-");
	document.getElementById("n1").innerHTML = vals[1];
	document.getElementById("p1").innerHTML = vals[0];

	vals = pvalues[1].split("-<,>-");
	document.getElementById("n2").innerHTML = vals[1];
	document.getElementById("p2").innerHTML = vals[0];

	vals = pvalues[2].split("-<,>-");
	document.getElementById("n3").innerHTML = vals[1];
	document.getElementById("p3").innerHTML = vals[0];

	vals = pvalues[3].split("-<,>-");
	document.getElementById("n4").innerHTML = vals[1];
	document.getElementById("p4").innerHTML = vals[0];

	vals = pvalues[4].split("-<,>-");
	document.getElementById("n5").innerHTML = vals[1];
	document.getElementById("p5").innerHTML = vals[0];

	vals = pvalues[5].split("-<,>-");
	document.getElementById("n6").innerHTML = vals[1];
	document.getElementById("p6").innerHTML = vals[0];

	vals = pvalues[6].split("-<,>-");
	document.getElementById("n7").innerHTML = vals[1];
	document.getElementById("p7").innerHTML = vals[0];

	vals = pvalues[7].split("-<,>-");
	document.getElementById("n8").innerHTML = vals[1];
	document.getElementById("p8").innerHTML = vals[0];

	vals = pvalues[8].split("-<,>-");
	document.getElementById("n9").innerHTML = vals[1];
	document.getElementById("p9").innerHTML = vals[0];

	vals = pvalues[9].split("-<,>-");
	document.getElementById("n10").innerHTML = vals[1];
	document.getElementById("p10").innerHTML = vals[0];

	vals = pvalues[10].split("-<,>-");
	document.getElementById("n11").innerHTML = vals[1];
	document.getElementById("p11").innerHTML = vals[0];

	vals = pvalues[11].split("-<,>-");
	document.getElementById("n12").innerHTML = vals[1];
	document.getElementById("p12").innerHTML = vals[0];

	vals = pvalues[12].split("-<,>-");
	document.getElementById("n13").innerHTML = vals[1];
	document.getElementById("p13").innerHTML = vals[0];

	vals = pvalues[13].split("-<,>-");
	document.getElementById("n14").innerHTML = vals[1];
	document.getElementById("p14").innerHTML = vals[0];

	vals = pvalues[14].split("-<,>-");
	document.getElementById("n15").innerHTML = vals[1];
	document.getElementById("p15").innerHTML = vals[0];

	vals = pvalues[15].split("-<,>-");
	document.getElementById("n16").innerHTML = vals[1];
	document.getElementById("p16").innerHTML = vals[0];

	vals = pvalues[16].split("-<,>-");
	document.getElementById("n17").innerHTML = vals[1];
	document.getElementById("p17").innerHTML = vals[0];

	vals = pvalues[17].split("-<,>-");
	document.getElementById("n18").innerHTML = vals[1];
	document.getElementById("p18").innerHTML = vals[0];

	vals = pvalues[18].split("-<,>-");
	document.getElementById("n19").innerHTML = vals[1];
	document.getElementById("p19").innerHTML = vals[0];

	vals = pvalues[19].split("-<,>-");
	document.getElementById("n20").innerHTML = vals[1];
	document.getElementById("p20").innerHTML = vals[0];
	
	$ = jQuery;
	$("#pvals").show();
	
	enableEverything();
}

function protect1()
{
	var nods = vis.nodes();
	if(document.getElementById("form:accord:a1").checked || document.getElementById("form:accord:a2").checked || document.getElementById("form:accord:a3").checked ||
		document.getElementById("form:accord:a4").checked  || document.getElementById("form:accord:a5").checked  || document.getElementById("form:accord:a6").checked || 
		document.getElementById("form:accord:a7").checked || document.getElementById("form:accord:a8").checked || document.getElementById("form:accord:a9").checked || 
		document.getElementById("form:accord:a10").checked || document.getElementById("form:accord:a11").checked || document.getElementById("form:accord:a12").checked ||
		document.getElementById("form:accord:a13").checked || document.getElementById("form:accord:a14").checked || document.getElementById("form:accord:a15").checked ||
		document.getElementById("form:accord:a16").checked || document.getElementById("form:accord:a17").checked || document.getElementById("form:accord:a18").checked ||
		document.getElementById("form:accord:a19").checked || document.getElementById("form:accord:a20").checked)
	{
		document.getElementById('form:sizer').click();
	}
}

function over1()
{
	document.getElementById("form:accord:a1").checked = document.getElementById("o1").checked;
	document.getElementById("form:accord:a2").checked = document.getElementById("o1").checked;
	document.getElementById("form:accord:a3").checked = document.getElementById("o1").checked;
	document.getElementById("form:accord:a6").checked = document.getElementById("o1").checked;
}

function over2()
{
	document.getElementById("form:accord:a7").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a8").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a9").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a10").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a11").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a12").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a13").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a14").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a15").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a16").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a4").checked = document.getElementById("o2").checked;
	document.getElementById("form:accord:a5").checked = document.getElementById("o2").checked;
}

function over3()
{
	document.getElementById("form:accord:a17").checked = document.getElementById("o3").checked;
	document.getElementById("form:accord:a18").checked = document.getElementById("o3").checked;
	document.getElementById("form:accord:a19").checked = document.getElementById("o3").checked;
	document.getElementById("form:accord:a20").checked = document.getElementById("o3").checked;
}

function protect2()
{
	var nods = vis.nodes();
	document.getElementById('form:Hs').click();
	$ = jQuery;
	$("#f1").css('color', '#900');
}

function protect3()
{
	var nods = vis.nodes();
	document.getElementById('form:Mm').click();
	$ = jQuery;
	$("#f2").css('color', '#900');
}

function protect4()
{
	var nods = vis.nodes();
	document.getElementById('form:test2').click();
}

function protect5()
{
	var nods = vis.nodes();
//	document.getElementById('form:fscore').click();

	var nu = document.getElementById("score").value;
	
	if(!isNaN(nu))
	{
		removeEdgesByPubNum(nu);
	}
}

function protect6()
{
	var nods = vis.nodes();
	document.getElementById('form:stem').click();
	$ = jQuery;
	
	$("#f5").css('color', '#900');
}

function removeEdgeType(tip)
{

	$ = jQuery;
	
	if(tip=="physical") $("#f3").css('color', '#900');
	else $("#f4").css('color', '#900');
	
	var nods = vis.edges();
	
	var idEdges = new Array();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible && nods[i].data.type==tip)
		{
			idEdges[y] = nods[i].data.id;
			y++;
		}
	}
	
	y=filteredEdges.length;
	
	for(var i=0;i<idEdges.length;i++)
	{
		filteredEdges[y] = idEdges[i];
		y++;
		
	}
	
	var nods = vis.nodes();
	
	var visNods = new Array();
	var visNum = new Array();
	
	y=0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			visNods[i] = nods[i].data.id;
			visNum[i] = 0;
		}
	}
	
	
	var edgs = vis.edges();

	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			var found = false;
			
			for(var g=0;g<idEdges.length && !found;g++)
			{
				if(edgs[i].data.id==idEdges[g]) found = true;
			}
			
			if(!found)
			{
				var idx = edgs[i].data.source;
				var idy = edgs[i].data.target;
				var xfound = false;
				var yfound = false;
				
				for(var g=0;g<visNods.length && !(xfound && yfound);g++)
				{
					if(visNods[g]==idx)
					{
						xfound = true;
						visNum[g]++;
					}
					else if(visNods[g]==idy)
					{
						yfound = true;
						visNum[g]++;
					}
				}
				
			}
		}
	}
	
	y=filteredNodes.length;
	
//	alert(visNods.length);
	
	for(var g=0;g<visNods.length;g++)
	{
		if(visNum[g]==0)
		{
			filteredNodes[y] = visNods[g];
			y++;
		}
	}

	setFilter();
	
}

function removeEdgesByPubNum(nu)
{
	
	var eds = vis.edges();
	
	var y=filteredEdges.length;
	
	for(var i=0;i<eds.length;i++)
	{
		if(eds[i].visible)
		{
			if(nu>eds[i].data.pubnum)
			{
				filteredEdges[y] = eds[i].data.id;
				y++;
			}
		}
	}
	
	var nods = vis.nodes();
	
	var visNods = new Array();
	var visNum = new Array();
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			visNods[i] = nods[i].data.id;
			visNum[i] = 0;
		}
	}
	
	var edgs = vis.edges();

	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			var found = false;
			
			if(nu>edgs[i].data.pubnum) found = true;
			
			if(!found)
			{
				var idx = edgs[i].data.source;
				var idy = edgs[i].data.target;
				var xfound = false;
				var yfound = false;
				
				for(var g=0;g<visNods.length && !(xfound && yfound);g++)
				{
					if(visNods[g]==idx)
					{
						xfound = true;
						visNum[g]++;
					}
					else if(visNods[g]==idy)
					{
						yfound = true;
						visNum[g]++;
					}
				}
			}
		}
	}
	
	
	y=filteredNodes.length;
	
//	alert(visNods.length);
	
	for(var g=0;g<visNods.length;g++)
	{
		if(visNum[g]==0)
		{
			filteredNodes[y] = visNods[g];
			y++;
		}
	}
	
	setFilter();
	
}

function paintEdges()
{
	$ = jQuery;
	$("#f6").css('color', '#900');
	
	var eds = vis.edges();
	
	var bypass;
	
	
	if(bypassCurrent==null)
	{
		bypass = {
			nodes: {},
			edges: {}
		};
	}
	else
	{
		bypass = {
			nodes: {},
			edges: {}
		};
		bypass["nodes"] = bypassCurrent;
	}
	
	for(var i=0;i<eds.length;i++)
	{
		if(eds[i].visible)
		{
			bypass["edges"][eds[i].data.id] = { width: eds[i].data.pubnum };
		}
	}
	
	bypassCurrent = bypass;
	vis.visualStyleBypass(bypass);
}

function downloadTab()
{
	var data = document.getElementById("n1").innerHTML+";"+document.getElementById("p1").innerHTML+"-<>-"+
		document.getElementById("n2").innerHTML+";"+document.getElementById("p2").innerHTML+"-<>-"+
		document.getElementById("n3").innerHTML+";"+document.getElementById("p3").innerHTML+"-<>-"+
		document.getElementById("n4").innerHTML+";"+document.getElementById("p4").innerHTML+"-<>-"+
		document.getElementById("n5").innerHTML+";"+document.getElementById("p5").innerHTML+"-<>-"+
		document.getElementById("n6").innerHTML+";"+document.getElementById("p6").innerHTML+"-<>-"+
		document.getElementById("n7").innerHTML+";"+document.getElementById("p7").innerHTML+"-<>-"+
		document.getElementById("n8").innerHTML+";"+document.getElementById("p8").innerHTML+"-<>-"+
		document.getElementById("n9").innerHTML+";"+document.getElementById("p9").innerHTML+"-<>-"+
		document.getElementById("n10").innerHTML+";"+document.getElementById("p10").innerHTML+"-<>-"+
		document.getElementById("n11").innerHTML+";"+document.getElementById("p11").innerHTML+"-<>-"+
		document.getElementById("n12").innerHTML+";"+document.getElementById("p12").innerHTML+"-<>-"+
		document.getElementById("n13").innerHTML+";"+document.getElementById("p13").innerHTML+"-<>-"+
		document.getElementById("n14").innerHTML+";"+document.getElementById("p14").innerHTML+"-<>-"+
		document.getElementById("n15").innerHTML+";"+document.getElementById("p15").innerHTML+"-<>-"+
		document.getElementById("n16").innerHTML+";"+document.getElementById("p16").innerHTML+"-<>-"+
		document.getElementById("n17").innerHTML+";"+document.getElementById("p17").innerHTML+"-<>-"+
		document.getElementById("n18").innerHTML+";"+document.getElementById("p18").innerHTML+"-<>-"+
		document.getElementById("n19").innerHTML+";"+document.getElementById("p19").innerHTML+"-<>-"+
		document.getElementById("n20").innerHTML+";"+document.getElementById("p20").innerHTML;
	
	document.getElementById("form:hiddenExtraData").value = data;

	document.getElementById('form:giveMeATab').click();
	
}




function connectedBypass(weighted)
{
	if(bypassCurrent!=null && bypassShape!=null)
	{
		disableEverything();
		
		var nods = vis.nodes();
		
		var edgs = vis.edges();
		
		var bypass = bypassCurrent;
		
		for(var i=0;i<nods.length;i++)
		{
			if(nods[i].visible && (bypass["nodes"][nods[i].data.id].shape=="RECTANGLE" || bypass["nodes"][nods[i].data.id].shape=="OCTAGON"))
			{
				var foundScore = 0;
				
				for(var z=0;z<edgs.length;z++)
				{
					if(edgs[z].visible)
					{
						var otherId = null;
						
						if(edgs[z].data.source==nods[i].data.id)
						{
							otherId = edgs[z].data.target;
						}
						else if(edgs[z].data.target==nods[i].data.id)
						{
							otherId = edgs[z].data.source;
						}
						
						if(otherId!=null && bypass["nodes"][otherId].shape=="ELLIPSE" && bypass["nodes"][otherId].size>20)
						{
							var score;
							
							if(weighted) score = (bypass["nodes"][otherId].size-20)/10;
							else score = 1;
							
							foundScore += score;
						}
					}
				}
				
				if(foundScore>0)
				{
					bypass["nodes"][nods[i].data.id].size = 20 + foundScore*5;
					bypass["nodes"][nods[i].data.id].shape = "OCTAGON";
					if(bypassColor==null) bypass["nodes"][nods[i].data.id].color = "#009ACD";
					if(foundScore>6)
					{
						bypass["nodes"][nods[i].data.id].labelFontSize = 11 + (foundScore-1);
					}
				}
				else if(bypass["nodes"][nods[i].data.id].shape=="OCTAGON")
				{
					bypass["nodes"][nods[i].data.id] = { size: 20, shape: "RECTANGLE" };
				}
			}
		}
		
		vis.visualStyleBypass(bypass);
		
		enableEverything();
	}
	
}


function disableEverything()
{
	$ = jQuery;
	$("input[type=button]").attr("disabled", "disabled");
	$("#f1,#f2,#f3,#f4,#f5,#f6,#bpdf").attr("disabled", "disabled");
	$("#loading-image").show();
}


function enableEverything()
{
	$ = jQuery;
	$("input[type=button]").removeAttr("disabled");
	$("#f1,#f2,#f3,#f4,#f5,#f6,#bpdf").removeAttr("disabled");
	$("#loading-image").hide();
}


function afterDBExpression()
{
	enableEverything();
	
	defaultColor();
	
	var nids = new Array();
	
	nids[0] = new Array();
	nids[1] = new Array();
	
	var t1 = 0;
	var t2 = 0;
	
	var da = document.getElementById("form:hiddenExtraData").value.split("-<>-");
	
	var tres = document.getElementById("cutoffDB").value.replace(",", ".");
	
	for(var i=0;i<da.length;i++)
	{
		var ga = da[i].split("-<,>-");
		
		var valz = parseFloat(ga[1]);
		
		
		
		
		if(valz<(0-tres))
		{
			nids[0][t1] = ga[0];
			t1++;
		}
		else if(valz>tres)
		{
			nids[1][t2] = ga[0];
			t2++;
		}
	}
	
	paint2C(nids);
	
//	alert(document.getElementById("form:hiddenExtraData").value);
}


function cutAfterDBExpression()
{
	enableEverything();
	
	defaultColor();
	
	var nids = new Array();
	
	nids[0] = new Array();
	nids[1] = new Array();
	
	var t1 = 0;
	var t2 = 0;
	
	var da = document.getElementById("form:hiddenExtraData").value.split("-<>-");
	
	var tres = document.getElementById("cutoffDB").value.replace(",", ".");
	
	for(var i=0;i<da.length;i++)
	{
		var ga = da[i].split("-<,>-");

		var valz = parseFloat(ga[1]);
		
		
		
		
		if(valz<(0-tres))
		{
			nids[0][t1] = ga[0];
			t1++;
		}
		else if(valz>tres)
		{
			nids[1][t2] = ga[0];
			t2++;
		}
	}
	
	paint2C(nids);
	
	filter2C(nids);
}

function protect7()
{
	var nods = vis.nodes();
	
	if(!isNaN(document.getElementById("cutoffDB").value.replace(",", ".")) && 
		document.getElementById("cutoffDB").value.replace(",", ".")!="" && document.getElementById("cutoffDB").value.replace(",", ".")>0)
	{
		document.getElementById('form:expdb').click();
	}
}

function protect8()
{
	var nods = vis.nodes();
	
	if(!isNaN(document.getElementById("cutoffDB").value.replace(",", ".")) && 
		document.getElementById("cutoffDB").value.replace(",", ".")!="" && document.getElementById("cutoffDB").value.replace(",", ".")>0)
	{
		document.getElementById('form:cxpdb').click();
	}
}

function setNodeFullData()
{
	purgeFileData();
	var data = "";
	
	var nods = vis.nodes();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			if(i>0) data += "-<>-"+nods[i].data.id+"-<,>-"+nods[i].data.label+"-<,>-"+nods[i].data.entrezid;
			else data = nods[i].data.id+"-<,>-"+nods[i].data.label+"-<,>-"+nods[i].data.entrezid;
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}

function protect9()
{
	var nods = vis.nodes();
	if(document.getElementById("form:accord:a1").checked || document.getElementById("form:accord:a2").checked || document.getElementById("form:accord:a3").checked ||
		document.getElementById("form:accord:a4").checked  || document.getElementById("form:accord:a5").checked  || document.getElementById("form:accord:a6").checked || 
		document.getElementById("form:accord:a7").checked || document.getElementById("form:accord:a8").checked || document.getElementById("form:accord:a9").checked || 
		document.getElementById("form:accord:a10").checked || document.getElementById("form:accord:a11").checked || document.getElementById("form:accord:a12").checked ||
		document.getElementById("form:accord:a13").checked || document.getElementById("form:accord:a14").checked || document.getElementById("form:accord:a15").checked ||
		document.getElementById("form:accord:a16").checked || document.getElementById("form:accord:a17").checked || document.getElementById("form:accord:a18").checked ||
		document.getElementById("form:accord:a19").checked || document.getElementById("form:accord:a20").checked)
	{
		setNodeFullData();
		document.getElementById('form:giveMeASetTab').click();
	}
	
	
}

function changeTabSam()
{
	var x=document.getElementById("drop3").selectedIndex;
	var y=document.getElementById("drop3").options;
	
	var x1 =  y[x].text;
	
	var select = ClearOptionsFast("drop4");
	
	var sdata = new Array(); 
	
	if(x1=="mESC-EB")
	{
		sdata[0] = "Day 0";
		sdata[1] = "Day 1";
		sdata[2] = "Day 2";
		sdata[3] = "Day 3";
		sdata[4] = "Day 4";
		sdata[5] = "Day 5";
		sdata[6] = "Day 6";
		sdata[7] = "Day 7";
		sdata[8] = "Day 10";
	}
	else if(x1=="hiPSC-CM")
	{
		sdata[0] = "Day 0";
		sdata[1] = "Day 2";
		sdata[2] = "Day 5";
		sdata[3] = "Day 7";
		sdata[4] = "Day 9";
		sdata[5] = "Day 11";
	}
	else if(x1=="mES-XEN")
	{
		sdata[0] = "Day 0";
		sdata[1] = "Day 1";
		sdata[2] = "Day 2";
		sdata[3] = "Day 3";
		sdata[4] = "Day 4";
		sdata[5] = "Day 5";
	}
	else if(x1=="mES-trophoblast")
	{
		sdata[0] = "Day 0";
		sdata[1] = "Day 1";
		sdata[2] = "Day 2";
		sdata[3] = "Day 3";
		sdata[4] = "Day 4";
		sdata[5] = "Day 5";
	}
	else if(x1=="mES-neural ectoderm")
	{
		sdata[0] = "Day 1";
		sdata[1] = "Day 2";
		sdata[2] = "Day 3";
		sdata[3] = "Day 4";
		sdata[4] = "Day 5";
		sdata[5] = "Day 6";
	}
	else if(x1=="MEFs-iPSCs")
	{
		sdata[0] = "Day 3 vs day 0";
		sdata[1] = "Day 6 vs day 0";
		sdata[2] = "Day 9 vs day 0";
		sdata[3] = "Day 12 vs day 0";
		sdata[4] = "iPS vs day 0";
	}
	
	for(var i=0;i<sdata.length;i++)
	{
		var opt = document.createElement('option');
		opt.value = sdata[i];
		opt.id = sdata[i];
	    opt.innerHTML = sdata[i];
	    select.appendChild(opt);
	}
	
}

function setNodeDataDro()
{
	disableEverything();
	purgeFileData();
	
	var data = "";
	
	var nods = vis.nodes();
	
	var y = 0;
	
	var x1=document.getElementById("drop3").selectedIndex;
	var y1=document.getElementById("drop3").options;
	
	var x11 =  y1[x1].text;

	var x2=document.getElementById("drop4").selectedIndex;
	var y2=document.getElementById("drop4").options;
	
	var x21 =  y2[x2].text;
	
	data = x21+" "+x11;
	
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			data += "-<>-"+nods[i].data.id;
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}


function setCandidateNodeData()
{
	var nods = vis.nodes();
	
	if(bypassCurrent!=null && bypassShape!=null)
	{
		var bypass = bypassCurrent;
		
		purgeFileData();
		var data = "";
	
		for(var i=0;i<nods.length;i++)
		{
			if(nods[i].visible && bypass["nodes"][nods[i].data.id].shape=="OCTAGON")
			{
				var score = (bypass["nodes"][nods[i].data.id].size - 20)/5;
			
				if(data!="") data += "-<>-";
				data += nods[i].data.label+"-<,>-"+nods[i].data.entrezid+"-<,>-"+score;
			}
		}
	
		document.getElementById("form:hiddenExtraData").value = data;
	
		document.getElementById('form:giveMeAScoreTab').click();
	}
}

function protect10()
{
	var nods = vis.edges();
	var data = document.getElementById("cofcutoff").value.replace(",", ".");
	if(!isNaN(data))
	{
		var dz = parseFloat(data);
		if(dz<=1 && dz>=-1) document.getElementById('form:conf').click();
	}
}




function mapToEdges()
{
//	$ = jQuery;
//	x = cnodes[i].split("-<,>-");
	
	var dataEdges = getTargetNodes();
	
	var eds = vis.edges();
	
	var idEdges = new Array();
	var corEdges = new Array();
	var edgesToRemove = new Array();
	var y = 0;
	
	for(var i=0;i<dataEdges.length;i++)
	{
		var sed = dataEdges[i].split("-<,>-");
		idEdges[i] = "e"+sed[0];
		corEdges[i] = sed[1];
	}
	
	for(var z=0;z<eds.length;z++)
	{
		if(eds[z].visible)
		{
			var found = false;
			for(var i=0;i<idEdges.length && !found;i++)
			{
				if(eds[z].data.id==idEdges[i])
				{
					found = true;
				}
			}
			
			if(!found)
			{
				edgesToRemove[y] = eds[z].data.id;
				y++;
			}
		}
	}

	removeEdges_aux(edgesToRemove);
	
	
	var bypass;
	
	if(bypassCurrent==null)
	{
		bypass = {
			nodes: {},
			edges: {}
		};
	}
	else
	{
		bypass = {
			nodes: {},
			edges: {}
		};
		bypass["nodes"] = bypassCurrent;
	}

	for(var i=0;i<idEdges.length;i++)
	{
		bypass["edges"][idEdges[i]] = { width: corEdges[i] };
	}

	bypassCurrent = bypass;
	vis.visualStyleBypass(bypass);
	
	enableEverything();
}

function removeEdges_aux(idEdges)
{
	var y=filteredEdges.length;
	
	for(var i=0;i<idEdges.length;i++)
	{
		filteredEdges[y] = idEdges[i];
		y++;
	}
	
	var nods = vis.nodes();
	
	var visNods = new Array();
	var visNum = new Array();
	
	y=0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			visNods[i] = nods[i].data.id;
			visNum[i] = 0;
		}
	}
	
	
	var edgs = vis.edges();

	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			var found = false;
			
			for(var g=0;g<idEdges.length && !found;g++)
			{
				if(edgs[i].data.id==idEdges[g]) found = true;
			}
			
			if(!found)
			{
				var idx = edgs[i].data.source;
				var idy = edgs[i].data.target;
				var xfound = false;
				var yfound = false;
				
				for(var g=0;g<visNods.length && !(xfound && yfound);g++)
				{
					if(visNods[g]==idx)
					{
						xfound = true;
						visNum[g]++;
					}
					else if(visNods[g]==idy)
					{
						yfound = true;
						visNum[g]++;
					}
				}
				
			}
		}
	}
	
	y=filteredNodes.length;
	
	for(var g=0;g<visNods.length;g++)
	{
		if(visNum[g]==0)
		{
			filteredNodes[y] = visNods[g];
			y++;
		}
	}

	setFilter();
}



function protect11()
{
	var nods = vis.edges();
	document.getElementById('form:conff').click();
}


//function changeTabSamCO()
//{
//	var x=document.getElementById("drop5").selectedIndex;
//	var y=document.getElementById("drop5").options;
//	
//	var x1 =  y[x].text;
//	
//	var select = ClearOptionsFast("drop6");
//	
//	var sdata = new Array(); 
//	
//	if(x1=="General")
//	{
//		select.setAttribute("disabled", "disabled"); 
//		sdata[0] = "Not valid";
//	}
//	else
//	{
//		select.removeAttribute("disabled");
//	}
//	
//	for(var i=0;i<sdata.length;i++)
//	{
//		var opt = document.createElement('option');
//		opt.value = sdata[i];
//		opt.id = sdata[i];
//	    opt.innerHTML = sdata[i];
//	    select.appendChild(opt);
//	}
//	
//}


function getCOType()
{
	var x=document.getElementById("drop5").selectedIndex;
	var y=document.getElementById("drop5").options;
	
	var x1 =  y[x].text;
	
	if(x1=="General")
	{
		return "cs";
	}
	else if(x1=="Differentation of mESC via embryonic bodies")
	{
		return "eb";
	}
	else if(x1=="Differentation hIPSC into cardic myocytes")
	{
		return "cm";
	}
	else if(x1=="Reprogramming of MEFs into iPSCs")
	{
		return "rmi";
	}
	else if(x1=="Differentation mES into neural ectoderm")
	{
		return "ne";
	}
	else if(x1=="Differentation mES into trophoblast")
	{
		return "t";
	}
	else if(x1=="Differentation mES into XEN lineage")
	{
		return "xen";
	}
	else return "mts";

}


function setEdgeDataCO()
{
	disableEverything();
	
	purgeFileData();
	var data = getCOType();
	
	var edgs = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			data += "-<>-"+edgs[i].data.id.replace("e","");
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}


function setEdgeDataCofCut()
{
	disableEverything();
	
	purgeFileData();
	
	var data = document.getElementById("cofcutoff").value.replace(",", ".")+"-<>-"+getCOType();
	
	var nods = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<nods.length;i++)
	{
		if(nods[i].visible)
		{
			data += "-<>-"+nods[i].data.id.replace("e","");
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}



function loadingScreenOpen()
{
	$ = jQuery;
	
	$("#loading-image").show();
}

function loadingScreenClose()
{
	$ = jQuery;
	
	$("#loading-image").hide();
}



function startTimer() {
	
	if(doneStartup!=true)
	{
		setTimeout(function(){startTimer()}, 500);
		var nods = vis.nodes();
		doneStartup=true;
		$("#loading-image").hide();
	}
}

function getDeeplink()
{
	$ = jQuery;
	
	$("#deeplink").show();
}


function connectedCoEBypass(weighted)
{
	if(bypassCurrent!=null && bypassShape!=null)
	{
		
		var dataEdges = getTargetNodes();
		
		var idEdges = new Array();
		var corEdges = new Array();
		
		for(var i=0;i<dataEdges.length;i++)
		{
			var sed = dataEdges[i].split("-<,>-");
			idEdges[i] = "e"+sed[0];
			corEdges[i] = sed[1];
		}
		
		
		
		
		
//		disableEverything();
		
		var nods = vis.nodes();
		
		var edgs = vis.edges();
		
		var bypass = bypassCurrent;
		
		for(var i=0;i<nods.length;i++)
		{
			if(nods[i].visible && (bypass["nodes"][nods[i].data.id].shape=="RECTANGLE" || bypass["nodes"][nods[i].data.id].shape=="OCTAGON"))
			{
				var foundScore = 0;
				
				for(var z=0;z<edgs.length;z++)
				{
					if(edgs[z].visible)
					{
						var otherId = null;
						
						if(edgs[z].data.source==nods[i].data.id)
						{
							otherId = edgs[z].data.target;
						}
						else if(edgs[z].data.target==nods[i].data.id)
						{
							otherId = edgs[z].data.source;
						}
						
						var adjust = null;
						
						for(var m=0;m<idEdges.length && adjust==null;m++)
						{
							if(edgs[z].data.id==idEdges[m])
							{
								adjust = parseInt(corEdges[m]);
							}
						}
						
						if(adjust==null) adjust = 0;
						
						if(otherId!=null && bypass["nodes"][otherId].shape=="ELLIPSE" && bypass["nodes"][otherId].size>20)
						{
							var score;
							
							if(weighted) score = adjust*((bypass["nodes"][otherId].size-20)/10);
							else score = adjust;
							
							foundScore += score;
						}
					}
				}
				
				if(foundScore>0)
				{
					bypass["nodes"][nods[i].data.id].size = 20 + foundScore*5;
					bypass["nodes"][nods[i].data.id].shape = "OCTAGON";
					if(bypassColor==null) bypass["nodes"][nods[i].data.id].color = "#009ACD";
					if(foundScore>6)
					{
						bypass["nodes"][nods[i].data.id].labelFontSize = 11 + (foundScore-1);
					}
				}
				else if(bypass["nodes"][nods[i].data.id].shape=="OCTAGON")
				{
					bypass["nodes"][nods[i].data.id] = { size: 20, shape: "RECTANGLE" };
				}
			}
		}
		
		vis.visualStyleBypass(bypass);
		
		enableEverything();
	}
	
}

function getCOTypeForF()
{
	var x=document.getElementById("drop5").selectedIndex;
	var y=document.getElementById("drop5").options;
	
	var x1 =  y[x].text;
	
	if(x1=="General")
	{
		return "cs";
	}
	else if(x1=="Differentation of mESC via embryonic bodies")
	{
		return "eb";
	}
	else if(x1=="Differentation hIPSC into cardic myocytes")
	{
		return "cm";
	}
	else if(x1=="Reprogramming of MEFs into iPSCs")
	{
		return "rmi";
	}
	else if(x1=="Differentation mES into neural ectoderm")
	{
		return "ne";
	}
	else if(x1=="Differentation mES into trophoblast")
	{
		return "t";
	}
	else if(x1=="Differentation mES into XEN lineage")
	{
		return "xen";
	}
	else return "mts";

}


function setEdgeDataCOForF()
{
	disableEverything();
	
	purgeFileData();
	var data = getCOTypeForF();
	
	var edgs = vis.edges();
	
	var y = 0;
	
	for(var i=0;i<edgs.length;i++)
	{
		if(edgs[i].visible)
		{
			data += "-<>-"+edgs[i].data.id.replace("e","");
			y++;
		}
	}
	
	document.getElementById("form:hiddenExtraData").value = data;
}

function protect12()
{
	var nods = vis.edges();
	document.getElementById('form:conffForFilter').click();
}

function protect13()
{
	var nods = vis.edges();
	document.getElementById('form:conffForFilterW').click();
}
//for resource use the same system used to determine for the individual nodes