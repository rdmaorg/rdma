var savedUserID = -1;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
	        stateSave: false,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columnDefs": [ { "orderable": false, "targets": 8 } ],
			"columns": [
			            { "data": "id" },
			            { "data": "firstName" },
			            { "data": "lastName" },
			            { "data": "userName" },
			            { "data": "domain" },
			            { "data": "admin" ,"render" : function(data, type, row){ 
				            	if(row.admin){
				            		return '<i class="fa fa-check-circle active-icon" aria-hidden="true"></i>';
				            	} else {
				            		return '<i class="fa fa-times-circle inactive-icon" aria-hidden="true"></i>';
				            	}
			            	} 
			            },
			            { "data": "locked" ,"render" : function(data, type, row){ 
				            	if(row.locked){
				            		return '<i class="fa fa-check-circle active-icon" aria-hidden="true"></i>';
				            	} else {
				            		return '<i class="fa fa-times-circle inactive-icon" aria-hidden="true"></i>';
				            	}
			            	} 
			            },	
			            { "data": "active" ,"render" : function(data, type, row){ 
				            	if(row.active){
				            		return '<i class="fa fa-check-circle active-icon" aria-hidden="true"></i>';
				            	} else {
				            		return '<i class="fa fa-times-circle inactive-icon" aria-hidden="true"></i>';
				            	}
			            	} 
			            },			        
			            { "data": "active","render" : function(data, type, row){ 	
			            	return '<button class="btn btn-primary btn-xs editUser" data-userid="'+ row.id +'"><span class="glyphicon glyphicon-pencil"></span> Edit</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-warning btn-xs deleteUser" data-userid="'+ row.id+ '"><span class="glyphicon glyphicon-remove"></span> Delete</button>';
			            	} 
			            }
			        ]
	};

	tblUserList = $('#tbl_user').configureDataTable(config, {
		url: restUri.user.table,
		complete: function(){
			//hideLoading();
			associateDeleteUser();
			associateEditUser();
			associatePostUser();
		},
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
		}
	});
	
	tblUserList.off( 'responsive-display');
	tblUserList.on( 'responsive-display', function ( e, datatable, row) {
		associateDeleteUser();
		associateEditUser();
		associatePostUser();
	} );
};

//Edit user data
var associateEditUser = function(){
	$('.editUser').click(function(){
 		var btn = $(this);
//		var btn = element;
//		console.log("Deleting Server Id " + btn.data('username'));
		editUser(btn.data('userid'));
	})
};

var editUser = function(userId) {	
	showLoading();
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.user.item,{userId: userId}),
        data: { get_param: 'id,firstName,lastName,userName,password,domain,admin,locked,active' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){		
		$("#name").val(data.firstName);
		$("#lastname").val(data.lastName);
		$("#username").val(data.userName);
		$("#password").val(data.password);
		$("#url").val(data.domain);
		$("#admin > [value=" + data.admin + "]").prop('selected', true);
		$("#locked > [value=" + data.locked + "]").prop('selected', true);
		$("#active > [value=" + data.active + "]").prop('selected', true);
		$('#modalUser').modal('show');
		$("#modalUser").on('shown.bs.modal', function () {
            $("#name").focus();
		});
		savedUserID = data.id;
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });		 
}

//Delete user
var associateDeleteUser = function(){
	$('.deleteUser').confirmation({
		placement: "left",
		btnOkLabel: "Delete User",
		onConfirm : function(event, element) {
//			var btn = $(this);
			var btn = element;
//			console.log("Deleting Server Id " + btn.data('serverid'));
			deleteUser(btn.data('userid'));
			event.preventDefault();
		}
	});
};

var deleteUser = function(userId) {
	$.ajax({
        type: "delete",
        url: mapPathVariablesInUrl(restUri.user.del,{userId: userId}),
        contentType: "application/json; charset=utf-8"
    }).done(function(data){
    	var table = $('#tbl_user').DataTable();
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

//Post user
var associatePostUser = function(){	
	var form = $("#newUser");
	form.validate();
	$('#Save-user').click(function(e){		
		if (form.valid()) {
			$(this).confirmation({
				placement: "left",
				btnOkLabel: "Save User",
				onConfirm : function(event, element) {			
					showLoading();
					var name = $("#name").val();
					var lastname = $("#lastname").val();
					var username = $("#username").val();
					var pass = ($("#password").val()?$("#password").val():""); //Avoiding undefined when the field is not present in case when authentication.local = false
					var url = $("#url").val();
					var admin = $("#admin").val();
					var locked = $("#locked").val();
					var active = $("#active").val();
					var access = $("#useraccess").val();
					$.ajax({
				        type: "post",
				        url: restUri.user.save,
				        data: JSON.stringify([{id:savedUserID,firstName:name,lastName:lastname,userName:username,password:pass,domain:url,admin:admin,locked:locked,active:active,userAccess:[]}]),				        
				        contentType: "application/json; charset=utf-8"
				    }).done(function(data){
				    	var table = $('#tbl_user').DataTable();
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
						$('#modalUser').modal('hide');
				    });
					event.preventDefault();
				}
			});	
			$(this).confirmation('show');
		} else {
			$(this).confirmation('destroy');
		}
		e.preventDefault();
	});
};

var associateInsertButton = function(){
	$("#inserUser").click(function(){
		$("#modalUser").find('form').trigger('reset');
		$("#modalUser").on('shown.bs.modal', function () {
			$("#name").focus();
		});
		associatePostUser();
	});
}

var resetModalValidatorEvent = function(){
	$("#modalUser").on('hide.bs.modal', function() {
		var validator = $("#newUser").validate();
		validator.destroy();
	});
}

$(document).ready(function(){
	configureDataTable();	
	associateInsertButton();
	resetModalValidatorEvent();
});