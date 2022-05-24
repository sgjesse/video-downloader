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
class CommentToken(start: Int, end : Int, val identifier : String) : Token("CommentToken", start, end, identifier)
class TagToken(start: Int, end : Int, val identifier : String) : Token("TagToken", start, end, identifier)
class IdentifierToken(start: Int, end : Int, val identifier : String) : Token("IdentifierToken", start, end, identifier)
class UrlToken(start: Int, end : Int, val url : String) : Token("UrlToken", start, end, url)
class IntToken(start: Int, end : Int, val value : Int) : Token("IntToken", start, end, "")
class DoubleToken(start: Int, end : Int, val value : Double) : Token("DoubleToken", start, end, "")
class QuotedStringToken(start: Int, end : Int, val string : String) : Token("QuotedStringToken", start, end, "")