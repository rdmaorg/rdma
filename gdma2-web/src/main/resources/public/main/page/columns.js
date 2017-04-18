var tableSessionId = sessionStorage.getItem("idTable");;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" }
			        ]
	};

	$('#tbl_column').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.column.item, {id: tableSessionId}), 
		complete: function(){
	//		hideLoading();
		}
	});
};


$(document).ready(function(){
	configureDataTable();
	

});