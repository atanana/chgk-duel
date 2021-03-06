module.exports = function (socket, $duelButton, $teamInput1, $teamInput2) {
    socket.addConnectionListener(() => {
        $duelButton.removeAttr('disabled');
        $duelButton.text('Дуэль!');
    });

    $duelButton.on('click', () => {
        const teamId1 = parseInt($teamInput1.val());
        if (teamId1) {
            const teamId2 = parseInt($teamInput2.val());
            if (teamId2) {
                if (teamId1 !== teamId2) {
                    socket.send({
                        teamId1: teamId1,
                        teamId2: teamId2
                    });
                } else {
                    window.alert('Введите разные id!');
                }
            } else {
                window.alert('Введите id второй команды!');
            }
        } else {
            window.alert('Введите id первой команды!');
        }
    });
};