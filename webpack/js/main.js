const socket = require('./socket.js');
const log = document.getElementById('log');

document.getElementById('duel').onclick = () => {
    socket.send({
        teamId1: 1,
        teamId2: 2
    });
};

socket.addMessageListener(function (data) {
    log.value += data + '\n';
});