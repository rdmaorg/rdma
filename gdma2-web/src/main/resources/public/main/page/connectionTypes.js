var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "connectionClass" },
			            { "data": "sqlGetTables" }
			        ]
	};

	$('#tbl_connection').configureDataTable(config, {url: restUri.connection.table, complete: function(){
//		hideLoading();
	}});
};


$(document).ready(function(){
	configureDataTable();
});