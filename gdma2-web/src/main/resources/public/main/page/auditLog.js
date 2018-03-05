var tableAuditLog;
var configureDataTable = function() {

	var config = {
		fixedHeader : true,
		lengthMenu: [ [10, 25, 50, 100], [10, 25, 50, 100] ],
		order : [ [ 4, "desc" ] ],
		idSrc : 'id',
		"columns": [
		            { "data": "id" },
		            { "data": "auditHeader.tableID.name" },
		            { "data": "auditHeader.type",
		              "render": function (data, type, row) {
		                  switch (data) {
			                  case 'C':
							      data = 'Create';
								  break;
			                  case 'U':
								  data = 'Update';
								  break;
						      case 'D':
								  data = 'Delete';
								  break;
							  default:
								  break;
						  }
		                  return data;
		                }	
		            },
		            { "data": "auditHeader.modifiedBy" },
		            { "data": "auditHeader.modifiedOn" },
		            { "data": "columnID.name" },
		            { "data": "oldValue" },
		            { "data": "newValue" },
		            { "data": "auditHeader.whereClause" }
		        ]
	};

	return $('#tbl_audit_log').configureDataTable(config, {
		url : restUri.log.audit,
		complete : function() {
		}
	});
};

$(document).ready(function() {
	tableAuditLog = configureDataTable();
});