/*
 * Dependencies:
 * 1. JQuery
 * 2. JQuery DataTables
 * 3. Moment JS
 * 4. JQuery DataTable Moment JS
 */
(function($) {
	var container;
	
	var gopts={
			serverSide: true,
//			url:url,
			dateTimeFormat: "DD-MMM-YYYY HH:mm:SS",
			pager_length: 5,
			complete: function(){
//				hideLoading();
			},
			error: function(message, error){
				alert(message + "\n" + JSON.stringify(error) );
			}
	};
	
	var getOptions = function(opts){
		var options = {};
		if(opts){
			options.dateTimeFormat = opts.dateTimeFormat? opts.dateTimeFormat : gopts.dateTimeFormat;
			options.pager_length = opts.pager_length? opts.pager_length : gopts.pager_length;
			options.url = opts.url? opts.url : "";
			options.serverSide = opts.url || (opts.serverSide && opts.url)? true : false;
			options.complete = opts.complete? opts.complete : gopts.complete;
			options.error = opts.error? opts.error : gopts.error;
		} else {
			options = gopts;
		}
		return options;
	};
	
	$.fn.dataTable.ext.errMode = 'throw';
	
	$.fn.configureDataTable = function(dtConfig, opts) {
		container = jQuery(this);
		opts = getOptions(opts);
		var cfg = {
		    fixedHeader: true,
		    "processing" : true,
			"stateSave" : true,
			"stateDuration" : -1,
			"pageLength" : 10,
			"lengthMenu": [ [10, 25, 50, -1], [10, 25, 50, "All"] ],
			"language": {
			      "emptyTable": "No information available"
			}
		};
		
		if (opts.serverSide) {
			$.extend(cfg, {
				"serverSide" : true,
				"ajax" : {
					"url" : opts.url
				}
			});
		} else {
			$.extend(cfg, {
				"serverSide" : false
			});
			$.fn.dataTable.moment(opts.dateTimeFormat);
		}

		$.fn.DataTable.ext.pager.numbers_length = opts.pager_length;
		
		if (dtConfig) {
			$.extend(cfg, dtConfig);
		}
		
		if(dtConfig.dataTablesPostRequest == true){
			cfg.ajax.type = "POST";    //Convert any requests to (rest/datatable/table) to POST because large amount of columns were causing 'large request uri' error
		}
		
		var tbl=$(container).on('error.dt', function(e, settings, techNote, message){
			opts.error(message, (settings.jqXHR? settings.jqXHR : {"error": "Unknown Error"}) );
		}).DataTable(cfg);
		
//		console.log("configureDataTable::DT.Config: " + JSON.stringify(cfg));
		
		if (opts.serverSide){
			$(container).on( 'draw.dt', function () {
				opts.complete();
			} );
		} else {
			opts.complete();
		}
		
		return tbl;
	};

}(jQuery));

