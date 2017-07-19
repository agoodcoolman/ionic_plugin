import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindowManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by jin on 2017/7/14.
 */
public class IonicRunAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            // 获取项目下的路径。
            Project project = e.getProject();
            String basePath = project.getBasePath();
            System.out.println(basePath);
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(" cmd /c ionic -v", null, new File("D:\\androidProject\\hytApp"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
