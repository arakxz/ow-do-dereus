$(function () {
    /**
     * @namespace
     *
     * @property {Object} store
     * @property {Object} methods
     */
    var wrapper = {
        
        store: null, methods: {
            
        },
        
        init: function () {
            
            this.store = new Object();
            this.store.container = $('#request--container');
            
            $('.dropify').dropify();

            return this;

        }
    }.init();
    
    $('#date-range.input-daterange').datepicker({
    	format: 'yyyy-mm-dd',
        autoclose: true,
        todayHighlight: true
    });
    
});