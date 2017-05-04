var dropDownTable;
var initiateModalDropDownColumns = function() {
	resetModal();
	populateSelectServer();
	associateSaveDropDownColumn();
	$('#columnName').html('<b>&nbsp' + selectedColumnName + '</b>');
}

var populateSelectServer = function(){
	$.ajax({
        type: "get",
        url: restUri.server.list,
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$.each(data, function(i, server) {
    	    $("<option value='" + server.id + "'>" + server.name + "</option>").appendTo("#select-server");
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
	associateServerChanged();
}

var associateServerChanged = function(){
	$('#select-server').on('change', function(e) {
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
			populateSelectTable($(e.target)[0].value);
			$('#table-control').show();
		} else {
			$('#table-control').hide();
		}
	});
}

var populateSelectTable = function(serverId){
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.table.sync_table_server, {id: serverId}),
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$.each(data, function(i, server) {
    	    $("<option value='" + server.id + "'>" + server.name + "</option>").appendTo("#select-table");
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
	associateTableChanged();
}

var associateTableChanged = function(){
	$('#select-table').on('change', function(e) {
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
			syncColumns($(e.target)[0].value);
			populateColumnsSelectors($(e.target)[0].value);
			$('#columns-control').show();
		} else {
			$('#columns-control').hide();
		}
	});
}

var syncColumns = function(tableId){
	$.ajax({
		type: "get",
		url: mapPathVariablesInUrl(restUri.column.sync,{id: tableId}),
		contentType: "application/json; charset=utf-8",
	}).done(function(data){
	}).fail(function(e){
		handleError('#global-alert', e);
	}).always(function(){
		hideLoading();
	});
}

var populateColumnsSelectors = function(tableId){
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.column.list_active, {id: tableId}),
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$.each(data, function(i, server) {
    	    $("<option value='" + server.id + "'>" + server.name + "</option>").appendTo("#select-col-display");
    	    $("<option value='" + server.id + "'>" + server.name + "</option>").appendTo("#select-col-store");
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
	associateColumnsChange();
}

var associateColumnsChange = function(){
	associateColumnChanged("select-col-display");
	associateColumnChanged("select-col-store");
}

var associateColumnChanged = function(id){
	$('#'+id).on('change', function(e) {
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
			populateColumnsSelectors($(e.target)[0].value);
		} 
		verifySelect();
	});
}

var verifySelect = function(){
	if(($("#select-col-display")[0].value != undefined && $("#select-col-display")[0].value != null)
			|| ($("#select-col-store")[0].value != undefined && $("#select-col-store")[0].value != null)){
		$("#select-col-display").prop('required',true);
		$("#select-col-store").prop('required',true);
	} else {
		$("#select-col-display").prop('required',false);
		$("#select-col-store").prop('required',false);
	}
	
}
var associateSaveDropDownColumn = function() {
	var form = $("#dropDownColumn");
	form.validate();
	$("#save-dropdowncolumn").click(function(e) {
		if(form.valid){
			$(this).confirmation({
				placement : "left",
				btnOkLabel : "Edit dropdown columns",
				onConfirm : function(event, element) {
					showLoading();
					var obj = origColumns[selectdedColumnId];
					if(changedColumns[obj.id]){
						changedColumns[obj.id].dropDownColumnDisplay = $("#select-col-display")[0].value;
						changedColumns[obj.id].dropDownColumnStore = $("#select-col-store")[0].value;
					} else {
						changedColumns[obj.id] = jQuery.extend({}, obj);
						changedColumns[obj.id].dropDownColumnDisplay = $("#select-col-display")[0].value;
						changedColumns[obj.id].dropDownColumnStore = $("#select-col-store")[0].value;
					}
					$('#columnStore'+obj.id).value($("#select-col-display")[0].value);
					$('#columnDisplay'+obj.id).value($("#select-col-display")[0].value);
					$('#modalDropDownColumns').modal('hide');
					resetModal();
					verifyButtonsRow();
					$("#global-success").slideDown(500);
			    	window.setTimeout(function() {
			    		$("#global-success").slideUp(500);
			    	}, 4000);
					hideLoading();
				}
			});
			$(this).confirmation('show');
		} else {
			$(this).confirmation('destroy');
		}
		e.preventDefault();
	});
	
	$("#close-dropdown").click(function(e) {
		if(($("#select-col-display")[0].value != undefined && $("#select-col-display")[0].value != null)
				&& ($("#select-col-store")[0].value != undefined && $("#select-col-store")[0].value != null)){
			$(this).confirmation({
				placement : "right",
				title : "Discard unsaved changes?",
				btnOkLabel : "Yes",
				onConfirm : function(event, element) {
					resetModal();
					$('#modalDropDownColumns').modal('hide');
				}
			});
			$(this).confirmation('show');
		} else {
			resetModal();
			$(this).confirmation('hide');
			$('#modalDropDownColumns').modal('hide');
		}
		e.preventDefault();		
	});
	
}

var resetModal = function(){
	$("#dropDownColumn").find('form').trigger('reset');
	$("#select-col-display").prop('required',false);
	$("#select-col-store").prop('required',false);
	$('#table-control').hide();
	$('#columns-control').hide();
}
