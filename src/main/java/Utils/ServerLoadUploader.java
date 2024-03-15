package Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerLoadUploader implements Runnable {

    private final String targetUrl;

    public ServerLoadUploader(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public void run() {
        try {
            // 获取服务器负载
            double cpuLoad = ServerLoadInfo.getCpuLoad();
            double memoryLoad = ServerLoadInfo.getMemoryLoad();
            double diskLoad = ServerLoadInfo.getDiskLoad();

            // 将服务器负载上传到指定接口
            uploadServerLoad(cpuLoad, memoryLoad, diskLoad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadServerLoad(double cpuLoad, double memoryLoad, double diskLoad) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 构建请求体
        String requestBody = String.format("cpu=%f&memory=%f&disk=%f", cpuLoad, memoryLoad, diskLoad);

        // 发送请求
        connection.getOutputStream().write(requestBody.getBytes());

        // 获取响应
        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        // 输出响应
        System.out.println("Server response: " + response.toString());
    }

    public static void main(String[] args) {
        String targetUrl = "http://example.com/uploadServerLoad";
        ServerLoadUploader serverLoadUploader = new ServerLoadUploader(targetUrl);

        // 创建定时任务
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(serverLoadUploader, 0, 5, TimeUnit.MINUTES);
    }
}