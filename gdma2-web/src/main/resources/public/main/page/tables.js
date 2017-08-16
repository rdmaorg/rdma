var serverSessionId = getParameterByName("severId"); // sessionStorage.getItem("id");
var selector = "#tbl_tables" + serverSessionId;
var serverSessionName = getParameterByName("serverName"); // sessionStorage.getItem("name");
var selectedTableId = -1;
var table;
var tableEdit;
var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"columnDefs": [ { className: "text-center", "targets": [3,4] },{ "orderable": false, "targets": 4 }  ],
			serverside: true,
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "alias" },
			            { "data": "active" ,"render" : function(data, type, row){ 
				            	if(row.active){
				            		return '<i class="fa fa-check-circle active-icon" aria-hidden="true"></i>';
				            	} else {
				            		return '<i class="fa fa-times-circle inactive-icon" aria-hidden="true"></i>';
				            	}
			            	} 
			            },			            
			            { "data": "name","render" : function(data, type, row){ 
			            	var disabled = row.active === false;
			            	var $buttonEditTable = $('<button/>', {
			            		"class":"btn btn-primary btn-xs editTable",
			            		'disabled': disabled,
			            		'data-tableid': row.id,
			            		'data-tablename': row.alias,
			                    html: '<i class="fa fa-pencil-square-o"></i> Edit Alias'
			                });
			            	var $buttonEditAccess = $('<button/>', {
			            		"class":"btn btn-primary btn-xs editAccess",
			            		'disabled': disabled,
			            		'data-tableid': row.id,
			            		'data-tablename': row.alias,
			                    html: '<i class="fa fa-pencil-square-o"></i> Edit Access'
			                });
			            	var $buttonViewColumns = $('<button/>', {
			            		"class":"btn btn-info btn-xs viewColumns",
			            		'disabled': disabled,
			            		'data-tableid': row.id,
			            		'data-tablename': row.alias,
			                    html: '<i class="fa fa-columns"></i> Columns'
			                });
			            	return $buttonEditTable.prop('outerHTML') + '&nbsp;' + $buttonEditAccess.prop('outerHTML') + '&nbsp;' +  $buttonViewColumns.prop('outerHTML');
			            	} 
			            }
			        ]
	};
	table = $(selector).configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.table.table, {id: serverSessionId}),
		complete: function(){
//		hideLoading();
			showEditTableAccessModal();
			associateViewColumns();
			showEditTableAliasModal();
//			prependIconToButton();
		}
	});
	table.off('responsive-display');
	table.on('responsive-display', function() {
		showEditTableAccessModal();
		associateViewColumns();
		showEditTableAliasModal();
//		prependIconToButton();
	});
};

var showEditTableAccessModal = function(){
	$(".editAccess").off('click'); 
	$(".editAccess").on('click', function(){
		var btn = $(this);
		selectedTableId = btn.data('tableid');
		selectedTableName = btn.data('tablename');
		initiateModalUserAccess();
//		$("#modalUserAccess").find('form').trigger('reset');
		$("#editUserAccess").trigger('reset');
		$('#modalUserAccess').modal('show');
	});
	
}

//Edit table
var showEditTableAliasModal = function(){
	$('.editTable').off('click'); 
	$('.editTable').on('click', function(){
//		var btn = $(this);
		tableEdit = table.row($(this).closest('tr')[0]).data();
		$("#modalTable").find('form').trigger('reset');
		$("#tableNameModal").val(tableEdit.name);
		$("#alias").val(tableEdit.alias);
		$('#modalTable').modal('show');
	})
};

//Post table
var associatePostTable = function(){	
	var form = $("#editAlias");
	form.validate();
	$('#Save-table').click(function(e){		
		if (form.valid()) {
			$(this).confirmation({
				placement: "left",
				title : "Save Table Alias?",
				btnOkLabel : "Yes",
				onConfirm : function(event, element) {			
					showLoading();
					tableEdit.alias = $("#alias").val();
					$.ajax({
				        type: "post",
				        url: restUri.table.save,
				        data: JSON.stringify(tableEdit),
				        contentType: "application/json; charset=utf-8"
				    }).done(function(data){
				    	var table = $(selector).DataTable();
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
						$('#modalTable').modal('hide');
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

//View table columns
var associateViewColumns = function(){
	$('.viewColumns').off('click'); 
	$('.viewColumns').on('click', function(){
 		var btn = $(this);
 		syncColumns(btn.data('tableid'),btn.data('tablename'));
 		//viewTable(btn.data('tableid'),btn.data('tablename'));
	})
};

var syncColumns = function(tableId, tableName){
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.column.sync,{id: tableId}),
        contentType: "application/json; charset=utf-8",
    }).done(function(data){
 		viewTable(btn.data('tableid'),btn.data('tablename'));
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
} 
var viewTable = function(tableId,tableName) {
//	sessionStorage.setItem("idTable",serverId);
//	sessionStorage.setItem("nameTable",tableName);
	window.location.href = "columns?severId="+serverSessionId+"&serverName="+serverSessionName+"&tableId="+tableId+"&tableName="+tableName; 
}

$(document).ready(function(){
	$(".tbl_tables").attr("id",'tbl_tables' + serverSessionId);
	configureDataTable();	
	associatePostTable();
    $("#serverName").html(serverSessionName);
    $("span[name='serverName']").html(serverSessionName);
    $("#modalTable").on('hide.bs.modal', function() {
    	var validator = $( "#editAlias" ).validate();
    	validator.destroy();
    });
});

//var prependIconToButton = function(){
//	var $italicPencil = $('<i>', {'class': 'fa fa-pencil-square-o'});
//	$italicPencil.prependTo('.editTable, .editAccess');
//	var $italicTable = $('<i>', {'class': 'fa fa-columns'});
//	$italicTable.prependTo('.viewColumns');
//}