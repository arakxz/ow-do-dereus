$(function () { 'use strict';
    /**
     * @param {Object} wrapper
     */
    var wrapper = {

        /**
         * @param {Object} new user
         * @param {Object} new user logic
         */
        $u : null, $ulogic : {
            submit : function (event) { event.preventDefault();
                // TODO
                // Validate the submit
            },
            change : function () {
                var users = null;
                var value = $(this).val().trim();

                if (value.length) {

                    users = Ox00.users.filter(function (user) {
                        if (user.username === value) {
                            return user;
                        }
                    });

                    if (users.length) {
                        $.toast({
                            heading : 'Alert',
                            text : 'User is registered',
                            loaderBg : '#ff6849',
                            icon : 'error'
                        });
                    }
                }

            }
        },

        /**
         * @param {Object} wizard
         * @param {Object} wizard logic
         */
        $w : null, $wlogic : {

            change : function (event) {

                var that = wrapper.$w;
                    that.step2.clear();

                var option = $('option:selected', this);
                var container = option.data('roles');

                if (typeof container === 'undefined' || container === null) {
                    return;
                }

                for (let i = 0, j = container.length, item = null; i < j; i++) {
                    item = container[i];
                    that.step2.check(item.id);
                }

            },

            keydown : function (event) {

                if (event.which !== 13) {
                    return;
                }

                var that = wrapper.$w;
                var node = that.step2.find('select[name="username"]');
                    node.empty();

                var search = $(this).val();
                var collection = Ox00.users.filter(function (user) {
                    if (user.username.indexOf(search) !== -1) {
                        return user;
                    }
                });

                if (collection.length) {
                    node.append($('<option>', { value : null, text : 'Choose' }));
                    
                    for (let i = 0,
                             j = collection.length, item = null; i < j; i++) {
                        
                        item = collection[i];
                        node.append($('<option>', { value : item.username, text : item.username }).data('roles', item.roles));

                    }

                    that.step2.clear();
                    that.container.carousel('next');
                }

            }
        },

        /**
         * @return this
         */
        init : function () {

            this.$u = new Object();

            this.$u.container = $('#register--user');
            this.$u.container.find('input[name="username"]').on('change', this.$ulogic.change);

            this.$w = new Object();

            this.$w.container = $('#wizard--user-role');
            this.$w.step1 = $('#wizard--user-role-step-1');
            this.$w.step2 = $.extend($('#wizard--user-role-step-2'), {
                clear : function () {
                    this.find('input[type="checkbox"]').prop('checked', false);
                },
                check : function (id) {
                    this.find(`#role--checkbox-${id}`).prop('checked', true);
                }
            });

            this.$w.step1.find('input[name="username"]').on('keydown', this.$wlogic.keydown);
            this.$w.step2.find('select[name="username"]').on('change', this.$wlogic.change);

            this.loaded();

            return this;
        },

        loaded : function () {
            switch (Ox00.response) {
                case Cx00:
                    $.toast({
                        heading : 'Warning',
                        text : 'New usert registered',
                        loaderBg : '#ff6849',
                        icon : 'success'
                    });
                break;
                case Cx06:
                    $.toast({
                        heading : 'Warning',
                        text : 'Updated user',
                        loaderBg : '#ff6849',
                        icon : 'success'
                    });
                break;
                case Cx01:
                    $.toast({
                        heading : 'Warning',
                        text : 'All field is required',
                        loaderBg : '#ff6849',
                        icon : 'warning'
                    });
                break;
                case Cx02:
                case Cx03:
                case Cx04:
                    $.toast({
                        heading : 'Alert',
                        text : 'User or Password incorrect',
                        loaderBg : '#ff6849',
                        icon : 'error'
                    });
                break;
                case Cx05:
                    $.toast({
                        heading : 'Alert',
                        text : 'User is registered',
                        loaderBg : '#ff6849',
                        icon : 'error'
                    });
                break;
            }
        }

    }.init();

});