module.exports = function (socket, resultsView) {
    socket.addMessageListener(function (data) {
        if (data.type === 'DuelResult') {
            resultsView.addResult(data.team1, data.team2);
        }
    });
};