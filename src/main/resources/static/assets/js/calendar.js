$(function () {
    /**
     * @namespace
     *
     * @property {Object} store
     * @property {Object} methods
     */
    var wrapper = {

        store: null, methods: {

            createNewEvent: function (event) { event.preventDefault();

                $.imodal({

                    title: 'Add Event',
                    body() {

                        var options = '';
                        for (category in Ox00.categories) {
                            options += `<option value="${category}">${Ox00.categories[category]}</option>`
                        }

                        return ''
                            + '<form action="/dashboard/calendar/event" method="post">'
                            + '  <div class="row">'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Event Name</label>'
                            + '        <input class="form-control" placeholder="Insert Event Name" type="text" name="title">'
                            + '      </div>'
                            + '    </div>'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Category</label>'
                            + '        <select class="form-control" name="category">'+ options +'</select>'
                            + '      </div>'
                            + '    </div>'
                            + '  </div>'
                            + '</form>';

                    },
                    button: 'Create event',

                    then() {

                        var title = this.find('input[name="title"]');
                        var category = this.find('select[name="category"]');

                        if (!title.val().trim()) {
                            console.log('empty');
                            return;
                        }

                        this.wait().find('form').submit();

                    }

                });
            },


            /**
             * 
             */
            drop: function (date, event, ui) {
               // removed 
            },


            eventClick: function(event) {

                var days = event.end === null
                		 ? 1
                		 : event.end.diff(event.start, 'days') + 1;

                $.imodal({

                    title: 'Add Event',
                    body() {

                    	var aux = event.className[0];
                        var options = '';
                        
                        for (category in Ox00.categories) {
                            options += (category !== aux)
                                    ? `<option value="${category}">${Ox00.categories[category]}</option>`
                                    : `<option value="${category}" selected>${Ox00.categories[category]}</option>`;
                        }

                        return ''
                            + '<div>'
                            + '  <div class="row">'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Event Name</label>'
                            + '        <input class="form-control" placeholder="Insert Event Name" type="text" value="'+ event.title +'" name="title">'
                            + '      </div>'
                            + '    </div>'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Category</label>'
                            + '        <select class="form-control" name="category">'+ options +'</select>'
                            + '      </div>'
                            + '    </div>'
                            + '    <div class="col-md-12">'
                            + '        <label class="control-label">Duración</label>'
                            + '        <input type="range" name="duration" class="form-control" min="1" max="10" step="1" id="range" value="'+ days +'">'
                            + '    </div>'
                            + '  </div>'
                            + '</div>';

                    },
                    button: 'Update event',

                    then() {

                        var title = this.find('input[name="title"]').val().trim();
                        var category = this.find('select[name="category"]').val().trim();
                        var duration = parseInt(this.find('input[name="duration"]').val().trim()) || 1; 

                        if (!title.length) {
                            console.log('empty');
                            return;
                        }

                        // Add more or less duration to event
                        event.end = moment(event.start.format('YYYY-MM-DD 23:59:59'));
                        event.end.add(duration - 1, 'days');

                        /** @callback */
                        var $update = wrapper.calendar.update;
                            $update(Object.assign(event, { title, category }), event);

                        this.done();

                    }

                });
            },


            eventDelete: function ($event) {

            },


            /**
             * @param {Object} event
             * @param {Object} delta
             *
             * @callback rollback
             */
            eventDrop: function (event, delta, rollback) {

            	/** @callback */
                var $update = wrapper.calendar.update;
                    $update(Object.assign(event, { category: event.className[0] }));

            },


            /**
             * @param {Object} event
             */
            eventReceive: function (event) {

            	// TODO
                // When the event is dragged until a date this
                // event is not a finish date, by default we will assign
                // the start date
            	if (event.end === null) {
            		event.end = moment(event.start.format('YYYY-MM-DD 23:59:59'));
            	}

                /** @callback */
                var $render = wrapper.calendar.render;
                    $render({
                        title: event.title,
                        start: event.start.format('YYYY-MM-DD'),
                        end: event.end.format('YYYY-MM-DD HH:mm:ss'),
                        category: event.category
                    }, event, function () {                    	
                    	wrapper.store.container.fullCalendar('removeEvents', event._id);
                    });
                
            },
            
            
            /**
             * @param {Object} event
             * @param {Object} delta
             *
             * @callback rollback
             */
            eventResize: function(event, delta, rollback) {

                /** @callback */
                var $update = wrapper.calendar.update;
                    $update(Object.assign(event, { category: event.className[0] }));
                
            },


            /**
             * @param {Object} start
             * @param {Object} end
             */
            select: function (start, end) {

                $.imodal({

                    title: 'Add Event',
                    body() {

                        var options = '';
                        for (category in Ox00.categories) {
                            options += `<option value="${category}">${Ox00.categories[category]}</option>`
                        }

                        return ''
                            + '<form action="/dashboard/calendar/event" method="post">'
                            + '  <div class="row">'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Event Name</label>'
                            + '        <input class="form-control" placeholder="Insert Event Name" type="text" name="title">'
                            + '      </div>'
                            + '    </div>'
                            + '    <div class="col-md-6">'
                            + '      <div class="form-group">'
                            + '        <label class="control-label">Category</label>'
                            + '        <select class="form-control" name="category">'+ options +'</select>'
                            + '      </div>'
                            + '    </div>'
                            + '  </div>'
                            + '</form>';

                    },
                    button: 'Create event',

                    then() {

                        var title = this.find('input[name="title"]').val().trim();
                        var category = this.find('select[name="category"]').val().trim();

                        if (!title.length) {
                            console.log('empty');
                            return;
                        }

                        /** @callback */
                        var $render = wrapper.calendar.render;
                            $render({
                                title,
                                start: start.format('YYYY-MM-DD'),
                                end: start.format('YYYY-MM-DD 23:59:59'),
                                // TODO
                                // It is sent the same day with the last available time.
                                // end: end.format('YYYY-MM-DD'),
                                category
                            });
                        
                        this.done();

                    }

                });
            }

        },

        /**
         * @return {Object} wrapper object
         */
        init: function () {
                        
            var $store = this.store = new Object();
            var $methods = this.methods;
            
            $('#calendar--events div.calendar-events').each(function () {
                $(this)
                    // store data so the calendar knows to render an event upon drop
                    .data('event', {
                        title: $(this).data('title'),
                        category : $(this).data('class'),
                        className: $(this).data('class')
                    })
                    // make the event draggable using jQuery UI
                    .draggable({
                        zIndex: 999,
                        revert: true,
                        revertDuration: 0
                    });
            });

            $('#calendar--events-create').on('click', $methods.createNewEvent);

            $store.container = $('#calendar--container');
            $store.container.fullCalendar({
                header: {
                    left: 'prev,next',
                    center: 'title',
                    right: 'today'
                },
                allDayDefault: false,
                displayEventTime: false,
                droppable: true,
                // drop: $methods.drop,
                selectable: true,
                select: $methods.select,
                editable: true,
                eventClick: $methods.eventClick,
                eventDrop: $methods.eventDrop,
                eventReceive: $methods.eventReceive,
                // eventResize: $methods.eventResize,
                events: {
                    url: '/dashboard/calendar/events',
                    error: function () {
                        // TODO
                        console.log('error');
                    }
                },
                loading: function(bool) {
                    // TODO
                }
            });

            return this;

        },
        
        
        /**
         * @namespace calendar util 
         */
        calendar: {
            
            /**
             * @param {Object}  data
             * @param {Object} [original]
             * 
             * @callback rollback
             */
            render: function(data, original, rollback) {
                
                /** @callback */
                var $container = wrapper.store.container;

                $.ajax({
                    url: '/dashboard/calendar/event/register',
                    method: 'POST',
                    data 
                })
                .done(function (response) {
                	if (original !== void 0) {
                		$container.fullCalendar('removeEvents', original._id);
                	}
                	$container.fullCalendar('renderEvent', response.data);
                    
                });
                
            },
            
                        
            /**
             * @param {Object}  data
             * @param {Object} [original]
             */
            update: function (data, original) {
                
                /** @callback */
                var $container = wrapper.store.container;

                $.ajax({
                    url: `/dashboard/calendar/event/${data.id}/update`,
                    method: 'PUT',
                    data: {
                        title: data.title,
                        start: data.start.format('YYYY-MM-DD'),
                        end: data.end !== null
                        		? data.end.format('YYYY-MM-DD 23:59:59')
                        		: data.start.format('YYYY-MM-DD 23:59:59'),
                        category: data.category
                    }
                })
                .done(function (response) {

                    if (original === void 0) {
                        return;
                    }

                    // TODO
                    // Change the second parameter to an array
                    $container.fullCalendar(
                    		'updateEvent', new Array(Object.assign(original, response.data))
                    );
                    $container.fullCalendar('refetchEvents');

                });

            }
        }

    }.init();

});
