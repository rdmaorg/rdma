var tableSessionId = sessionStorage.getItem("idTable");
var tableSessionName = sessionStorage.getItem("nameTable");
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columnDefs": [ { className: "text-center", "targets": [8] },{ "orderable": false, "targets": 8 }  ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "columnType" },
			            { "data": "displayed" },
			            { "data": "allowInsert" },
			            { "data": "allowUpdate" },
			            { "data": "nullable" },
			            { "data": "active" },			            
			            { "data": "name","render" : function(data, type, row){ 
			            	return '<button class="btn btn-primary btn-xs editAccess" data-tableid="'+ row.id+ '" data-tablename="'+ row.name +'" ><i class="fa fa-pencil-square-o"></i> Edit Access</button>'
			            	} 
			            }
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