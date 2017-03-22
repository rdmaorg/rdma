var showLoading = function(where){
	var selector = '#loading-spinner';
	if(where){
		selector = where;
	}
	if(!$(selector).is(":visible")){
		$(selector).show();
	}
};

var hideLoading = function(where){
	var selector = '#loading-spinner';
	if(where){
		selector = where;
	}
	if($(selector).is(":visible")){
		$(selector).hide();
	}
};
