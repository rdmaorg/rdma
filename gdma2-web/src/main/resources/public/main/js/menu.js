var dataModulePage = getContextPath() + "/";

$(document).ready(function(){
	buildDataModuleMenu();
	loadUserMenu();
	loadAdminMenu();
});



var buildDataModuleMenu = function(){
	// Get Server list
	showLoading();
    $.ajax({
        type: "get",
        asyn: false,
        url: restUri.datatable.servers,
        data: { get_param: 'data' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){
    	$(".sidebar-menu").html('<li class="header">SERVER LIST</li>');
    	$.each(data, function(i, server) {
    		$("<li id='server" + server.id + "' class='treeview'><a><i class='fa fa-database'></i><span class='serverN'> " + server.name + " </span><span class='pull-right-container'><i class='fa fa-angle-left pull-right'></i></span></a><ul id='list" + server.id + "' class='treeview-menu'><div id='header" + server.id + "'></div></ul></li>").appendTo(".sidebar-menu");
    		function tablesId() {
    			showLoading();
        		$.ajax({
        	        type: "get",
        	        url: mapPathVariablesInUrl(restUri.datatable.tables, {id: server.id}),
        	        data: { get_param: 'id' },
        	        success: function(data, textStatus, jqXHR) {
        			$.each(data, function(i, table) {
        	            var serverId = table.server.id;
        			    $("<li><a href='" + dataModulePage + "' class='table-data' data-id='"+table.id+"'><i class='fa fa-table'></i><span> " + table.name + " </span></a></li>").appendTo("#server" + serverId + " .treeview-menu");
        			});        			
        	        },
        	        contentType: "application/json; charset=utf-8",
        	        dataType: 'json'
        	    }).fail(function(e){
        	    	handleError('#global-alert', e);
        	    }).complete(function(e){
        	    	$(".table-data").click(function(){
        		    	var link = $(this);
        		    	sessionStorage.setItem("tableId", link.data("id"));
        		    });
        	    }).always(function(){
        	    	
        	    	hideLoading();
        	    });
    		}
    		(function ($) {
    			  // custom css expression for a case-insensitive contains()
    			  jQuery.expr[':'].Contains = function(a,i,m){
    			      return (a.textContent || a.innerText || "").toUpperCase().indexOf(m[3].toUpperCase())>=0;
    			  };


    			  function listFilter(header, list) { // header is any element, list is an unordered list
    			    // create and add the filter form to the header
    			    var form = $("<form>").attr({"class":"filterform","action":"#"}),
    			        input = $("<input>").attr({"class":"filterinput","type":"text"});
    			    $(form).append(input).appendTo(header);

    			    $(input)
    			      .change( function () {
    			        var filter = $(this).val();
    			        if(filter) {
    			          // this finds all links in a list that contain the input,
    			          // and hide the ones not containing the input while showing the ones that do
    			          $(list).find("a:not(:Contains(" + filter + "))").parent().slideUp();
    			          $(list).find("a:Contains(" + filter + ")").parent().slideDown();
    			        } else {
    			          $(list).find("li").slideDown();
    			        }
    			        return false;
    			      })
    			    .keyup( function () {
    			        // fire the above change event after every letter
    			        $(this).change();
    			    });
    			  }


    			  //ondomready
    			  $(function () {
    			    listFilter($("#header" + server.id + ""), $("#list" + server.id + ""));
    			  });
			}(jQuery))
    		$("#server" + server.id + "").one("mouseenter",function(){
    			tablesId();
    		});
    		
    		// Link to server tables
    		//$("#server" + server.id + " .serverN").one("click",function(){
			//	sessionStorage.setItem("id",server.id);
			//	window.location.href = "tables";
    		//});
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    	hideLoading();
    });
}

var loadAdminMenu = function(){
	
	//Get Admin Menu
	$.ajax({
        type: "get",
        url: restUri.menu.app,
        data: { get_param: 'data' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){	
    	$.each(data[0].children, function(i, menu) {
    		$('<div class="form-group"><a class="btn btn-primary btn-block btn-sm" href="/gdma2'+ menu.view +'"><h3><i class="fa '+ menu.iconClass +'" aria-hidden="true"></i><br>'+ menu.name +'</h3><p>'+ menu.description +'</p></a></div>').appendTo("#control-sidebar-settings-tab");	
    	});
    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    });
	
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
}

var loadUserMenu = function(){
	//Get User Menu
	$.ajax({
        type: "get",
        url: restUri.menu.user,
        data: { get_param: 'data' },
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    }).done(function(data){	
		$(".user-menu span").html(data[0].name);
    	$.each(data[0].children, function(i, menu) {
    		$('<li><a href="/gdma2'+ menu.view +'" class="btn btn-default btn-flat">'+ menu.name +'</a></li>').appendTo(".dropdown-menu-right");
    	});

    }).fail(function(e){
    	handleError('#global-alert', e);
    }).always(function(){
    });
}