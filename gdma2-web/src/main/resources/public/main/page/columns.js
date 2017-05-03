var changedColumns = new Object();
var origColumns = new Object();
var table;
var tableSessionId = sessionStorage.getItem("idTable");
var tableSessionName = sessionStorage.getItem("nameTable");
var configureDataTable = function(){
	changedColumns = new Object();
	origColumns = new Object();
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columnDefs": [ { className: "text-center", "targets": [7,8] },{ "orderable": false, "targets": 8 }  ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "columnType","render" : function(data, type, row) {
			            		return row.columnType + ' - ' + row.columnTypeString;
			            	}
			            },
			            { "data": "primarykey" ,
						  "className": "text-center",
						  "orderable" : false,
						  "render" : function(data, type, row) {
								var checked = row.primarykey ? ' checked'	: '';
								origColumns[row.id] = row;
								return '<input id="pk'+ row.id +'" class="primary-key" data-id="' + row.id
										+ '" type="checkbox"' + checked + '>';
							}
		            	},
		            	{ "data": "displayed" ,
						  "className": "text-center",
						  "orderable" : false,
						  "render" : function(data, type, row) {
								var checked = row.displayed ? ' checked'	: '';
								return '<input id="disp'+ row.id +'" class="displayed" data-id="' + row.id
										+ '" type="checkbox"' + checked + '>';
							}
		            	},
			            { "data": "allowInsert" ,
						  "className": "text-center",
						  "orderable" : false,
						  "render" : function(data, type, row) {
								var checked = row.allowInsert ? ' checked'	: '';
								return '<input id="allowI'+ row.id +'" class="allow-insert" data-id="' + row.id
										+ '" type="checkbox"' + checked + '>';
						  }
			            },
			            { "data": "allowUpdate" ,
						  "className": "text-center",
						  "orderable" : false,
						  "render" : function(data, type, row) {
								var checked = row.allowUpdate ? ' checked'	: '';
								return '<input id="allowU'+ row.id +'" class="allow-update" data-id="' + row.id
										+ '" type="checkbox"' + checked + '>';
						  }
			            },
			            { "data": "nullable" ,
							  "className": "text-center",
							  "orderable" : false,
							  "render" : function(data, type, row) {
									var checked = row.nullable ? ' checked'	: '';
									return '<input id="null'+ row.id +'" class="nullable" data-id="' + row.id
											+ '" type="checkbox"' + checked + '>';
							  }
			            },
			            { "data": "dropDownColumnDisplay","render" : function(data, type, row){ 
			            		var value = row.dropDownColumnDisplay === null ? "": row.dropDownColumnDisplay;
		            			return '<input type="text" class="column-display input-disabled" data-id="' + row.id+ '" value="'+ value +'">' ;
			            	} 
			            },
			            { "data": "dropDownColumnStore","render" : function(data, type, row){ 
			            	 	var value = row.dropDownColumnStore === null ? "": row.dropDownColumnStore;
		            			return '<input type="text" class="column-store input-disabled" data-id="' + row.id+ '" value="'+ value +'">' ;
			            	} 
			            },
			            { "data": "special","render" : function(data, type, row){ 
			            		return createDropDownSpecial(row.id, row.special);
			            	}			            
			            },
			            { "data": "minWidth", "render" : function(data, type, row){ 
			            	    var value = row.minWidth === null ? "": row.minWidth;
		            			return '<input type="text" maxlength="3" class="min-width input-disabled" data-id="' + row.id+ '" value="'+ value +'">' ;
		            		}			       
			            },
			            { "data": "maxWidth", "render" : function(data, type, row){ 
			            		var value = row.maxWidth === null ? "": row.maxWidth;
		            			return '<input type="text" maxlength="3" class="max-width input-disabled" data-id="' + row.id+ '" value="'+ value +'">' ;
	            			}			 
			            },
			            { "data": "columnSize", "render" : function(data, type, row){ 
			            		var value = row.columnSize === null ? "": row.columnSize;
		            			return '<input type="text" maxlength="3" class="column-size input-disabled" data-id="' + row.id+ '" value="'+ value +'">' ;
            				}			 
			            }
			        ]
	};

	table = $('#tbl_column').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.column.table, {id: tableSessionId}), 
		complete: function(){
	//		hideLoading();
			associateCheckBoxes();
			associateInputs();
			associateDropDownInputs();
			associateSpecialSelect();
			postColumnsData();
		}
	});
};

var createDropDownSpecial = function(id, value){
	var select = '<select class="special" id="' + id + '" data-id="' + id + '">';
	if(value !== undefined){
		if(value.toUpperCase()==='N'){
			select+= '<option value="N" selected>No</option>';
			select+= '<option value="U">User</option>';
			select+= '<option value="D">Data</option>';
		} else {
			if(value.toUpperCase()==='U'){
				select+= '<option value="N">No</option>';
				select+= '<option value="U" selected>User</option>';
				select+= '<option value="D">Data</option>';
			} else {
				if(value.toUpperCase()==='D'){
					select+= '<option value="N">No</option>';
					select+= '<option value="U">User</option>';
					select+= '<option value="D" selected>Data</option>';
				} else {
					select+= '<option value="N">No</option>';
					select+= '<option value="U">User</option>';
					select+= '<option value="D">Data</option>';
				}
			}
		}
	} else {
		select+= '<option value="N">No</option>';
		select+= '<option value="U">User</option>';
		select+= '<option value="D">Data</option>';
	}
	select+='</select>';
    return select;
}

var associateCheckBoxes = function(){
	associateCheckBox("primary-key", "primarykey");
	associateCheckBox("displayed", "displayed");
	associateCheckBox("allow-insert", "allowInsert");
	associateCheckBox("allow-update", "allowUpdate");
	associateCheckBox("nullable", "nullable");
}

var associateCheckBox = function(classes, variableName){
	$('.' + classes).off('change');
	$('.' + classes).change(function(e) {
		verifyChanges(e,variableName);
		verifyButtonsRow();
	});
}

var associateInputs = function(){
	associateInput("min-width", "minWidth");	
	associateInput("max-width", "maxWidth");
	associateInput("column-size", "columnSize");
}

var associateInput = function(input,varName){
	$("."+input).click(function(e) {
		$(e.target).removeClass("input-disabled");
		clicOutInputEvent(e.target);
		var oldValue = e.target.value;
		$(this).confirmation({
			placement : "bottom",
			btnOkLabel : "Ok",
			onConfirm : function(event, element) {
				$(e.target).addClass("input-disabled");
				verifyChanges(e,varName);
				verifyButtonsRow();
			},
			onCancel : function(event, element) {
				e.target.value=oldValue;
				$(e.target).addClass("input-disabled");
			}
		});
		$(this).confirmation('show');
	});
}

var associateDropDownInputs = function(){
	associateDropDownInput("column-display");
	associateDropDownInput("column-store");
}

var associateDropDownInput = function(classes){
	$("."+classes).click(function(e) {
		
	});
}

var associateSpecialSelect = function(){
	$('.special').on('change', function(e) {
		verifyChanges(e,"special");
		verifyButtonsRow();
	});
	
}
var clicOutInputEvent = function(input){
	$(window).one( "click", function(e) {
		if($(e.target).data("id") !== $(input).data("id")){
			$(input).addClass("input-disabled");
		} else {
			clicOutInputEvent(input);
		}
	});
}

var verifyChanges = function(e, variableName){
	var obj = origColumns[$(e.target).data("id")];
	if(changedColumns[obj.id]){
		if($(e.target)[0].checked !== undefined){
			changedColumns[obj.id][variableName] = $(e.target)[0].checked;
		} else {
			changedColumns[obj.id][variableName] = $(e.target)[0].value;
		}
	} else {
		changedColumns[obj.id] = jQuery.extend({}, obj);
		if($(e.target).checked !== undefined){
			changedColumns[obj.id][variableName] = $(e.target)[0].checked;
		} else {
			changedColumns[obj.id][variableName] = $(e.target)[0].value;
		}
	}
}

var verifyButtonsRow = function(){
	if(!jQuery.isEmptyObject(changedColumns)){
		$("#buttons-row").show();
	} else {
		$("#buttons-row").hide();
	}
}

var postColumnsData = function(){
	$("#save-columns").click(function(e) {
		$(this).confirmation({
			placement : "left",
			tittle: "Are you sure you wish to save the changed data?",
			btnOkLabel : "Ok",
			onConfirm : function(event, element) {
				showLoading();
				var list = getModifiedObjects();
				if (list.length > 0) {
					$.ajax({
						type : "post",
						url : restUri.column.save,
						data : JSON.stringify(list),
						contentType : "application/json; charset=utf-8"
					}).done(function(data) {
						changedColumns = new Object();
						$("#buttons-row").hide();
						table.draw(false);
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
			    	hideLoading();
				}
			}
		});
		$(this).confirmation('show');
		e.preventDefault();
	});
}

var cancelColumnsEdition = function(){
	$("#cancel-columns").click(function(e) {
		$(this).confirmation({
			placement : "left",
			btnOkLabel : "Are you sure you wish to cancel the changes?",
			onConfirm : function(event, element) {
				showLoading();
				changedColumns = new Object();
				table.draw(false);
			   	hideLoading();
			}
		});
		$(this).confirmation('show');
	});
}

var getModifiedObjects = function() {
	var list = new Array();
	var i = 0;
	$.each(changedColumns, function(key, value) {
		list[i] = value;
		i++;
	});
	return list;
}

$(document).ready(function(){
	configureDataTable();
    $("#tableName").html(sessionStorage.getItem("nameTable"));
	
});