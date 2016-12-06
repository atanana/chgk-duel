package com.atanana.parsers

import org.scalatest.{Matchers, WordSpecLike}

class SiteJsonParserTest extends WordSpecLike with Matchers {
  "Parser" must {
    val parser = new SiteJsonParser

    "should parse team info" in {
      val json =
        """[
              {
                "idteam": "37740",
                "name": "Привкус",
                "town": "Минск",
                "comment": ""
              }
            ]"""
      parser.parseTeamInfo(json) shouldEqual TeamInfo("Привкус", "Минск")
    }

    "should parse team results on tournament" in {
      val json = """[{"tour":"1","mask":["0","0","0","0","1","1","1","0","0","1","0","0"]},{"tour":"2","mask":["0","0","1","0","1","1","1","0","0","0","1","0"]},{"tour":"3","mask":["1","0","0","0","1","1","1","1","0","1","0","0"]},{"tour":"4","mask":["1","0","0","0","0","1","0","0","0","1","0","1"]}]"""
      parser.parseTeamResult(json) shouldEqual 19
    }
  }
}
