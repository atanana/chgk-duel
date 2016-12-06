package com.atanana

import com.atanana.parsers.TeamInfo

class DuelResultGenerator {
  def generate(teamInfo1: TeamInfo, teamInfo2: TeamInfo, tournamentsData: Map[Long, (Int, Int)]): (TeamDuelResult, TeamDuelResult) = {
    var result1 = TeamDuelResult(teamInfo1.name, teamInfo1.town, 0, 0)
    var result2 = TeamDuelResult(teamInfo2.name, teamInfo2.town, 0, 0)

    tournamentsData.foreach { entry =>
      val team1Result = entry._2._1
      val team2Result = entry._2._2
      result1 = result1.copy(totalQuestions = result1.totalQuestions + team1Result)
      result2 = result2.copy(totalQuestions = result2.totalQuestions + team2Result)

      if (team1Result > team2Result) {
        result1 = result1.copy(wins = result1.wins + 1)
      } else if (team2Result > team1Result) {
        result2 = result2.copy(wins = result2.wins + 1)
      }
    }

    (result1, result2)
  }
}

case class TeamDuelResult(name: String, town: String, wins: Int, totalQuestions: Int)