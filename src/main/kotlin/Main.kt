import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.reflect.*

suspend fun main(args: Array<String>) {
    println("Hello World!")


    var x : String =
        """
            #EXTM3U
            #EXT-X-INDEPENDENT-SEGMENTS
            #EXT-X-VERSION:3
            #EXT-X-MEDIA:URI="h5b8xwlp_project_smoke_313-captions.m3u8",TYPE=SUBTITLES,GROUP-ID="subs",LANGUAGE="en",NAME="English",DEFAULT=NO,AUTOSELECT=YES,FORCED=NO,CHARACTERISTICS="public.accessibility.describes-music-and-sound,public.accessibility.transcribes-spoken-dialog"
            #EXT-X-STREAM-INF:BANDWIDTH=2405248,AVERAGE-BANDWIDTH=2180614,RESOLUTION=960x540,CODECS="avc1.64001f,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-540p-2000k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=7478681,AVERAGE-BANDWIDTH=6777974,RESOLUTION=1920x1080,CODECS="avc1.640028,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-1080p-6500k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=5244217,AVERAGE-BANDWIDTH=4734665,RESOLUTION=1280x720,CODECS="avc1.64001f,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-720p-4500k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=3574942,AVERAGE-BANDWIDTH=3202331,RESOLUTION=1280x720,CODECS="avc1.64001f,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-720p-3000k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=1401830,AVERAGE-BANDWIDTH=1261161,RESOLUTION=768x432,CODECS="avc1.64001e,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-432p-1100k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=940563,AVERAGE-BANDWIDTH=848725,RESOLUTION=640x360,CODECS="avc1.64001e,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-360p-730k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=505590,AVERAGE-BANDWIDTH=461531,RESOLUTION=480x270,CODECS="avc1.640015,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-270p-365k.m3u8
            #EXT-X-STREAM-INF:BANDWIDTH=252920,AVERAGE-BANDWIDTH=234048,RESOLUTION=416x234,CODECS="avc1.64000c,mp4a.40.2",SUBTITLES="subs"
            h5b8xwlp_project_smoke_313-16x9-1080p-234p-145k.m3u8
        """.trimIndent()

    // https://datatracker.ietf.org/doc/html/rfc8216#section-4.3.5
  
    // Master playlist tags
    // EXT-X-MEDIA
    //  TYPE
    //    AUDIO, VIDEO, SUBTITLES, and CLOSED-CAPTIONS.  This attribute is REQUIRED
    //  URI Quoted String (OPTIONAL)
    //  GROUP-ID Quoted String (REQUIRED)
    //  LANGUAGE Quoted String (OPTIONAL)
    //  ASSOC-LANGUAGE Quoted String (OPTIONAL)
    //  NAME Quoted String (REQUIRED)

    val scanner : Scanner = Scanner(x)
    while (scanner.hasNext()) {
      val token : Token = scanner.next()
      if (token.instanceOf(HashToken::class)) {
        val identifier : IdentifierToken = expectIdentifier(scanner)
        if (expectColonOrEol(scanner) is ColonToken) {
          var next : Token = expectNext(scanner)
          while (next !is EolToken) {
            next = expectNext(scanner)
          }
        }
      } else if (token.instanceOf(IdentifierToken::class)){
        var next : Token = expectNext(scanner)
        while (next !is EolToken) {
          next = expectNext(scanner)
        }
      } else {
        throw RuntimeException("XX")
      }
    }


    }

    fun expectIdentifier(scanner: Scanner) : IdentifierToken {
        val token: Token = expectNext(scanner)
        if (token is IdentifierToken) {
            return token
        }
        throw RuntimeException()
    }

  fun expectColonOrEol(scanner: Scanner) : Token {
      val token: Token = expectNext(scanner)
      if (token is ColonToken || token is EolToken) {
        return token
     }
     throw RuntimeException()
  }

fun expectNext(scanner: Scanner) : Token {
        if (!scanner.hasNext()) {
            throw RuntimeException("AA")
        }
        return scanner.next();
    }
    /*
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    val client = HttpClient(CIO)
    val response: HttpResponse = client.get("https://ga.video.cdn.pbs.org/videos/steven-raichlens-project-smoke/1aa5adbb-5f11-4a79-beba-5b5c5c8121fe/1000021560/hd-16x9-mezzanine-1080p/h5b8xwlp_project_smoke_313-16x9-1080p.m3u8")
    println(response.status)
    println(response.bodyAsText())
    client.close()
    */