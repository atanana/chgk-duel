const $ = require('jquery');

require('bootstrap-loader');

const socket = require('./socket.js');
const queueView = require('./queue/queueView.js')($('#queue-container'));
const queuePresenter = require('./queue/queuePresenter.js')(socket, queueView);

document.getElementById('duel').onclick = () => {
    socket.send({
        teamId1: 1,
        teamId2: 2
    });
};