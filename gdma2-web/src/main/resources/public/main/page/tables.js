var serverSessionId = sessionStorage.getItem("id");
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "name","render" : function(data, type, row){ 
			            	console.log(row.id);
			            	console.log('================= /n');
			            	console.log(row);
			            	return '<button class="btn btn-primary btn-xs editTable" data-serverid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewColumns" data-serverid="'+ row.id+ '"><i class="fa fa-columns"></i> Columns</button>'
			            	} 
			            }
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