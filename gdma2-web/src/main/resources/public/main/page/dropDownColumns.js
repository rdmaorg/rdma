var dropDownTable;
var initiateModalDropDownColumns = function() {
	configureServerDatatable();
	associateSaveDropDownColumn();
}

var configureServerDatatable = function() {
	var config = {
		fixedHeader : true,
		order: [[ 1, "asc" ]],
		"page" : 0,
		"stateSave" : false,
		"oLanguage": {
	         "sSearch": "Search:"
	     },
		"destroy" : true,
		"columns" : [
				{
	                "className":      'details-control fa fa-plus-circle',
	                "orderable":      false,
	                "data":           null,
	                "defaultContent": ''
	            },
	            {
	            	"data": "id"
				},
				{
					"data": "name"
				}
			]
	};
	dropDownTable = $('#tabl_dropdown').configureDataTable(config, {
		url : restUri.server.table,
		dataSrc : "",
		complete : function() {
			// hideLoading();
			configureDropDownTable();
		},
		error : function(message, e) {
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert', e);
		}
	});
}


var configureDropDownTable = function(){
	 $('#tabl_dropdown tbody').on('click', 'td.details-control', function () {
        var tr = $(this).closest('tr');
        var row = dropDownTable.row( tr );
 
        if ( row.child.isShown() ) {
            // This row is already open - close it
            row.child.hide();
            tr.removeClass('shown');
            tr.find("td").removeClass('fa-minus-circle');            
            tr.find("td").addClass('fa-plus-circle');
        }
        else {
            $.Deferred().done(row.child( createDatatable(row.data(),"datatable-table"+row.data().id)).show(),configureTable(row.data()));
            tr.addClass('shown');
            tr.find("td").addClass('fa-minus-circle');            
            tr.find("td").removeClass('fa-plus-circle');
        }
    } );
}

var createDatatable = function(d, tableId){
	return '<table id="'+ tableId +'"'+
				'class="table table-striped table-hover dt-responsive" cellspacing="0"'+
				'width="100%">'+
				'<thead>'+
					'<tr>'+
						'<th></th>'+
						'<th>Id</th>'+
						'<th>Name</th>'+
					'</tr>'+
				'</thead>'+
			'</table>';
}

var configureTable = function(d) {
	var config = {
		fixedHeader : true,
		order: [[ 1, "asc" ]],
		"page" : 0,
		"stateSave" : false,
		"oLanguage": {
	         "sSearch": "Search:"
	     },
		"destroy" : true,
		"columns" : [
				{
	                "className":      'details-control table',
	                "orderable":      false,
	                "data":           null,
	                "defaultContent": ''
	            },
	            {
	            	"data": "id"
				},
				{
					"data": "name"
				}
			]
	};
	$('#datatable-table'+ d.id).configureDataTable(config, {
		url : mapPathVariablesInUrl(restUri.table.table, {id: d.id}),
		dataSrc : "",
		complete : function() {
			// hideLoading();
		},
		error : function(message, e) {
			console.error("ERROR: " + JSON.stringify(e));
			handleError('#global-alert', e);
		}
	});
}
 var createColumnsDetail = function( d ) {
    // `d` is the original data object for the row
    return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'+
        '<tr>'+
            '<td>Column id:</td>'+
            '<td>'+d.extn+'</td>'+
        '</tr>'+
        '<tr>'+
	        '<td>Column Name:</td>'+
	        '<td>'+d.name+'</td>'+
        '</tr>'+
        '<tr>'+
            '<td>Extra info:</td>'+
            '<td>And any further details here (images etc)...</td>'+
        '</tr>'+
    '</table>';
}
 
var associateSaveDropDownColumn = function() {
	$("#save-dropdowncolumn").click(function(e) {
		$(this).confirmation({
			placement : "left",
			btnOkLabel : "Edit dropdown column",
			onConfirm : function(event, element) {
				showLoading();
//				columnDisplay
				hideLoading();
			}
		});
		$(this).confirmation('show');
		e.preventDefault();
	});
}
