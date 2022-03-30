package org.example;

import org.openqa.selenium.chrome.ChromeDriver;
import org.util.ForFile;

/**
 * description:
 * author:xiangyang
 */

public class ChromeDriver_01 {


    public   String CHROME_DRIVER = "webdriver.chrome.driver";

    public  String DRIVER_PATH = "D:\\webdriver/chromedriver.exe";

    public  String URL = "https://blog.csdn.net/zhangkaiyazky/article/details/102525628";


    public static void main(String[] args) {
        ChromeDriver_01 driver_01 = new ChromeDriver_01();
        driver_01.download_file();
    }


    public void download_file() {
        System.setProperty(CHROME_DRIVER, DRIVER_PATH);
        ChromeDriver web = new ChromeDriver();
        web.manage().window().maximize();
        web.get(URL);
        try {
            if (ForFile.createFile("web", web.getPageSource())) {
                System.out.println("输入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
