var configureDataTable = function(){
	
	var config={
	        fixedHeader: true,
			order: [[ 1, "asc" ]],
			"lengthMenu": [ [25, 50, 100], [25, 50, 100] ],
			"columns": [
			            { "data": "id" },
			            { "data": "name" },
			            { "data": "username" },
			            { "data": "password" },
			            { "data": "connectionUrl" },
			            { "data": "connectionType.name", "defaultContent": "" },
			            { "data": "prefix" },
			            { "data": "active" }
			        ]
	};

	$('#tbl_server').configureDataTable(config, {url: restUri.server.table, complete: function(){
//		hideLoading();
	}});
};


$(document).ready(function(){
	configureDataTable();	
	
	$("#Save-server").click(function(){
		var name = $("#name").val();
		var username = $("#username").val();
		var password = $("#password").val();
		var url = $("#url").val();
		var prefix = $("#prefix").val();
		var active = $("#active").val();

	    $.ajax({
	        type: "post",
	        url: "http://localhost:8080/gdma2/rest/server/save",
	        data: JSON.stringify({id:"10",name:name,username:username,password:password,connectionUrl:url,prefix:prefix,active:active,alias:"AAA",connectionType:{"id":1,"name":"Postgres 9.6.2 JDBC","connectionClass":"org.postgresql.Driver","sqlgetTables":"select * from information_schema.tables WHERE table_schema='public'"},"tables":[],"connected":false,"lastError":""}),
		    success: function(data, textStatus, jqXHR) {
	            console.log('success');
	        },
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json'
	    });
	});	
	
	// Get Server list
    $.ajax({
        type: "get",
        asyn: false,
        url: "http://localhost:8080/gdma2/rest/server/list",
        data: { get_param: 'id' },
        success: function(data, textStatus, jqXHR) {
        	$.each(data, function(i, server) {
        		$("<li id='server" + server.id + "' class='treeview'><a href=''><i class='fa fa-database'></i><span> " + server.name + " </span><span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul class='treeview-menu'></ul></li>").appendTo(".sidebar-menu");
        	});
        	tableList();
        },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    });
	
	// Get Table list
    function tableList() {    	
    	$.ajax({
	        type: "get",
	        url: "http://localhost:8080/gdma2/rest/table/list",
	        data: { get_param: 'id' },
	        success: function(data, textStatus, jqXHR) {
			$.each(data, function(i, table) {
	            var serverId = table.server.id;
			    $("<li><a href=''><i class='fa fa-table'></i><span> " + table.name + " </span></a></li>").appendTo("#server" + serverId + " .treeview-menu");
			});
	        },
	        contentType: "application/json; charset=utf-8",
	        dataType: 'json'
	    });
    }
});