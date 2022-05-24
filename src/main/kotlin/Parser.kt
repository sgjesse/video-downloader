import io.ktor.util.collections.*

class Parser (val scanner : Scanner) {
  var current : Token = EofToken(-1)

  private fun expectTag(name : String) {
    if (!(current is TagToken && current.content.equals(name))) {
      throw ParserException()
    }
    current = scanner.next()
  }

  private fun expectTag() : TagToken {
    val token = current
    if (token !is TagToken) {
      throw ParserException()
    }
    current = scanner.next()
    return token
  }

  private fun expectUrl() : UrlToken {
    val token = current
    if (token !is UrlToken) {
      throw ParserException()
    }
    current = scanner.next()
    return token
  }

  private fun expectEol() {
    if (current !is EolToken) {
      throw ParserException()
    }
    current = scanner.next()
  }

  private fun expectEolOrEof() {
    if (current is EolToken) {
      current = scanner.next()
    } else if (current !is EofToken) {
      throw ParserException()
    }
  }

  private fun expectColon() {
    if (current !is ColonToken) {
      throw ParserException()
    }
    current = scanner.next()
  }

  private fun expectComma() {
    if (current !is CommaToken) {
      throw ParserException()
    }
    current = scanner.next()
  }

  private fun expectEquals() {
    if (current !is EqualsToken) {
      throw ParserException()
    }
    current = scanner.next()
  }

  private fun expectInt() : Int {
    val token = current
    if (token !is IntToken) {
      throw ParserException()
    }
    current = scanner.next()
    return token.value;
  }

  private fun expectHeader() {
    expectTag("EXTM3U")
    expectEolOrEof()
  }

  private fun expectKV() {
    while (current !is EolToken && current !is EofToken) {
      if (current !is IdentifierToken) {
        throw ParserException()
      }
      current = scanner.next()
      expectEquals()
      current = scanner.next()
      if (current is CommaToken) {
        expectComma()
      }
    }
    if (current is EolToken) {
      expectEol()
    }
  }

  fun parseMasterPlaylist() : MasterPlaylist {
    val builder = MasterPlaylist.Builder()
    current = scanner.next()
    expectHeader()
    while (true) {
      val tag = expectTag()
      when (tag.identifier) {
        "EXT-X-INDEPENDENT-SEGMENTS" -> {
          expectEolOrEof()
        }
        "EXT-X-VERSION" -> {
          expectColon()
          builder.version(expectInt())
          expectEolOrEof()
        }
        "EXT-X-MEDIA" -> {
          expectColon()
          expectKV()
        }
        "EXT-X-STREAM-INF" -> {
          expectColon()
          expectKV()
        }
        else -> throw ParserException()
      }
      //expectEolOrEof()
    }
    return builder.build()
  }

  /*
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment1_4_av.ts?null=0

   */
  fun parseMediaPlaylist() : MediaPlaylist {
    val builder = MediaPlaylist.Builder()
    current = scanner.next()
    expectHeader()
    while (current !is EofToken) {
      val tag = expectTag()
      when (tag.identifier) {
        "EXT-X-TARGETDURATION" -> {
          expectColon()
          //builder.version(expectInt())
          expectInt()
          expectEolOrEof()
        }
        "EXT-X-ALLOW-CACHE" -> {
          expectColon()
          //builder.version(expectInt())
          //expectIdentifier()
          current = scanner.next() // Skip.
          expectEolOrEof()
        }
        "EXT-X-PLAYLIST-TYPE" -> {
          expectColon()
          //builder.version(expectInt())
          //expectIdentifier()
          current = scanner.next() // Skip.
          expectEolOrEof()
        }
        "EXT-X-MEDIA-SEQUENCE" -> {
          expectColon()
          //builder.version(expectInt())
          expectInt()
          expectEolOrEof()
        }
        "EXT-X-VERSION" -> {
          expectColon()
          builder.version(expectInt())
          expectEolOrEof()
        }
        "EXT-X-MEDIA" -> {
          expectColon()
          expectKV()
        }
        "EXT-X-STREAM-INF" -> {
          expectColon()
          expectKV()
        }
        "EXTINF" -> {
          expectColon()
          if (current is IntToken || current is DoubleToken) {
            current = scanner.next();
          }
          expectComma()
          // TODO check title
          //  current = scanner.next();
          expectEolOrEof()
          builder.addSegemnt(expectUrl().url)
          expectEolOrEof()
        }
        "EXT-X-ENDLIST" -> {
          expectEolOrEof()
        }
        else -> throw ParserException()
      }
      //expectEolOrEof()
    }
    return builder.build()
  }

}

class MasterPlaylist private constructor(
  val version : Int?) {


  data class Builder(
    var version : Int? = null) {

    fun version(version : Int) = apply { this.version = version }
    fun build() = MasterPlaylist(version)

  }
}

class MediaPlaylist private constructor(
  val version : Int,
  val segments: List<String>) {


  data class Builder(
    var version : Int? = null,
    var segments : MutableList<String> = mutableListOf()) {

    fun version(version : Int) = apply { this.version = version }
    fun addSegemnt(segment: String) = apply { segments.add(segment) }
    fun build() = MediaPlaylist(version ?: 1, segments.toList())

  }
}
class ParserException : Exception()
