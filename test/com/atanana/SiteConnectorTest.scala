package com.atanana

import org.scalatest.{Matchers, WordSpecLike}
import mockws.MockWS
import org.scalatest.concurrent.ScalaFutures
import play.api.mvc.Action
import play.api.mvc.Results._
import play.api.test.Helpers._

class SiteConnectorTest extends WordSpecLike with ScalaFutures with Matchers {
  "Connector" must {
    "get team page" in {
      val ws = MockWS {
        case (GET, "http://rating.chgk.info/team/123") => Action {
          Ok("test response")
        }
      }

      whenReady(new SiteConnector(ws).teamPage(123)) {
        result => result shouldEqual "test response"
      }
    }

    "get tournament teams info" in {
      val ws = MockWS {
        case (GET, "http://rating.chgk.info/api/tournaments/321/results/111.json") => Action {
          Ok("test response 1")
        }
        case (GET, "http://rating.chgk.info/api/tournaments/321/results/222.json") => Action {
          Ok("test response 2")
        }
      }

      whenReady(new SiteConnector(ws).tournamentInfo(321, 111, 222)) {
        result => result shouldEqual("test response 1", "test response 2")
      }
    }

    "get team info" in {
      val ws = MockWS {
        case (GET, "http://rating.chgk.info/api/teams/123.json") => Action {
          Ok("test response")
        }
      }

      whenReady(new SiteConnector(ws).teamInfo(123)) {
        result => result shouldEqual "test response"
      }
    }
  }
}
