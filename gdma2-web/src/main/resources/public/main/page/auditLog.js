var configureDataTable = function() {

	var config = {
		fixedHeader : true,
		lengthMenu: [ [10, 25, 50, 100], [10, 25, 50, 100] ],
		order : [ [ 1, "asc" ] ],
		idSrc : 'id',
		"columns": [
		            { "data": "id" },
		            { "data": "auditHeader.id" },
		            { "data": "auditHeader.tableID" },
		            { "data": "auditHeader.type" },
		            { "data": "auditHeader.modifiedBy" },
		            { "data": "auditHeader.modifiedOn" },
		            { "data": "columnID" },
		            { "data": "oldValue" },
		            { "data": "newValue" }
		        ]
	};

	$('#tbl_audit_log').configureDataTable(config, {
		url : restUri.log.audit,
		complete : function() {
		}
	});
};

$(document).ready(function() {
	configureDataTable();
});