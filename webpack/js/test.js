const socket = new WebSocket("ws://localhost:9000/duel");

socket.onopen = () => {
    console.log('Connection established');
    socket.send('Hello, world!');
};

socket.onclose = (event) => {
    if (event.wasClean) {
        console.log('Connection closed ok');
    } else {
        alert('Connection was lost. Code: ' + event.code + ' reason: ' + event.reason);
    }
};

socket.onmessage = (event) => {
    console.log('Data received: ' + event.data);
    document.writeln(event.data)
};

socket.onerror = (error) => {
    alert("Error " + error.message);
};