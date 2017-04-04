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
			            	return '<button class="btn btn-primary btn-xs editTable" data-serverid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit Access</button>'
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
});