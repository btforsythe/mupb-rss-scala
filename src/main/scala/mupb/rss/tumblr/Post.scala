package mupb.rss.tumblr

import scala.xml.{Node, Utility, XML}

/**
  * Created by bforsythe on 8/4/16.
  */
class Post(postElement: Node) {
  def title = { (postElement \ "title").text }

  def rawDescription: String = (postElement \ "description").text
  def description = {
    println(s"raw description is $rawDescription")
    println(s"unescaped it is ${unescapeHack(rawDescription)}")
//    XML.loadString(Utility.unescape(rawDescription))
  }

  def imageLinks = {
    description
//    (description \\ "img" \ "@src").text
  }

  private def unescapeHack(toUnescape: String) = {
    XML.loadString(s"<p>$toUnescape</p>").text
  }
}
