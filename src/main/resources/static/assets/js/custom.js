(function ($, undefined) { 'use strict';

    var template = ''
        + '<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="modal" aria-hidden="true" id="l6058925967">'
        + '  <div class="modal-dialog">'
        + '    <div class="modal-content">'
        + '      <div class="modal-header">'
        + '        <h4 class="modal-title" id="l9752552239">&nbsp;</h4>'
        + '        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'
        + '      </div>'
        + '      <div class="modal-body" id="l4341937273">&nbsp;</div>'
        + '      <div class="modal-footer">'
        + '        <button type="button" class="btn btn-secondary waves-effect" data-dismiss="modal">Close</button>'
        + '        <button type="button" class="btn btn-success waves-effect waves-light" id="l0970989501">&nbsp;</button>'
        + '      </div>'
        + '    </div>'
        + '  </div>'
        + '</div>';

    
    var initialize = null;
    /**
     * @return {Object}
     */
    function init() {
        if (initialize === null) {
            initialize = {
                container: $.extend(
                        $('#l6058925967'), {
                            done: function () {
                                this.modal('hide');
                                       
                                return this;
                            },
                            wait: function () {
                                this.find('#l0970989501')
                                        .html('<i class="mdi mdi-timer text-white"></i> Wait...')
                                        .prop('disabled', true);
                                       
                                return this;
                            }
                        }
                ),
                header   : $('#l9752552239'),
                body     : $('#l4341937273'),
                button   : $('#l0970989501')
            };
        }
        return initialize; 
    };
    
    /**
     * @param {Object} settings
     * 
     * @return
     */
    $.imodal = function(settings) {

        var $el = init();

        $el.header.html(settings.title);
        $el.body.html(
                typeof settings.body !== 'function' ? settings.body
                                                    : settings.body()
        );
        $el.button
                .html(settings.button)
                .prop('disabled', false)
                .off('click').on('click', settings.then.bind($el.container));

        return $el.container.modal();
        
    };
    
    $(function () {
        $('body').append(template);
    });

} (jQuery));

/*
 * Template Name: Admin Pro Admin
 * Author: Wrappixel
 * Email: niravjoshi87@gmail.com
 * File: js
 */
$(function() {
    "use strict";
    $(function() {
        $(".preloader").fadeOut();
    });
    jQuery(document).on('click', '.mega-dropdown', function(e) {
        e.stopPropagation()
    });
    // ============================================================== 
    // This is for the top header part and sidebar part
    // ==============================================================  
    var set = function() {
        var width = (window.innerWidth > 0) ? window.innerWidth : this.screen.width;
        var topOffset = 0;
        if (width < 1170) {
            $("body").addClass("mini-sidebar");
            $('.navbar-brand span').hide();
            $(".sidebartoggler i").addClass("ti-menu");
        } else {
            $("body").removeClass("mini-sidebar");
            $('.navbar-brand span').show();
        }

        var height = ((window.innerHeight > 0) ? window.innerHeight : this.screen.height) - 1;
        height = height - topOffset;
        if (height < 1) height = 1;
        if (height > topOffset) {
            $(".page-wrapper").css("min-height", (height) + "px");
        }

    };
    $(window).ready(set);
    $(window).on("resize", set);

    // ============================================================== 
    // Theme options
    // ==============================================================     
    $(".sidebartoggler").on('click', function() {
        if ($("body").hasClass("mini-sidebar")) {
            $("body").trigger("resize");
            $("body").removeClass("mini-sidebar");
            $('.navbar-brand span').show();
            
        } else {
            $("body").trigger("resize");
            $("body").addClass("mini-sidebar");
            $('.navbar-brand span').hide();
            
        }
    });

    // this is for close icon when navigation open in mobile view
    $(".nav-toggler").click(function() {
        $("body").toggleClass("show-sidebar");
        $(".nav-toggler i").toggleClass("ti-menu");
        $(".nav-toggler i").addClass("ti-close");
    });

    $(".search-box a, .search-box .app-search .srh-btn").on('click', function() {
        $(".app-search").toggle(200);
    });
    // ============================================================== 
    // Right sidebar options
    // ============================================================== 
    $(".right-side-toggle").click(function() {
        $(".right-sidebar").slideDown(50);
        $(".right-sidebar").toggleClass("shw-rside");
    });
    // ============================================================== 
    // This is for the floating labels
    // ============================================================== 
    $('.floating-labels .form-control').on('focus blur', function(e) {
        $(this).parents('.form-group').toggleClass('focused', (e.type === 'focus' || this.value.length > 0));
    }).trigger('blur');

    // ============================================================== 
    // Auto select left navbar
    // ============================================================== 
    $(function() {
        var url = window.location;
        var element = $('ul#sidebarnav a').filter(function() {
            return this.href == url;
        }).addClass('active').parent().addClass('active');
        while (true) {
            if (element.is('li')) {
                element = element.parent().addClass('in').parent().addClass('active');
            } else {
                break;
            }
        }

    });
    // ============================================================== 
    //tooltip
    // ============================================================== 
    $(function() {
        $('[data-toggle="tooltip"]').tooltip()
    })
    // ============================================================== 
    //Popover
    // ============================================================== 
    $(function() {
        $('[data-toggle="popover"]').popover()
    })
    // ============================================================== 
    // Sidebarmenu
    // ============================================================== 
    $(function() {
        $('#sidebarnav').AdminMenu();
    });

    // ============================================================== 
    // Perfact scrollbar
    // ============================================================== 
    $('.scroll-sidebar, .right-side-panel, .message-center, .right-sidebar').perfectScrollbar();
    
    // ============================================================== 
    // Resize all elements
    // ============================================================== 
    $("body").trigger("resize");
    // ============================================================== 
    // To do list
    // ============================================================== 
    $(".list-task li label").click(function() {
        $(this).toggleClass("task-done");
    });

    

    // ============================================================== 
    // Collapsable cards
    // ==============================================================
    $('a[data-action="collapse"]').on('click', function(e) {
        e.preventDefault();
        $(this).closest('.card').find('[data-action="collapse"] i').toggleClass('ti-minus ti-plus');
        $(this).closest('.card').children('.card-body').collapse('toggle');

    });
    // Toggle fullscreen
    $('a[data-action="expand"]').on('click', function(e) {
        e.preventDefault();
        $(this).closest('.card').find('[data-action="expand"] i').toggleClass('mdi-arrow-expand mdi-arrow-compress');
        $(this).closest('.card').toggleClass('card-fullscreen');
    });

    // Close Card
    $('a[data-action="close"]').on('click', function() {
        $(this).closest('.card').removeClass().slideUp('fast');
    });
    
    // ============================================================== 
    // Login and Recover Password 
    // ============================================================== 
    $('#to-recover').on("click", function() {
        $("#loginform").slideUp();
        $("#recoverform").fadeIn();
    });
    
});