var handleError = function(where, e){
	var component = $(where);
	if(e && e.responseText){
		var error = JSON.parse(e.responseText);
		if(error && error.error){
			component.alertMessage({type:"error", message: "<strong>Error! </strong>" + error.error});
		}
	} else {
		component.alertMessage({type:"error", message: "<strong>Error! </strong>" + JSON.stringify(e)});
	}
};