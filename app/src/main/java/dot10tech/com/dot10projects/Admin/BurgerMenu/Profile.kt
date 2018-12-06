package dot10tech.com.dot10projects.Admin.BurgerMenu

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import dot10tech.com.dot10projects.R
import kotlinx.android.synthetic.main.profile.*

class Profile:AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        val pp=intent.getStringExtra("pp")
        val fn=intent.getStringExtra("fn")
        val ln=intent.getStringExtra("ln")

        title=""

        firstname.text=fn
        lastname.text=ln

        Picasso.get().load(pp)
            .fit().placeholder(R.drawable.progress_animation)
            .into(profile_image)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}