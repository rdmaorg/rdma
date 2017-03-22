/**
 * 
<div id="global-alert" class="alert alert-dismissible" role="alert" style="display: none;">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close">&times;</button>
  <span>Information goes here</span>
</div>
 * 
 */


(function($) {
	
	var msgClass = {
			"info": "alert-info",
			"success": "alert-success",
			"warning": "alert-warning",
			"error": "alert-danger"
	};
	
	var gopts = {
			type: "info",
			operation: "show",
			message: ""
	};
	
	var getOptions = function(opts){
		if(opts){
			var options = {};
			options.type = opts.type? opts.type: gopts.type;
			options.operation = opts.operation? opts.operation: gopts.operation;
			options.message = opts.message? opts.message: gopts.message;
			return options;
		} else {
			return gopts;
		}
	};
	
	
	$.fn.alertMessage = function(opts){
		var theParent = jQuery(this);
		opts = getOptions(opts);
		if(opts.operation === gopts.operation){
			var messageClass = msgClass[opts.type];
			if(!messageClass){
				messageClass = msgClass[gopts.type];
			}
			if(opts.message){
				var theSpan = theParent.find("span");
				theParent.find(".close").click(function() {
					theParent.slideUp();
				});

				theParent.removeClass("alert-danger alert-success alert-danger  alert-warning alert-info ").addClass(messageClass);
				theSpan.empty();				
				theSpan.html(opts.message);
				theParent.slideDown();
			} else {
				console.error("A message MUST be provided for " + theParent);
			}
		} else {
			theParent.slideUp();
		}
	};
	
}(jQuery));




