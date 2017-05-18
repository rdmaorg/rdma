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
		multipleEditing: true
	};
	
	var getOptions = function(opts){
		var options = {};
		if(opts){
			options.multipleEditing = opts.multipleEditing? opts.multipleEditing : gopts.multipleEditing;
		} else {
			options = gopts;
		}
		return options;
	};
	
	$.fn.configureEditor = function(editorConfig, opts) {
		opts = getOptions(opts);
		var cfg = {
			ajax : function(method, url, d, successCallback, errorCallback) {
				if (d.action === 'create') {
					if (editorConfig.createFunction) {
						editorConfig.createFunction(d, successCallback, errorCallback);
					}
				} else if (d.action === 'edit') {
					if (editorConfig.editFunction) {
						editorConfig.editFunction(d, successCallback, errorCallback);
					}
				} else if (d.action === 'remove') {
					if (editorConfig.removeFunction) {
						editorConfig.removeFunction(d, successCallback, errorCallback);
					}
				}
			}
		};
		
		if (editorConfig) {
			$.extend(cfg, editorConfig);
		}
		
		var editor = new $.fn.dataTable.Editor(cfg);
		
		return editor;
	};

}(jQuery));

