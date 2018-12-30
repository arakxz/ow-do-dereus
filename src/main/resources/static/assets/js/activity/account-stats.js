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

            new Chartist.Line('#sales-overview2', {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                series: [{
                    meta: '',
                    data: Ox00.data.line
                }]
            }, {
                lineSmooth: false,
                axisX: {
                    showGrid: false
                },
                plugins: [ Chartist.plugins.tooltip() ]
            });
            
            if (Ox00.init.pie) {
                console.log('aqui')
                new Chartist.Pie('#visitor', {
                    series: Ox00.data.pie
                }, {
                    donut: true,
                    donutWidth: 30,
                    showLabel: false,
                    plugins: [ Chartist.plugins.tooltip() ]
                });
            }

            return this;
        }
    }.init();
});