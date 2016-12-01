const $ = require('jquery');

let _queue = [];
let _ownJobs = [];

module.exports = function ($container) {
    function refreshView() {
        _queue.forEach(item => $container.append($(
            `
            <div class="${_ownJobs.indexOf(item) !== -1 ? 'own-job' : ''}">
                <span class="glyphicon glyphicon-stats"></span>
            </div>
            `
        )));
    }

    return {
        queueChanged: function (queue) {
            _queue = queue;
            $container.empty();
            refreshView();
        },
        addOwnJob: function (id) {
            _ownJobs.push(id);
            refreshView();
        }
    }
};