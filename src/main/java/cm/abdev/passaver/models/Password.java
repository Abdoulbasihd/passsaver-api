package cm.abdev.passaver.models;

import javax.persistence.*;

@Entity
@Table(name = "passwords")
public class Password {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column (name = "application_or_site")
    private String  appName;

    @Column (name = "password_encoded")
    private String passCoded;

    @Column(name = "description")
    private String description;

    //TODO adding user...

    public Password() {
        //default constructor
    }

    public Password(String appName, String passCoded, String description) {
        this.appName = appName;
        this.passCoded = passCoded;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPassCoded() {
        return passCoded;
    }

    public void setPassCoded(String passCoded) {
        this.passCoded = passCoded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString(){
        return "Password [id="+id+", app="+appName+", password encoded value = "+passCoded+", description="+description+"]";
    }

}
