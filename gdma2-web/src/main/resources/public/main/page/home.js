//var tableId;
//var serverId;
var columnsMetadata = new Array();
var tableData;
var datatableEditor;

$(document).ready(function(){	
	if(tableId){
		loadBreadCrumbs(tableId);
		loadDatatable();
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
        url: mapPathVariablesInUrl(restUri.datatable.columnsMetaData, {'id': tableId}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	columnsMetadata = data.filter(filterDisplayed);
    	//sort columns by columnID
    	columnsMetadata.sort(function(a, b) {
		    return parseInt(a.id) - parseInt(b.id);
		});
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
                			delete d.data[i].columns[j].columnPK;
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
	
//	datatableEditor.add( {
//        "type":    "hidden",
//        "name":    "oldValues",
//        "default": "oldValues"
//    } );
	
	// Disable KeyTable while the main editing form is open
	datatableEditor.on( 'open', function ( e, mode, action ) {
            if ( mode === 'main' ) {
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
	            			var field = this.field( 'columns.'+i+'.val' ) ? this.field( 'columns.'+i+'.val' ) : this.field( 'columns.'+i+'.val.value' );
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
	            			var field = this.field( 'columns.'+i+'.val' ) ? this.field( 'columns.'+i+'.val' ) : this.field( 'columns.'+i+'.val.value' );
	            			// Only validate user input values - different values indicate that
	            			// the end user has not entered a value
	            			if ( ! field.isMultiValue() ) {
	            				if ( ! field.val() ) {
	            					field.error( 'A value must be given for ' + columnsMetadata[i].alias);
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
			 "dataSrc": function (json) {
				 //json does not contain columns configured as 'displayed=false' in admin module
				 var return_data = new Array();
				 for(var i=0;i< json.data.length; i++){
					 for(var j=0;j< json.data[i].columns.length; j++){
					   json.data[i].columns[j].columnPK = ''+columnsMetadata[j].id;
					   if(json.data[i].columns[j].val && json.data[i].columns[j].val.dropdownOptions){
						 json.data[i].columns[j].val.position = j;
					   } else {
					     json.data[i].columns[j].position = j;
					     if(json.data[i].columns[j].val && json.data[i].columns[j].val.timestamp){
					    	 json.data[i].columns[j].val = json.data[i].columns[j].val.timestamp;					    	 
					     }
					   }
					 }
				 }
				 editor = this.datatableEditor;
				 return json.data;
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
	  datatableEditor.inline( cell.index(),{ submit: 'allIfChanged',
  	  submitOnBlur: true
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
    
    /*$(window).scroll(function(){
        var topH = $(this).scrollTop() - parseInt($('.content').css('padding-top'));	
        $("#datatableControlWrapper").css("top",topH+"px");
    })*/
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
                	  name: 'columns.'+i+'.val' };
		if(columnsMetadata[i].special !== 'N'){
			fields[i].type = "readonly";
		};
		if(columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.BOOLEAN){
//				|| columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.TINYINT){
			fields[i].type = "checkbox";
		};
		if(columnsMetadata[i].dropDownColumnDisplay){
			fields[i].type = "select";
			fields[i].name = 'columns.'+i+'.val.value';
		};
		if(columnsMetadata[i].columnTypeString.toUpperCase() === "DATE"){
			fields[i].type = "datetime";
		};
		
		fields.push( {
	        "type":    "hidden",
	        "name":    "oldValues",
	        "default": "oldValues"
	    } );
		
	}
	return fields;
}

var createDataTableColumns = function(columnsMetadata){
	var columnsData = [];
	columnsData.push({
         data: null,
         defaultContent: '',
         className: 'select-checkbox',
         orderable: false
     });
	for(var i = 0; i < columnsMetadata.length; i++){
		var dataValue = 'columns.'+i+'.val';
		var editFieldValue = columnsMetadata[i].dropDownColumnDisplay ? 'columns.'+i+'.val.value' : 'columns.'+i+'.val';
		
		var renderFunction = function ( data, type, row ) {
	 	  if(data && data.dropdownOptions){
	 	    var options = [];
	 	    var returnValue = data.value; 
	 	    $.each(data.dropdownOptions, function(k,v){
	 	      options.push({'label':v[2], 'value':v[1]});
	 	      if(data.value == v[1]){
	 	        returnValue = v[2];
	 	      }
		    });
	 	    editor.field('columns.'+data.position+'.val.value').update(options);
		    return returnValue;
 	      } else if (data && data.timestamp){
 	    	  return data.timestamp;
 	      }
	 	  return data;
		};
		
		var className = '';
		if(columnsMetadata[i].allowUpdate){
			className = 'editable';
		}
		
		columnsData.push({'data': dataValue,
		  'editField': editFieldValue, 
		  'render': renderFunction,
		  'className': className});
	};
	return columnsData;
}

var createDropDownColumn = function(displayId, storeId, selectedValue){
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.datatable.dropdown, {'did': displayId, 'sid':storeId}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	//maybe not necessary
//    	data.sort(function(a, b) {
//		    return parseInt(a[0]) - parseInt(b[0]);
//		});
    	var select = '<select class="special" id="' + storeId + '" data-id="' + storeId + '">';
		$.each(data, function(i, column) {
			selectedValue === column[1] ? select+= '<option value="'+ column[1] +'" selected>'+ column[2] +'</option>' : select+= '<option value="'+ column[1] +'">'+ column[2] +'</option>';
		});
    }).fail(function(e){
    	handleError('#global-alert', e);
    	window.setTimeout(function() {
    		$("#global-alert").slideUp(500);
    	}, 4000);
    });
    select+='</select>';
 return select;
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
//	                $("#result").text(data);
	                console.log("SUCCESS : ", data);
	                $("#global-success-text").html(data.numRecords + ' record(s) successfully uploaded. Please refresh your page to see the latest changes.');
	                $("#global-success").slideDown(500);
                    window.setTimeout(function() {
                          $("#global-success").slideUp(500);
                          $("#global-success-text").html('Success!');
                    }, 4000);
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
	            	$('#fileUpload')[0].value='';
	            	tableData.draw();
//	            	$("#fileUpload").prop("disabled", false);
//	            	$('#fileUpload').val(null);
	            },
	    		// Custom XMLHttpRequest
//	    		xhr: function() {
//	    			var myXhr = $.ajaxSettings.xhr();
//	    			if (myXhr.upload) {
//	    				// For handling the progress of the upload
//	    				myXhr.upload.addEventListener('progress', function(e) {
//	    					if (e.lengthComputable) {
//	    						$('progress').attr({
//	    							value: e.loaded,
//	    							max: e.total,
//	    						});
//	    					}
//	    				} , false);
//	    			}
//	    			return myXhr;
//	    		},
	    	});
//	    }
	    
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
		if(columnsMetadata[i].allowUpdate === true){
			datatableEditor.enable(editorFields[i].name);
		} else {
			datatableEditor.disable(editorFields[i].name);
		}
	}
};
