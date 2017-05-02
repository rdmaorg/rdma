var changedColumns = new Object();
var origColumns = new Object();
var tableSessionId = sessionStorage.getItem("idTable");
var tableSessionName = sessionStorage.getItem("nameTable");
var configureDataTable = function(){
	
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
			            { "data": "dropDownColumnDisplay" },
			            { "data": "dropDownColumnStore" },
			            { "data": "special","render" : function(data, type, row){ 
			            		return createDropDowSpecial(row.id, row.special);
			            	}			            
			            },
			            { "data": "minWidth", "render" : function(data, type, row){ 
		            			return '<input type="text" class="min-width input-disabled">' ;
		            		}			       
			            },
			            { "data": "maxWidth", "render" : function(data, type, row){ 
		            			return '<input type="text" class="max-width input-disabled">' ;
	            			}			 
			            },
			            { "data": "columnSize", "render" : function(data, type, row){ 
		            			return '<input type="text" class="column-size input-disabled">' ;
            				}			 
			            }
			        ]
	};

	$('#tbl_column').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.column.table, {id: tableSessionId}), 
		complete: function(){
	//		hideLoading();
			associateCheckBoxes();
			associateInput("min-width");	
			associateInput("max-width");
			associateInput("column-size");
		}
	});
};

var createDropDowSpecial = function(id, value){
	var select = '<select class="special" id="' + id + '">';
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
}

var associateInput = function(input){
	$("."+input).click(function(e) {
		$(e.target).removeClass("input-disabled");
		var oldValue = e.target.value;
		$(this).confirmation({
			placement : "left",
			btnOkLabel : "Ok",
			onConfirm : function(event, element) {
				console.log("confirmated");
				$(e.target).addClass("input-disabled");
			},
			onCancel : function(event, element) {
				e.target.value=oldValue;
				$(e.target).addClass("input-disabled");
			}
		});
		$(this).confirmation('show');
	});
}
$(document).ready(function(){
	configureDataTable();
    $("#tableName").html(sessionStorage.getItem("nameTable"));
	
});