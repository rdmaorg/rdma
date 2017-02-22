var getContextPath = function(){
	var contextDelimeter="/";
	var start = 0;
	var end = 4;
	
	var currentURL = window.location.href;
	currentURL = currentURL.split(contextDelimeter).slice(start, end).join(contextDelimeter);
	console.log("WebUtil: context path: " + currentURL);
	return currentURL;
};


var getRestBaseUri = function(){
	return getContextPath()+"/rest";
};

//convenience method that will parse a url and get the value of mentioned query parameter  
//http://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
var getParameterByName = function (name) {
	var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
	return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

//Convenience method to filter path variables in a url from an object.
//The method allows multiple path variables to be filtered with properties in the object
//Each property must be uniquely named
//E.g., if url is http://www.google.com/{search}/further/path and object is {search: "WhatEver"}, then the resultant url will be http://www.google.com/WhatEver/further/path 
var mapPathVariablesInUrl = function(url, pathVars){
    for( var p in pathVars){
        url = url.replace("{"+p+"}", pathVars[p]);
    }
    return url;
};

//convenience method that will populate the mentioned select control with provided list of options 
var populateSelectControl = function(control, options) {
	$(control).html(''); 
	$.each(options, function(i, o) {
		var str = $(control).html() + '<option>' + o + '</option>';
		$(control).html(str); 
	}); 
	console.log(control + ': ' + $(control).html());
}


