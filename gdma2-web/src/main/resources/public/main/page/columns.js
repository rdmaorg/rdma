var changedColumns = new Object();
var origColumns = new Object();
var table;
var selectdedColumnId;
var selectedColumnName;
var tableSessionId = sessionStorage.getItem("idTable");
var serverSessionName = sessionStorage.getItem("name");
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
                                { "data": "alias" , "render" : function(data, type, row) {
                                       var value = row.alias === null ? "": row.alias;
                                       return '<div class="alias input-edit fa fa-pencil" data-id="' + row.id+ '"><input id="alias'+row.id+'" type="text" class=" input-disabled" value="'+ value +'"  data-id="' + row.id+ '"></div>' ;
                                }
                                },
                                { "data": "columnTypeString" },
                                { "data": "primarykey" ,
                                          "className": "text-center",
                                          "orderable" : false,
                                          "render" : function(data, type, row) {
                                                      var checked = row.primarykey ? ' checked'      : '';
                                                     origColumns[row.id] = row;
                                                     return '<input id="pk'+ row.id +'" class="primary-key" data-id="' + row.id
                                                                   + '" type="checkbox"' + checked + '>';
                                               }
		                         },
		                         { "data": "displayed" ,
		                                          "className": "text-center",
		                                          "orderable" : false,
		                                          "render" : function(data, type, row) {
		                                                     var checked = row.displayed ? ' checked' : '';
		                                                     return '<input id="disp'+ row.id +'" class="displayed" data-id="' + row.id
		                                                                   + '" type="checkbox"' + checked + '>';
		                                               }
		                         },
                                { "data": "allowInsert" ,
                                          "className": "text-center",
                                          "orderable" : false,
                                          "render" : function(data, type, row) {
                                                     var checked = row.allowInsert ? ' checked'     : '';
                                                     return '<input id="allowI'+ row.id +'" class="allow-insert" data-id="' + row.id
                                                                   + '" type="checkbox"' + checked + '>';
                                          }
                                },
                                { "data": "allowUpdate" ,
                                          "className": "text-center",
                                          "orderable" : false,
                                          "render" : function(data, type, row) {
                                                     var checked = row.allowUpdate ? ' checked'     : '';
                                                     return '<input id="allowU'+ row.id +'" class="allow-update" data-id="' + row.id
                                                                   + '" type="checkbox"' + checked + '>';
                                          }
                                },
                                { "data": "nullable" ,
                                                 "className": "text-center",
                                                 "orderable" : false,
                                                 "render" : function(data, type, row) {
                                                            var checked = row.nullable ? ' checked'  : '';
                                                            return '<input id="null'+ row.id +'" class="nullable" data-id="' + row.id
                                                                         + '" type="checkbox"' + checked + '>';
                                                 }
                                },
                                { "data": "dropDownColumnDisplay", "orderable" : false, "render" : function(data, type, row){ 
                                        var value = verifyValue(row,"dropDownColumnDisplay") === null ? "": verifyValue(row,"dropDownColumnDisplay").name;
                                        return '<div class="column-display input-edit fa fa-bars" data-id="' + row.id+ '"><input type="text" id="columnDisplay'+row.id+'" class=" input-disabled" value="'+ value +'"></div>' ;
                                } 
                                },
                                { "data": "dropDownColumnStore", "orderable" : false, "render" : function(data, type, row){ 
                                        var value = verifyValue(row,"dropDownColumnStore") === null ? "": verifyValue(row,"dropDownColumnStore").name;
                                        return '<div class="column-store input-edit fa fa-bars" data-id="' + row.id+ '"><input type="text" id="columnStore'+row.id+'" class="input-disabled" value="'+ value +'"></div>' ;
                                } 
                                },
                                { "data": "special","render" : function(data, type, row){ 
                                        return createDropDownSpecial(row.id, row.special);
                                }                               
                                },
                                { "data": "columnSize", "render" : function(data, type, row){ 
                                        var value = row.columnSize === null ? "": row.columnSize;
                                        return '<div class="input-edit fa fa-pencil column-size" data-id="' + row.id+ '">'+
                                        '<input type="text" id="colS'+row.id+'" maxlength="3" class=" input-disabled" value="'+ value +'"'+
                                        ' onkeypress="return event.charCode >= 48 && event.charCode <= 57" data-id="' + row.id+ '">'+
                                        '</div>' ;
                                 }                   
                                }
                            ]
       };

       table = $('#tbl_column').configureDataTable(config, {
             url: mapPathVariablesInUrl(restUri.column.table, {id: tableSessionId}), 
             complete: function(){
       //           hideLoading();
                    associateCheckBoxes();
                    associateInputs();
                    associateDropDownInputs();
                    associateSpecialSelect();
                    postColumnsData();
                    lineExpansionEvent();
                    cancelColumnsEdition();
             }
       });
};

var verifyValue = function(row, varName){
       if(changedColumns[row.id]){
             return changedColumns[row.id][varName];
       } else {
             return row[varName];
       }
}

var createDropDownSpecial = function(id, value){
       var select = '<select class="special" id="' + id + '" data-id="' + id + '">';
       if(value !== undefined){
             if(value.toUpperCase()==='N'){
                    select+= '<option value="N" selected>No</option>';
                    select+= '<option value="U">User</option>';
                    select+= '<option value="D">Date</option>';
             } else {
                    if(value.toUpperCase()==='U'){
                           select+= '<option value="N">No</option>';
                           select+= '<option value="U" selected>User</option>';
                           select+= '<option value="D">Date</option>';
                    } else {
                           if(value.toUpperCase()==='D'){
                                 select+= '<option value="N">No</option>';
                                 select+= '<option value="U">User</option>';
                                 select+= '<option value="D" selected>Date</option>';
                           } else {
                                 select+= '<option value="N">No</option>';
                                 select+= '<option value="U">User</option>';
                                 select+= '<option value="D">Date</option>';
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
             verifyChanges(e.target,variableName);
       });
}

var associateInputs = function(){
       associateInput("column-size", "columnSize");
       associateInput("alias", "alias");
       
}
var rigthValue;
var associateInput = function(input,varName){
       $("."+input).click(function(e) {
             $(e.target).removeClass("input-disabled");
		     clicOutInputEvent(e.target, varName);
			 if (!$(e.target).attr('id')) {
				var obj = $(e.target).find("input");
			 	$(obj).focus();
			 	$(obj).removeClass("input-disabled");
			 }
       });
       $("."+input).on('keyup', function (e) {
		    if (e.keyCode == 13) {
		    	verifyChanges(e.target,varName);
		    }
		});
}

var associateDropDownInputs = function(){
       associateDropDownInput("column-display");
       associateDropDownInput("column-store");
}

var associateDropDownInput = function(classes){
       $("."+classes).click(function(e) {
                    var input = $(this);
                    selectdedColumnId = input.data('id');
                    selectedColumnName = origColumns[selectdedColumnId].name;
                    initiateModalDropDownColumns();
                    $("#modalDropDownColumns").find('form').trigger('reset');
       });
}

var associateSpecialSelect = function(){
       $('.special').on('change', function(e) {
             verifyChanges(e.target,"special");
       });
       
}
var clicOutInputEvent = function(input, varName){
       $(window).one( "click", function(e) {
//             addEventEnter(input, varName);
             if($(e.target).data("id") !== $(input).data("id")){
                    verifyChanges(input, varName);
             } else {
                    clicOutInputEvent(input,varName);
             }
       });
}
var addEventEnter = function(input, varName){
       var obj;
       if($(input).attr('id')){
             obj = $(input);
       } else {
             obj = $(input).find("input");
       }
       document.getElementById(obj.attr('id')).onkeypress = function(e) {
             if (!e)
                    e = window.event;
             var keyCode = e.keyCode || e.which;
             if (keyCode == '13') {
                    verifyChanges(obj, varName);
                    return false;
             }
       }
}
var verifyChanges = function(e, variableName){
       var obj = origColumns[$(e).data("id")];
       if(changedColumns[obj.id]){
             if($(e).is(':checkbox')){
                 changedColumns[obj.id][variableName] = $(e)[0].checked;
             } else {
                if ($(e)[0].value) {
                       changedColumns[obj.id][variableName] = $(e)[0].value;
                } else {
                       var input = $(e).find("input");
                       if(input){
                             changedColumns[obj.id][variableName] = input[0].value;
                       }
                }
             }
             if(!objectChanged(obj, changedColumns[obj.id])){
            	 delete changedColumns[obj.id];
             }
       } else {
             changedColumns[obj.id] = jQuery.extend({}, obj);
             if($(e).is(':checkbox')){
                    changedColumns[obj.id][variableName] = $(e)[0].checked;
             } else {
                    if ($(e)[0].value) {
                           changedColumns[obj.id][variableName] = $(e)[0].value;
                    } else {
                           var input = $(e).find("input");
                           if(input){
                                 changedColumns[obj.id][variableName] = input[0].value;
                           }
                    }
             }
             if(!objectChanged(obj, changedColumns[obj.id])){
            	 delete changedColumns[obj.id];
             }
       }
       verifyButtonsRow();
}


var objectChanged = function(originalObject, changedObject){
	if(originalObject.alias !== changedObject.alias){
		return true;
	}
	if(originalObject.primarykey !== changedObject.primarykey){
		return true;
	}
	if(originalObject.displayed !== changedObject.displayed){
		return true;
	}
	if(originalObject.allowInsert !== changedObject.allowInsert){
		return true;
	}
	if(originalObject.allowUpdate !== changedObject.allowUpdate){
		return true;
	}
	if(originalObject.nullable !== changedObject.nullable){
		return true;
	}
	if(diffDropDonw(originalObject.dropDownColumnDisplay, changedObject.dropDownColumnDisplay)){
		return true;
	}
	if(diffDropDonw(originalObject.dropDownColumnStore, changedObject.dropDownColumnStore)){
		return true;
	}
	if(originalObject.special !==  changedObject.special){
		return true;
	}
	if(originalObject.columnSize+"" !== changedObject.columnSize+""){
		return true;
	}
	return false;
}

var diffDropDonw = function(dropDownOrig, dropDownChanged){
	if(dropDownOrig === null && dropDownChanged !== null){
		return true;
	}
	if(dropDownOrig !== null && dropDownChanged === null){
		return true;
	}
	if(dropDownOrig !== null && dropDownChanged !== null
			&& dropDownOrig.id !=  dropDownChanged.id){
		return true;
	}
	return false;
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
	     			title: "Save changed data?",
	     			btnOkLabel : "Yes",
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
                    title: "Discard unsaved changes?",
                    btnOkLabel : "Yes",
                    btnOkClass : "btn btn-sm btn-danger",
                    onConfirm : function(event, element) {
                           showLoading();
                           changedColumns = new Object();
                           table.draw(false);
                           verifyButtonsRow();
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
             list[i] = jQuery.extend({}, value);
             i++;
       });
       return list;
}

var lineExpansionEvent = function(){
       //  datatable line expansion event
    $('#tbl_column tbody').on('click', 'td:first-child', function () {
       associateCheckBoxes();
       associateInputs();
       associateDropDownInputs();
       associateSpecialSelect();
    });
}

$(document).ready(function(){
    configureDataTable();
    associateSaveDropDownColumn();   
    $("#serverName").html(serverSessionName);
    $("#tableName").html(sessionStorage.getItem("nameTable"));
});

