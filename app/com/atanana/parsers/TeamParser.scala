package com.atanana.parsers

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}

class TeamParser {
  def parse(source: String): List[Long] = {
    val document: Document = JsoupBrowser().parseString(source)
    (document >> elementList(".tournaments_table tbody tr"))
      .filter(_.children.size > 1)
      .flatMap(row => {
        val cells: List[Element] = row.children.toList
        val place: Float = cells(9).text.toString.replace(',', '.').toFloat

        if (place != 9999) {
          List(cells(1).text.toString.toLong)
        } else {
          List.empty
        }
      })
  }
}
