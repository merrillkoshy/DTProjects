package dot10tech.com.dot10projects.Chats

import dot10tech.com.dot10projects.UI.UploadObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ChatImageUploadInterface {
    @Multipart
    @POST("/mobileApp/scripts/chats/chatImagesupload.php")
    fun uploadFile(@Part file: MultipartBody.Part, @Part("name") name: RequestBody): Call<UploadObject>
}