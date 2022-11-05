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

  private fun isEol() : Boolean {
    return current is EolToken
  }

  private fun expectEol() {
    if (!isEol()) {
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

  private fun expectNumber() : Long {
    val token = current
    if (token !is NumberToken) {
      throw ParserException()
    }
    current = scanner.next()
    return token.longValue();
  }

  private fun expectHeader() {
    expectTag("EXTM3U")
    expectEolOrEof()
  }

  private fun expectKV(consume : (key : String, value : Token) -> Unit) {
    while (current !is EolToken && current !is EofToken) {
      if (current !is IdentifierToken) {
        throw ParserException()
      }
      val key = current.content
      current = scanner.next()
      expectEquals()
      consume.invoke(key, current)
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
    while (current !is EofToken) {
      // Empty lines are allowed, https://datatracker.ietf.org/doc/html/rfc8216#section-4.1 second paragraph.
      if (isEol()) {
        expectEol()
        continue
      }
      val tag = expectTag()
      when (tag.identifier) {
        "EXT-X-INDEPENDENT-SEGMENTS" -> {
          builder.independentSegments = true
          expectEolOrEof()
        }
        "EXT-X-VERSION" -> {
          expectColon()
          builder.version(expectInt())
          expectEolOrEof()
        }
        "EXT-X-MEDIA" -> {
          expectColon()
          expectKV() { _, _ -> }
        }
        "EXT-X-STREAM-INF" -> {
          expectColon()
          val variantStreamBuilder = VariantStream.Builder()
          expectKV() { k, v ->
            when (k) {
              "BANDWIDTH" -> {
                if (v is IntToken) {
                  variantStreamBuilder.bandwidth(v.value)
                }
              }
              "AVERAGE-BANDWIDTH" -> {
                if (v is IntToken) {
                  variantStreamBuilder.averageBandwidth(v.value)
                }
              }
              "CODECS" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.codecs(v.content)
                }
              }
              "RESOLUTION" -> {
                if (v is ResolutionToken) {
                  variantStreamBuilder.resolution(v)
                }
              }
              "FRAME-RATE" -> {
                if (v is DoubleToken) {
                  variantStreamBuilder.frameRate(v.value)
                }
              }
              "HDCP-LEVEL" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.hdcpLevel(v.content)
                }
              }
              "AUDIO" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.audio(v.content)
                }
              }
              "VIDEO" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.video(v.content)
                }
              }
              "SUBTITLES" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.subtitles(v.content)
                }
              }
              "CLOSED-CAPTIONS" -> {
                if (v is IdentifierToken) {
                  variantStreamBuilder.closedCaptions(v.content)
                }
              }
              else -> {
                // Ignore
              }
            }
          }
          variantStreamBuilder.uri = expectUrl().url
          builder.addVariantStream(variantStreamBuilder.build())
          expectEolOrEof()
        }
        "EXT-X-I-FRAME-STREAM-INF" -> {
          expectColon()
          expectKV() { _, _ -> }
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
      // Empty lines are allowed, https://datatracker.ietf.org/doc/html/rfc8216#section-4.1 second paragraph.
      if (isEol()) {
        expectEol()
        continue
      }
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
          expectKV() { _, _ -> }
        }
        "EXT-X-STREAM-INF" -> {
          expectColon()
          expectKV() { _, _ -> }
          expectUrl()
          expectEolOrEof()
        }
        "EXT-X-INDEPENDENT-SEGMENTS" -> {
          // TODO.
          expectEolOrEof()
        }
        "EXT-X-MAP" -> {
          // TODO.
          expectColon()
          expectKV() { _, _ -> }
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
          if (current is TagToken) {
            val tag = expectTag()
            when (tag.identifier ) {
              "EXT-X-BYTERANGE" -> {
                expectColon()
                expectNumber()
                if (current is AtToken) {
                  current = scanner.next()
                  expectNumber()
                }
                expectEolOrEof()
              } else -> throw ParserException()
            }
          }
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
  val version : Int?,
  val independentSegments : Boolean?) {


  data class Builder(
    var version : Int? = null,
    var independentSegments : Boolean? = null,
    val variantStreams: MutableList<VariantStream> = arrayListOf()
  ) {

    fun version(version : Int) = apply { this.version = version }
    fun independentSegments(independentSegments : Boolean) = apply { this.independentSegments = independentSegments }
    fun addVariantStream(variantStream : VariantStream) { variantStreams.add(variantStream) }
    fun build() = MasterPlaylist(version, independentSegments)

  }
}

class VariantStream private constructor(
  val bandwidth : Int,
  val averageBandwidth : Int?,
  val codecs : String?,
  val resolution : String?,
  val frameRate : Double?,
  val hdcpLevel : String?,
  val audio : String?,
  val video : String?,
  val subtitles : String?,
  val closedCaptions : String?,
  val uri : String,
) {
  data class Builder(
    var bandwidth : Int? = null,
    var averageBandwidth : Int? = null,
    var codecs : String? = null,
    var resolution : String? = null,
    var frameRate : Double? = null,
    var hdcpLevel : String? = null,
    var audio : String? = null,
    var video : String? = null,
    var subtitles : String? = null,
    var closedCaptions : String? = null,
    var uri : String? = null,
  ) {
    fun bandwidth(bandwidth : Int) = apply { this.bandwidth = bandwidth }
    fun averageBandwidth(averageBandwidth : Int) = apply { this.averageBandwidth = averageBandwidth }
    fun codecs(codecs : String) = apply { this.codecs = codecs }
    fun resolution(resolution : ResolutionToken) = apply { this.resolution = resolution.content }
    fun frameRate(frameRate : Double) = apply { this.frameRate = frameRate }
    fun hdcpLevel(hdcpLevel : String) = apply { this.hdcpLevel = hdcpLevel }
    fun audio(audio : String) = apply { this.audio = audio }
    fun video(video : String) = apply { this.video = video }
    fun subtitles(subtitles : String) = apply { this.subtitles = subtitles }
    fun closedCaptions(closedCaptions : String) = apply { this.closedCaptions = closedCaptions }
    fun build() = VariantStream(bandwidth!!, averageBandwidth, codecs, resolution, frameRate, hdcpLevel, audio, video, subtitles, closedCaptions, uri!!)
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
