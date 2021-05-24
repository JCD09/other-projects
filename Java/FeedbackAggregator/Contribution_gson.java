import com.google.gson.annotations.SerializedName;

public class Contribution_gson {

    @SerializedName("score")
    private double score;

    @SerializedName("comment")
    private String comment;

    public double getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

}
