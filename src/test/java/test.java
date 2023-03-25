import cn.hutool.core.convert.Convert;
import com.iv.ersr.common.entity.enums.MaterialQualityEnum;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author MXY
 * @Description TODO
 * @date 2022/5/18 10:55
 */
public class test {
    /**
     *SeleniumDownloader功能测试
     *该代码使用的是chrome浏览器,chromedriver百度自行下载
     *chrome和chromedriver版本保持严格匹配,我因为用错版本一直报错
     */
    @Test
    public void test(){
        String gamePicture = "https://store.nintendo.com.hk/media/catalog/product/cache/fbd142b527b990ca39daf426d49f9eed/h/a/hac_herobanner_rectangle_av5j_2.jpg";
        MultipartFile multipartFile = urlToMultipartFile(gamePicture, "游戏1-1");
        System.out.println(multipartFile);
    }

    @Test
    public void test2(){
        MaterialQualityEnum materialQualityEnum = Convert.toEnum(MaterialQualityEnum.class, 3);
        System.out.println(materialQualityEnum);
    }

    public MultipartFile fileToMultipartFile(File file) {
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(16, null);
        FileItem item = diskFileItemFactory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            int len = 8192;
            while ((bytesRead = fis.read(buffer, 0, len)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonsMultipartFile(item);
    }

    /**
     * url转MultipartFile
     */
    public MultipartFile urlToMultipartFile(String url, String fileName) {
        File file = null;
        MultipartFile multipartFile = null;
        try {
            HttpURLConnection httpUrl = (HttpURLConnection) new URL(url).openConnection();
            httpUrl.connect();
            file = inputStreamToFile(httpUrl.getInputStream(),fileName);
            multipartFile = fileToMultipartFile(file);
            httpUrl.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    public File inputStreamToFile(InputStream ins, String name) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir") + File.separator + name);
        OutputStream os = new FileOutputStream(file);
        int len = 8192;
        byte[] buffer = new byte[len];
        int bytesRead;
        while ((bytesRead = ins.read(buffer, 0, len)) != -1){
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }
}
