package org.util;

import java.io.*;

/**
 * description:
 * author:xiangyang
 */
public class ForFile {

    private static String path = System.getProperty("user.dir") + File.separator + "downDir" + File.separator;
    private static String filenameTemp;


    public static boolean createFile(String filename, String filecontent) {
        Boolean b = false;
        filenameTemp = path + filename + ".html";
        File file = new File(filenameTemp);
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                b = true;
                System.out.println("success create file,the file is " + filenameTemp);
                writreFileContent(filenameTemp, filecontent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }


    /**
     * 往文件里写入内容
     * @param filepath 文件名
     * @param newStr 写入内容
     */
    public static boolean writreFileContent(String filepath, String newStr) throws IOException {
        Boolean bool = false;
        String filein = newStr + "\r\n";
        String temp = "";
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;


        try {
            File file = new File(filepath);
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            //文件原有内容
            for (int i = 0; (temp = br.readLine()) != null; i++) {
                sb.append(temp);
                sb = sb.append(System.getProperty("line.separator"));
            }
            sb.append(filein);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(sb.toString().toCharArray());
            pw.flush();
            bool = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
        }

        return bool;
    }
}
