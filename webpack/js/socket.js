const socket = new WebSocket(`ws://${location.host}/duel`);
const messageListeners = [];

socket.onopen = () => {
    console.log('Connection established');
};

socket.onclose = (event) => {
    console.log(event);
    if (event.wasClean) {
        console.log('Connection closed ok');
    } else {
        alert('Connection was lost. Code: ' + event.code + ' reason: ' + event.reason);
    }
};

socket.onmessage = (event) => {
    console.log('Data received: ' + event.data);
    for (let listener of messageListeners) {
        listener(JSON.parse(event.data))
    }
};

socket.onerror = (error) => {
    alert("Error " + error.message);
};

module.exports = {
    send: function (data) {
        socket.send(JSON.stringify(data));
    },
    addMessageListener: function (listener) {
        messageListeners.push(listener)
    }
};