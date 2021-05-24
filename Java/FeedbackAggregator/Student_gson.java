import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Student_gson {
    @SerializedName("name")
    private String name;

    @SerializedName("sfu_email")
    private String sfuEmail;

    @SerializedName("contribution")
    private Contribution_gson contribution;



    public String getSfuEmail() {
        return this.sfuEmail;
    }


    public Contribution_gson getContribution() {
        return this.contribution;
    }


}
