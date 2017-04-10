var serverSessionId = sessionStorage.getItem("id");
var selectedTableId = -1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "alias" },
			            { "data": "active" },			            
			            { "data": "name","render" : function(data, type, row){ 
			            	return '<button class="btn btn-primary btn-xs editAccess" data-tableid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit Access</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewColumns" data-tableid="'+ row.id+ '"><i class="fa fa-columns"></i> Columns</button>'
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
		configureUserAccessDatatable();
		$("#modalUserAccess").find('form').trigger('reset');
		$('#modalUserAccess').modal('show');
	});
	
}
$(document).ready(function(){
	configureDataTable();	
	//Get Connection types
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
	
	getServerDataLeftPanel();
	
});

var getServerDataLeftPanel = function(){
	//Get Connection types
	$.ajax({
	    type: "get",
	    url: restUri.connection.list,
	    data: { get_param: 'id,name' },
	    contentType: "application/json; charset=utf-8",
	    dataType: 'json'
	}).done(function(data){
		$.each(data, function(i, connection) {
		    $("<option value='" + connection.id + "'>" + connection.name + "</option>").appendTo("#connectionType");
		});
	}).fail(function(e){
		handleError('#global-alert', e);
	}).always(function(){
		hideLoading();
	});
	
	// Get Server list
	showLoading();
	$.ajax({
	    type: "get",
	    asyn: false,
	    url: restUri.server.list,
	    data: { get_param: 'id' },
	    contentType: "application/json; charset=utf-8",
	    dataType: 'json'
	}).done(function(data){
		$.each(data, function(i, server) {
			$("<li id='server" + server.id + "' class='treeview'><a href=''><i class='fa fa-database'></i><span> " + server.name + " </span><span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul class='treeview-menu'></ul></li>").appendTo(".sidebar-menu");
			function tablesId() {
				showLoading();
	    		$.ajax({
	    	        type: "get",
	    	        url: mapPathVariablesInUrl(restUri.table.list_for_server_active, {serverId: server.id}),
	    	        data: { get_param: 'id' },
	    	        success: function(data, textStatus, jqXHR) {
	    			$.each(data, function(i, table) {
	    	            var serverId = table.server.id;
	    			    $("<li><a href='" + restUri.table.list_for_server + "'><i class='fa fa-table'></i><span> " + table.name + " </span></a></li>").appendTo("#server" + serverId + " .treeview-menu");
	    			});        			
	    	        },
	    	        contentType: "application/json; charset=utf-8",
	    	        dataType: 'json'
	    	    }).fail(function(e){
	    	    	handleError('#global-alert', e);
	    	    }).always(function(){
	    	    	hideLoading();
	    	    });
			}
			$("#server" + server.id + "").one("click",function(){
				tablesId();
			});
		});
		
	}).fail(function(e){
		handleError('#global-alert', e);
	}).always(function(){
		hideLoading();
	});
}