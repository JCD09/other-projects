package WebServerApplication.Messages;


// NOTES: getters and setters mus be gotten right if one wats to generate the response
public class AboutMessage{
    String authorName = "Misha Chavarha";
    String appName = "Reactive";

    public AboutMessage(){}

    @Override
    public String toString() {
        return "AboutMessage{" +
                "authorName='" + authorName + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
