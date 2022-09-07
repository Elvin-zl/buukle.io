package top.buukle.opensource.generator.plus.service.util;

import java.io.*;

public class FileUtil {
    public static String readFileAsStr(String path) {
        return readFileAsStr(new File(path));
    }

    public static String readFileAsStr(File file) {
        StringBuilder sb = new StringBuilder();
        String tempstr = null;
        try {
            if (!file.exists())
                throw new FileNotFoundException();
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "utf-8"));
            while ((tempstr = br.readLine()) != null) {
                sb.append(tempstr).append("\r\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static void writeStringToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        writer.write(content);
        writer.close();
        outputStreamWriter.close();
        fileOutputStream.close();
    }
    public static File writeStreamToFile(String filePath, InputStream tempStream) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        OutputStream os = new FileOutputStream(file);
        int bytesRead ;
        byte[] buffer = new byte[8192];
        while ((bytesRead = tempStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        tempStream.close();
        return file;
    }
}