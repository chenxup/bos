package cn.itcast.bos.web.action;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


@ParentPackage(value = "json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {

    //上传文件的名称
    private File imgFile;
    private String imgFileFileName;

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }


    /**
     * 上传文件
     * @return
     * @throws Exception
     */
    @Action(value = "imageAction_upload", results = {@Result(type = "json")})
    public String uploadImage() throws Exception {
        //文件的绝对路径
        String realPath = ServletActionContext.getServletContext().getRealPath("/upload");
        //文件的相对路径
        String contextPath = ServletActionContext.getRequest().getContextPath() + "/upload/";

        //处理文件名
        String filename = UUID.randomUUID().toString().replace("-", "");
        filename = filename + imgFileFileName.substring(imgFileFileName.lastIndexOf("."));

        //保存文件
        FileUtils.copyFile(imgFile, new File(realPath, filename));

        //返回结果
        Map map = new HashMap();
        map.put("error", 0);
        map.put("url", contextPath + filename);

        this.push(map);
        return SUCCESS;
    }

    /**
     * 文件管理
     * 思路，copy file_manager_json
     * @return
     * @throws Exception
     */
    @Action(value = "imageAction_imageManaer", results = {@Result(type = "json")})
    public String imageManaer() throws Exception {
        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = ServletActionContext.getServletContext().getRealPath("/upload");
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        HttpServletRequest request = ServletActionContext.getRequest();
        String dirName = request.getParameter("dir");

        //根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";


        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);

        //遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }


        Map result = new HashMap();
        result.put("moveup_dir_path", moveupDirPath);
        result.put("current_dir_path", currentDirPath);
        result.put("current_url", currentUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        this.push(result);
        return SUCCESS;
    }
}
