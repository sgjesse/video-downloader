import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.cio.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import java.io.File

/*
#ffmpeg -i all.vtt all-orig.srt
cat long-filelist-subs.txt
file 'long-segment-0.vtt'
file 'long-segment-1.vtt'
file 'long-segment-2.vtt'
file 'long-segment-3.vtt'
file 'long-segment-4.vtt'
file 'long-segment-5.vtt'

cat filelist.txt
file 'long-segment-0.ts'
file 'long-segment-1.ts'
file 'long-segment-2.ts'
file 'long-segment-3.ts'
file 'long-segment-4.ts'
file 'long-segment-5.ts'
file 'long-segment-6.ts'
file 'long-segment-7.ts'
file 'long-segment-8.ts'
file 'long-segment-9.ts'
file 'long-segment-10.ts'
file 'long-segment-11.ts'
...

# Combine the VTT files into one
ffmpeg -f concat -i long-filelist-subs.txt long.vtt

# Combine the TS files into one adding subtitles
ffmpeg -f concat -i filelist.txt -vf subtitles=long.vtt long-low-subs.mp4

ffmpeg -ss 00:04:24 -to 00:05:55 -i long-low-subs.mp4 -c copy cut.mp4
 */
suspend fun main(args: Array<String>) {
  val client = HttpClient(CIO)
  //val response: HttpResponse = client.get("https://drod23c.akamaized.net/all/clear/none/58/62871619a95a611eac709058/00922201790/stream_fmp4/master_manifest.m3u8")
  val response: HttpResponse =
    client.get("https://drod23c.akamaized.net/all/clear/none/58/62871619a95a611eac709058/00922201790/stream_fmp4/19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_5500.m3u8")
  println(response.status)
  println(response.bodyAsText())
  val result = Parser(Scanner(response.bodyAsText())).parseMediaPlaylist()
  return
  var index: Int = 1
  result.segments.forEach {
    println(it)
    val url = "https://drod23c.akamaized.net/all/clear/none/58/62871619a95a611eac709058/00922201790/stream_fmp4/" + it
    println(url)
    val file: File = File("/tmp/bridge/segment-${index}.ts")

    runBlocking {
      val httpResponse: HttpResponse = client.get("https://ktor.io/") {
        onDownload { bytesSentTotal, contentLength ->
          println("Received $bytesSentTotal bytes from $contentLength")
        }
      }
      val responseBody: ByteArray = httpResponse.body()
      file.writeBytes(responseBody)
      println("A file saved to ${file.path}")
    }
    println("file 'segment-${index}.ts'")
    index++
  }
  client.close()
}

suspend fun x() {
  val x =
    """
      #EXTM3U
      #EXT-X-VERSION:6
      #EXT-X-INDEPENDENT-SEGMENTS


      #EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="audio_low",NAME="English",LANGUAGE="en",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="1",URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_audio_64kbps.m3u8"
      #EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="audio_medium",NAME="English",LANGUAGE="en",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="2",URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_audio_128kbps.m3u8"
      #EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID="audio_high",NAME="English",LANGUAGE="en",AUTOSELECT=YES,DEFAULT=YES,CHANNELS="2",URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_audio_192kbps.m3u8"

      #EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="subs",NAME="Fremmedsprogstekster",DEFAULT=YES,AUTOSELECT=YES,FORCED=NO,LANGUAGE="da",URI="../subtitles/Foreign-19761178-0da82349-ef5a-4bbb-87c6-f7488a169f6d/playlist.m3u8"
      #EXT-X-MEDIA:TYPE=SUBTITLES,GROUP-ID="subs",NAME="Dansk",DEFAULT=NO,AUTOSELECT=NO,FORCED=NO,LANGUAGE="da",URI="../subtitles/Foreign_HardOfHearing-19761178-0da82349-ef5a-4bbb-87c6-f7488a169f6d/playlist.m3u8"

      #EXT-X-I-FRAME-STREAM-INF:AVERAGE-BANDWIDTH=16958,BANDWIDTH=34791,VIDEO-RANGE=SDR,CODECS="avc1.42C01E",RESOLUTION=640x360,URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_500_iframe.m3u8"
      #EXT-X-I-FRAME-STREAM-INF:AVERAGE-BANDWIDTH=39387,BANDWIDTH=83865,VIDEO-RANGE=SDR,CODECS="avc1.64001F",RESOLUTION=852x480,URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_1100_iframe.m3u8"
      #EXT-X-I-FRAME-STREAM-INF:AVERAGE-BANDWIDTH=60621,BANDWIDTH=123045,VIDEO-RANGE=SDR,CODECS="avc1.640028",RESOLUTION=960x540,URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_2000_iframe.m3u8"
      #EXT-X-I-FRAME-STREAM-INF:AVERAGE-BANDWIDTH=95127,BANDWIDTH=205052,VIDEO-RANGE=SDR,CODECS="avc1.640029",RESOLUTION=1280x720,URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_3500_iframe.m3u8"
      #EXT-X-I-FRAME-STREAM-INF:AVERAGE-BANDWIDTH=141705,BANDWIDTH=319841,VIDEO-RANGE=SDR,CODECS="avc1.640029",RESOLUTION=1920x1080,URI="19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_5500_iframe.m3u8"

      #EXT-X-STREAM-INF:BANDWIDTH=642940,AVERAGE-BANDWIDTH=564615,FRAME-RATE=25.000,CODECS="avc1.42C01E,mp4a.40.2",RESOLUTION=640x360,VIDEO-RANGE="SDR",AUDIO="audio_low",SUBTITLES="subs"
      19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_500.m3u8
      #EXT-X-STREAM-INF:BANDWIDTH=1397339,AVERAGE-BANDWIDTH=1223953,FRAME-RATE=25.000,CODECS="avc1.64001F,mp4a.40.2",RESOLUTION=852x480,VIDEO-RANGE="SDR",AUDIO="audio_medium",SUBTITLES="subs"
      19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_1100.m3u8
      #EXT-X-STREAM-INF:BANDWIDTH=2489882,AVERAGE-BANDWIDTH=2182992,FRAME-RATE=25.000,CODECS="avc1.640028,mp4a.40.2",RESOLUTION=960x540,VIDEO-RANGE="SDR",AUDIO="audio_high",SUBTITLES="subs"
      19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_2000.m3u8
      #EXT-X-STREAM-INF:BANDWIDTH=4201586,AVERAGE-BANDWIDTH=3674307,FRAME-RATE=25.000,CODECS="avc1.640029,mp4a.40.2",RESOLUTION=1280x720,VIDEO-RANGE="SDR",AUDIO="audio_high",SUBTITLES="subs"
      19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_3500.m3u8
      #EXT-X-STREAM-INF:BANDWIDTH=6484146,AVERAGE-BANDWIDTH=5663762,FRAME-RATE=25.000,CODECS="avc1.640029,mp4a.40.2",RESOLUTION=1920x1080,VIDEO-RANGE="SDR",AUDIO="audio_high",SUBTITLES="subs"
      19377136-22d39335-39a7-4a31-9fb9-e15c23a976dc_video_5500.m3u8
    """.trimIndent()

  val y = Parser(Scanner(x)).parseMasterPlaylist()
  val result = Parser(Scanner("")).parseMediaPlaylist()
  val client = HttpClient(CIO)
  var index : Int = 0
  result.segments.forEach {
    if (true) {
      val response: HttpResponse = client.get(it) {
        onDownload { bytesSentTotal, contentLength ->
          println("Received $bytesSentTotal bytes from $contentLength")
        }
      }
        //println(response.status)
        val file: File = File("/tmp/klow/segment-${index}.ts")
        response.bodyAsChannel().copyAndClose(file.writeChannel())
      }
    }
    println("file 'segment-${index}.ts'")
    /*val bytes = response.bodyAsChannel()
    val byteBufferSize = 1024 * 100
    val byteBuffer = ByteArray(byteBufferSize)

    var read = 0

    do {
      val currentRead = bytes.readAvailable(byteBuffer, 0, byteBufferSize)
      if (currentRead > 0) {
        read += currentRead
      }
    } while (currentRead >= 0)
    println("Read: $read")*/
    //println(file)
    index++
  //}
  client.close()

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
