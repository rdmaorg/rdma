var savedServerID = -1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columnDefs": [ { "orderable": false, "targets": 8 } ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "username" },
			            { "data": "password" },
			            { "data": "connectionUrl" },
			            { "data": "connectionType.name", "defaultContent": "" },
			            { "data": "prefix" },
			            { "data": "active" },
			            { "data": "active","render" : function(data, type, row){ 	
			            	return '<button class="btn btn-primary btn-xs editServer" data-serverid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-warning btn-xs deleteServer" data-serverid="'+ row.id+ '"><i class="fa fa-trash-o"></i> Delete</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewServer" data-serverid="'+ row.id+ '"><i class="fa fa-table"></i> Tables</button>'
			            	} 
			            }
			        ]
	};

	tblServerList = $('#tbl_server').configureDataTable(config, {
		url: restUri.server.table,
		complete: function(){
			//hideLoading();
			associateDeleteServer();
			associateEditServer();
			associatePostServer();
			associateViewServer();
		},
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
		}
	});
	
	tblServerList.off( 'responsive-display');
	tblServerList.on( 'responsive-display', function ( e, datatable, row) {
		associateDeleteServer();
		associateEditServer();
		associatePostServer();
		associateViewServer();
	} );
};

//Edit server data
var associateEditServer = function(){
	$('.editServer').click(function(){
 		var btn = $(this);
//		var btn = element;
//		console.log("Deleting Server Id " + btn.data('serverid'));
		editServer(btn.data('serverid'));
	})
};

var editServer = function(serverId) {	
	showLoading();
	savedServerID = serverId;
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.server.item,{serverId: serverId}),
        data: { get_param: 'id,name,username,password,connectionUrl,connection.id,prefix,active' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
		$("#name").val(data.name);
		$("#username").val(data.username);
		$("#password").val(data.password);
		$("#url").val(data.connectionUrl);
		$("#connectionType").val(data.connectionType.id);
		$("#prefix").val(data.prefix);
		$("#active > [value=" + data.active + "]").prop('selected', true);
		$('#modalServer').modal('show');
		$("#modalServer").on('shown.bs.modal', function () {
            $("#name").focus();
		});
		associatePostServer();
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });		 
}

//Delete server
var associateDeleteServer = function(){
	$('.deleteServer').confirmation({
		placement: "left",
		btnOkLabel: "Delete Server",
		onConfirm : function(event, element) {
//			var btn = $(this);
			var btn = element;
//			console.log("Deleting Server Id " + btn.data('serverid'));
			deleteServer(btn.data('serverid'));
		}
	});
};

var deleteServer = function(serverId) {
	serverId
	$.ajax({
        type: "delete",
        url: mapPathVariablesInUrl(restUri.server.del,{serverId: serverId}),
        contentType: "application/json; charset=utf-8"
    }).done(function(data){
    	var table = $('#tbl_server').DataTable();
    	table.destroy();
    	configureDataTable();
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });	
}		

//Post server
var associatePostServer = function(){	
	var form = $("#newServer");
	form.validate();
	$('#Save-server').click(function(e){		
		if (form.valid()) {
			$(this).confirmation({
				placement: "left",
				btnOkLabel: "Save Server",
				onConfirm : function(event, element) {			
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
				        data: JSON.stringify({id:savedServerID,name:name,username:username,password:password,connectionUrl:url,prefix:prefix,active:active,alias:"AAA","connectionType":{"id":connectionType},"tables":[],"connected":false,"lastError":""}),
				        contentType: "application/json; charset=utf-8"
				    }).done(function(data){
				    	var table = $('#tbl_server').DataTable();
				    	table.destroy();
				    	configureDataTable();
				    	savedServerID = -1;
				    }).fail(function(e){
				    	handleError('#global-alert', e);
				    }).always(function(){
				    	hideLoading();
						$('#modalServer').modal('hide');
				    });
				}
			});	
			$(this).confirmation('show');
		} else {
			$(this).confirmation('destroy');
		}
		e.preventDefault();
	});
};

//View server tables
var associateViewServer = function(){
	$('.viewServer').click(function(){
 		var btn = $(this);
//		var btn = element;
		console.log("View Server Id " + btn.data('serverid'));
 		viewServer(btn.data('serverid'));
	})
};

var viewServer = function(serverId) {
	sessionStorage.setItem("id",serverId);
	window.location.href = "tables";
}

$(document).ready(function(){	

	configureDataTable();
	
	$("#addServer").click(function(){
		$("#modalServer").find('form').trigger('reset');
		$("#modalServer").on('shown.bs.modal', function () {
            $("#name").focus();
		});
	});
	
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