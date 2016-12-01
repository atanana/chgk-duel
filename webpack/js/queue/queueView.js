const $ = require('jquery');

module.exports = function ($container) {
    return {
        update: function (views) {
            $container.empty();
            //noinspection JSUnusedLocalSymbols
            for (let [_, itemClass] of views) {
                $container.append($(
                    `
                    <div class="${itemClass}">
                        <span class="glyphicon glyphicon-stats"></span>
                    </div>
                    `
                ))
            }
        }
    }
};