const jsdom = require('mocha-jsdom')
const sinon = require('sinon');
const chai = require('chai');
chai.should();

describe('queueView.js', function () {
    jsdom();

    let queueView;
    let container;

    beforeEach(function () {
        container = {
            append: sinon.spy(),
            empty: sinon.spy()
        };
        queueView = require('../js/queueView.js')(container);
    });

    describe('#queueChanged(queue)', function () {
        it('should clear container before add views', function () {
            queueView.queueChanged(['1', '2', '3']);
            container.empty.calledOnce.should.be.equal(true);
        });

        it('should add views to container', function () {
            queueView.queueChanged(['1', '2', '3']);
            container.append.callCount.should.be.equal(3);
            function checkItem($item) {
                $item.is('div').should.be.equal(true);
                $item.find('span').length.should.be.equal(1);
            }

            checkItem(container.append.args[0][0]);
            checkItem(container.append.args[1][0]);
            checkItem(container.append.args[2][0]);
        });
    });

    describe('#addOwnJob(id)', function () {
        it('should select own job when not empty', function () {
            queueView.queueChanged(['1', '2', '3']);
            queueView.addOwnJob([2]);
        });
    });
});