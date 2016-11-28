const $ = require('../../bower_components/jquery/dist/jquery.js');

const socket = require('./socket.js');
const $log = $('#log');

document.getElementById('duel').onclick = () => {
    socket.send({
        teamId1: 1,
        teamId2: 2
    });
};

socket.addMessageListener(function (data) {
    $log.val($log.val() + data + '\n');
});