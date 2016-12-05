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
            function createViewProps(itemClass, label) {
                return {
                    itemClass: itemClass,
                    label: label
                }
            }

            queueView.update(new Map([
                ['1', createViewProps('', '')],
                ['2', createViewProps('test class', 'test label')],
                ['3', createViewProps('', '')]
            ]));
            container.append.callCount.should.be.equal(3);
            function checkItem($item, classStr, label) {
                $item.is('div').should.be.equal(true);
                $item.attr('class').should.be.equal(classStr);
                const $spans = $item.find('span');
                const spansCount = label ? 2 : 1;
                $spans.length.should.be.equal(spansCount);

                if (label) {
                    const $label = $spans.last();
                    $label.attr('class').should.be.equal('job-label');
                    $label.text().should.be.equal(label);
                }
            }

            checkItem(container.append.args[0][0], '', '');
            checkItem(container.append.args[1][0], 'test class', 'test label');
            checkItem(container.append.args[2][0], '', '');
        });
    });
});