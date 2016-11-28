const $ = require('jquery');

const socket = require('./socket.js');
const queueView = require('./queueView.js')($('#queue-container'));

document.getElementById('duel').onclick = () => {
    socket.send({
        teamId1: 1,
        teamId2: 2
    });
};

socket.addMessageListener(function (data) {
    if (data.type === 'DuelsQueueState') {
        queueView.queueChanged(data.requests)
    }
});