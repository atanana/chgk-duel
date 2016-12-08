module.exports = function ($container) {
    function createSubContainer() {
        return $('<div/>')
    }

    function createTeamView(team) {
        return $(
            `
<div class="team-result panel panel-default">
    <div class="panel-body">
        <div class="page-header">
            <h1>${team.name}
                <br>
                <small>${team.town}</small>
            </h1>
        </div>
    </div>
</div>
            `
        );
    }

    return {
        addResult: function (team1, team2) {
            $container.removeClass('hidden');
            $container.append(createSubContainer()
                .append(createTeamView(team1))
                .append(createTeamView(team2)));
        }
    }
};