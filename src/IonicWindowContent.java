import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.netty.buffer.ByteBufUtf8Writer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by jin on 2017/7/18.
 */
public class IonicWindowContent implements ToolWindowFactory {
    ToolWindow toolWindow;
    private JButton runButton;
    private JButton buildButton;
    private JTextArea textArea1;
    private JPanel panel1;
    private JScrollPane scrollpan;


    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel1, "", false);
        toolWindow.getContentManager().addContent(content);


        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            // 获取项目下的路径。
                            String basePath = project.getBasePath();
                            Runtime runtime = Runtime.getRuntime();
                            Process process = runtime.exec(" cmd /c ionic run android", null, new File("D:\\androidProject\\hytApp"));

                            InputStream in = process.getInputStream();
                            byte[] bytes = new byte[1024];
                            int length = -1;

                            while (-1 != (length = in.read(bytes))) {
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byteArrayOutputStream.write(bytes);
                                byteArrayOutputStream.flush();
                                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream);
                                outputStreamWriter.flush();
//                                textArea1.write(outputStreamWriter);
                                textArea1.append(new String(bytes));
                                textArea1.setCaretPosition(textArea1.getDocument().getLength());
                                Thread.sleep(50);
                            }

                            process.destroy();

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }.start();

            }
        });

    }


}
