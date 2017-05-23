var tableId;
var columnsMetadata = new Array();
var tableData;
var editorData;

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
    	columnsMetadata.unshift({name:"", alias:""});
    	$("#tableHeaderRow").empty();
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
		fields: editorFields
	}
	editorData = $('#table_data').configureEditor(configEditor);
	var columnsData = createDataColumns(columnsMetadata);
	var config={
		 "dataSrc": "data",
		 "columns": columnsData,
         dom : "Bfrtip",
         select: true,
		 buttons: [
              { extend: "create", editor: editorData },
              { extend: "edit",   editor: editorData },
              { extend: "remove", editor: editorData },
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
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
			window.setTimeout(function() {
	    		$("#global-alert").slideUp(500);
	    	}, 4000);
		}
	});
	
}

var createEditorFields = function(columnsMetadata){
	var fieldTypes = {
			VARCHAR: "VARCHAR",
			BOOLEAN: "BOOLEAN",
			TINYINT: "TINYINT"
	}
	var fields = [];
	for(var i = 1; i < columnsMetadata.length; i++){
		fields[i-1] = { label: columnsMetadata[i].alias ,
                	  name: ""+i };
		if(columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.BOOLEAN
				|| columnsMetadata[i].columnTypeString.toUpperCase() === fieldTypes.TINYINT){
			fields[i-1].type = "checkbox";
		}
	}
	return fields;
}

var createDataColumns = function(columnsMetadata){
	var columnsData = [];
	for(var i = 0; i < columnsMetadata.length; i++){		
		if (columnsMetadata[i].dropDownColumnDisplay){
			console.log('columnsMetadata['+i+'].dropDownColumnDisplay: ' + JSON.stringify(columnsMetadata[i].dropDownColumnDisplay));
			columnsData.push({"data": ''+i,
						 render: function ( data, type, row ) {
						 	 
							 console.log('data: ' + JSON.stringify(data));
							 if(data){
						 	 console.log('data.did: ' + data.did);
						 	 console.log('data.sid: ' + data.sid);
						 	 console.log('type: ' + type);
						 	 console.log('row: ' + row);
					 	     console.log('data.dropdownOptions: ' + data.dropdownOptions);
					 	     if(data.dropdownOptions){
						 	    	 var $select = $("<select></select>", {
						 	    		 "id": "dropdownoption" + row[0],
						 	    		 "value": data.value
						 	    	 });						 	 
						 	    	 $.each(data.dropdownOptions, function(k,v){
						 	    		 var $option = $("<option></option>", {
						 	    			 "text": v[2],
						 	    			 "value": v[1]
						 	    		 });
						 	    		 if(data.value == v[1]){
						 	    			 $option.attr("selected", "selected")
						 	    		 }
						 	    		 $select.append($option);
						 	    	 });
						 	    	 return $select.prop("outerHTML");
						 	     }
							 }
					 	     return data;
						    }
					});
		} else {
			columnsData.push({"data": ''+i});
		}
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
	tableId = sessionStorage.getItem("tableId");
	if(tableId){
		loadDatatable();
	}
});

function filterDisplayed(column) {
    return column.displayed === true;
};