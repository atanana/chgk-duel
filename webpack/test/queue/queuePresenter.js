const sinon = require('sinon');
const chai = require('chai');
const should = chai.should();

describe('queuePresenter.js', function () {
    describe('socket interaction', function () {
        let socket;
        let queueView;
        let queuePresenter;

        beforeEach(function () {
            socket = {
                addMessageListener: sinon.spy()
            };
            queueView = {
                update: sinon.spy()
            };
            queuePresenter = require('../../js/queue/queuePresenter.js')(socket, queueView);
        });

        it('should add socket listener', function () {
            socket.addMessageListener.calledOnce.should.be.equal(true);
            should.exist(socket.addMessageListener.args[0][0]);
        });

        it('should update view with queue state', function () {
            const listener = socket.addMessageListener.args[0][0];
            listener({
                type: 'DuelsQueueState',
                requests: ['1', '2', '3']
            });

            queueView.update.calledOnce.should.be.equal(true);
            const views = queueView.update.args[0][0];
            views.has('1').should.be.equal(true);
            views.has('2').should.be.equal(true);
            views.has('3').should.be.equal(true);
        });

        it('should set correct classes to views', function () {
            const listener = socket.addMessageListener.args[0][0];
            listener({
                type: 'DuelsQueueState',
                requests: ['1', '2', '3']
            });
            listener({
                type: 'DuelRequestAccepted',
                uuid: '2'
            });

            queueView.update.calledTwice.should.be.equal(true);
            let views = queueView.update.args[0][0];
            views.get('1').should.be.equal('');
            views.get('2').should.be.equal('');
            views.get('3').should.be.equal('');

            views = queueView.update.args[1][0];
            views.get('1').should.be.equal('');
            views.get('2').should.be.equal('own-job');
            views.get('3').should.be.equal('');
        });
    });
});