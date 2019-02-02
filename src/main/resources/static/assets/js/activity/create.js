$(function () {
    /**
     * @namespace
     *
     * @property {Object} store
     * @property {Object} methods
     */
    var wrapper = {
        
        store: null, methods: {
        	loaded() {
        		switch (Ox00.response) {
	        		case Cx00:
	                    $.toast({
	                        heading : 'Success',
	                        text : 'A new activity has been created',
	                        loaderBg : '#ff6849',
	                        icon : 'success'
	                    });
	                break;
	        		case Cx01:
	                    $.toast({
	                        heading : 'Warning',
	                        text : 'An error occurred while creating an activity',
	                        loaderBg : '#ff6849',
	                        icon : 'warning'
	                    });
	                break;
        		}
        	}
        },
        
        init: function () {
            
            this.store = new Object();
            this.store.container = $('#request--container');
            
            $('.dropify').dropify();

            this.methods.loaded();
            
            return this;

        }
    }.init();
    
    $('#date-range input[name="start"]').datepicker({
    	clearBtn: true,
    	format: 'yyyy-mm-dd 00:00:00',
        autoclose: true,
        todayHighlight: true
    });
    $('#date-range input[name="end"]').datepicker({
    	clearBtn: true,
    	format: 'yyyy-mm-dd 23:59:59',
        autoclose: true,
        todayHighlight: true
    });
    
});