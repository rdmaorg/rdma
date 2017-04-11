var savedServerID = -1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
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
				        data: JSON.stringify({id:savedServerID,name:name,username:username,password:password,connectionUrl:url,prefix:prefix,active:active,alias:"","connectionType":{"id":connectionType},"tables":[],"connected":false,"lastError":""}),
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
 		viewServer(btn.data('serverid'));
	})
};

var syncTables = function(serverId){
	
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.table.sync_table_server,{serverId: serverId}),
        contentType: "application/json; charset=utf-8",
    }).done(function(data){
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
}

var viewServer = function(serverId) {
	syncTables(serverId);
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
	
	// Cookie help tutorial    
    //var exist = $.cookie('endHelper');
    //if (exist == "1"){
    //} else {
    //    bootstro.start(".bootstro", {
    //        onExit : function(params)
    //        {
    //        	$.cookie('endHelper', '1');
    //        },
    //   }); 
    //}
	
});