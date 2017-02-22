//convenience method to give full address as a single field from an array containing multiple fields.
var getCompleteAddress = function(address, delimeter){
	var DEFAULT_ADDRESS_DELIMITED = ", ";
	if(!delimeter){
		delimeter = DEFAULT_ADDRESS_DELIMITED;
	}
	
	var completeAddress="";
	
	if(address && address.length > 0){
		for (var i=0; i < address.length; i++){
			if(address[i]){
				if(completeAddress){
					completeAddress += delimeter + address[i];
				} else {
					completeAddress += address[i];
				}
			}
		}
	}

	return completeAddress;
};

//convenience method to format numbers with commas at thousand place
var formatNumber = function(n, decimalPlaces) {
	if(decimalPlaces){
		n = parseFloat(n).toFixed(decimalPlaces);	
	}
    var parts=n.toString().split(".");
    return parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",") + (parts[1] ? "." + parts[1] : "");
};

//convenience method for padding a number with leading zeroes
//http://stackoverflow.com/questions/2998784/how-to-output-integers-with-leading-zeros-in-javascript
//Usage: padNumber(input, 6, "^"); or padNumber(input, 6) == padNumber(input, 6, "0");
function padNumber(input, size, padding){
	var padding=padding?padding:"0";
	var s = input+"";
    while (s.length < size) s = padding + s;
    return s;
}

//convenience method to remove control characters such as tab, cr/lf etc from input
var removeControlCharacters = function (input){
	return input.replace(/[\x00-\x1F\x7F-\x9F]/g, "");
};