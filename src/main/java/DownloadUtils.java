import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtils {
    // 目标链接字符串
    private String wordUrl;
    // 单词字符串
    private String wordString;
    // 目标文件的格式
    private String targetType;
    // 存放文件路径
    private File rootDir;

    public DownloadUtils(String wordUrl, String wordString, String targetType, File rootDir) {
        super();
        this.wordUrl = wordUrl;
        this.wordString = wordString;
        this.targetType = targetType;
        this.rootDir = rootDir;
    }

    public DownloadUtils(String wordUrl, String wordString, String targetType, String rootDir) {
        super();
        this.wordUrl = wordUrl;
        this.wordString = wordString;
        this.targetType = targetType;
        this.rootDir = new File(rootDir);
    }

    public DownloadUtils() {
        super();
    }

    /**
     * 开始下载
     *
     * @throws Exception
     */
    public void httpDownload() throws Exception {
        validate();
        final String urls = wordUrl;
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) new URL(urls)
                .openConnection();
        // 开启链接
        urlConnection.connect();
        InputStream inputStream = urlConnection.getInputStream();

        if(!rootDir.exists()){
            rootDir.mkdirs();
        }

        File temp = new File(rootDir,
                "Chadi-" + wordString + "." + targetType);

        //if (!temp.exists()) {
            temp.createNewFile();
        //}
        FileOutputStream fileOutputStream = new FileOutputStream(temp, true);
        int tem;
        while (-1 != (tem = inputStream.read())) {
            fileOutputStream.write(tem);
            fileOutputStream.flush();
        }
        fileOutputStream.close();
        inputStream.close();
    }

    private void validate() throws Exception {
        if (wordUrl == null || wordUrl.equals("")) {
            throw new Exception("下载路径不能为空!");
        }
        if (null == rootDir ) {
            throw new Exception("目标文件夹不存在!");
        }

    }

}
