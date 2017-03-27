package com.chinaamc.pluto.backup.store;

import com.chinaamc.pluto.backup.Backup;
import com.chinaamc.pluto.backup.BackupCodec;
import com.chinaamc.pluto.backup.BackupEnvironment;
import com.chinaamc.pluto.util.Configuration;
import com.chinaamc.pluto.util.CuratorZookeeperClient;
import com.chinaamc.pluto.util.ZooKeeperClient;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZookeeperBackupStore extends AbstractBackupStore {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final BackupCodec backupCodec;

    private final ZooKeeperClient client;

    public ZookeeperBackupStore(BackupCodec backupCodec, String connectionString) {
        this.backupCodec = backupCodec;
        this.client = new CuratorZookeeperClient(connectionString);
    }

    @Override
    public long append(Backup backup) {
        return backup.getTraceId();
    }

    @Override
    public synchronized void save(List<Backup> backups) {
        byte[] bytes = backupCodec.writeBackups(backups);
        BackupEnvironment environment = Configuration.getInstance().getBackupEnvironment();
        String path = environment.getBackupLog();
        createPathIfNeeded(path);
        client.setData(path, bytes);
    }

    private void createPathIfNeeded(String path) {
        client.create(path, false);
    }


    @Override
    public List<Backup> getBackups() {
        BackupEnvironment environment = Configuration.getInstance().getBackupEnvironment();
        String path = environment.getBackupLog();
        createPathIfNeeded(path);
        byte[] bytes = client.getData(path);
        List<Backup> backups = backupCodec.readBackups(bytes);
        if (CollectionUtils.isEmpty(backups)) {
            return null;
        } else {
            return backups;
        }
    }

    @Override
    public void removeBackup(Backup backup) {
    }

}
