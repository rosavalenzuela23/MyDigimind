package rodriguez.rosa.mydigimind3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import rodriguez.rosa.mydigimind3.ui.Task
import rodriguez.rosa.mydigimind3.ui.home.HomeFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onStart() {
        super.onStart()

        editTextPassword = findViewById(R.id.et_contra)
        editTextEmail = findViewById(R.id.et_correo)

        if (Firebase.auth.currentUser != null) {
            Firebase.auth.signOut()

            HomeFragment.tasks = ArrayList<Task>()
            HomeFragment.first = true


            editTextEmail.setText("")
            editTextPassword.setText("")

            Toast.makeText(this, "Se cerro sesion", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_registrarse: Button = findViewById(R.id.btn_registrarse)
        val btn_contra: TextView = findViewById(R.id.tv_olvidasteContra)
        val btn_ingresar: Button = findViewById(R.id.btn_ingresar)

        btn_registrarse.setOnClickListener{
            val intent: Intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        btn_contra.setOnClickListener{
            val intent: Intent = Intent(this, ContrasenaActivity::class.java)
            startActivity(intent)
        }

        val et_correo: EditText = findViewById(R.id.et_correo)
        val et_contra: EditText = findViewById(R.id.et_contra)

        btn_ingresar.setOnClickListener{
            val correo: String = et_correo.text.toString()
            val contra: String = et_contra.text.toString()
            if (areFieldsValid(correo, contra)) {
                login(correo, contra)
            }
        }

    }

    private fun areFieldsValid(correo: String, contra: String): Boolean {
        val MIN_PASSWORD_LENGTH = 6

        return if(correo.isBlank() || contra.isBlank()) {
            false
        } else if(contra.length < MIN_PASSWORD_LENGTH) {
            false
        } else {
            true
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val intent: Intent = Intent(this, MainActivity::class.java)
                    this.startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(
                        baseContext,
                        "Autenticación fallida.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}