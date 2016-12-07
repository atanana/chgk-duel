package com.atanana

import javax.inject.Inject

import com.atanana.parsers.{SiteJsonParser, TeamParser}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DuelResultGenerator @Inject()(siteConnector: SiteConnector, teamParser: TeamParser, jsonParser: SiteJsonParser) {
  def generate(team1Id: Long, team2Id: Long): Future[(TeamDuelResult, TeamDuelResult)] = {
    siteConnector.teamPage(team1Id)
      .zip(siteConnector.teamPage(team2Id))
      .flatMap(teamPages => teamsDuelTournaments(team1Id, team2Id, teamPages))
      .map(teamResults)
      .zip(teamsInfo(team1Id, team2Id))
      .map {
        case (((team1Wins, team2Wins, team1Total, team2Total)), (team1Info, team2Info)) =>
          (
            TeamDuelResult(team1Info.name, team1Info.town, team1Wins, team1Total),
            TeamDuelResult(team2Info.name, team2Info.town, team2Wins, team2Total)
          )
      }
  }

  private def teamsDuelTournaments(team1Id: Long, team2Id: Long, teamPages: (String, String)) = {
    val tournamentIds = teamParser.parse(teamPages._1).toSet.intersect(teamParser.parse(teamPages._2).toSet).toList
    val pageFutures = tournamentIds.map(tournamentId => siteConnector.tournamentInfo(tournamentId, team1Id, team2Id))
    Future.sequence(pageFutures)
  }

  private def teamResults(tournamentsData: List[(String, String)]) = {
    var team1Total = 0
    var team2Total = 0
    var team1Wins = 0
    var team2Wins = 0

    tournamentsData
      .map(data => (jsonParser.parseTeamResult(data._1), jsonParser.parseTeamResult(data._2)))
      .foreach {
        case (team1Result, team2Result) =>
          team1Total += team1Result
          team2Total += team2Result

          if (team1Result > team2Result) {
            team1Wins += 1
          } else if (team2Result > team1Result) {
            team2Wins += 1
          }
      }

    (team1Wins, team2Wins, team1Total, team2Total)
  }

  private def teamsInfo(team1Id: Long, team2Id: Long) = {
    siteConnector.teamInfo(team1Id).zip(siteConnector.teamInfo(team2Id))
      .map {
        case (team1Page, team2Page) => (
          jsonParser.parseTeamInfo(team1Page),
          jsonParser.parseTeamInfo(team2Page)
        )
      }
  }
}

case class TeamDuelResult(name: String, town: String, wins: Int, totalQuestions: Int)