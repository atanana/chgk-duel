const jsdom = require('mocha-jsdom');
const sinon = require('sinon');
const chai = require('chai');
const should = chai.should();

describe('resultsPresenter.js', function () {
    jsdom();

    describe('socket interaction', function () {
        let socket;
        let resultsView;
        let resultsPresenter;

        beforeEach(function () {
            socket = {
                addMessageListener: sinon.spy()
            };
            resultsView = {
                addResult: sinon.spy()
            };
            resultsPresenter = require('../../js/results/resultsPresenter.js')(socket, resultsView);
            window.alert = sinon.spy();
        });

        function createTeamData(name, town, wins, total) {
            return {name, town, wins, total};
        }

        it('should add socket listener', function () {
            socket.addMessageListener.calledOnce.should.be.equal(true);
            should.exist(socket.addMessageListener.args[0][0]);
        });

        it('should correct transmit team data to view', function () {
            socket.addMessageListener.args[0][0]({
                type: 'DuelResult',
                team1: createTeamData('name 1', 'town 1', 12, 123),
                team2: createTeamData('name 2', 'town 2', 21, 321)
            });

            const team1 = resultsView.addResult.args[0][0];
            const team2 = resultsView.addResult.args[0][1];

            function checkTeam(team, name, town, wins, total) {
                team.name.should.be.equal(name);
                team.town.should.be.equal(town);
                team.wins.should.be.equal(wins);
                team.total.should.be.equal(total);
            }

            checkTeam(team1, 'name 1', 'town 1', 12, 123);
            checkTeam(team2, 'name 2', 'town 2', 21, 321);
        });

        it('should correct calculate winner and looser #1', function () {
            socket.addMessageListener.args[0][0]({
                type: 'DuelResult',
                team1: createTeamData('', '', 12, 123),
                team2: createTeamData('', '', 21, 321)
            });

            const team1 = resultsView.addResult.args[0][0];
            const team2 = resultsView.addResult.args[0][1];

            should.not.exist(team1.isWinner);
            team1.isLooser.should.be.equal(true);
            team2.isWinner.should.be.equal(true);
            should.not.exist(team2.isLooser);
        });

        it('should correct calculate winner and looser #2', function () {
            socket.addMessageListener.args[0][0]({
                type: 'DuelResult',
                team1: createTeamData('', '', 112, 123),
                team2: createTeamData('', '', 21, 321)
            });

            const team1 = resultsView.addResult.args[0][0];
            const team2 = resultsView.addResult.args[0][1];

            team1.isWinner.should.be.equal(true);
            should.not.exist(team1.isLooser);
            should.not.exist(team2.isWinner);
            team2.isLooser.should.be.equal(true);
        });

        it('should correct calculate winner and looser #3', function () {
            socket.addMessageListener.args[0][0]({
                type: 'DuelResult',
                team1: createTeamData('', '', 12, 123),
                team2: createTeamData('', '', 12, 321)
            });

            const team1 = resultsView.addResult.args[0][0];
            const team2 = resultsView.addResult.args[0][1];

            should.not.exist(team1.isWinner);
            should.not.exist(team1.isLooser);
            should.not.exist(team2.isWinner);
            should.not.exist(team2.isLooser);
        });

        it('should transmit duel error', function () {
            socket.addMessageListener.args[0][0]({
                type: 'DuelFailure',
                team1: 1,
                team2: 2
            });

            window.alert.args[0][0].should.be.equal('Дуэль между командами 1 и 2 завершилась неудачей!');
        });
    });
});