package com.atanana

import com.atanana.parsers.{SiteJsonParser, TeamInfo, TeamParser}
import org.mockito.Mockito.{verify, when}
import org.mockito.{Mock, MockitoAnnotations}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DuelResultGeneratorTest extends FunSuite with Matchers with MockitoSugar with BeforeAndAfter with ScalaFutures {
  val team1Id: Int = 123
  val team2Id: Int = 321

  val team1Page = "team 1 page"
  val team2Page = "team 2 page"

  val team1Info = "team 1 info"
  val team2Info = "team 2 info"

  val team1Name = "team 1 name"
  val team2Name = "team 2 name"

  val team1Town = "team 1 town"
  val team2Town = "team 2 town"

  val tournament1Team1 = "tournament 1 team 1"
  val tournament1Team2 = "tournament 1 team 2"
  val tournament2Team1 = "tournament 2 team 1"
  val tournament2Team2 = "tournament 2 team 2"
  val tournament3Team1 = "tournament 3 team 1"
  val tournament3Team2 = "tournament 3 team 2"

  @Mock
  var siteConnector: SiteConnector = _

  @Mock
  var teamParser: TeamParser = _

  @Mock
  var jsonParser: SiteJsonParser = _

  var duelResultGenerator: DuelResultGenerator = _

  before {
    MockitoAnnotations.initMocks(this)

    when(siteConnector.teamPage(team1Id)).thenReturn(Future(team1Page))
    when(siteConnector.teamPage(team2Id)).thenReturn(Future(team2Page))

    when(siteConnector.teamInfo(team1Id)).thenReturn(Future(team1Info))
    when(siteConnector.teamInfo(team2Id)).thenReturn(Future(team2Info))

    when(siteConnector.tournamentInfo(1, team1Id, team2Id)).thenReturn(Future((tournament1Team1, tournament1Team2)))
    when(siteConnector.tournamentInfo(2, team1Id, team2Id)).thenReturn(Future((tournament2Team1, tournament2Team2)))
    when(siteConnector.tournamentInfo(3, team1Id, team2Id)).thenReturn(Future((tournament3Team1, tournament3Team2)))

    when(teamParser.parse(team1Page)).thenReturn(List.empty)
    when(teamParser.parse(team2Page)).thenReturn(List.empty)

    when(jsonParser.parseTeamInfo(team1Info)).thenReturn(TeamInfo(team1Name, team1Town))
    when(jsonParser.parseTeamInfo(team2Info)).thenReturn(TeamInfo(team2Name, team2Town))

    duelResultGenerator = new DuelResultGenerator(siteConnector, teamParser, jsonParser)
  }

  test("should return valid teams info") {
    whenReady(duelResultGenerator.generate(team1Id, team2Id)) {
      case (result1, result2) =>
        result1.name shouldEqual team1Name
        result1.town shouldEqual team1Town

        result2.name shouldEqual team2Name
        result2.town shouldEqual team2Town
    }
  }

  test("should get tournament intersection for calculations") {
    when(teamParser.parse(team1Page)).thenReturn(List[Long](1, 2, 3, 5))
    when(teamParser.parse(team2Page)).thenReturn(List[Long](4, 3, 2, 6))

    whenReady(duelResultGenerator.generate(team1Id, team2Id)) {
      case (_, _) =>
        verify(siteConnector).tournamentInfo(2, team1Id, team2Id)
        verify(siteConnector).tournamentInfo(3, team1Id, team2Id)
    }
  }

  test("should calculate correct team results") {
    when(teamParser.parse(team1Page)).thenReturn(List[Long](1, 2, 3))
    when(teamParser.parse(team2Page)).thenReturn(List[Long](1, 2, 3))

    when(jsonParser.parseTeamResult(tournament1Team1)).thenReturn(4)
    when(jsonParser.parseTeamResult(tournament1Team2)).thenReturn(6)
    when(jsonParser.parseTeamResult(tournament2Team1)).thenReturn(7)
    when(jsonParser.parseTeamResult(tournament2Team2)).thenReturn(5)
    when(jsonParser.parseTeamResult(tournament3Team1)).thenReturn(10)
    when(jsonParser.parseTeamResult(tournament3Team2)).thenReturn(12)

    whenReady(duelResultGenerator.generate(team1Id, team2Id)) {
      case (result1, result2) =>
        result1.totalQuestions shouldEqual 21
        result1.wins shouldEqual 1

        result2.totalQuestions shouldEqual 23
        result2.wins shouldEqual 2
    }
  }
}
