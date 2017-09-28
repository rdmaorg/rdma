var configureDataTable = function() {

	var config = {
		fixedHeader : true,
		lengthMenu: [ [10, 25, 50, 100], [10, 25, 50, 100] ],
		order : [ [ 1, "asc" ] ],
		idSrc : 'id',
		"columns": [
		            { "data": "id" },
		            { "data": "activity" },
		            { "data": "activityFrom" },
		            { "data": "activityBy" },
		            { "data": "activityOn" }
		        ]
	};

	$('#tbl_activity_log').configureDataTable(config, {
		url : restUri.log.activity,
		complete : function() {
		}
	});
};

$(document).ready(function() {
	configureDataTable();
});