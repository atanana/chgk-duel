let _queue = [];
let _ownJobs = [];

module.exports = function (socket, queueView) {
    function updateView() {
        const views = new Map();
        _queue.forEach(item => {
            const isOwnJob = _ownJobs.indexOf(item) !== -1;
            const itemClass = isOwnJob ? 'own-job' : '';
            const label = isOwnJob ? 'Ваш запрос' : '';
            views.set(item, {
                itemClass: itemClass,
                label: label
            });
        });
        queueView.update(views)
    }

    socket.addMessageListener(function (data) {
        if (data.type === 'DuelsQueueState') {
            _queue = data.requests;
            updateView();
        } else if (data.type === 'DuelRequestAccepted') {
            _ownJobs.push(data.uuid);
            updateView();
        }
    });
};