var configureDataTable = function(){
	var config={
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "username" },
			            { "data": "password" },
			            { "data": "connectionUrl" },
			            { "data": "connectionType.name", "defaultContent": "" },
			            { "data": "prefix" },
			            { "data": "active" }
			        ]
	};
	
	$('#tbl_server').configureDataTable(config, {url: restUri.server.table, complete: function(){
//		hideLoading();
	}});
};


$(document).ready(function(){
	configureDataTable();
});