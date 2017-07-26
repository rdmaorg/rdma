var changedCheckboxes = new Object();
var origCheckboxes = new Object();
var tableUserAccess;

var initiateModalUserAccess = function() {
	changedCheckboxes = new Object();
	origCheckboxes = new Object();
//	$('#tableName').html('<b>&nbsp' + selectedTableName + '</b>');
	$('#selectedTableName').html('<b>&nbsp' + selectedTableName + '</b>');
	configureUserAccessDatatable();
	associateSaveUserAccess();
}

var configureUserAccessDatatable = function() {
	var config = {
		fixedHeader : true,
		lengthMenu: [ [10, 25, 50, 100], [10, 25, 50, 100] ],
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
					"defaultContent" : ""
				},{
					"data" : "fullAccess",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isAllChecked(row) ? ' checked'	: '';
						origCheckboxes[row.id] = row;
						return '<input id="'+ row.id +'fullA" name="'+ row.id +'fullA" class="allowFullAccess" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				},
				{
					"data" : "allowDisplay",
					"className": "text-center",
					"orderable" : false,
					"render" : function(data, type, row) {
						var checked = isCheckedDisplay(row, "allowDisplay") ? ' checked'
								: '';
						return '<input id="'+ row.id +'allowD" name="'+ row.id +'allowD"  class="allowDisplay" data-id="' + row.id
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
						return '<input  id="'+ row.id +'allowU" name="'+ row.id +'allowU" class="allowUpdate" data-id="' + row.id
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
						return '<input id="'+ row.id +'allowI" name="'+ row.id +'allowI" class="allowInsert" data-id="' + row.id
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
						return '<input id="'+ row.id +'allowDel" name="'+ row.id +'allowDel" class="allowDelete" data-id="' + row.id
								+ '" type="checkbox"' + checked + '>';
					}
				} ]
	};
	tableUserAccess = $('#tbl_userAccess').configureDataTable(config, {
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
	
	tableUserAccess.off( 'responsive-display');
	tableUserAccess.on( 'responsive-display', function ( e, datatable, row, showHide, update ) {
		associateCheckBoxes();
	} );
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

var isCheckedDisplay = function(row, rule) {
	if (changedCheckboxes[row.id]) {
		return changedCheckboxes[row.id][rule];
	} else {
		if(row.allowDisplay || row.allowUpdate || row.allowInsert || row.allowDelete){
			return true;
		}
		return row[rule];
	}
}

var associateCheckBoxes = function() {
	associateCheckbox("allowDisplay");
	associateCheckbox("allowUpdate");
	associateCheckbox("allowInsert");
	associateCheckbox("allowDelete");
	associateCheckboxFullAccess();
	associateCheckboxDisplay();
}

var associateCheckbox = function(rule) {
	$('.' + rule).off('change');
	$('.' + rule).change(function(e) {
		var ck = $(e.target);		
		var object = origCheckboxes[ck.data('id')];
		propagateCheckBoxStatus(object.id, rule, ck[0].checked);
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
		if( verifyDisplayAccess(ck.data('id')) === true ) {
			changedCheckboxes[object.id]['allowDisplay'] = true;
		};
	});
}

var propagateCheckBoxStatus = function(id, rule, checked)  {
	var setRule = '';
	if (rule === "allowDisplay"){
		setRule = "allowD";
	} else if (rule === "allowUpdate"){
		setRule = "allowU";
	} else if (rule === "allowInsert"){
		setRule = "allowI";
	} else if (rule === "allowDelete"){
		setRule = "allowDel";
	}
	$("[name='"+id+setRule+"']").prop( "checked", checked );
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
		selectAllCheckBoxes(ck.data('id'),ck[0].checked);
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
	});
}

var associateCheckboxDisplay = function(){
	$('.allowDisplay').off('change');
	$('.allowDisplay').change(function(e) {
		var rules = {
			allowDisplay: 'allowDisplay',
			allowUpdate: 'allowUpdate',
			allowInsert: 'allowInsert',
			allowDelete: 'allowDelete'
		};
		var ck = $(e.target);
		var object = origCheckboxes[ck.data('id')];
		if (typeof changedCheckboxes[object.id] === typeof undefined) {
			changedCheckboxes[object.id] = jQuery.extend({}, object);
		};
		changedCheckboxes[object.id][rules.allowDisplay] = ck[0].checked;
		if (ck[0].checked === false) {			
			changedCheckboxes[object.id][rules.allowUpdate] = ck[0].checked;
			changedCheckboxes[object.id][rules.allowInsert] = ck[0].checked;
			changedCheckboxes[object.id][rules.allowDelete] = ck[0].checked;
			selectAllCheckBoxes(ck.data('id'),ck[0].checked);
		};
		// verify if the object was changed or not 
		// in order to save only modified objects
		if (!objectChanged(object, changedCheckboxes[object.id])) {
			delete changedCheckboxes[object.id];
		};
		verifyFullAccess(ck.data('id'));
	});
}

var verifyFullAccess = function(id){
	if(($("[name='"+id+"allowD']")[0].checked || (typeof $("[name='"+id+"allowD']")[1] !== typeof undefined && $("[name='"+id+"allowD']")[1].checked) ) 
		&& ($("[name='"+id+"allowU']")[0].checked || (typeof $("[name='"+id+"allowU']")[1] !== typeof undefined && $("[name='"+id+"allowU']")[1].checked) )
		&& ($("[name='"+id+"allowI']")[0].checked || (typeof $("[name='"+id+"allowI']")[1] !== typeof undefined && $("[name='"+id+"allowI']")[1].checked) )
		&& ($("[name='"+id+"allowDel']")[0].checked || (typeof $("[name='"+id+"allowDel']")[1] !== typeof undefined && $("[name='"+id+"allowDel']")[1].checked) )
		){
		$("[name='"+id+"fullA']").prop( "checked", true );
		$("[name='"+id+"allowD']").prop( "checked", true );
		$("[name='"+id+"allowU']").prop( "checked", true );
		$("[name='"+id+"allowI']").prop( "checked", true );
		$("[name='"+id+"allowDel']").prop( "checked", true );
		
	} else {
		$("[name='"+id+"fullA']").prop( "checked", false );
	}
}

var verifyDisplayAccess = function(id){
	if(($("[name='"+id+"allowU']")[0].checked || (typeof $("[name='"+id+"allowU']")[1] !== typeof undefined && $("[name='"+id+"allowU']")[1].checked))
		|| ($("[name='"+id+"allowI']")[0].checked || (typeof $("[name='"+id+"allowI']")[1] !== typeof undefined && $("[name='"+id+"allowI']")[1].checked))
		|| ($("[name='"+id+"allowDel']")[0].checked || (typeof $("[name='"+id+"allowDel']")[1] !== typeof undefined && $("[name='"+id+"allowDel']")[1].checked)) ){
		$("[name='"+id+"allowD']").prop( "checked", true );
		return true;
	}
	
	return false;
}

var selectAllCheckBoxes = function(id,check){
	$("[name='"+id+"allowD']").prop( "checked", check );
	$("[name='"+id+"allowU']").prop( "checked", check );
	$("[name='"+id+"allowI']").prop( "checked", check );
	$("[name='"+id+"allowDel']").prop( "checked", check );
	
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
	$("#save-userAccess").off('click'); 
	$("#save-userAccess").on('click', function(e) {
		$(this).confirmation({
			placement : "left",
			title : "Save User Access?",
			btnOkLabel : "Yes",
			onConfirm : function(event, element) {
				var list = getModifiedObjects();
				if (list.length > 0) {
					showLoading();
					$.ajax({
						type : "post",
						url : restUri.userAcces.update,
						data : JSON.stringify(list),
						contentType : "application/json; charset=utf-8"
					}).done(function(data) {
						buildDataModuleMenu();
						changedCheckboxes = new Object();
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
	$("#modal-close-access").off('click'); 
	$("#modal-close-access").on('click', function(e) {
		if(!jQuery.isEmptyObject(changedCheckboxes)){
			$(this).confirmation({
				placement : "right",
				title : "Discard unsaved changes?",
				btnOkLabel : "Yes",
				btnOkClass : "btn btn-sm btn-danger",
				onConfirm : function(event, element) {
					$('#modalUserAccess').modal('hide');
				}
			});
			$(this).confirmation('show');
		} else {
//			$(this).confirmation('hide');
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
