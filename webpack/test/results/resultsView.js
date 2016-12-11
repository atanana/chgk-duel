const jsdom = require('mocha-jsdom');
const sinon = require('sinon');
const chai = require('chai');
const should = chai.should();

describe('resultsView.js', function () {
    jsdom();

    let resultsView;
    let container;
    let $;

    beforeEach(function () {
        // container = {
        //     removeClass: sinon.spy(),
        //     addClass: sinon.spy(),
        //     append: sinon.spy(),
        //     children: sinon.stub().returns([])
        // };

        $ = require('jquery');
        $.fn.tooltip = sinon.spy();
        container = $('<div class="hidden"/>');
        resultsView = require('../../js/results/resultsView.js')(container, $);
    });

    function createTeam(name, town, wins, total, isWinner, isLooser) {
        return {name, town, wins, total, isWinner, isLooser};
    }

    function getResultViews() {
        return container.children().find('.team-result');
    }

    describe('correctly works with multiple results', function () {
        it('adds multiple views for multiple results', function () {
            resultsView.addResult(createTeam('', '', 1, 1, false, false), createTeam('', '', 1, 1, false, false));
            resultsView.addResult(createTeam('', '', 1, 1, false, false), createTeam('', '', 1, 1, false, false));
            resultsView.addResult(createTeam('', '', 1, 1, false, false), createTeam('', '', 1, 1, false, false));

            container.children().length.should.be.equal(3);
        });
    });

    describe('creates correct results views', function () {
        beforeEach(function () {
            resultsView.addResult(createTeam('team 1', 'town 1', 1, 1, false, true),
                createTeam('team 2', 'town 2', 2, 2, true, false));
        });

        it('shows correct data', function () {
            const resultViews = getResultViews();
            checkView(resultViews[0], 'team 1', 'town 1', 1, 1);
            checkView(resultViews[1], 'team 2', 'town 2', 2, 2);

            function checkView(view, name, town, wins, total) {
                const $view = $(view);

                const $header = $view.find('h1');
                $header.contents().get(0).nodeValue.trim().should.be.equal(name);
                $header.find('small').text().should.be.equal(town);

                const $teamWins = $view.find('.team-wins');
                $teamWins.contents().get(0).nodeValue.trim().should.be.equal(wins.toString());
                $teamWins.find('small').text().should.be.equal(total.toString());
            }
        });

        it('add correct class to scores', function () {
            const resultViews = getResultViews();
            checkClass(resultViews[0], 'looser');
            checkClass(resultViews[1], 'winner');

            function checkClass(view, className) {
                $(view).find('.team-wins').hasClass(className).should.be.equals(true);
            }
        });

        it('shows tooltips', function () {
            $.fn.tooltip.calledTwice.should.be.equal(true);
        });

        it('shows container', function () {
            container.hasClass('hidden').should.be.equal(false);
        });
    });

    describe('result views can be deleted', function () {
        function addResult() {
            resultsView.addResult(createTeam('', '', 1, 1, false, false), createTeam('', '', 1, 1, false, false));
        }

        beforeEach(function () {
            addResult();
        });

        function getFirstCloseButton() {
            return $(container.find('.close').get(0));
        }

        function clickClose() {
            getFirstCloseButton().trigger('click');
        }

        it('add close button to result view', function () {
            should.exist(getFirstCloseButton());
        });

        it('press on close button should delete view', function () {
            getResultViews().length.should.be.equal(2);
            clickClose();
            getResultViews().length.should.be.equal(0);
        });

        it('hides container without views', function () {
            addResult();
            addResult();

            checkContainerVisible(true);
            clickClose();
            checkContainerVisible(true);
            clickClose();
            checkContainerVisible(true);
            clickClose();
            checkContainerVisible(false);

            function checkContainerVisible(visible) {
                return container.hasClass('hidden').should.be.equal(!visible);
            }
        })
    });
});