import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class ParserTest {
  @Test
  fun emptySource() {
    val parser = Parser(Scanner(""))
    assertThrows<ParserException> { parser.parseMasterPlaylist() }
  }

  @Test
  fun missingHeader() {
    val parser = Parser(Scanner("#EXT-X-VERSION:3"))
    assertThrows<ParserException> { parser.parseMasterPlaylist() }
  }

  @Test
  fun invalidHeader() {
    val parser = Parser(Scanner("#EXTM3U"))
    assertThrows<ParserException> { parser.parseMasterPlaylist() }
  }

  @Test
  fun emptyMasterPlaylist() {
    val parser = Parser(Scanner("#EXTM3U\n"))
    parser.parseMasterPlaylist()
  }

  @Test
  fun sampleMasterPlaylist() {
    val parser = Parser(Scanner(
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
        """.trimIndent()))
    parser.parseMasterPlaylist()
  }

  @Test
  fun sampleMediaPlaylist() {
    val parser = Parser(Scanner(
      """
      #EXTM3U
      #EXT-X-TARGETDURATION:10
      #EXT-X-ALLOW-CACHE:YES
      #EXT-X-PLAYLIST-TYPE:VOD
      #EXT-X-VERSION:3
      #EXT-X-MEDIA-SEQUENCE:1
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment1_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment2_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment3_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment4_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment5_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment6_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment7_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment8_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment9_4_av.ts?null=0
      #EXTINF:10.000,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment10_4_av.ts?null=0
      #EXTINF:4.002,
      https://drod06p-vh.akamaihd.net/i/all/clear/streaming/50/6217efc8af5a610db4ceb050/Matilde-kharkiv-torsda_1599ed6d01c24d8ab370a675079c9769_,500,1100,2000,3500,5500,.mp4.csmil/segment11_4_av.ts?null=0
      #EXT-X-ENDLIST
    """.trimIndent()))
    val result = parser.parseMediaPlaylist()
    assertEquals(11, result.segments.size)
    result.segments.forEach { println("$it") }
  }
}

