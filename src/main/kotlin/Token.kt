sealed class Token(val type : String, val start: Int, val end : Int, val content : String) {
  public open fun isHashToken() : Boolean { return false }
  public override fun toString() : String {
    return "$type($start, $end, $content)"
  }
}

class EofToken(pos: Int) : Token("EofToken", pos, pos, "")
class EolToken(pos: Int) : Token("EolToken", pos, pos, "")
class HashToken(pos: Int) : Token("HashToken", pos, pos + 1, "#") {
  public override fun isHashToken() : Boolean { return true }
}

class ColonToken(pos: Int) : Token("ColonToken", pos, pos + 1, ":")
class EqualsToken(pos: Int) : Token("EqualsToken", pos, pos + 1, "=")
class CommaToken(pos: Int) : Token("CommaToken", pos, pos + 1, ",")
class IdentifierToken(start: Int, end : Int, val identifier : String) : Token("IdentifierToken", start, end, identifier)
class NumberToken(start: Int, end : Int, val number : Int) : Token("NumberToken", start, end, "")