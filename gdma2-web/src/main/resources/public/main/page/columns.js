var tableSessionId = 1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" }
			        ]
	};

	$('#tbl_column').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.column.table, {id: tableSessionId}), 
		complete: function(){
	//		hideLoading();
		}
	});
};


$(document).ready(function(){
	configureDataTable();
	

});