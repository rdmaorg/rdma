$(document).ready(function(){	
	//Get Connection types
	$.ajax({
        type: "get",
        url: restUri.connection.list,
        data: { get_param: 'id,name' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$.each(data, function(i, connection) {
    	    $("<option value='" + connection.id + "'>" + connection.name + "</option>").appendTo("#connectionType");
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });

	// Get Server list
	showLoading();
    $.ajax({
        type: "get",
        asyn: false,
        url: restUri.server.table,
        data: { get_param: 'data' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$.each(data.data, function(i, server) {
    		$("<li id='server" + server.id + "' class='treeview'><a class><i class='fa fa-database'></i><span class='serverN'> " + server.name + " </span><span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul class='treeview-menu'></ul></li>").appendTo(".sidebar-menu");
    		function tablesId() {
    			showLoading();
        		$.ajax({
        	        type: "get",
        	        url: mapPathVariablesInUrl(restUri.table.list_for_server_active, {serverId: server.id}),
        	        data: { get_param: 'id' },
        	        success: function(data, textStatus, jqXHR) {
        			$.each(data, function(i, table) {
        	            var serverId = table.server.id;
        			    $("<li><a href='" + restUri.table.list_for_server + "'><i class='fa fa-table'></i><span> " + table.name + " </span></a></li>").appendTo("#server" + serverId + " .treeview-menu");
        			});        			
        	        },
        	        contentType: "application/json; charset=utf-8",
        	        dataType: 'json'
        	    }).fail(function(e){
        	    	handleError('#global-alert', e);
        	    }).always(function(){
        	    	hideLoading();
        	    });
    		}
    		$("#server" + server.id + "").one("mouseenter",function(){
    			tablesId();
    		});
    		$("#server" + server.id + " .serverN").one("click",function(){
				sessionStorage.setItem("id",server.id);
				window.location.href = "tables";
    		});
    	});
    	
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
    
});