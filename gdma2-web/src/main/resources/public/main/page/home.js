var tableId;
var serverId;
var columnsMetadata = new Array();
var tableData;
var datatableEditor;

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
    	console.log('columnsMetadata: ');
    	console.log(columnsMetadata);
    	
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
		createFunction: insertData,
		editFunction: editData,
		removeFunction: removeData,
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
                	console.log('deleting rows: ' + JSON.stringify(d));
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
                onBlur: 'submit'
            }
        }
	}
	datatableEditor = $('#table_data').configureEditor(configEditor);

	var columnsData = createDataTableColumns(columnsMetadata);
	console.log('columnsData: ' + JSON.stringify(columnsData));
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
					   }
					 }
				 }
				 editor = this.datatableEditor;
				 return json.data;
			 }
		 },
		 "columns": columnsData,
		 idSrc : 'rowNumber',
         dom : "Blfrtip",
         keys: {
             columns: ':not(:first-child)',
             keys: [ 9 ]
         },
         select: {
             style:    'os',
             selector: 'td:first-child'
         },
		 buttons: [
              { extend: "create", editor: datatableEditor },
              { extend: "edit",   editor: datatableEditor },
              { extend: "remove", editor: datatableEditor },
              { extend: "csv", 
            	text:"Download",
                exportOptions: {
                  modifier: {
                    search: 'none'
                  }
                } }
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
	
	// Activate an inline edit on click of a table cell
  $('#table_data').on( 'click', 'tbody td:not(:first-child)', function (e) {
    	datatableEditor.inline( this, { submit: 'allIfChanged',
    		submitOnBlur: true
    		} );
    } );
  
  //Inline editing on tab focus
  tableData.on( 'key-focus', function ( e, datatable, cell ) {
	  datatableEditor.inline( cell.index(),{ submit: 'allIfChanged',
  		submitOnBlur: true
		} );
  } );
	
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
		if(columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.BOOLEAN
				|| columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.TINYINT){
			fields[i].type = "checkbox";
		}
		if(columnsMetadata[i].dropDownColumnDisplay){
			fields[i].type = "select";
			fields[i].name = 'columns.'+i+'.val.value';
		}
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
 	      }
	 	  return data;
		};
		
		columnsData.push({'data': dataValue,
		  'editField': editFieldValue, 
		  'render': renderFunction});
	};
	return columnsData;
}

var insertData = function(d, successCallback, errorCallback){
	console.log('insertData function triggered');
	console.log(d.data);
	successCallback(d);
}

var editData = function(d, successCallback, errorCallback){
	console.log('editData function triggered');
	console.log(d.data);
	successCallback(d);
}

var removeData = function(d, successCallback, errorCallback){
	console.log('removeData function triggered');
	console.log(d.data);
	successCallback(d);
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


$(document).ready(function(){	
	$("#serverName").html(sessionStorage.getItem("serverName"));
	$("#tableName").html(sessionStorage.getItem("tableName"));
	serverId = sessionStorage.getItem("serverId");
	tableId = sessionStorage.getItem("tableId");
	if(tableId){
		loadDatatable();
	}
});

function filterDisplayed(column) {
    return column.displayed === true;
};