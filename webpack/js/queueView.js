const $ = require('jquery');

module.exports = function ($container) {
    return {
        queueChanged: function (queue) {
            $container.empty();
            queue.forEach(item => $container.append($('<div/>')));
        }
    }
};