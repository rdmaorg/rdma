var config={
	        fixedHeader: true,
			order: [[ 0, "asc" ]],
			"lengthMenu": [ [10, 25, 50, 100], [10, 25, 50, 100] ],
			"columnDefs": [ { "orderable": false, "targets": 8 } ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "username" },
			            { "data": "password" },
			            { "data": "connectionUrl" },
			            { "data": "connectionType.name", "defaultContent": "" },
			            { "data": "prefix" },
			            { "data": "active" },
			            { "data": "active","render" : function(data, type, row){ 	
			            	return '<button class="btn btn-primary btn-xs editServer" data-serverid="'+ row.id+ '"><i class="fa fa-pencil-square-o"></i> Edit</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-warning btn-xs deleteServer" data-serverid="'+ row.id+ '"><i class="fa fa-trash-o"></i> Delete</button>'
			            	+ '&nbsp;'
			            	+'<button class="btn btn-info btn-xs viewServer" data-serverid="'+ row.id+ '"><i class="fa fa-table"></i> Tables</button>'
			            	} 
			            }
			        ]
	};

tblServerList = $('#tbl_server').configureDataTable(config, {
	url: restUri.server.table,
	complete: function(){
		//hideLoading();
		associateDeleteServer();
		associateEditServer();
		associatePostServer();
		associateViewServer();
	},
	error: function(message, e){
		console.error("ERROR: " + JSON.stringify(e));
		handleError('#global-alert',e);
	}
});