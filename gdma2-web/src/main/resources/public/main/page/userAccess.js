var configureUserAccessDatatable = function(){
	var config={
		        fixedHeader: true,
				"destroy": true,
				"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
				"columns": [
				            { "data": "id" },
				            { "data": "user.userName" , "defaultContent": "" },
				            { "data": "user.firstName", "orderable": false, "defaultContent": "" },
				            { "data": "allowDisplay", "orderable": false,"render": function(data, type, row){
				            		var checked = row.allowDisplay ? ' checked': '';
				            		return '<input type="checkbox"'+ checked +'>';
				            	}
				            },
				            { "data": "allowUpdate", "orderable": false,"render": function(data, type, row){
				            		var checked = row.allowUpdate ? ' checked': '';
				            		return '<input data-row="'+row+'" type="checkbox"'+ checked +'>';
				            	}
				            },
				            { "data": "allowInsert", "orderable": false,"render": function(data, type, row){
				            		var checked = row.allowInsert ? ' checked': '';
				            		return '<input type="checkbox"'+ checked +'>';
				            	}
				            },
				            { "data": "allowDelete", "orderable": false,"render": function(data, type, row){
				            		var checked = row.allowDelete ? ' checked': '';
				            		return '<input type="checkbox"'+ checked +'>';
				            	}
				            }
				        ]
		};
	$('#tbl_userAccess').configureDataTable(config, {
		url: mapPathVariablesInUrl(restUri.userAcces.table, {id: selectedTableId}),
		dataSrc: "",
		complete: function(){
			//hideLoading();
		},
		error: function(message, e){
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert',e);
		}
	});
}