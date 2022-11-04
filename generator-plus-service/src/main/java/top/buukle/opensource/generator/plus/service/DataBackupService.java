package top.buukle.opensource.generator.plus.service;

import top.buukle.opensource.generator.plus.utils.SystemUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface DataBackupService {

    void backup() throws IOException;
}
