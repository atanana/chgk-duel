const socket = new WebSocket("ws://localhost:9000/duel");
const log = document.getElementById('log');

socket.onopen = () => {
    console.log('Connection established');
};

socket.onclose = (event) => {
    if (event.wasClean) {
        console.log('Connection closed ok');
    } else {
        console.log('Connection was lost. Code: ' + event.code + ' reason: ' + event.reason);
    }
};

socket.onmessage = (event) => {
    console.log('Data received: ' + event.data);
    log.value += event.data + '\n';
};

socket.onerror = (error) => {
    console.log("Error " + error.message);
};

document.getElementById('duel').onclick = () => {
    socket.send("test");
};