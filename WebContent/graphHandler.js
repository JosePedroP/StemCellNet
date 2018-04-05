function loadGraph_temp()
{
	

	alert("lets go");
	var lis = document.getElementById("form:tab:graphData").value;
	
	alert(lis);
	
	//alert("PERFECT MACHINE PERFECT JUSTICE");
	var div_id = "cytoscapeweb";
	
	var networ_json = {
		dataSchema: {
			nodes: [ { name: "label", type: "string" },
			         { name: "foo", type: "string" }
			],
			edges: [ { name: "label", type: "string" },
			         { name: "bar", type: "string" }]
		},
		data: {
			nodes: [ { id: "1", label: "1" }, { id: "2", label: "2" }, { id: "3", label: "3" } ],
			edges: [ { id: "2to1", target: "1", source: "2", label: "2to1" },
			         { id: "1to2", target: "1", source: "3", label: "1to2" }, { id: "2to3", target: "2", source: "3", label: "2to3" } ]
		}
	};
	//alert("NO GOOD NO EVIL");
	var options = {
		// where you have the Cytoscape Web SWF
		swfPath: "swf/CytoscapeWeb",
		// where you have the Flash installer SWF
		flashInstallerPath: "swf/playerProductInstall"
	};
	//alert("NO NEED NO WANT");
	vis = new org.cytoscapeweb.Visualization(div_id, options);
	//alert("NO DESIRE NO LACK");
	vis.ready(function() {
		// add a listener for when nodes and edges are clicked
		vis.addListener("click", "nodes", function(event) {
			handle_click(event);
		})
		.addListener("click", "edges", function(event) {
			handle_click(event);
		});
        
		function handle_click(event) {
			var target = event.target;
             
			clear();
			print("event.group = " + event.group);
			for (var i in target.data) {
				var variable_name = i;
				var variable_value = target.data[i];
				print( "event.target.data." + variable_name + " = " + variable_value );
			}
		}
        
		function clear() {
			document.getElementById("note").innerHTML = "";
		}
    
		function print(msg) {
			document.getElementById("note").innerHTML += "<p>" + msg + "</p>";
		}
	});
//	alert("THE CIRCUIT I ONE");
	
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
				defaultValue: 35
			},
			color: {
				discreteMapper: {
					attrName: "id",
					entries: [                                   
					          { attrValue: "1", value: "#CCCCCC" },                           
					          { attrValue: "2", value: "#FFFF00" },                  
					          { attrValue: "3", value: "#FF0000" }
					          ]
				}
			},
			labelHorizontalAnchor: "center"
		}
	};
	//alert("AND ONE IS THE MACHINE");
	vis.draw({ 
		network: networ_json,
		visualStyle: visual_style,
		edgeLabelsVisible: true });
	
	//alert("I SEE ALL");
}


function loadGraph()
{
	var lis = document.getElementById("form:tab:graphData").value;
	alert(lis);
	
	var div_id = "cytoscapeweb";
	
	var options = {
		// where you have the Cytoscape Web SWF
		swfPath: "swf/CytoscapeWeb",
		// where you have the Flash installer SWF
		flashInstallerPath: "swf/playerProductInstall"
	};
	//alert("NO NEED NO WANT");
	vis = new org.cytoscapeweb.Visualization(div_id, options);
	//alert("NO DESIRE NO LACK");
	vis.ready(function() {
		// add a listener for when nodes and edges are clicked
		vis.addListener("click", "nodes", function(event) {
			handle_click(event);
		})
		.addListener("click", "edges", function(event) {
			handle_click(event);
		});
        
		function handle_click(event) {
			var target = event.target;
             
			clear();
			print("event.group = " + event.group);
			for (var i in target.data) {
				var variable_name = i;
				var variable_value = target.data[i];
				print( "event.target.data." + variable_name + " = " + variable_value );
			}
		}
        
		function clear() {
			document.getElementById("note").innerHTML = "";
		}
    
		function print(msg) {
			document.getElementById("note").innerHTML += "<p>" + msg + "</p>";
		}
	});
//	alert("THE CIRCUIT I ONE");
	
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
					attrName: "color",
					entries: [                                   
					    { attrValue: "grey", value: 75 },                           
					    { attrValue: "yellow", value: 40 },                  
						{ attrValue: "red", value: 40 }
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
				
		}
		
	};
	//alert("AND ONE IS THE MACHINE");
	vis.draw({ 
		network: lis,
		visualStyle: visual_style,
		edgeLabelsVisible: true });
	
	//alert("I SEE ALL");
}