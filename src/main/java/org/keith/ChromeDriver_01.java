package org.keith;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.util.ForFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * description: 根据关键字搜索视频
 * author:xiangyang
 */

public class ChromeDriver_01 {


    private static   String CHROME_DRIVER = "webdriver.chrome.driver";

    private static  String DRIVER_PATH = "D:\\webdriver/chromedriver.exe";

    /**想要搜索的网站**/
    private static  String URL = "https://haokan.baidu.com/";

    private static String KEY_WORDS = "经典传奇";


    public static void main(String[] args) throws Exception{


        httpDownload(download_file(), ".\\downDir/haokan.mp4");
    }


    public static String  download_file() throws Exception {

        Thread.sleep(3000);
        System.setProperty(CHROME_DRIVER, DRIVER_PATH);
        ChromeDriver web = new ChromeDriver();
        web.manage().window().maximize();
        web.manage().deleteAllCookies();
        try {
            web.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//等待界面加载完
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        //打开目标地址
        web.get("https://haokan.baidu.com/web/search/page?query=%E7%BB%8F%E5%85%B8%E4%BC%A0%E5%A5%87&sfrom=recommend");
        //输入关键字
//        Thread.sleep(1000);
//        web.findElement(By.className("page-search-input")).sendKeys(KEY_WORDS);
//        web.findElement(By.className("search-input-btn")).click();

        Thread.sleep(1000);
        System.out.println(web.getCurrentUrl());
        Thread.sleep(2000);
//        String pageSource = web.getPageSource();
//        ForFile.createFile("haokan", pageSource);

        //点击视频
        Thread.sleep(1000);
//        web.findElement(By.xpath("//*[@id=\"rooot\"]/section/main/section/main/div/div/a[1]")).click();

        List<WebElement> elements = web.findElements(By.className("message-list"));
        List<String> hrefs = new ArrayList<>();
        for (WebElement element : elements) {
            String href = element.getAttribute("href");
            hrefs.add(href);
        }

        String handle = web.getWindowHandle();
        for (String handles : web.getWindowHandles()) {
            if (handles.equals(handle)) {
                continue;
            }
            web.switchTo().window(handles);
        }
        //
        Thread.sleep(1000);
        System.out.println(web.getCurrentUrl());
        System.out.println(web.getTitle());
        WebElement element = web.findElement(By.xpath("//*[@id=\"mse\"]/div[2]/video"));
        String src = element.getAttribute("src");

        String pageSource = web.getPageSource();
        ForFile.createFile("haokan", pageSource);
        System.out.println(src);

//        web.quit();
        return src;

    }


    public static boolean httpDownload(String httpUrl, String saveFile) {
        // 1.下载网络文件
        int byteRead;
        java.net.URL url;
        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return false;
        }

        try {
            //2.获取链接
            URLConnection conn = url.openConnection();
            //3.输入流
            InputStream inStream = conn.getInputStream();
            //3.写入文件
            FileOutputStream fs = new FileOutputStream(saveFile);

            byte[] buffer = new byte[1024];
            while ((byteRead = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteRead);
            }
            inStream.close();
            fs.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }




}
