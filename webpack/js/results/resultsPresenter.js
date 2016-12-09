module.exports = function (socket, resultsView) {
    socket.addMessageListener(function (data) {
        if (data.type === 'DuelResult') {
            const team1 = data.team1;
            const team2 = data.team2;

            if (team1.wins !== team2.wins) {
                if (team1.wins > team2.wins) {
                    team1.isWinner = true;
                    team2.isLooser = true;
                } else {
                    team1.isLooser = true;
                    team2.isWinner = true;
                }
            }

            resultsView.addResult(team1, team2);
        }
    });
};