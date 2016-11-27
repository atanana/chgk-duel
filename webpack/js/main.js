const socket = require('./socket.js');
const log = document.getElementById('log');

document.getElementById('duel').onclick = () => {
    socket.send({
        value: "test"
    });
};

socket.addMessageListener(function (data) {
    log.value += data + '\n';
});