package controller;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Richard on 22.02.2016.
 */
public class User {

    String firstName;
    String lastName;
    int id;
    String email;
    Date birthDate;
    String imageUrl;
    Boolean locked;
    String password;

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", imageUrl='" + imageUrl + '\'' +
                ", locked=" + locked +
                ", password='" + password + '\'' +
                '}';
    }

    public User(int id, String firstName, String lastName, String email, String password, Date birthDate, String imageUrl){

        this.id=id;
        this.firstName=firstName;
        this.lastName=lastName;
        this.email=email;
        this.password=password;
        this.birthDate=birthDate;

        this.imageUrl=imageUrl;
        this.locked=false;

    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
        return sdf.format(birthDate).toString();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Boolean getLocked() {
        return locked;
    }

    public String getPassword() {
        return password;
    }


    public static void main(String[] args){
        User bastian=new User(1,"Bastian", "Wolz","B.W@email.de", "1234", new Date(), null);

        System.out.println(bastian.toString());
    }
}
