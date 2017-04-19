var table;
var connectionId = -1;
var configureDataTable = function() {

	var config = {
		fixedHeader : true,
		order : [ [ 1, "asc" ] ],
		idSrc : 'id',
		"columns" : [
				{ "data" : "id"	},
				{ "data" : "name" },
				{ "data" : "connectionClass" },
				{ "data" : "sqlGetTables"},
				{"data" : "active", "render" : function(data, type, row) {
						return '<button class="btn btn-primary btn-xs editConnection"><i class="fa fa-pencil-square-o"></i> Edit</button>'
								+ '&nbsp;'
								+ '<button class="btn btn-warning btn-xs deleteConnection" data-connectionid="'+ row.id+ '"><i class="fa fa-trash-o"></i> Delete</button>';
					}
				}

		]
	};

	table = $('#tbl_connection').configureDataTable(config, {
		url : restUri.connection.table,
		complete : function() {
			associateEditButton();
			associateDeleteButton();
		}
	});
};

var associateEditButton = function() {
	$('.editConnection').click(function() {
		var connectionType = table.row(this.closest('tr')).data();
		updateModal(connectionType);
	});
}

var updateModal = function(connectionType) {
	connectionId = connectionType.id;
	$('#name').val(connectionType.name);
	$('#connectionClass').val(connectionType.connectionClass);
	$('#sqlGetTables').val(connectionType.sqlGetTables);
	$('#modalConnectionType').modal('show');
	$("#modalConnectionType").on('shown.bs.modal', function() {
		$("#name").focus();
	});
	associatePostConnection();
}

var associateInsertButton = function(){
	$("#addConnection").click(function(){
		$("#modalConnectionType").find('form').trigger('reset');
		$("#modalConnectionType").on('shown.bs.modal', function () {
			$("#name").focus();
		});
		associatePostConnection();
	});
}

var associateDeleteButton = function(){
	$('.deleteConnection').confirmation({
		placement : "left",
		btnOkLabel : "Delete Connection",
		onConfirm : function(event, element) {
			// var btn = $(this);
			var btn = element;
			// console.log("Deleting Server Id " + btn.data('serverid'));
			deleteConnection(btn.data('connectionid'));
		}
	});
}

var deleteConnection = function(connectionId){
	$.ajax({
        type: "delete",
        url: mapPathVariablesInUrl(restUri.connection.del,{id: connectionId}),
        contentType: "application/json; charset=utf-8"
    }).done(function(data){
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
var resetModalValidatorEvent = function(){
	$("#modalConnectionType").on('hide.bs.modal', function() {
		var validator = $("#newConnectionType").validate();
		validator.destroy();
	}) 
}

var associatePostConnection = function() {
	var form = $("#newConnectionType");
	form.validate();
	$('#Save-conection').click(function(e) {
		if (form.valid()) {
			$(this).confirmation({
				placement : "left",
				btnOkLabel : "Save Connection Type",
				onConfirm : function(event, element) {
					showLoading();
					var connection = {
						id: connectionId,
						name: $("#name").val(),
						connectionClass: $('#connectionClass').val(),
						sqlGetTables: $('#sqlGetTables').val()
					};
					$.ajax({
						type : "post",
						url : restUri.connection.save,
						data : JSON.stringify(connection),
						contentType : "application/json; charset=utf-8"
					}).done(function(data) {
						table.draw(false);
						$("#global-success").slideDown(500);
						window.setTimeout(function() {
							$("#global-success").slideUp(500);
						}, 4000);
						connectionId = -1;
					}).fail(function(e) {
						handleError('#global-alert', e);
					}).always(function() {
						hideLoading();
						$('#modalConnectionType').modal('hide');
					});
				}
			});
			$(this).confirmation('show');
		} else {
			$(this).confirmation('destroy');
		}
		e.preventDefault();
	});

}

$(document).ready(function() {
	configureDataTable();
	associateInsertButton();
	resetModalValidatorEvent();
});