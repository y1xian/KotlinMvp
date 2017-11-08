package template;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yyx
 * @date 2017/11/6
 */
public class AndroidMvpAction extends AnAction {
    Project project;
    VirtualFile selectGroup;
    String pack;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        selectGroup = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String className = Messages.showInputDialog(project, "请输入类名称", "NewMvpGroup", Messages.getQuestionIcon());
        if (className == null || className.equals("")) {
            System.out.print("没有输入类名");
            return;
        }
        if (className.equals("base") || className.equals("BASE") || className.equals("Base")) {
            createMvpBase();
        } else {
            createClassMvp(className);
        }
        project.getBaseDir().refresh(false, true);
    }

    /**
     * 创建MVP的Base文件夹
     */
    private void createMvpBase() {
        String path = selectGroup.getPath() + "/base";

        File floder = new File(path);
        // if file doesnt exists, then create it
        if (floder.exists()) {
            return;
        }

        String packageName = path.substring(path.indexOf("java") + 5, path.length()).replace("/", ".");
        String presenter = readFile("IBasePresenter.txt").replace("&package&", packageName);
        String view = readFile("IBaseView.txt").replace("&package&", packageName);
        String presenterImpl = readFile("BasePresenter.txt").replace("&package&", packageName);
        String activity = readFile("BaseActivity.txt").replace("&package&", packageName);
        String fragment = readFile("BaseFragment.txt").replace("&package&", packageName);
        String commonUtils = readFile("CommonUtils.txt").replace("&package&", packageName);
        String exceptionHelper = readFile("ExceptionHelper.txt").replace("&package&", packageName);

        writetoFile(presenter, path, "IBasePresenter.kt");
        writetoFile(presenterImpl, path, "BasePresenter.kt");
        writetoFile(view, path, "IBaseView.kt");
        writetoFile(activity, path, "BaseActivity.kt");
        writetoFile(fragment, path, "BaseFragment.kt");
        writetoFile(commonUtils, path, "CommonUtils.kt");
        writetoFile(exceptionHelper, path, "ExceptionHelper.kt");

    }

    /**
     * 创建MVP架构
     */
    private void createClassMvp(String className) {
        boolean isFragment = className.endsWith("Fragment") || className.endsWith("fragment");
        if (className.endsWith("Fragment") || className.endsWith("fragment") || className.endsWith("Activity") || className.endsWith("activity")) {
            className = className.substring(0, className.length() - 8);
        }
        String path = selectGroup.getPath() + "/" + className.toLowerCase();
        String packageName = path.substring(path.indexOf("java") + 5, path.length()).replace("/", ".");
        String mvpPath = FileUtil.traverseFolder(path.substring(0, path.indexOf("java")));
        mvpPath = mvpPath.substring(mvpPath.indexOf("java") + 5, mvpPath.length()).replace("/", ".").replace("\\", ".");

        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        pack = path.substring(path.indexOf("java") + 5, path.indexOf("/view")).replace("/", ".");
        System.out.print(mvpPath + "---" + className + "----" + packageName);

        String contract = readFile("Contract.txt").replace("&package&", packageName).replace("&base&", mvpPath).replace("&Contract&", className + "Contract");
        String presenter = readFile("Presenter.txt").replace("&package&", packageName).replace("&base&", mvpPath).replace("&Contract&", className + "Contract").replace("&Presenter&", className + "Presenter");

        String layoutpath = project.getBasePath() + "/app/src/main/res/layout/";
        String layout = readFile("layout.txt");

        if (isFragment) {
            String fragment = readFile("Fragment.txt").replace("&package&", packageName).replace("&base&", mvpPath).replace("&Fragment&", className + "Fragment").replace("&Contract&", className + "Contract").replace("&Presenter&", className + "Presenter").replace("&pack&", pack).replace("&fragment_main&", "fragment_" + camel2Underline(className));
            writetoFile(fragment, path, className + "Fragment.kt");
            writetoFile(layout, layoutpath, "fragment_" + camel2Underline(className) + ".xml");
        } else {
            String activity = readFile("Activity.txt").replace("&package&", packageName).replace("&base&", mvpPath).replace("&Activity&", className + "Activity").replace("&Contract&", className + "Contract").replace("&Presenter&", className + "Presenter").replace("&pack&", pack).replace("&activity_main&", "activity_" + camel2Underline(className));
            writetoFile(activity, path, className + "Activity.kt");
            writetoFile(layout, layoutpath, "activity_" + camel2Underline(className) + ".xml");
        }
        writetoFile(contract, path, className + "Contract.kt");
        writetoFile(presenter, path, className + "Presenter.kt");


    }

    /**
     * 驼峰法转下划线
     *
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }


    private String readFile(String filename) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("code/" + filename);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (Exception e) {
        }
        return content;
    }

    private void writetoFile(String content, String filepath, String filename) {
        try {
            File floder = new File(filepath);
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                floder.mkdirs();
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }

}
