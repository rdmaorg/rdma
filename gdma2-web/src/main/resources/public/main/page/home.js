//var tableId;
//var serverId;
var columnsMetadata = new Array();
var tableData;
var datatableEditor;
var userHasEditAccess;
var userHasInsertAccess;


$(document).ready(function(){	
	if(tableId){
		loadBreadCrumbs(tableId);
		
		$.ajax({
	        type: "get", 
	        url: mapPathVariablesInUrl(restUri.userAcces.userAccessToTable, {'tableId': tableId}),
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json'
	    }).done(function(userAccessdata){
	    	
	    	userHasEditAccess = userAccessdata[0].allowUpdate;
	    	userHasInsertAccess =  userAccessdata[0].allowInsert;
	    	
	    	loadDatatable();
	    	

	    }).fail(function(e){
	    	
	    }).complete(function(e){
	    	
	    }).always(function(){
	    	hideLoading();
	    });
		
	}
});

var loadBreadCrumbs = function(tableId){
	$("#serverName").html(serverName);
	$("#tableName").html('&#45;&nbsp;' + tableName);
}

//load columns metadata
var loadDatatable = function(){
	showLoading();
	$.ajax({
        type: "get",
//        url: mapPathVariablesInUrl(restUri.datatable.columnsMetaData, {'id': tableId}),
        url: mapPathVariablesInUrl(restUri.datatable.metaData, {'tableId': tableId}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	columnsMetadata = data.filter(filterDisplayed);
    	//sort columns by columnID
//    	columnsMetadata.sort(function(a, b) {
//		    return parseInt(a.id) - parseInt(b.id);
//		});

    	//columnsMetadata.unshift({name:"", alias:""});
    	$("#tableHeaderRow").empty();
    	//select checkbox header
    	$("<th></th>").appendTo("#tableHeaderRow");
		$.each(columnsMetadata, function(i, column) {
			$("<th>" + column.alias + "</th>").appendTo("#tableHeaderRow");
		});
    }).fail(function(e){
    	handleError('#global-alert', e);
    	window.setTimeout(function() {
    		$("#global-alert").slideUp(500);
    	}, 4000);
    }).complete(function(e){
    	configureDataTable(columnsMetadata);
    	
    }).always(function(){
    	hideLoading();
    	$("#tableRow").show();
    });
}

//configureEditor
var configureDataTable = function(columnsMetadata){
	var editorFields = createEditorFields(columnsMetadata);
	var configEditor = {
		table: "#table_data",
		idSrc : 'rowNumber',
		ajax: {
            create: {
                type: 'POST',
                url:  mapPathVariablesInUrl(restUri.datatable.create, {'serverId': serverId,'tableId': tableId})
            },
            edit: {
                type: 'POST',
                url:  mapPathVariablesInUrl(restUri.datatable.update, {'serverId': serverId,'tableId': tableId})
            },
            remove: {
                type: 'POST',
                url:  mapPathVariablesInUrl(restUri.datatable.remove, {'serverId': serverId,'tableId': tableId}),
                "data": function ( d ) {
            		for(var i in d.data) {
                		delete d.data[i].rowNumber;
                		for(var j = 0; j < d.data[i].columns.length;j++){ 
                			delete d.data[i].columns[j].position;
                			delete d.data[i].columns[j].columnName;
                			if(d.data[i].columns[j].val && d.data[i].columns[j].val.dropdownOptions){
                				d.data[i].columns[j].val = d.data[i].columns[j].val.value
                			}
                		};
                	};
                    return d;
                  }
            }
        },
		fields: editorFields,
        formOptions: {
            inline: {
                onBlur: 'submit',
                submit: 'allIfChanged'
            }
        }
	}
	datatableEditor = $('#table_data').configureEditor(configEditor);
	
	// Disable KeyTable while the main editing form is open
	datatableEditor.on( 'open', function ( e, mode, action ) { 
            if ( mode === 'main' || mode === 'inline') {
            	tableData.keys.disable();
            	if (action === 'create') {
            		enableDisableAllowInsertFields(columnsMetadata, editorFields);
            	};
            	if (action === 'edit') {
            		enableDisableAllowUpdateFields(columnsMetadata, editorFields);
            	}
            }
        } )
        .on( 'close', function () {
        	enableDisableAllowUpdateFields(columnsMetadata, editorFields);
        	tableData.keys.enable();
        } )
        .on('initEdit', function(e, node, data) {
        	//TODO: Daniel to verify
        	//Usecase: Click on a column, it becomes editable, now click on a column in another row. In this case, the "data" is coming in as null.
        	//The code datatableEditor.field("oldValues").val(data.columns); was failing.
        	//So, Farrukh placed the if check. The correct fix may be somewhere else.
        	if(data && data.columns){
    		    datatableEditor.field("oldValues").val(data.columns);
        	}
		})
        .on( 'preSubmit', function ( e, data, action ) {
        	//WORKAROUND TO REMOVE BACK END ERRORS
        	var el = $(datatableEditor.dom.formError);
    		el.html( '' ).css('display', 'none');
    		
			//VALIDATING CREATE
	        if ( action === 'create') {
	            for (var i = 0, len = columnsMetadata.length; i < len; i++) {
	            	if(columnsMetadata[i].allowInsert === true){
	            		if (!columnsMetadata[i].nullable && columnsMetadata[i].special === 'N'){
	            			var field = this.field('columns.'+columnsMetadata[i].name );
	            			
	            			// Only validate user input values - different values indicate that
	            			// the end user has not entered a value
	            			if ( ! field.isMultiValue() ) {
	            				if ( ! field.val() ) {
	            					field.error( 'A value must be given for ' + columnsMetadata[i].alias);
	            				}
	            			}
	            		}
	            	}
	        	}
	        };
	        //VALIDATING EDIT
	        if (action === 'edit') {
        		for (var i = 0, len = columnsMetadata.length; i < len; i++) {
	            	if(columnsMetadata[i].allowUpdate === true){
	            		if (!columnsMetadata[i].nullable && columnsMetadata[i].special === 'N'){
	            			var field = this.field( 'columns.'+columnsMetadata[i].name );
	            			
	            			// Only validate user input values - different values indicate that
	            			// the end user has not entered a value
	            			if ( ! field.isMultiValue() ) {
	            				if ( ! field.val() ) {
	            					
	            					if(e.currentTarget.s.mode=='inline'){ //if edit is an inline edit, only validate the selected cell
	            						//if(Object.keys(data.data[1].columns) === columnsMetadata[i].name){
	            						if(e.currentTarget.s.includeFields[0] === 'columns.'+columnsMetadata[i].name){
	            							field = this.field( 'columns.'+columnsMetadata[i].name );
	            							field.error( 'A value must be given for ' + columnsMetadata[i].alias);
	            						}
	            					}else{
	            						
	            						field.error( 'A value must be given for ' + columnsMetadata[i].alias);
	            						
	            					}	            				
	            					
	            				};
	            			};
	            		};
	            	};
	        	};
	        	
	            
	        };
	        // If any error was reported, cancel the submission so it can be corrected
	        if ( this.inError() ) {
	        	return false;
	        };
    } )
	.on( 'submitSuccess', function ( e, json, data ) {
		if(json.recordCreated === "true"){
			tableData.draw();
		};
    });
	
	var columnsData = createDataTableColumns(columnsMetadata);
	var config={
		 "ajax":{
			 "url":mapPathVariablesInUrl(restUri.datatable.table, {'id': tableId}),
			 "dataSrc": function (dataTableResponse) {
				 //dataTableResponse does not contain columns configured as 'displayed=false'                                                                                         in admin module
				 for(var i=0;i< dataTableResponse.data.length; i++){
				     if(dataTableResponse.data[i].columns.val && dataTableResponse.data[i].columns.val.timestamp){
				    	 dataTableResponse.data[i].columns.val = dataTableResponse.data[i].columns.val.timestamp;					    	 
				     }
				 }
				 editor = this.datatableEditor;
				 return dataTableResponse.data;
			 }
		 },
		 "columns": columnsData,
		 idSrc : 'rowNumber',
         dom : "rtip",
         keys: {
             columns: '.editable',
             editor: datatableEditor,
             keys: [ 9 ],
             editorKeys: 'tab-only'
         },
         select: {
             style:    'os',
             selector: 'td:first-child'
         },
         lengthMenu: [ [10, 25, 50, 100], [10, 25, 50, 100] ],
         fixedHeader: {
	    	headerOffset: $('#topNavBar').outerHeight() + $('#datatableControlWrapper').outerHeight()
    	 },
   	     order: [
    	     [1, 'asc']
    	 ]
	};
	tableData = $('#table_data').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.datatable.table, {'id': tableId}),
		complete: function(){
			//hideLoading();
		},
		dataFunction: function(d){
			d.myKey = "myValue";
		},
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
			window.setTimeout(function() {
	    		$("#global-alert").slideUp(500);
	    	}, 4000);
		}
	});
	
	//CONTROL BUTTONS
	new $.fn.dataTable.Buttons( tableData, {
		buttons: [
	              { extend: "create", editor: datatableEditor },
	              { extend: "edit",   editor: datatableEditor },
	              { extend: "remove", editor: datatableEditor },
	              'selectAll',
	              'selectNone',
	              { text:"Download",
	            	  action:function ( e, dt, node, config ) {
	            		  e.preventDefault();
	                      $('#downloadDataTableForm').attr('action', mapPathVariablesInUrl(restUri.datatable.download, {'tableId': tableId})).submit();
	                } },
	              {text: 'Upload', 
                      action:function ( e, dt, node, config ) {
                		$('#tableid').val(tableId); 
                        $('#fileUpload').get(0).click();
                    }}
	          ]
	} );
	
	tableData.buttons(0, null).container().appendTo(
			$('#datatableButtonsDiv'), tableData.table().container()
	);	
	verifyEnabledButtons(tableData);	
	configureUploadButton();	
	addDownloadButton(tableData);
	
	//SEARCH FILTER INPUT
	$('#dataTableSearchFilterInput').keyup(function(){
		tableData.search($(this).val()).draw() ;
	})
	
	$('#dataTableLength').change(function() {
		tableData.page.len( $(this).val() ).draw();
	});
	
    //Inline editing on tab focus
    tableData.on( 'key-focus', function ( e, datatable, cell ) {
	/*  datatableEditor.inline( cell.index(),{ submit: 'allIfChanged',
  	  submitOnBlur: true
	  });*/
    	var elems = document.querySelectorAll(".editable.focus");

    	[].forEach.call(elems, function(el) {
    	    el.classList.remove("focus");
    	});
    });

    // Activate an inline edit on click of a table cell
    // or a DataTables Responsive data cell
    tableData.on( 'click', 'tbody td:not(.child), tbody span.dtr-data', function (e) {
        // Ignore the Responsive control and checkbox columns
        if ( $(this).hasClass( 'control' ) || $(this).hasClass('select-checkbox') ) {
            return;
        }
        //Farrukh: Previously the following line was datatableEditor.inline( this ,{ submit: 'allIfChanged',
        //Changed this line as per https://datatables.net//forums/discussion/comment/86077/#Comment_86077
        //TODO: Daniel to verify
  	  	datatableEditor.inline( tableData.cell(this).index() ,{ submit: 'allIfChanged',
  	  	  submitOnBlur: true
	  });
    } );
    
    $(window).scroll(function(){
        var topH = $('.table.fixedHeader-floating').css('width');	
        $("#datatableControlWrapper").css("width",topH);
    });
    $(window).resize(function(){
        var topH = $('.table.fixedHeader-floating').css('width');	
        $("#datatableControlWrapper").css("width",topH);
    })
}

var createEditorFields = function(columnsMetadata){
	var fieldTypes = {
			VARCHAR: "VARCHAR",
			BOOLEAN: "BOOLEAN",
			TINYINT: "TINYINT"
	}
	var fields = [];
	for(var i = 0; i < columnsMetadata.length; i++){
		fields[i] = { label: columnsMetadata[i].alias ,
                	  name: 'columns.'+columnsMetadata[i].name };
		if(columnsMetadata[i].special !== 'N'){
			fields[i].type = "readonly";
		};
		if(columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.BOOLEAN){
			fields[i].type = "checkbox";
		};
		if(columnsMetadata[i].dropDownColumnDisplay){
			fields[i].type = "select";
			fields[i].options = columnsMetadata[i].datatableEditorFieldOptions;
		};
		if(	columnsMetadata[i].columnTypeString.toUpperCase() === "DATE" && userHasEditAccess == true){
			fields[i].type = "datetime";
		};
		//datetimeoffset are nested objects, i.e. they contain a timestamp and an offset.
		//Datatables wasn't reading the nested values and so [object Object] was displayed 
		//when you clicked on a cell with this datatype and [object Object] was passed back 
		//to the  server when saving the cell.
		//The code below extracts the timestamp of the object to pass back to the server. 
		if(	columnsMetadata[i].columnTypeString.toUpperCase() === "DATETIMEOFFSET"){
			fields[i].name =  fields[i].name+ '.timestamp';
		};
		
		fields.push( {
	        "type":    "hidden",
	        "name":    "oldValues",
	        "default": "oldValues"
	    } );
		
	}
	return fields;
}

//Creates a datatable render function for a dropdown column.
//datatableEditorFieldOptions - the dropdown options list json object
var getDropDownColumnRenderFunction = function (datatableEditorFieldOptions) {
    return function ( data, type, row ) {
 	  var returnValue = data; 
 	  //What we are doing is binding the variable (editorFieldOptions) within  
 	  //each function to a separate, unchanging value outside of the function
 	  var editorFieldOptions = datatableEditorFieldOptions;
 	  editorFieldOptions.forEach(function(obj) { 
 		  if(data == obj.value){
 			 returnValue = obj.label;  
 		  }
 	  });
	  return returnValue;
	};
}

var createDataTableColumns = function(columnsMetadata){
	var columnsData = [];
	
	//initializing with the checkbox as the first column
	columnsData.push({
         data: null,
         defaultContent: '',
         className: 'select-checkbox',
         orderable: false
     });
	
	for(var i = 0; i < columnsMetadata.length; i++){
		var dataValue = 'columns.'+columnsMetadata[i].name;
		var editFieldName = 'columns.'+columnsMetadata[i].name;
		
		//datetimeoffset are nested objects, i.e. they contain a timestamp and an offset.
		//Datatables wasn't reading the nested values and so [object Object] was displayed 
		//when you clicked on a cell with this datatype and [object Object] was passed back 
		//to the  server when saving the cell.
		//The code below sets the timestamp part of the object as the part of the object 
		// to edit. 
		if(columnsMetadata[i].columnTypeString === 'datetimeoffset'){
			editFieldName = 'columns.'+columnsMetadata[i].name+'.timestamp';
		}
		
		
		var renderFunction = function ( data, type, row ) {
 	      if (data && data.timestamp){
 	    	  return data.timestamp;
 	      }
	 	  return data;
		};
		
		//At runtime we have the data but we don't know which column the data belongs to. 
		//Also, the columns order and type are dynamic as well.
		//The solution is to create the renderFunction dynamically so every column has it's own renderFunction.
		//We pass datatableEditorFieldOptions as a parameter and the return is a regular datatable render function.
		if(columnsMetadata[i].datatableEditorFieldOptions[0] != null){
			renderFunction = getDropDownColumnRenderFunction(columnsMetadata[i].datatableEditorFieldOptions);
		}
		
		var className = '';
		if(columnsMetadata[i].allowUpdate){
			className = 'editable';
		}
		
		columnsData.push({'data': dataValue,
		  'editField': editFieldName, 
		  'render': renderFunction,
		  'className': className});
	};
	return columnsData;
}

function filterDisplayed(column) {
    return column.displayed === true;
};

var verifyEnabledButtons = function (dataTable) {
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.datatable.permissions, {'tableId': tableId}),        
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){	
    	var buttonsRemoved = [];
    	if(!data[0].allowInsert){
    		buttonsRemoved.push('0');
    	}
    	if(!data[0].allowUpdate){
    		buttonsRemoved.push('1');
    	}
    	if(!data[0].allowUpdate || !data[0].allowInsert){
    		buttonsRemoved.push('6');
    	}
    	if(!data[0].allowDelete){
    		buttonsRemoved.push('2');
    	}
    	dataTable.buttons( buttonsRemoved ).remove();
    	 
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    });
}

var addDownloadButton = function (datatable){
	
}

var configureUploadButton = function() {
	$('#fileUpload').on('change', function() {
		showLoading();
		waitingDialog.show('Validating and uploading the data.Please do not refresh.');
	    var file = this.files[0];
	    //$("#fileUpload").prop("disabled", true);
	    	$.ajax({
	    		// Your server script to process the upload
	    		url: restUri.datatable.upload,
	    		type: 'POST',
	    		enctype: 'multipart/form-data',
	    		data: new FormData($('#uploadDataTableForm')[0]),
	    		processData: false, //prevent jQuery from automatically transforming the data into a query string
	    		cache: false,
	    		contentType: false,
	    		success: function (data) {
	    			
	    			if(data.numRecords >= 0){
	    				console.log("SUCCESS : ", data);
		                $("#global-success-text").html(data.numRecords + ' record(s) successfully uploaded. Please refresh your page to see the latest changes.');
		                $("#global-success").slideDown(500);
	                    window.setTimeout(function() {
	                          $("#global-success").slideUp(500);
	                          $("#global-success-text").html('Success!');
	                    }, 4000);
	    			}else{
	    				console.log("ERROR : ", "Data conversion error, no records were uploaded!");
		    			handleError('#global-alert',e);
		    			window.setTimeout(function() {
		    	    		$("#global-alert").slideUp(500);
		    	    	}, 4000);

	    			}
	            },
	            error: function (e) {
	            	console.log("ERROR : ", e);
	    			handleError('#global-alert',e);
	    			window.setTimeout(function() {
	    	    		$("#global-alert").slideUp(500);
	    	    	}, 4000);

	            },
	            complete: function (e) {
					hideLoading();
						waitingDialog.hide('Validating and uploading the data.Please do not refresh.');
	            	$('#fileUpload')[0].value='';
	            	tableData.draw();
	            },
	    	});
	});
}

var enableDisableAllowInsertFields = function(columnsMetadata, editorFields){
	for(var i = 0; i < columnsMetadata.length; i++){
		if(columnsMetadata[i].allowInsert === true){
			datatableEditor.enable(editorFields[i].name);
		} else {
			datatableEditor.disable(editorFields[i].name);
		}
	}
};

var enableDisableAllowUpdateFields= function(columnsMetadata, editorFields){
	for(var i = 0; i < columnsMetadata.length; i++){ 
		if(columnsMetadata[i].allowUpdate === true && userHasEditAccess === true){
			datatableEditor.enable(editorFields[i].name);
		} else {
			datatableEditor.disable(editorFields[i].name);
		}
	}
};

var waitingDialog = waitingDialog
				|| (function($) {
					'use strict';

					// Creating modal dialog's DOM
					var $dialog = $('<div class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" role="dialog" aria-hidden="true" style="padding-top:15%; overflow-y:visible;">'
							+ '<div class="modal-dialog modal-m">'
							+ '<div class="modal-content">'
							+ '<div class="modal-header"><h3 style="margin:0;"></h3></div>'
							+ '<div class="modal-body">'
							+ '<div class="progress progress-striped active" style="margin-bottom:0;"><div class="progress-bar" style="width: 100%"></div></div>'
							+ '</div>' + '</div></div></div>');

					return {
						/**
						 * Opens our dialog
						 * @param message Custom message
						 * @param options Custom options:
						 * 				  options.dialogSize - bootstrap postfix for dialog size, e.g. "sm", "m";
						 * 				  options.progressType - bootstrap postfix for progress bar type, e.g. "success", "warning".
						 */
						show : function(message, options) {
							// Assigning defaults
							if (typeof options === 'undefined') {
								options = {};
							}
							if (typeof message === 'undefined') {
								message = 'Loading';
							}
							var settings = $.extend({
								dialogSize : 'm',
								progressType : '',
								onHide : null
							// This callback runs after the dialog was hidden
							}, options);

							// Configuring dialog
							$dialog.find('.modal-dialog').attr('class',
									'modal-dialog').addClass(
									'modal-' + settings.dialogSize);
							$dialog.find('.progress-bar').attr('class',
									'progress-bar');
							if (settings.progressType) {
								$dialog
										.find('.progress-bar')
										.addClass(
												'progress-bar-'
														+ settings.progressType);
							}
							$dialog.find('h3').text(message);
							// Adding callbacks
							if (typeof settings.onHide === 'function') {
								$dialog.off('hidden.bs.modal').on(
										'hidden.bs.modal', function(e) {
											settings.onHide.call($dialog);
										});
							}
							// Opening dialog
							$dialog.modal();
						},
						/**
						 * Closes dialog
						 */
						hide : function() {
							$dialog.modal('hide');
						}
					};

				})(jQuery);
