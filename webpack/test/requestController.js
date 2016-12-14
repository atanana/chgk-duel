const jsdom = require('mocha-jsdom');
const sinon = require('sinon');
const chai = require('chai');
const should = chai.should();

describe('requestController.js', function () {
    jsdom();

    let socket;
    let $button;
    let $teamInput1;
    let $teamInput2;
    let requestController;

    beforeEach(function () {
        socket = {
            send: sinon.spy(),
            addConnectionListener: sinon.spy()
        };
        $button = {
            on: sinon.spy(),
            removeAttr: sinon.spy(),
            text: sinon.spy()
        };
        $teamInput1 = {
            val: sinon.stub()
        };
        $teamInput2 = {
            val: sinon.stub()
        };

        requestController = require('../js/requestController.js')(socket, $button, $teamInput1, $teamInput2);
        window.alert = sinon.spy();
    });

    function click() {
        $button.on.args[0][1]();
    }

    function checkAlert(message) {
        window.alert.args[0][0].should.be.equal(message);
    }

    describe('event listeners', function () {
        it('should add click listener to button', function () {
            $button.on.calledOnce.should.be.equal(true);
            $button.on.args[0][0].should.be.eq('click');
            should.exist($button.on.args[0][1]);
        });

        it('add appropriate socket connection listener', function () {
            const connectionListener = socket.addConnectionListener.args[0][0];
            connectionListener();

            $button.removeAttr.calledOnce.should.be.equal(true);
            $button.removeAttr.args[0][0].should.be.equal('disabled');

            $button.text.calledOnce.should.be.equal(true);
            $button.text.args[0][0].should.be.equal('Дуэль!');
        });
    });

    describe('ids validation', function () {
        it('shouldn\'t send request when first team id is missing', function () {
            $teamInput1.val = sinon.stub().returns('');
            $teamInput2.val = sinon.stub().returns('123');

            click();

            socket.send.called.should.be.equal(false);
            checkAlert('Введите id первой команды!');
        });

        it('shouldn\'t send request when first team id is not number', function () {
            $teamInput1.val = sinon.stub().returns('test');
            $teamInput2.val = sinon.stub().returns('123');

            click();

            socket.send.called.should.be.equal(false);
            checkAlert('Введите id первой команды!');
        });

        it('shouldn\'t send request when second team id is missing', function () {
            $teamInput1.val = sinon.stub().returns('123');
            $teamInput2.val = sinon.stub().returns('');

            click();

            socket.send.called.should.be.equal(false);
            checkAlert('Введите id второй команды!');
        });

        it('shouldn\'t send request when second team id is not number', function () {
            $teamInput1.val = sinon.stub().returns('123');
            $teamInput2.val = sinon.stub().returns('test');

            click();

            socket.send.called.should.be.equal(false);
            checkAlert('Введите id второй команды!');
        });

        it('shouldn\'t send request when ids are equal', function () {
            $teamInput1.val = sinon.stub().returns('123');
            $teamInput2.val = sinon.stub().returns('123');

            click();

            socket.send.called.should.be.equal(false);
            checkAlert('Введите разные id!');
        });
    });

    describe('send data to socket', function () {
        it('should send team ids to socket', function () {
            $teamInput1.val = sinon.stub().returns('123');
            $teamInput2.val = sinon.stub().returns('321');

            click();

            const data = socket.send.args[0][0];
            data.teamId1.should.be.equal(123);
            data.teamId2.should.be.equal(321);
        })
    });
});