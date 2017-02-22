// convenience method that remove the mentioned array of values from target array and return a new array 
var removeItems = function(target, values) {
	var temp = []; 
	var i = 0; 
	$.each(target, function(index, t) {
		var remove = false; 
		$.each(values, function(index, v) {
			if (t==v) {
				remove = true; 
				return !remove; 
			}
		} ); 
		if (!remove) {
			temp[i++] = t; 
		}
	} ); 
	return temp; 
}

