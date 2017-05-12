var savedServerID = -1;
var configureDataTable = function(){
	
	var config={
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columnDefs": [ { className: "text-center", "targets": [6,7] },
			                { "orderable": false, "targets": 7 } ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "username" },
			            { "data": "connectionUrl" },
			            { "data": "connectionType.name", "defaultContent": "" },
			            { "data": "prefix" },
			            { "data": "active" ,"render" : function(data, type, row){ 
				            	if(row.active){
				            		return '<i class="fa fa-check-circle active-icon" aria-hidden="true"></i>';
				            	} else {
				            		return '<i class="fa fa-times-circle inactive-icon" aria-hidden="true"></i>';
				            	}
		            		} 
			            },			        
			            { "data": "active","render" : function(data, type, row){ 	
			            	return '<button class="btn btn-primary btn-xs editServer" data-serverid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewServer" data-serverid="'+ row.id+ '" data-servername="'+ row.name + '"><i class="fa fa-table"></i> Tables</button>'
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
    	var validator = $( "#newServer" ).validate();
    	validator.destroy();
		$("#name").val(data.name);
		$("#username").val(data.username);
		$("#password").val(data.password);
		$("#url").val(data.connectionUrl);
		$("#connectionType").val(data.connectionType.id);
		$("#prefix").val(data.prefix);
		$("#active").bootstrapSwitch('state',data.active);
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
    	table.draw(false);
    	$("#global-success").slideDown(500);
    	window.setTimeout(function() {
    		$("#global-success").slideUp(500);
    	}, 4000);
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
				title : "Save Server?",
				btnOkLabel : "Yes",
				onConfirm : function(event, element) {			
					showLoading();
					var name = $("#name").val();
					var username = $("#username").val();
					var password = $("#password").val();
					var url = $("#url").val();
					var connectionType = $("#connectionType").val();
					var prefix = $("#prefix").val();
					var active = $("#active").bootstrapSwitch('state');
					$.ajax({
				        type: "post",
				        url: restUri.server.save,
				        data: JSON.stringify({id:savedServerID,name:name,username:username,password:password,connectionUrl:url,prefix:prefix,active:active,"connectionType":{"id":connectionType},"tables":[],"connected":false,"lastError":""}),
				        contentType: "application/json; charset=utf-8"
				    }).done(function(data){
				    	var table = $('#tbl_server').DataTable();
				    	table.draw(false);
				    	$("#global-success").slideDown(500);
				    	window.setTimeout(function() {
				    		$("#global-success").slideUp(500);
				    	}, 4000);
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
 		viewServer(btn.data('serverid'),btn.data('servername'));
	})
};

var syncTables = function(serverId){
	showLoading();
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.table.sync_table_server,{id: serverId}),
        contentType: "application/json; charset=utf-8",
    }).done(function(data){
    }).complete(function(data){
    	completeSyncServer();
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
}

var viewServer = function(serverId,serverName) {
	syncTables(serverId);
	sessionStorage.setItem("id",serverId);
	sessionStorage.setItem("name",serverName);
}

var completeSyncServer = function(serverId,serverName){
	window.location.href = "tables";
}

$(document).ready(function(){	

	configureDataTable();
	
	$("#active").bootstrapSwitch();
	
	$("#addServer").click(function(){
		$("#modalServer").find('form').trigger('reset');
        $("#name").focus();
	});
	
	$("#modalServer").on('hide.bs.modal', function() {
		var validator = $( "#newServer" ).validate();
		validator.destroy();
    })
	
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