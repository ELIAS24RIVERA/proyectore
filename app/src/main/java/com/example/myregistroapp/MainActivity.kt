import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var database: ContactDatabase
    private lateinit var adapter: ContactAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        recyclerView = findViewById(R.id.recyclerView)

        database = ContactDatabase.getDatabase(this)

        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val telefono = etTelefono.text.toString()

            if (nombre.isNotEmpty() && telefono.isNotEmpty()) {
                val contacto = Contact(nombre = nombre, telefono = telefono)
                database.contactDao().insertar(contacto)
                actualizarLista()
                etNombre.text.clear()
                etTelefono.text.clear()
                Toast.makeText(this, "Contacto agregado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        actualizarLista()
    }

    private fun actualizarLista() {
        val contactos = database.contactDao().obtenerTodos()
        adapter = ContactAdapter(contactos) { contacto ->
            database.contactDao().eliminar(contacto)
            actualizarLista()
        }
        recyclerView.adapter = adapter
    }
}
