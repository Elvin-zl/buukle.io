package top.buukle.opensource.generator.plus.service.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileUtil {


    public static Boolean deleteFile(File file) {
        //判断文件不为null或文件目录存在
        if (file == null || !file.exists()) {
            log.debug("文件删除失败,请检查文件是否存在以及文件路径是否正确");
            return false;
        }
        //获取目录下子文件
        File[] files = file.listFiles();
        //遍历该目录下的文件对象
        for (File f : files) {
            //判断子目录是否存在子目录,如果是文件则删除
            if (f.isDirectory()) {
                //递归删除目录下的文件
                deleteFile(f);
            } else {
                //文件删除
                f.delete();
                //打印文件名
                log.debug("文件名：" + f.getName());
            }
        }
        //文件夹删除
        file.delete();
        log.debug("目录名：" + file.getName());
        return true;
    }

    public static void copyFile(File sourceFile,File targetFile){
        if(!sourceFile.canRead()){
            log.debug("源文件" + sourceFile.getAbsolutePath() + "不可读，无法复制！");
            return;
        }else{
            log.debug("开始复制文件" + sourceFile.getAbsolutePath() + "到" + targetFile.getAbsolutePath());
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;

            try{
                fis = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fis);
                fos = new FileOutputStream(targetFile);
                bos = new BufferedOutputStream(fos);
                int len = 0;
                while((len = bis.read()) != -1){
                    bos.write(len);
                }
                bos.flush();

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    if(fis != null){
                        fis.close();
                    }
                    if(bis != null){
                        bis.close();
                    }
                    if(fos != null){
                        fos.close();
                    }
                    if(bos != null){
                        bos.close();
                    }
                    log.debug("文件" + sourceFile.getAbsolutePath() + "复制到" + targetFile.getAbsolutePath() + "完成");
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyDirectory(String sourcePathString,String targetPathString){
        if(!new File(sourcePathString).canRead()){
            log.debug("源文件夹" + sourcePathString + "不可读，无法复制！");
            return;
        }else{
            (new File(targetPathString)).mkdirs();
            log.debug("开始复制文件夹" + sourcePathString + "到" + targetPathString);
            File[] files = new File(sourcePathString).listFiles();
            for(int i = 0; i < files.length; i++){
                if(files[i].isFile()){
                    copyFile(new File(sourcePathString + File.separator + files[i].getName()),new File(targetPathString + File.separator + files[i].getName()));
                }else if(files[i].isDirectory()){
                    copyDirectory(sourcePathString + File.separator + files[i].getName(),targetPathString + File.separator + files[i].getName());
                }
            }
            log.debug("复制文件夹" + sourcePathString + "到" + targetPathString + "结束");
        }
    }

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