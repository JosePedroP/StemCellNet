function startup()
{
	var params = document.URL.split("?");
	params = params[1].split("&");

	var lis = "";
	for(var i=0;i<params.length;i++)
	{
		var p = params[i].split("=");
		if(i>0) lis += " ";
		lis += p[1];
	}
	
	document.getElementById('form:searchText').value = lis;
	
	document.getElementById('form:clicky').click();
}