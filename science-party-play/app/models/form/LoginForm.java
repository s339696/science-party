package models.form;

import play.data.validation.Constraints;

/**
 * Form to parse payload of a login request
 */
public class LoginForm {

    @Constraints.Required()
    private String email;

    @Constraints.Required()
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
