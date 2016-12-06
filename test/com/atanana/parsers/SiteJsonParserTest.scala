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
  }
}
