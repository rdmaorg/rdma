var serverSessionId = sessionStorage.getItem("id");
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" }
			        ]
	};

	$('#tbl_tables').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.table.list_for_server, {serverId: serverSessionId}),
		dataSrc: "",
		complete: function(){
//		hideLoading();
		}
	});
};


$(document).ready(function(){
	configureDataTable();
});