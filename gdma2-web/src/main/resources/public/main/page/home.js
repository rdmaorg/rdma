var tableId;
var columns = new Array();
var tableData;
var editorData;

var loadDatatable = function(){
	showLoading();
	$.ajax({
        type: "get",
        url: mapPathVariablesInUrl(restUri.column.column_data, {id: tableId}),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$("#tableHeaderRow").empty();
		columns[0] = {
			name : ""
		}
		$("<th>"+ columns.name +"</th>").appendTo("#tableHeaderRow");
		$.each(data, function(i, column) {
			columns[i + 1] = column;
			$("<th>" + column.name + "</th>").appendTo("#tableHeaderRow");
		});
    }).fail(function(e){
    	handleError('#global-alert', e);
    	window.setTimeout(function() {
    		$("#global-alert").slideUp(500);
    	}, 4000);
    }).complete(function(e){
    	configureDataTable();
    }).always(function(){
    	hideLoading();
    	$("#tableRow").show();
    });
	
}
//configureEditor

var configureDataTable = function(){
	var editorFields = createEditorFields();
	
	var configEditor = {
		table: "#table_data",
		createFunction: insertData,
		editFunction: editData,
		removeFunction: removeData,
		fields: editorFields
	}
	
	editorData = $('#table_data').configureEditor(configEditor);
	
	var columnsData = createDataColumns();

	var config={
		 "dataSrc": "data",
		 "columns": columnsData,
         dom : "Bfrtip",
		 buttons: [
              { extend: "create", editor: editorData },
              { extend: "edit",   editor: editorData },
              { extend: "remove", editor: editorData }
          ]
	};

	tableData = $('#table_data').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.table.table_data, {id: tableId}),
		complete: function(){
			//hideLoading();
		},
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
			window.setTimeout(function() {
	    		$("#global-alert").slideUp(500);
	    	}, 4000);
		}
	});
	
}

var createEditorFields = function(){
	var fields = [];
	for(var i = 1; i < columns.length; i++){
		fields[i-1] = { label: columns[i].alias ,
                	  name: ""+i };
	}
	return fields;
}

var createDataColumns = function(){
	var columnsData = [];
	for(var i = 0; i < columns.length; i++){
		columnsData[i] = {"data": ""+i};
	}
	return columnsData;
}

var insertData = function(d, successCallback, errorCallback){
	console.log(d.data);
	successCallback(d);
}

var editData = function(d, successCallback, errorCallback){
	console.log(d.data);
	successCallback(d);
}

var removeData = function(d, successCallback, errorCallback){
	console.log(d.data);
	successCallback(d);
}

$(document).ready(function(){	
	tableId = sessionStorage.getItem("tableId");
	if(tableId){
		loadDatatable();
	}
    
});