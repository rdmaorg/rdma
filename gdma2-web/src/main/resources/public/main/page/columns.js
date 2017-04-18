var tableSessionId = sessionStorage.getItem("idTable");
var tableSessionName = sessionStorage.getItem("nameTable");
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "columnType" },
			            { "data": "displayed" },
			            { "data": "allowInsert" },
			            { "data": "allowUpdate" },
			            { "data": "nullable" },
			            { "data": "active" }
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
	
    $("#tableName").html(sessionStorage.getItem("nameTable"));
	
});