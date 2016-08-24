package mupb.rss.tumblr

import scala.io.Source
import scala.xml.{Elem, Node, NodeSeq, XML}
import java.io.InputStream

import Reader.siteName

object Reader {
  val siteName = """https?://(\w+).tumblr.com/?""".r
}

class Reader(in: InputStream, postConstructor: Function[Node, Post] = new Post(_)) {

  val xml = XML.load(in)
  
  private def channel = xml\"channel"
  def title = channel.title

  def name: String = channel.link match {
    case siteName(n) => n
    //TODO handle badly formatted link?
  }

  def posts = channel.items

  implicit class FeedElement(elem: NodeSeq) {

    def title = (elem \ "title").text

    def link = (elem \ "link").text

    def items = (elem \ "item").map(node => postConstructor.apply(node))
  }
}

