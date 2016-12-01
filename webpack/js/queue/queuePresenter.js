let _queue = [];
let _ownJobs = [];

module.exports = function (socket, queueView) {
    function updateView() {
        const views = new Map();
        _queue.forEach(item => {
            let itemClass = _ownJobs.indexOf(item) !== -1 ? 'own-job' : '';
            views.set(item, itemClass);
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