package top.buukle.opensource.generator.plus.service;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface DataBackupService {

    void backup() throws IOException, GitAPIException, InterruptedException;
}
