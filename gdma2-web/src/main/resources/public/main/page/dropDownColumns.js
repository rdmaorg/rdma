var dropDownTable;
var columns = new Object();
var selectedDropDownStore = null;
var selectedDropDownDisplay = null;
var initiateModalDropDownColumns = function() {
	resetModal();
	populateSelectServer();
	verifyDropDownselected();
	associateSaveDropDownColumn();
	$('#columnName').html('<b>&nbsp' + selectedColumnName + '</b>');
	$('#modalDropDownColumns').modal('show');
}

var verifyDropDownselected = function(){
	var obj = origColumns[selectdedColumnId];
	if(changedColumns[obj.id]){
		var changed = changedColumns[obj.id];
		if(changed.dropDownColumnDisplay !== null && changed.dropDownColumnStore !== null){
			populateSelectTable(changed.dropDownColumnDisplay.table.server.id);
			populateColumnsSelectors(changed.dropDownColumnDisplay.table.id);
			selectedDropDownStore = changed.dropDownColumnStore;
			selectedDropDownDisplay = changed.dropDownColumnDisplay;
		}
	} else if(obj.dropDownColumnDisplay !== null && obj.dropDownColumnStore !== null){
		populateSelectTable(obj.dropDownColumnDisplay.table.server.id);
		populateColumnsSelectors(obj.dropDownColumnDisplay.table.id);
		selectedDropDownStore = obj.dropDownColumnStore;
		selectedDropDownDisplay = obj.dropDownColumnDisplay;
	}
}

var populateSelectServer = function(){
	$.ajax({
        type: "get",
        url: restUri.server.list_active,
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$("#select-server").empty();
    	$('<option value=""></option>').appendTo("#select-server");
    	$.each(data, function(i, server) {
    	    $("<option value='" + server.id + "'>" + server.name + "</option>").appendTo("#select-server");
    	});
    	if(selectedDropDownDisplay !== null){
    		$("#select-server").val(selectedDropDownDisplay.table.server.id);
    	}
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    });
	associateServerChanged();
}

var associateServerChanged = function(){
	$('#select-server').on('change', function(e) {
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
			$.Deferred(populateSelectTable($(e.target)[0].value),$('#table-control').show());
		} else {
			$('#table-control').hide();
		}
	});
}

var populateSelectTable = function(serverId){
	showLoading("#loading-spinner-modal");
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.table.list_active, {id: serverId}),
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$("#select-table").empty();
    	$('<option value=""></option>').appendTo("#select-table");
    	$.each(data, function(i, table) {
    	    $("<option value='" + table.id + "'>" + server.alias + "</option>").appendTo("#select-table");
    	});
    	$('#table-control').show();
    	if(selectedDropDownDisplay !== null){
    		$("#select-table").val(selectedDropDownDisplay.table.id);
    	}
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading("#loading-spinner-modal");
    });
	associateTableChanged();
}

var associateTableChanged = function(){
	$('#select-table').on('change', function(e) {
		$("#select_col_display").val(null);
		$("#select_col_store").val(null);
		columns = new Object();
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
			populateColumnsSelectors($(e.target)[0].value);
		} else {
			$('#columns-control').hide();
		}
	});
}

var populateColumnsSelectors = function(tableId){
	showLoading("#loading-spinner-modal");
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.column.list_active, {id: tableId}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$("#select_col_display").empty();
    	$("#select_col_store").empty();
    	$('<option value=""></option>').appendTo("#select_col_display");
    	$('<option value=""></option>').appendTo("#select_col_store");
    	$.each(data, function(i, column) {
    		columns[column.id] = column;
    	    $("<option value='" + column.id + "'>" + column.name + "</option>").appendTo("#select_col_display");
    	    $("<option value='" + column.id + "'>" + column.name + "</option>").appendTo("#select_col_store");
    	});
    	$('#columns-control').show();
    	if(selectedDropDownDisplay !== null){
    		$("#select_col_display").val(selectedDropDownDisplay.id);
    		$("#select_col_store").val(selectedDropDownStore.id);
    	}
    }).complete(function(e){
    	$('#columns-control').show()
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading("#loading-spinner-modal");
    });
	associateColumnsChange();
}

var associateColumnsChange = function(){
	associateColumnChanged("select_col_display");
	associateColumnChanged("select_col_store");
}

var associateColumnChanged = function(id){
	$('#'+id).on('change', function(e) {
		if($(e.target)[0].value != undefined && $(e.target)[0].value != null){
//			populateColumnsSelectors($(e.target)[0].value);
		} 
		verifySelect();
	});
}

var verifySelect = function(){
//	if(($("#select_col_display")[0].value != undefined && $("#select_col_display")[0].value != null)
//			|| ($("#select_col_store")[0].value != undefined && $("#select_col_store")[0].value != null)){
//		$('#dropDownColumn').validate({
//			  rules: {
//				  select_col_display: {
//					  required: true
//			    }
//			  }
//			});
//	} else {
//		var form = $("#dropDownColumn");
//		form.validate();
//		$( "#select_col_display" ).rules( "remove", "required");
//		$( "#select_col_store" ).rules( "remove", "required");
//	}
}

var associateSaveDropDownColumn = function() {
	var form = $("#dropDownColumn");
	form.validate();
	$("#save-dropdowncolumn").click(function(e) {
		if(form.valid()){
			$(this).confirmation({
				placement : "left",
				btnOkLabel : "Edit dropdown columns",
				class: "medium-confirm-modal",
				onConfirm : function(event, element) {
					showLoading("#loading-spinner-modal");
					var obj = origColumns[selectdedColumnId];
					
					if(changedColumns[obj.id]){
						if($("#select_col_display")[0].value === undefined || $("#select_col_display")[0].value === null){
							setColumnData(obj.id, null, null, "", "");
						}else {
							changedColumns[obj.id].dropDownColumnDisplay = columns[$("#select_col_display")[0].value];
							changedColumns[obj.id].dropDownColumnStore = columns[$("#select_col_store")[0].value];
							$('#columnStore'+obj.id).val(changedColumns[obj.id].dropDownColumnStore.name);
							$('#columnDisplay'+obj.id).val(changedColumns[obj.id].dropDownColumnDisplay.name);
						}
					} else {
						changedColumns[obj.id] = jQuery.extend({}, obj);
						if($("#select_col_display")[0].value === undefined || $("#select_col_display")[0].value === null){
							setColumnData(obj.id, null, "");
						} else {
							changedColumns[obj.id].dropDownColumnDisplay = columns[$("#select_col_display")[0].value];
							changedColumns[obj.id].dropDownColumnStore = columns[$("#select_col_store")[0].value];
							$('#columnStore'+obj.id).val(changedColumns[obj.id].dropDownColumnStore.name);
							$('#columnDisplay'+obj.id).val(changedColumns[obj.id].dropDownColumnDisplay.name);
						}
					}
					$('#modalDropDownColumns').modal('hide');
					resetModal();
					verifyButtonsRow();
					hideLoading("#loading-spinner-modal");
				}
			});
			$(this).confirmation('show');
		} else {
			$(this).confirmation('destroy');
		}
		e.preventDefault();
	});
	
	$("#close-dropdown").click(function(e) {
		if(($("#select_col_display")[0].value != undefined && $("#select_col_display")[0].value != null)
				&& ($("#select_col_store")[0].value != undefined && $("#select_col_store")[0].value != null)){
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

var setColumnData = function(id, value, label){
	changedColumns[id].dropDownColumnDisplay = value;
	changedColumns[id].dropDownColumnStore = value;
	$('#columnStore'+obj.id).val(label);
	$('#columnDisplay'+obj.id).val(label);
}


var resetModal = function(){
	columns = new Object();
	$("#dropDownColumn").find('form').trigger('reset');
	$('#table-control').hide();
	$('#columns-control').hide();
	selectedDropDownStore = null;
	selectedDropDownDisplay = null;
	var form = $("#dropDownColumn").validate();
	form.destroy();
}
