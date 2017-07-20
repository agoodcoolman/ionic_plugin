import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import io.netty.buffer.ByteBufUtf8Writer;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by jin on 2017/7/18.
 */
public class IonicWindowContent implements ToolWindowFactory {
    private JButton runButton;
    private JButton buildButton;
    private JTextArea textArea1;
    private JPanel panel1;
    private JScrollPane scrollpan;
    private JButton uninstallButton;


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
                            Process process = runtime.exec(" cmd /c ionic run android", null, new File(basePath));

                            InputStream in = process.getInputStream();
                            byte[] bytes = new byte[1024];
                            int length = -1;

                            while (-1 != (length = in.read(bytes))) {

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

        uninstallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String basePath = project.getBasePath();
                try {
                    File configFile = new File(basePath + "\\config.xml");
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                    Document document = documentBuilder.parse(configFile);
                    NodeList widget = document.getElementsByTagName("widget");

                    String packageName = widget.item(0).getAttributes().getNamedItem("id").getNodeValue();

                    Runtime runtime = Runtime.getRuntime();
                    Process process = runtime.exec(" adb uninstall " +packageName );

                    InputStream in = process.getInputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;

                    while (-1 != (length = in.read(bytes))) {
                        new Thread(){
                            @Override
                            public void run() {
                                textArea1.append(new String(bytes));
                                textArea1.setCaretPosition(textArea1.getDocument().getLength());
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }.start();

                    }

                    process.destroy();

                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buildButton.addActionListener(new ActionListener() {
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
                            Process process = runtime.exec(" cmd /c ionic build android", null, new File(basePath));
                            InputStream in = process.getInputStream();
                            byte[] bytes = new byte[1024];
                            int length = -1;

                            while (-1 != (length = in.read(bytes))) {
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
