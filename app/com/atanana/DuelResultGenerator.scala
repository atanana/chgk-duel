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
      .map(data => {
        val teamsResult = data._1
        val teamsInfo = data._2
        (
          TeamDuelResult(teamsInfo._1.name, teamsInfo._1.town, teamsResult._1, teamsResult._3),
          TeamDuelResult(teamsInfo._2.name, teamsInfo._2.town, teamsResult._2, teamsResult._4)
        )
      })
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
      .foreach(data => {
        team1Total += data._1
        team2Total += data._2

        if (data._1 > data._2) {
          team1Wins += 1
        } else if (data._2 > data._1) {
          team2Wins += 1
        }
      })

    (team1Wins, team2Wins, team1Total, team2Total)
  }

  private def teamsInfo(team1Id: Long, team2Id: Long) = {
    siteConnector.teamInfo(team1Id).zip(siteConnector.teamInfo(team2Id))
      .map(data => (
        jsonParser.parseTeamInfo(data._1),
        jsonParser.parseTeamInfo(data._2)
      ))
  }
}

case class TeamDuelResult(name: String, town: String, wins: Int, totalQuestions: Int)