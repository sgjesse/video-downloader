open class ScannerBase(protected val source : String, protected val x : Map<Char, (Int) -> Token>) {
  private var pos = 0
  private var wasEol = true

  private fun isEof(pos : Int) : Boolean {
    return source.length == pos;
  }

  private fun isFirstIdentifierChar(pos : Int) : Boolean {
    return !isEof(pos) && (source[pos].isLetter() || source[pos] == '-' || source[pos] == '_'  || source[pos] == '.');
  }

  private fun isIdentifierChar(pos : Int) : Boolean {
    return !isEof(pos) && (source[pos].isLetterOrDigit() || source[pos] == '-' || source[pos] == '_' || source[pos] == '.');
  }

  private fun isTagChar(pos : Int) : Boolean {
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

  private fun checkToken() : Token? {
    val token = x.get(source[pos])
    if (token != null) {
      return token.invoke(pos++)
    }
    return token
  }

  private fun quotedString() : Token {
    pos++
    val start: Int = pos
    while (pos < source.length && source[pos] != '"') {
      pos++;
    }
    if (pos == source.length) {
      throw UnexpectedEofScannerException()
    }
    pos++
    return QuotedStringToken(start, pos, source.substring(start, pos - 1));
  }

  fun xnext() : Token {
    val xxx = wasEol
    wasEol = false
    if (source.length < pos) {
      throw EofScannerException();
    } else if (source.length == pos) {
      return EofToken(pos++)
    } else if (source[pos] == '\n') {
      wasEol = true
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
      wasEol = true
      return EolToken(eolPos)
    } else if (source[pos] == '#') {
      // Tags starts with #EXT.
      if (pos < source.length - 3
        && source[pos + 1] == 'E'
        && source[pos + 2] == 'X'
        && source[pos + 3] == 'T' ) {
        val start = pos + 1
        pos += 4
        while (isTagChar(pos)) {
          pos++
        }
        return TagToken(start, pos, source.substring(start, pos))
      } else {
        // Comment ends at EOL.
        val start = pos
        while (pos < source.length && source[pos] != '\n') {
          pos++;
        }
        return CommentToken(start, pos, source.substring(start, pos))
      }
    } else {
      if (xxx) {
        val start : Int = pos
        pos++
        while (!isEof(pos) && source[pos] != '\n') {
          pos++
        }
        return UrlToken(start, pos, source.substring(start, pos));
      }
      val token = checkToken();
      if (token != null) {
        return token;
      }
      if (source[pos] == '"') {
        return quotedString()
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
        if (source[pos] == '.') {
          pos++;
          while (isDigitChar(pos)) {
            pos++
          }
          return DoubleToken(start, pos, source.substring(start, pos).toDouble());
        }
        if (isIdentifierChar(pos)) {
          while (isLetterOrDigitChar(pos)) {
            pos++
          }
          return IdentifierToken(start, pos, source.substring(start, pos))
        }
        return IntToken(start, pos, source.substring(start, pos).toInt());
      } else {
        throw RuntimeException("Unexpected token " + source[pos])
      }
    }
  }
}

class Scanner(source: String) : ScannerBase(
  source,
  hashMapOf(
    '#' to ::HashToken,
    ':' to ::ColonToken,
    '=' to ::EqualsToken,
    ',' to ::CommaToken))

open class ScannerException : Exception()
class EofScannerException : ScannerException()
class UnexpectedCharacterScannerException : ScannerException()
class UnexpectedEofScannerException : ScannerException()
