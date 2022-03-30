package org.keith;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.po.BilibiliPo;
import org.po.VideoInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:
 * author:xiangyang
 */
public class download_bibi {

    private static String bibiApi = "https://api.bilibili.com/x/player/pagelist?bvid=";
    private static String api = "https://www.bilibili.com/video/";
    private static String text;
    private static Pattern pattern = Pattern.compile("part\":\"[\\d\\D]+?.\"");
    private static Pattern pattern2 = Pattern.compile("duration\":[\\s\\S]+?,");
    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36";
    private static VideoInfo VIDEO_INFO = new VideoInfo();
    private static String SAVE_PATH = ".\\Test\\性能测试";
    /** FFMPEG位置 `windows电脑下是ffmpeg.exe`*/
    private static String FFMPEG_PATH = "D:\\self_pro\\Selenium\\ffmpeg\\bin\\ffmpeg";


    public static void main(String[] args) throws Exception {
        Map<Integer, BilibiliPo> map = download_shipin("https://www.bilibili.com/video/BV1W64y1a7we/");
        for (Map.Entry<Integer, BilibiliPo> map1 : map.entrySet()) {
            htmlPraser(map1.getValue());
        }
    }

    /**
     * 获取bilibili的url内容
     * @param urlStr
     * @param charset
     * @return
     */
    public static String getUrl(String urlStr, String charset) {
        String tmp = "";
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), Charset.forName(charset)));
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    /**
     * 下载他的所有视频url和title等
     */
    public static Map<Integer,BilibiliPo> download_shipin(String bibi_url) {
        int a = 0;
        String name = null;
        String time = null;
        String bvid = bibi_url.substring(bibi_url.indexOf("BV"), bibi_url.lastIndexOf("/"));
        String bibi_url_real = bibiApi + bvid;
        text = getUrl(bibi_url_real, "utf-8");
        Matcher m1 = pattern.matcher(text);
        Matcher m2 = pattern2.matcher(text);
        Map<Integer, BilibiliPo> urlMap = new HashMap<Integer, BilibiliPo>();
        while (m1.find()) {
            a++;
            if (m2.find()) {
                BilibiliPo bilibiliPo = new BilibiliPo();
                //获得视频名称
                name = m1.group().substring(7, m1.group().length() - 1);
                //获得视频时长
                time = m2.group().substring(10, m2.group().length() - 1);
                bilibiliPo.setId(String.valueOf(a));
                bilibiliPo.setName(name);
                bilibiliPo.setTime("视频长度：" + Integer.parseInt(time) / 60 + "分钟" + Integer.parseInt(time) % 60 + "秒");
                bilibiliPo.setUrl(api + bvid + "?p=" + a);
                urlMap.put(a, bilibiliPo);
            }
        }
        return urlMap;
    }


    /**
     * 解析HTML
     * @param bilibiliPo
     */
    public static void htmlPraser(BilibiliPo bilibiliPo) throws Exception{
        String url = bilibiliPo.getUrl();
        String title = bilibiliPo.getName();
        HttpResponse request = HttpRequest.get(url).timeout(10000).execute();
        String html = request.body();
        Document document = Jsoup.parse(html);
        // 视频名称
        VIDEO_INFO.setVideoName(title);
        // 截取视频信息
        Pattern pattern = Pattern.compile("(?<=<script>window.__playinfo__=).*?(?=</script>)");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            VIDEO_INFO.setVideoInfo(new JSONObject(matcher.group()));
        } else {
            System.err.println("未匹配到视频信息，退出程序！");
            return;
        }
        getVideoInfo(url);
    }

    /** 解析视频和音频的具体信息 */
    private static void getVideoInfo(String url){
        // 获取视频的基本信息
        JSONObject videoInfo = VIDEO_INFO.getVideoInfo();
        JSONArray videoInfoArr = videoInfo.getJSONObject("data").getJSONObject("dash").getJSONArray("video");
        VIDEO_INFO.setVideoBaseUrl(videoInfoArr.getJSONObject(0).getStr("baseUrl"));
        VIDEO_INFO.setVideoBaseRange(videoInfoArr.getJSONObject(0).getJSONObject("SegmentBase").getStr("Initialization"));
        HttpResponse videoRes = HttpRequest.get(VIDEO_INFO.getVideoBaseUrl())
                .header("Referer", url)
                .header("Range", "bytes=" + VIDEO_INFO.getVideoBaseRange())
                .header("User-Agent", USER_AGENT)
                .timeout(5000)
                .execute();
        VIDEO_INFO.setVideoSize(videoRes.header("Content-Range").split("/")[1]);

        // 获取音频基本信息
        JSONArray audioInfoArr = videoInfo.getJSONObject("data").getJSONObject("dash").getJSONArray("audio");
        VIDEO_INFO.setAudioBaseUrl(audioInfoArr.getJSONObject(0).getStr("baseUrl"));
        VIDEO_INFO.setAudioBaseRange(audioInfoArr.getJSONObject(0).getJSONObject("SegmentBase").getStr("Initialization"));
        HttpResponse audioRes = HttpRequest.get(VIDEO_INFO.getAudioBaseUrl())
                .header("Referer", url)
                .header("Range", "bytes=" + VIDEO_INFO.getAudioBaseRange())
                .header("User-Agent", USER_AGENT)
                .timeout(5000)
                .execute();
        VIDEO_INFO.setAudioSize(audioRes.header("Content-Range").split("/")[1]);

        downloadFile(url);
    }

    /** 下载音视频 */
    private static void downloadFile(String url) {
        // 保存音视频的位置
        File fileDir = new File(SAVE_PATH + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        // 下载视频
        File videoFile = new File(SAVE_PATH + File.separator + VIDEO_INFO.getVideoName() + "_video.mp4");
        if (!videoFile.exists()) {
            System.out.println("--------------开始下载视频文件--------------");
            HttpResponse videoRes = HttpRequest.get(VIDEO_INFO.getVideoBaseUrl())
                    .header("Referer", url)
                    .header("Range", "bytes=0-" + VIDEO_INFO.getVideoSize())
                    .header("User-Agent", USER_AGENT)
                    .execute();
            videoRes.writeBody(videoFile);
            System.out.println("--------------视频文件下载完成--------------");
        }

        //下载音频
        File audioFile = new File(SAVE_PATH + File.separator + VIDEO_INFO.getVideoName() + "_audio.mp4");
        if (!audioFile.exists()){
            System.out.println("--------------开始下载音频文件--------------");
            HttpResponse audioRes = HttpRequest.get(VIDEO_INFO.getAudioBaseUrl())
                    .header("Referer", url)
                    .header("Range", "bytes=0-" + VIDEO_INFO.getVideoSize())
                    .header("User-Agent", USER_AGENT)
                    .execute();
            audioRes.writeBody(audioFile);
            System.out.println("--------------音频文件下载完成--------------");
        }

        mergeFiles(videoFile,audioFile);
    }

    /** 合并视频 ffmpeg.exe */
    private static void mergeFiles(File videoFile,File audioFile){
        System.out.println("--------------开始合并音视频--------------");
        String outFile = SAVE_PATH + File.separator + VIDEO_INFO.getVideoName() + ".mp4";
        List<String> commend = new ArrayList<>();
        commend.add(FFMPEG_PATH);
        commend.add("-i");
        commend.add(videoFile.getAbsolutePath());
        commend.add("-i");
        commend.add(audioFile.getAbsolutePath());
        commend.add("-vcodec");
        commend.add("copy");
        commend.add("-acodec");
        commend.add("copy");
        commend.add(outFile);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commend);
        try {
            builder.inheritIO().start().waitFor();
            System.out.println("--------------音视频合并完成--------------");
            videoFile.delete();
            audioFile.delete();
        } catch (InterruptedException | IOException e) {
            System.err.println("音视频合并失败！");
            e.printStackTrace();
        }

    }




}
