const messageListeners = [];
const connectionListeners = [];
let connected = false;

module.exports = function (socket) {
    socket.onopen = () => {
        console.log('Connection established');
        connected = true;
        for (let listener of connectionListeners) {
            listener()
        }
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

    return {
        send(data) {
            socket.send(JSON.stringify(data));
        },
        addMessageListener(listener) {
            messageListeners.push(listener)
        },
        addConnectionListener(listener) {
            if (connected) {
                listener();
            } else {
                connectionListeners.push(listener);
            }
        }
    };
};