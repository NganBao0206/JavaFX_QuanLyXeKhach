package com.ou.oubus;

import com.ou.service.AuthenticationResult;
import com.ou.service.CurrentUser;
import com.ou.service.UserService;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class Login {
    @FXML private TextField txtUsername;
    @FXML private TextField txtPassword;
    
    public void signIn (ActionEvent e) throws SQLException, IOException
    {
        Alert alert;
        if(txtUsername.getText().isBlank())
        {
            alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa nhập username", ButtonType.CLOSE);
            alert.show();
            return;
        }
        if (txtPassword.getText().isBlank())
        {
            alert = new Alert(Alert.AlertType.WARNING, "Bạn chưa nhập password", ButtonType.CLOSE);
            alert.show();
            return;
        }
        UserService us = new UserService();
        AuthenticationResult result = us.authenticatedUser(txtUsername.getText(), txtPassword.getText());
        if (result.isSuccess())
        {
            if ("admin".equals(result.getUserRole()))
            {
               App.setRoot("admin");
            }
            else if ("staff".equals(result.getUserRole()))
            {
                alert = new Alert(Alert.AlertType.INFORMATION, "Đăng nhập thành công bạn là " + result.getUserRole() + "  " + CurrentUser.getInstance().getUser().getName(), ButtonType.OK);
                alert.show();
                App.setRoot("employee");
            }
        }
        else
        {
            alert = new Alert(Alert.AlertType.ERROR, result.getFailureReason(), ButtonType.OK);
            alert.show();
        }
    }
}
