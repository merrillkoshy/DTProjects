package dot10tech.com.dot10projects.UI;

public class EndPoints {
    private static final String ROOT_URL = "https://dot10tech.com/mobileApp/scripts/";

    public static final String UPLOADAPIFILE = "ClientImageUploadApi.php?apicall=";
    public static final String UPDATEAPIFILE = "updateClient.php?apicall=";

    public static final String UPLOAD_URL = ROOT_URL + UPLOADAPIFILE+"uploadpic";
    public static final String UPDATE_URL = ROOT_URL + UPDATEAPIFILE+"update";
    public static final String ADD_NEW_USER_CREDS = ROOT_URL + UPLOADAPIFILE+ "getpics";
}
