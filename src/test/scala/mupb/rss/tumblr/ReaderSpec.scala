package mupb.rss.tumblr

import java.io.ByteArrayInputStream

import mupb.rss.test.UnitTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.mockito.MockitoSugar

import scala.xml.Elem
import scala.xml.Node

class ReaderSpec extends UnitTest /*with MockitoSugar*/ {

  var underTest: Reader = null

  def initUnderTest(xml: Elem, postConstructor: Function[Node, Post] = new Post(_)) = {
    val source = new ByteArrayInputStream(xml.toString().getBytes)
    underTest = new Reader(source, postConstructor)
  }

  "should read" - {
    "site title" in {
      initUnderTest(<rss><channel><title>Tumblr Title</title></channel></rss>)

      underTest.title should be("Tumblr Title")
    }

    "site name" in {
      initUnderTest(<rss><channel><link>http://sitename.tumblr.com/</link></channel></rss>)

      underTest.name should be("sitename")
    }

    "posts" - {
      "if no posts are found" in {
        initUnderTest(<rss><channel></channel></rss>)

        underTest.posts shouldBe empty
      }

      "if a single post is found" in {
        initUnderTest(<rss><channel><item /></channel></rss>)

        assert(underTest.posts.head.isInstanceOf[Post])
      }

      "if multiple posts are found" in {
        val xml = <rss>
          <channel>
            <item><title>foo</title></item>
            <item><title>bar</title></item>
          </channel>
        </rss>
        val items = xml \ "channel" \ "item"
        val firstItem = items(0)
        val secondItem = items(1)
        val (firstPost, secondPost, postConstructor) = mockPostsCreation(firstItem, secondItem)
        initUnderTest(xml, postConstructor)

        underTest.posts should contain inOrderOnly(firstPost, secondPost)
      }
    }
  }

  def mockPostsCreation(firstItem: Node, secondItem: Node) = {
    val firstPost = new Post(null)
    val secondPost = new Post(null)
    val postConstructor = ((node: Node) => node match {
      case n: Node if node == firstItem => firstPost
      case n: Node if node == secondItem => secondPost
      case _ => fail("I don't match")
    })
    (firstPost, secondPost, postConstructor)
  }
}