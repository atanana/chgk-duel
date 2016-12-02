package com.atanana.parsers

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class TeamParserTest extends FunSuite with Matchers {
  test("should parse") {
    val html: String = Source.fromFile("test/com/atanana/parsers/test.html", "cp1251").mkString
    val result = new TeamParser().parse(html)
    result should have length 67
    result should contain noElementsOf List(4023)
  }
}
