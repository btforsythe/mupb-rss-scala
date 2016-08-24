package mupb.rss.tumblr

import mupb.rss.test.UnitTest

import scala.xml.Utility

/**
  * Created by bforsythe on 8/4/16.
  */
class PostSpec extends UnitTest {
  "should have title" in {
    val xml = <item>
      <title>post title</title>
    </item>

    val underTest = new Post(xml)

    underTest.title should be("post title")
  }

  "should read img link from description" in {
    val imgSrc = "http://42.media.tumblr.com/s0meW31rdcharacters/tumblr_an0ther1d_500.jpg"
    val description = Utility.escape(s"<a href='foo'><img src='$imgSrc' /></a>")

    val xml = <item>
      <description>{description}</description>
    </item>

    val underTest = new Post(xml)

    underTest.imageLinks
//    underTest.imageLinks contains only(imgSrc)
  }
}
