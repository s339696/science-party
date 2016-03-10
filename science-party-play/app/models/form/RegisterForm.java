package models.form;

import play.data.validation.Constraints;

/**
 * Form to parse payload of a register request
 */
public class RegisterForm {

    @Constraints.Required()
    private String firstname;

    @Constraints.Required()
    private String lastname;

    @Constraints.Required()
    private String birthday;

    @Constraints.Required()
    private String email;

    @Constraints.Required()
    private String password;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

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
