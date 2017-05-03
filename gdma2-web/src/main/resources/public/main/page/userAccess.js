var changedCheckboxes = new Object();
var origCheckboxes = new Object();

var initiateModalUserAccess = function() {
	changedCheckboxes = new Object();
	$('#tableName').html('<b>&nbsp' + selectedTableName + '</b>');
	configureUserAccessDatatable();
	associateSaveUserAccess();
}

var configureUserAccessDatatable = function() {
	var changedCheckboxes = new Object();
	var config = {
		fixedHeader : true,
		order: [[ 1, "asc" ]],
		"page" : 0,
		"stateSave" : false,
		"oLanguage": {
	         "sSearch": "Search:"
	     },
		"destroy" : true,
		"columns" : [
				{
					"data" : "id"
				},
				{
					"data" : "user.userName",
					"defaultContent" : ""
				},
				{
					"data" : "user.firstName",
					"orderable" : false,
					"defaultContent" : ""
				},{
					"data" : "fullAccess",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isAllChecked(row) ? ' checked'	: '';
						origCheckboxes[row.id] = row;
						return '<input id="'+ row.id +'fullA" class="allowFullAccess" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowDisplay",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isChecked(row, "allowDisplay") ? ' checked'
								: '';
						return '<input id="'+ row.id +'allowD"  class="allowDisplay" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowUpdate",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isChecked(row, "allowUpdate") ? ' checked'
								: '';
						return '<input  id="'+ row.id +'allowU" class="allowUpdate" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowInsert",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isChecked(row, "allowInsert") ? ' checked'
								: '';
						return '<input id="'+ row.id +'allowI" class="allowInsert" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowDelete",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isChecked(row, "allowDelete") ? ' checked'
								: '';
						return '<input id="'+ row.id +'allowDel" class="allowDelete" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				} ]
	};
	$('#tbl_userAccess').configureDataTable(config, {
		url : mapPathVariablesInUrl(restUri.userAcces.table, {
			id : selectedTableId
		}),
		dataSrc : "",
		complete : function() {
			// hideLoading();
			associateCheckBoxes();
		},
		error : function(message, e) {
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert', e);
		}
	});
}

var isAllChecked = function(row){
	var rules = {
			allowDisplay: 'allowDisplay',
			allowUpdate: 'allowUpdate',
			allowInsert: 'allowInsert',
			allowDelete: 'allowDelete'
	};
	return isChecked(row,rules.allowDisplay) && isChecked(row,rules.allowUpdate) 
			&& isChecked(row,rules.allowInsert) && isChecked(row,rules.allowDelete);
}

var isChecked = function(row, rule) {
	if (changedCheckboxes[row.id]) {
		return changedCheckboxes[row.id][rule];
	} else {
		return row[rule];
	}
}

var associateCheckBoxes = function() {
	associateCheckbox("allowDisplay");
	associateCheckbox("allowUpdate");
	associateCheckbox("allowInsert");
	associateCheckbox("allowDelete");
	associateCheckboxFullAccess();
}

var associateCheckbox = function(rule) {
	$('.' + rule).off('change');
	$('.' + rule).change(function(e) {
		var ck = $(e.target);
		var object = origCheckboxes[ck.data('id')];
		if (changedCheckboxes[object.id]) {
			var objAux = changedCheckboxes[object.id];
			objAux[rule] = ck[0].checked;
			// verify if the object was changed or not for save only
			// modified objects
			if (objectChanged(object, objAux)) {
				changedCheckboxes[object.id][rule] = ck[0].checked;
			} else {
				delete changedCheckboxes[object.id];
			}
		} else {
			// clone the object for maintain the original for comparison
			changedCheckboxes[object.id] = jQuery.extend({}, object);
			changedCheckboxes[object.id][rule] = ck[0].checked;
		}
		//method to verify if need check Full Access checkbox or not
		verifyFullAccess(ck.data('id'));
	});
}

var associateCheckboxFullAccess = function(){
	$('.allowFullAccess').off('change');
	$('.allowFullAccess').change(function(e) {
		var rules = {
			allowDisplay: 'allowDisplay',
			allowUpdate: 'allowUpdate',
			allowInsert: 'allowInsert',
			allowDelete: 'allowDelete'
		};
		var ck = $(e.target);
		var object = origCheckboxes[ck.data('id')];
		if (changedCheckboxes[object.id]) {
			var objAux = changedCheckboxes[object.id];
			objAux[rules.allowDisplay] = ck[0].checked;
			objAux[rules.allowUpdate] = ck[0].checked;
			objAux[rules.allowInsert] = ck[0].checked;
			objAux[rules.allowDelete] = ck[0].checked;
			// verify if the object was changed or not for save only
			// modified objects
			if (objectChanged(object, objAux)) {
				changedCheckboxes[object.id][rules.allowDisplay] = ck[0].checked;
				changedCheckboxes[object.id][rules.allowUpdate] = ck[0].checked;
				changedCheckboxes[object.id][rules.allowInsert] = ck[0].checked;
				changedCheckboxes[object.id][rules.allowDelete] = ck[0].checked;
			} else {
				delete changedCheckboxes[object.id];
			}
		} else {
			// clone the object for maintain the original for comparison
			changedCheckboxes[object.id] = jQuery.extend({}, object);
			changedCheckboxes[object.id][rules.allowDisplay] = ck[0].checked;
			changedCheckboxes[object.id][rules.allowUpdate] = ck[0].checked;
			changedCheckboxes[object.id][rules.allowInsert] = ck[0].checked;
			changedCheckboxes[object.id][rules.allowDelete] = ck[0].checked;
		}
		selectAllCheckBoxes(ck.data('id'),ck[0].checked);
	});
}

var verifyFullAccess = function(id){
	if($("#"+id+"allowD")[0].checked && $("#"+id+"allowU")[0].checked &&
			$("#"+id+"allowI")[0].checked && $("#"+id+"allowDel")[0].checked){
		$("#"+id+"fullA").prop( "checked", true );
	} else {
		$("#"+id+"fullA").prop( "checked", false );
	}
}

var selectAllCheckBoxes = function(id,check){
	$("#"+id+"allowD").prop( "checked", check );
	$("#"+id+"allowU").prop( "checked", check );
	$("#"+id+"allowI").prop( "checked", check );
	$("#"+id+"allowDel").prop( "checked", check );
}

var objectChanged = function(origObject, newObject) {
	if (origObject.allowDisplay !== newObject.allowDisplay) {
		return true;
	} else if (origObject.allowUpdate !== newObject.allowUpdate) {
		return true;
	} else if (origObject.allowInsert !== newObject.allowInsert) {
		return true;
	} else if (origObject.allowDelete !== newObject.allowDelete) {
		return true;
	}
	return false;
}

var associateSaveUserAccess = function() {
	$("#save-userAccess").click(function(e) {
		$(this).confirmation({
			placement : "left",
			title : "Save User Access?",
			btnOkLabel : "Yes",
			onConfirm : function(event, element) {
				showLoading();
				var list = getModifiedObjects();
				if (list.length > 0) {
					$.ajax({
						type : "post",
						url : restUri.userAcces.update,
						data : JSON.stringify(list),
						contentType : "application/json; charset=utf-8"
					}).done(function(data) {
						buildDataModuleMenu();
						$("#global-success").slideDown(500);
				    	window.setTimeout(function() {
				    		$("#global-success").slideUp(500);
				    	}, 4000);
					}).fail(function(e) {
						handleError('#global-alert', e);
					}).always(function() {
						hideLoading();
						
					});
				} else {
					$("#global-success").slideDown(500);
			    	window.setTimeout(function() {
			    		$("#global-success").slideUp(500);
			    	}, 4000);
				}
			}
		});
		$(this).confirmation('show');
		e.preventDefault();
	});
	$("#modal-close").click(function(e) {
		if(!jQuery.isEmptyObject(changedCheckboxes)){
			$(this).confirmation({
				placement : "right",
				title : "Discard unsaved changes?",
				btnOkLabel : "Yes",
				onConfirm : function(event, element) {
					$('#modalUserAccess').modal('hide');
				}
			});
			$(this).confirmation('show');
		} else {
			$(this).confirmation('hide');
			$('#modalUserAccess').modal('hide');
		}
		e.preventDefault();		
	});
}

var getModifiedObjects = function() {
	var list = new Array();
	var i = 0;
	$.each(changedCheckboxes, function(key, value) {
		list[i] = value;
		i++;
	});
	return list;
}
