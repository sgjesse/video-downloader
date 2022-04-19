class Scanner(val source: String) {
  private var pos = 0

  private fun isEof(pos : Int) : Boolean {
    return source.length == pos;
  }

  private fun isFirstIdentifierChar(pos : Int) : Boolean {
    return !isEof(pos) && (source[pos].isLetter() || source[pos] == '-' || source[pos] == '_'  || source[pos] == '.');
  }

  private fun isIdentifierChar(pos : Int) : Boolean {
    return !isEof(pos) && (source[pos].isLetterOrDigit() || source[pos] == '-' || source[pos] == '_' || source[pos] == '.');
  }

  private fun isDigitChar(pos : Int) : Boolean {
    return !isEof(pos) && source[pos].isDigit();
  }

  private fun isLetterOrDigitChar(pos : Int) : Boolean {
    return !isEof(pos) && (source[pos].isLetterOrDigit() || source[pos] == '-');
  }

  fun hasNext() : Boolean {
    return !isEof(pos)
  }
  fun next() : Token {
    val t = xnext()
    println(t)
    return t
  }

  fun xnext() : Token {
    if (source.length < pos) {
      throw EofScannerException();
    } else if (source.length == pos) {
      return EofToken(pos++)
    } else if (source[pos] == '#') {
      return HashToken(pos++);
    } else if (source[pos] == ':') {
      return ColonToken(pos++);
    } else if (source[pos] == '=') {
      return EqualsToken(pos++);
    } else if (source[pos] == ',') {
      return CommaToken(pos++);
    } else if (source[pos] == '\n') {
      return EolToken(pos++);
    } else if (source[pos] == '\r') {
      val eolPos = pos++
      if (source.length == pos) {
        throw UnexpectedEofScannerException()
      }
      if (source[pos] != '\n') {
        throw UnexpectedCharacterScannerException()
      }
      pos++
      return EolToken(eolPos)
    } else if (source[pos] == '"') {
      pos++
      val start: Int = pos
      while (source[pos] != '"') {
        pos++;
      }
      pos++
      return IdentifierToken(start, pos, source.substring(start, pos - 1));
    } else if (isFirstIdentifierChar(pos)) {
      val start : Int = pos
      pos++
      while (isIdentifierChar(pos)) {
        pos++
      }
      return IdentifierToken(start, pos, source.substring(start, pos));
    } else if (isDigitChar(pos)) {
      val start : Int = pos
      pos++
      while (isDigitChar(pos)) {
        pos++
      }
      if (isIdentifierChar(pos)) {
        while (isLetterOrDigitChar(pos)) {
          pos++
        }
        return IdentifierToken(start, pos, source.substring(start, pos))
      }
      return NumberToken(start, pos, source.substring(start, pos).toInt());
    } else {
      throw RuntimeException("Unexpected token " + source[pos])
    }
  }
}

open class ScannerException : Exception()
class EofScannerException : ScannerException()
class UnexpectedCharacterScannerException : ScannerException()
class UnexpectedEofScannerException : ScannerException()
