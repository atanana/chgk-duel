module.exports = function (socket, $duelButton, $teamInput1, $teamInput2) {
    $duelButton.on('click', () => {
        const teamId1 = parseInt($teamInput1.val());
        if (teamId1) {
            const teamId2 = parseInt($teamInput2.val());
            if (teamId2) {
                socket.send({
                    teamId1: teamId1,
                    teamId2: teamId2
                });
            } else {
                window.alert('Введите id второй команды!');
            }
        } else {
            window.alert('Введите id первой команды!');
        }
    });
};