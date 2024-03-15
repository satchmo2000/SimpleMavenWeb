package Utils;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.FileStore;
import java.nio.file.Files;

public class ServerLoadInfo {

    public static void main(String[] args) {
        try {
            // 获取CPU使用率
            double cpuLoad = getCpuLoad();
            System.out.println("CPU使用率: " + cpuLoad + "%");

            // 获取内存使用率
            double memoryLoad = getMemoryLoad();
            System.out.println("内存使用率: " + memoryLoad + "%");

            // 获取磁盘使用率
            double diskLoad = getDiskLoad();
            System.out.println("磁盘使用率: " + diskLoad + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getCpuLoad() {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = operatingSystemMXBean.getSystemCpuLoad() * 100;
        return Math.round(cpuLoad);
    }

    public static double getMemoryLoad() {
        OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double totalMemory = operatingSystemMXBean.getTotalPhysicalMemorySize();
        double freeMemory = operatingSystemMXBean.getFreePhysicalMemorySize();
        double memoryLoad = (totalMemory - freeMemory) / totalMemory;
        return Math.round(memoryLoad * 100);
    }

    public static double getDiskLoad() {
        try {
            FileStore fileStore = Files.getFileStore(new File("/").toPath());
            long totalSpace = fileStore.getTotalSpace();
            long usableSpace = fileStore.getUsableSpace();
            double diskLoad = (double) (totalSpace - usableSpace) / totalSpace;
            return Math.round(diskLoad * 100);
        }
        catch(Exception e){
            return -1;
        }
    }
}