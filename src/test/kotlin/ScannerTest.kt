import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

internal class ScannerTest {

  @Test
  fun emptySource() {
    val scanner = Scanner("")
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun emptyLines() {
    val scanner = Scanner("\n\n")
    assertTrue { scanner.next() is EolToken }
    assertTrue { scanner.next() is EolToken }
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun emptyLinesWithCR() {
    val scanner = Scanner("\r\n\r\n")
    assertTrue { scanner.next() is EolToken }
    assertTrue { scanner.next() is EolToken }
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun missingLF() {
    val scanner = Scanner("\r#")
    assertThrows<UnexpectedCharacterScannerException> { scanner.next() }
  }

  @Test
  fun missingLFEof() {
    val scanner = Scanner("\r")
    assertThrows<UnexpectedEofScannerException> { scanner.next() }
  }

  @Test
  fun emptyPlaylistFile() {
    val scanner = Scanner("#EXTM3U")
    assertTrue { scanner.next() is TagToken }
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun emptyPlaylistFileWithEol() {
    val scanner = Scanner("#EXTM3U\n")
    assertTrue { scanner.next() is TagToken }
    assertTrue { scanner.next() is EolToken }
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun quotedString() {
    val scanner = Scanner("\"XXX\"")
    assertTrue { scanner.next() is QuotedStringToken }
    assertTrue { scanner.next() is EofToken }
    assertThrows<EofScannerException> { scanner.next() }
  }

  @Test
  fun quotedStringUnterminated() {
    val scanner = Scanner("\"XXX")
    assertThrows<UnexpectedEofScannerException> { scanner.next() }
  }}