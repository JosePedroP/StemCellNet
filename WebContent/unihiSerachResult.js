var current;
var proteins;
var proteinsRef;
var empty2;
var empty3;
var empty4;

function startup()
{
	current = new Array();
	
	empty2 = true;
	empty3 = true;
	empty4 = true;
	
	var paras = new Array();
	var params = document.URL.split("?");
	params = params[1].split("&");
	
	var lis = "";
	
	for(var i=0;i<params.length;i++)
	{
		var p = params[i].split("=");
		paras[i] = p[1];
		current[i] = true;
		if(i>0) lis += "-<?>-";
		lis += p[1];
	}
	
	document.getElementById("form:hiddenProteinsName").value = lis;
	
	document.getElementById('form:update1').click();
	
	document.getElementById("deeplink").innerHTML = "<br/>"+document.URL;
	
//	http://localhost:8080/unihi/unihiSearchResult.jsf?prot1=GADD45A&prot2=PARK2&prot3=SNCA
}

function getids()
{
	var lis = document.getElementById("form:hiddenProteinsIds").value;
	document.getElementById("form:hiddenProteins").value = lis;
	proteins = lis.split("-<?>-");
	proteinsRef = new Array();
	
	for(var i=0;i<proteins.length;i++)
	{
		proteinsRef[i] = proteins[i].split(",")[0];
	}
	
	if(proteins.length>0)
	{
		$ = jQuery;
		
		$("#netb").show();
		$("#netd").show();
		$("#neta").show();
		$("#netx").show();
		$("#deep").show();
		updatePorteinList();
	}
}

function updatePorteinList()
{
//	alert("updatePorteinList Start");
	
	var z = 0;
	var lis = "";
	
	$ = jQuery;
	
	for (var i=0;i<proteins.length;i++)
	{
		var ba = "#chk_"+proteinsRef[i];
		var $object = $(ba);
		if($object.length>0)
		{
			if(document.getElementById("chk_"+proteinsRef[i]).checked)
			{
				current[i] = true;
				if(z>0) lis += "-<?>-";
				lis += proteins[i];
				z++;
			}
			else current[i] = false;
		}
		else current[i] = false;
	}
	
	document.getElementById("form:hiddenProteins").value = lis;
	
	if(z>35 || z==0) $("#netb").attr("disabled", "disabled");
	else $("#netb").removeAttr("disabled");
	
//	alert(lis);
//	
//	alert("updatePorteinList end");
}

function checkIfChange()
{
//	alert("checkIfChange Start");
	
	var res = false;
	for (var i=0;res==false && i<proteins.length;i++)
	{
//		alert("chk_"+proteins[i]+" = "+document.getElementById("chk_"+proteins[i]).checked);
//		alert(document.getElementById("chk_"+proteins[i]).checked+"!="+current[i]);
		if(document.getElementById("chk_"+proteinsRef[i]).checked!=current[i]) res = true;
	}

//	alert("checkIfChange end");
	
	return res;
}

function setLink()
{ //Add the s=size tag in here
	var ex = "";
	
	for (var i=0;i<current.length;i++)
	{
		if(current[i])
		{
			if(ex=="") ex += "?";
			else ex += "&";
			ex += "c="+proteins[i].split(",")[0];
		}	
	}
	document.getElementById("linky").href="stemCellNetNetwork.jsf"+ex;
}

function clickLinky()
{
	setLink();
	window.location = document.getElementById('linky').href; 
//	document.getElementById('linky').click();
}

function download()
{
	var da = "";
	
	$ = jQuery;
	
	for (var i=0;i<proteins.length;i++)
	{
		var ba = "#chk_"+proteinsRef[i];
		var $object = $(ba);
		if($object.length>0)
		{
			if(document.getElementById("chk_"+proteinsRef[i]).checked)
			{
				if(da=="") da = proteinsRef[i];
				else da += "-<>-"+proteinsRef[i];
			}
		}
	}
	
	if(da!="")
	{
		document.getElementById("form:hiddenExtraData").value = da;
		document.getElementById('form:giveMeATXT').click();
	}
	
}

function downloadAll()
{
	var da = "";
	
	$ = jQuery;
	
	for (var i=0;i<proteins.length;i++)
	{
		var ba = "#chk_"+proteinsRef[i];
		var $object = $(ba);
		if($object.length>0)
		{
//			if(document.getElementById("chk_"+proteinsRef[i]).checked)
//			{
			if(da=="") da = proteinsRef[i];
			else da += "-<>-"+proteinsRef[i];
//			}
		}
	}
	
//	alert(da);
	
	if(da!="")
	{
		document.getElementById("form:hiddenExtraData").value = da;
		document.getElementById('form:giveMeATXT').click();
	}
	
}

function downloadXGMML()
{
	var da = "";
	
	$ = jQuery;
	
	for (var i=0;i<proteins.length;i++)
	{
		var ba = "#chk_"+proteinsRef[i];
		var $object = $(ba);
		if($object.length>0)
		{
			if(da=="") da = proteinsRef[i];
			else da += "-<>-"+proteinsRef[i];
		}
	}
	
//	alert(da);
	
	if(da!="")
	{
		document.getElementById("form:hiddenExtraData").value = da;
		document.getElementById('form:giveMeAXGMML').click();
	}
	
}

function getDeeplink()
{
	$ = jQuery;
	
	$("#deeplink").show();
}

function checkAllBoxes()
{
	$ = jQuery;
	
	var b = $("#allbox").is(':checked');
	
	$(".protBox").attr('checked', b);
	
	updatePorteinList();
}
