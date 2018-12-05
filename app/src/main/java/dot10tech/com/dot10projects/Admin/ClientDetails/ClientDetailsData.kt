package dot10tech.com.dot10projects.Admin.ClientDetails

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import dot10tech.com.dot10projects.Admin.AddNewProjectasAdmin
import dot10tech.com.dot10projects.Admin.EditProjectasAdmin
import dot10tech.com.dot10projects.Admin.OngoingProjects
import okhttp3.*
import java.io.IOException

class ClientDetailsData():AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val option=intent.getIntExtra("option",0)
        val clientName=intent.getStringArrayExtra("cN")
        val clientImageUrl=intent.getStringArrayExtra("ciU")


        when(option){
                1->{val intent= Intent(this, AddNewProjectasAdmin::class.java)
                        intent.putExtra("cN", clientName)
                        intent.putExtra("ciU", clientImageUrl)
                        startActivity(intent)
                    }
                2->{val intent= Intent(this, EditProjectasAdmin::class.java)
                    intent.putExtra("cN", clientName)
                    intent.putExtra("ciU", clientImageUrl)
                    startActivity(intent)
                }
                3->{val intent= Intent(this, OngoingProjects::class.java)

                    intent.putExtra("cN", clientName)
                    intent.putExtra("ciU", clientImageUrl)
                    startActivity(intent)
                }
                else->{
                    Toast.makeText(this,"Parse error",Toast.LENGTH_LONG).show()
                    finish()
            }
        }
    }






}

