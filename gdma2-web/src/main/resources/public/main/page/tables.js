var serverSessionId = sessionStorage.getItem("id");
var selectedTableId = -1;
var selectedTableName = "";
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			serverside: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "alias" },
			            { "data": "active" },			            
			            { "data": "name","render" : function(data, type, row){ 
			            	return '<button class="btn btn-primary btn-xs editAccess" data-tableid="'+ row.id+ '" '+ 'data-tablename="'+ row.name + '" ><i class="fa fa-pencil-square-o"></i> Edit Access</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewColumns" data-tableid="'+ row.id + '"><i class="fa fa-columns"></i> Columns</button>'
			            	} 
			            }
			        ]
	};
	$('#tbl_tables').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.table.table, {id: serverSessionId}),
		dataSrc: "",
		complete: function(){
//		hideLoading();
			associateEditAcces();
		}
	});
};

var associateEditAcces = function(){
	$(".editAccess").click(function(){
		var btn = $(this);
		selectedTableId = btn.data('tableid');
		selectedTableName = btn.data('tablename');
		$("#modalUserAccess").find('form').trigger('reset');
		$('#modalUserAccess').modal('show');
		initiateModalUserAccess();
	});
	
}
$(document).ready(function(){
	configureDataTable();	

	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.server.item,{serverId: serverSessionId}),
        data: { get_param: 'name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	var serverName = data.name;
    	$("#serverName").html(serverName);
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });	
	
});