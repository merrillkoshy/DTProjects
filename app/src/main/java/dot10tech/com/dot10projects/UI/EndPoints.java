package dot10tech.com.dot10projects.UI;

public class EndPoints {
    private static final String ROOT_URL = "https://dot10tech.com/mobileApp/scripts/";

    public static final String UPLOADAPIFILE = "ClientImageUploadApi.php?apicall=";
    public static final String UPDATEAPIFILE = "updateClient.php?apicall=";
    public static final String UPDATETEAMAPIFILE = "updateTeamAssignment.php?apicall=";
    public static final String NEWCLIENTDETAILFILE="addNewClient.php";
    public static final String NEWCLIENTLOGOFILE="addClientLogo.php";
    public static final String CHAT_DIR="chats/addNewChat.php";

    /*public static final String UPLOAD_URL = ROOT_URL + UPLOADAPIFILE+"uploadpic";*/

    public static final String ADDNEWCHAT=ROOT_URL+CHAT_DIR;
    public static final String ADDNEWCHATIMAGE=ROOT_URL+CHAT_DIR;
    public static final String NEW_CLIENT=ROOT_URL+NEWCLIENTDETAILFILE;
    public static final String NEW_CLIENTLOGO=ROOT_URL+NEWCLIENTLOGOFILE;
    public static  final String UPLOAD_URL="https://dot10tech.com";
    public static final String UPDATE_URL = ROOT_URL + UPDATEAPIFILE+"update";
    public static final String UPDATETEAM_URL = ROOT_URL + UPDATETEAMAPIFILE+"update";
    public static final String ADD_NEW_USER_CREDS = ROOT_URL + UPLOADAPIFILE+ "getpics";
}
