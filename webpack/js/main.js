const $ = require('jquery');

require('bootstrap-loader');

const socket = require('./socket.js');
const queueView = require('./queue/queueView.js')($('#queue-container'));
const queuePresenter = require('./queue/queuePresenter.js')(socket, queueView);

require('./requestController.js')(socket, $('#duel'), $('#team-id-1'), $('#team-id-2'));