const $ = require('jquery');

module.exports = function ($container) {
    return {
        update: function (views) {
            $container.empty();
            //noinspection JSUnusedLocalSymbols
            for (let [_, data] of views) {
                let $item = $(
                    `
                    <div class="${data.itemClass}">
                        <span class="glyphicon glyphicon-stats"></span>
                    </div>
                    `
                );

                if (data.label) {
                    $item.append($(`<span class="job-label">${data.label}</span>`));
                }

                $container.append($item)
            }
        }
    }
};