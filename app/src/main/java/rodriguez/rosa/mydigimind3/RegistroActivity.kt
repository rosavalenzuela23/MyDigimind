package rodriguez.rosa.mydigimind3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegistroActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn_registrar: Button = findViewById(R.id.btn_registrar)

        btn_registrar.setOnClickListener{
            valida_registro()
        }

    }

    private fun valida_registro(){
        val et_correo: EditText = findViewById(R.id.et_correo_reg)
        val et_contra1: EditText = findViewById(R.id.et_contra_reg)
        val et_contra2: EditText = findViewById(R.id.et_contra2_reg)

        val correo: String = et_correo.text.toString()
        val contra1: String = et_contra1.text.toString()
        val contra2: String = et_contra2.text.toString()

        val MIN_PASSWORD_LENGTH = 6

        if(correo.isBlank() || contra1.isBlank() || contra2.isBlank()) {
            Toast.makeText(
                this, "Ingresar datos",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if(contra1 != contra2) {
            Toast.makeText(
                this, "Las contraseña no coinciden",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if(contra1.length < MIN_PASSWORD_LENGTH) {
            Toast.makeText(
                this, "La contraseña tiene que ser mayor a ${MIN_PASSWORD_LENGTH - 1 } caracteres",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        registrarFirebase(correo, contra1)
    }

    private fun registrarFirebase(email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    Toast.makeText(baseContext, "${user!!.email} se ha creado correctamente",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(user)
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}