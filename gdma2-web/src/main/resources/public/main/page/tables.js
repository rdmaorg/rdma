var serverSessionId = sessionStorage.getItem("id");
var serverSessionName = sessionStorage.getItem("name");
var selectedTableId = -1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"columnDefs": [ { className: "text-center", "targets": [4] },{ "orderable": false, "targets": 4 }  ],
			serverside: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "alias" },
			            { "data": "active" },			            
			            { "data": "name","render" : function(data, type, row){ 
			            	return '<button class="btn btn-primary btn-xs editAccess" data-tableid="'+ row.id+ '" data-tablename="'+ row.name +'" ><i class="fa fa-pencil-square-o"></i> Edit Access</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewColumns" data-tableid="'+ row.id+ '" data-tablename="'+ row.name +'"><i class="fa fa-columns"></i> Columns</button>'
			            	} 
			            }
			        ]
	};

	$('#tbl_tables').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.table.table, {id: serverSessionId}),
		complete: function(){
//		hideLoading();
			associateEditAcces();
			associateViewTable();
		}
	});
};

var associateEditAcces = function(){
	$(".editAccess").click(function(){
		var btn = $(this);
		selectedTableId = btn.data('tableid');
		selectedTableName = btn.data('tablename');
		initiateModalUserAccess();
		$("#modalUserAccess").find('form').trigger('reset');
		$('#modalUserAccess').modal('show');
	});
	
}

//View table columns
var associateViewTable = function(){
	$('.viewColumns').click(function(){
 		var btn = $(this);
 		viewTable(btn.data('tableid'),btn.data('tablename'));
	})
};

var viewTable = function(serverId,tableName) {
	sessionStorage.setItem("idTable",serverId);
	sessionStorage.setItem("nameTable",tableName);
	window.location.href = "columns";
}

$(document).ready(function(){
	configureDataTable();	

    $("#serverName").html(serverSessionName);
	
});