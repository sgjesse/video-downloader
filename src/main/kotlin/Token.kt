sealed class Token(val type : String, val start: Int, val end : Int, val content : String) {
  public override fun toString() : String {
    return "$type($start, $end, $content)"
  }
}

class EofToken(pos: Int) : Token("EofToken", pos, pos, "")
class EolToken(pos: Int) : Token("EolToken", pos, pos, "")
class HashToken(pos: Int) : Token("HashToken", pos, pos + 1, "#")
class ColonToken(pos: Int) : Token("ColonToken", pos, pos + 1, ":")
class EqualsToken(pos: Int) : Token("EqualsToken", pos, pos + 1, "=")
class CommaToken(pos: Int) : Token("CommaToken", pos, pos + 1, ",")
class AtToken(pos: Int) : Token("AtToken", pos, pos + 1, "@")
class CommentToken(start: Int, end : Int, val identifier : String) : Token("CommentToken", start, end, identifier)
class TagToken(start: Int, end : Int, val identifier : String) : Token("TagToken", start, end, identifier)
class IdentifierToken(start: Int, end : Int, val identifier : String) : Token("IdentifierToken", start, end, identifier)
class ResolutionToken(start: Int, end : Int, val width : Int, val height : Int) : Token("ResolutionToken", start, end, "${width}x${height}")
class UrlToken(start: Int, end : Int, val url : String) : Token("UrlToken", start, end, url)
abstract class NumberToken(start: Int, end : Int, val number : String) : Token("NumberToken", start, end, number) {
  abstract fun longValue() : Long
}
class IntToken(start: Int, end : Int, val value : Int) : NumberToken(start, end, "$value") {
  override fun longValue() : Long {
    return value.toLong()
  }
}
class LongToken(start: Int, end : Int, val value : Long) : NumberToken(start, end, "$value") {
  override fun longValue() : Long {
    return value
  }
}
class DoubleToken(start: Int, end : Int, val value : Double) : Token("DoubleToken", start, end, "$value")
class QuotedStringToken(start: Int, end : Int, val string : String) : Token("QuotedStringToken", start, end, string)
class ByteRangeToken(start: Int, end : Int, val first : Long, val second : Long) : Token("ByteRangeToken", start, end, "${first}@${second}")
