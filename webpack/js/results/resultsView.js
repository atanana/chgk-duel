module.exports = function ($container, $) {
    function createSubContainer() {
        let $resultContainer = $(`
<div>
    <button type="button" class="close"><span aria-hidden="true">&times;</span></button>
    <div class="clearfix"></div>
</div>
`);
        $resultContainer.find('.close').on('click', () => {
            $resultContainer.remove();

            if (!$container.children().length) {
                $container.addClass('hidden');
            }
        });
        return $resultContainer;
    }

    function createTeamView(team) {
        let winsClass = '';

        if (team.isWinner) {
            winsClass = 'winner';
        } else if (team.isLooser) {
            winsClass = 'looser';
        }

        let $result = $(
            `
<div class="team-result panel panel-default">
    <div class="panel-body">
        <div class="page-header">
            <h1>${team.name}
                <br>
                <small>${team.town}</small>
            </h1>
        </div>

        <span class="team-wins ${winsClass}">
            ${team.wins}
            <small data-toggle="tooltip" data-placement="top" title="Количество взятых вопросов">${team.total}</small>
        </span>
    </div>
</div>
            `
        );

        $result.find('small').tooltip();

        return $result;
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