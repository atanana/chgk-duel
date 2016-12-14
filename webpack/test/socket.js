const jsdom = require('mocha-jsdom');
const sinon = require('sinon');
const chai = require('chai');
const socketFactory = require('../js/socket.js');

describe('socket.js', function () {
    jsdom();

    let nativeSocket;
    let socket;

    beforeEach(function () {
        window.alert = sinon.spy();
        nativeSocket = {
            send: sinon.spy()
        };
        socket = socketFactory(nativeSocket)
    });

    describe('connection listeners', function () {
        it('calls connection listeners', function () {
            const listener = sinon.spy();
            socket.addConnectionListener(listener);
            listener.calledOnce.should.be.equal(false);
            nativeSocket.onopen();

            listener.calledOnce.should.be.equal(true);
        });

        it('immediately calls connection listeners when already connected', function () {
            const listener = sinon.spy();
            nativeSocket.onopen();
            socket.addConnectionListener(listener);

            listener.calledOnce.should.be.equal(true);
        });
    });

    it('transmits data to message listeners', function () {
        const listener = sinon.spy();
        socket.addMessageListener(listener);
        nativeSocket.onmessage({
            data: '{"test1":"test data", "test2": "test data 2"}'
        });
        listener.calledOnce.should.be.equal(true);
        const data = listener.args[0][0];
        data.test1.should.be.equal('test data');
        data.test2.should.be.equal('test data 2');
    });

    it('sends data to socket', function () {
        socket.send({
            test1: 'test data',
            test2: 'test data 2'
        });
        nativeSocket.send.calledOnce.should.be.equal(true);
        nativeSocket.send.args[0][0].should.be.equal('{"test1":"test data","test2":"test data 2"}');
    });
});