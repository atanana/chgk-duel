/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(1);

/***/ },
/* 1 */
/***/ function(module, exports) {

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

/***/ }
/******/ ]);