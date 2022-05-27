package mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import mx.edu.ittepic.daar.ladm_u5_centrohitorico_losinvencibles.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val autenticacion = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseAuth.getInstance().currentUser != null) {
            //SESION ACTIVA
            invocarOtraVentana()
        }

        binding.inscribir.setOnClickListener {
            autenticacion.createUserWithEmailAndPassword(binding.correo.text.toString(), binding.txtPass.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        binding.correo.setText("")
                        binding.txtPass.setText("")
                        Toast.makeText(this, "SE CREO", Toast.LENGTH_LONG).show()
                        autenticacion.signOut()
                    }else{
                        AlertDialog.Builder(this)
                            .setMessage("ERROR NO SE CONTRUYO")
                            .setTitle("ATENCION")
                            .show()
                    }
                }
        }

        binding.autenticar.setOnClickListener {
            val dialogo = ProgressDialog(this)
            dialogo.setMessage("AUTENTICANDO USUARIO/CONTRASEÑA")
            dialogo.setCancelable(false)
            dialogo.show()

            autenticacion.signInWithEmailAndPassword(
                binding.correo.text.toString(),
                binding.txtPass.text.toString()
            ).addOnCompleteListener {
                dialogo.dismiss()
                if(it.isSuccessful){
                    invocarOtraVentana()
                    return@addOnCompleteListener
                }else{
                    AlertDialog.Builder(this)
                        .setMessage("NO COINCIDE CON CORREO/CONTRASEÑA")
                        .setTitle("ERROR")
                        .show()
                }
            }
        }

        binding.recuperar.setOnClickListener {
            val dialogo = ProgressDialog(this)
            dialogo.setCancelable(false)
            dialogo.setMessage("SOLICITANDO RECUPERACION CONTRASEÑA")
            dialogo.show()

            autenticacion.sendPasswordResetEmail(
                binding.correo.text.toString()
            ).addOnSuccessListener {
                dialogo.dismiss()
                AlertDialog.Builder(this)
                    .setMessage("SE ENVIO CORREO, REVISA BANDEJA")
                    .show()
            }
        }
    }

    private  fun invocarOtraVentana() {
        val otraVentana = Intent(this,MainActivity::class.java)
        startActivity(otraVentana)
        finish()
    }

    override fun onResume() {
        super.onResume()
        supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        supportActionBar!!.show()
    }
}