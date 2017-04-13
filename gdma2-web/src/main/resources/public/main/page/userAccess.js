var changedCheckboxes = new Object();
var origCheckboxes = new Object();

var initiateModalUserAccess = function() {
	changedCheckboxes = new Object();
	$('#tableName').html('<b>&nbsp'+ selectedTableName+'</b>');
	configureUserAccessDatatable();
}
var configureUserAccessDatatable = function() {
	var config = {
		fixedHeader : true,
		"page" : 0,
		"stateSave" : false,
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
				},
				{
					"data" : "allowDisplay",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = verifyIfisChecked(row, "allowDisplay") ? ' checked'
								: '';
						origCheckboxes[row.id] = row;
						return '<input class="allowDisplay" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowUpdate",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = verifyIfisChecked(row, "allowUpdate") ? ' checked'
								: '';
						return '<input class="allowUpdate" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowInsert",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = verifyIfisChecked(row, "allowInsert") ? ' checked'
								: '';
						return '<input class="allowInsert" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowDelete",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = verifyIfisChecked(row, "allowDelete") ? ' checked'
								: '';
						return '<input class="allowDelete" data-id="' + row.id
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
			associateSaveUserAccess();
		},
		error : function(message, e) {
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert', e);
		}
	});
}

var verifyIfisChecked = function(row, rule) {
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
}

var associateCheckbox = function(rule) {
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
			// clone the object for mantain the original for comparation
			changedCheckboxes[object.id] = jQuery.extend({}, object);
			changedCheckboxes[object.id][rule] = ck[0].checked;
		}
	});
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
	$("#save-userAccess").click(function() {
		var list = getModifiedObjects();
		if (list.length > 0) {
			$.ajax({
				type : "post",
				url : restUri.userAcces.update,
				data : JSON.stringify(list),
				contentType : "application/json; charset=utf-8"
			}).done(function(data) {

			}).fail(function(e) {
			}).always(function() {
				$('#modalUserAccess').modal('hide');
			});
		}
	});
}

var getModifiedObjects = function() {
	var list = new Array();1
	var i = 0;
	$.each(changedCheckboxes, function(key, value) {
		list[i] = value;
		i++;
	});
	return list;
}
