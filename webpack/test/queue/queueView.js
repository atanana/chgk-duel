const jsdom = require('mocha-jsdom');
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
        queueView = require('../../js/queue/queueView.js')(container);
    });

    describe('#update(views)', function () {
        it('should clear container before add views', function () {
            queueView.update(new Map());
            container.empty.calledOnce.should.be.equal(true);
        });

        it('should add correct views to container', function () {
            queueView.update(new Map([
                ['1', ''],
                ['2', 'class'],
                ['3', '']
            ]));
            container.append.callCount.should.be.equal(3);
            function checkItem($item, classStr) {
                $item.is('div').should.be.equal(true);
                $item.find('span').length.should.be.equal(1);
                $item.attr('class').should.be.equal(classStr);
            }

            checkItem(container.append.args[0][0], '');
            checkItem(container.append.args[1][0], 'class');
            checkItem(container.append.args[2][0], '');
        });
    });
});