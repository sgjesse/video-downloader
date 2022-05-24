import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.cio.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
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
  val result = Parser(Scanner("")).parseMediaPlaylist()
  val client = HttpClient(CIO)
  var index : Int = 0
  result.segments.forEach {
    if (true) {
      val response: HttpResponse = client.get(it)
      //println(response.status)
      val file: File = File("/tmp/klow/segment-${index}.ts")
      response.bodyAsChannel().copyAndClose(file.writeChannel())
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
  }
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
