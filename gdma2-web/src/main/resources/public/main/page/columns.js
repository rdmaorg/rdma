var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
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
	
	// Get Connection types
	 $.ajax({
        type: "get",
        url: "http://localhost:8080/gdma2/rest/connection/list",
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
	
	// Post new server
	$("#Save-server").click(function(event){
		showLoading();
		var name = $("#name").val();
		var username = $("#username").val();
		var password = $("#password").val();
		var url = $("#url").val();
		var connectionType = $("#connectionType").val();
		var prefix = $("#prefix").val();
		var active = $("#active").val();

	    $.ajax({
	        type: "post",
	        url: restUri.server.save,
	        data: JSON.stringify({name:name,username:username,password:password,connectionUrl:url,prefix:prefix,active:active,alias:"AAA","connectionType":{"id":connectionType},"tables":[],"connected":false,"lastError":""}),
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json'
	    }).done(function(data){
			$('#modalServer').modal('hide');
	    }).fail(function(e){
	    	handleError('#global-alert', e);
	    }).always(function(){
	    	hideLoading();
	    });	
	    event.preventDefault();
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
        	        url: mapPathVariablesInUrl(restUri.table.list_for_server, {serverId: server.id}),
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