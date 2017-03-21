var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "firstName" },
			            { "data": "lastName" },
			            { "data": "userName" },
			            { "data": "domain" },
			            { "data": "admin" },
			            { "data": "locked" },
			            { "data": "active" },
			            { "data": "userAccess" }
			        ]
	};

	$('#tbl_user').configureDataTable(config, {url: restUri.user.table, complete: function(){
//		hideLoading();
	}});
};


$(document).ready(function(){
	configureDataTable();	
});