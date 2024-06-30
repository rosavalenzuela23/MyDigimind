package rodriguez.rosa.mydigimind3.util

import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import rodriguez.rosa.mydigimind3.ui.Task
import rodriguez.rosa.mydigimind3.ui.home.HomeFragment

class TaskDAO {

    companion object {

        data class actividad(
            var actividad: String = "Actividad sin nombre", var tiempo: String = "", var uid: String = "",
            var Mo: Boolean = false,
            var Tu: Boolean = false,
            var We: Boolean = false,
            var Th: Boolean = false,
            var Fr: Boolean = false,
            var Sa: Boolean = false,
            var Su: Boolean = false
        )
        {
            fun configurarDias(dias: ArrayList<String>) {
                for (dia in dias) {
                    when(dia) {
                        "Monday" -> {
                            this.Mo = true
                        }
                        "Tuesday" -> {
                            this.Tu = true
                        }
                        "Wednesday" -> {
                            this.We = true
                        }
                        "Thursday" -> {
                            this.Th = true
                        }
                        "Friday" -> {
                            this.Fr = true
                        }
                        "Saturday" -> {
                            this.Sa = true
                        }
                        "Sunday" -> {
                            this.Su = true
                        }
                    }
                }
            }
        }

        fun createNewTask( task: Task ) {
            val db = Firebase.firestore
            val auth = Firebase.auth

            task.uid = auth.currentUser!!.uid

            val activityToSave = actividad(task.title, task.time, auth.currentUser!!.uid)

            activityToSave.configurarDias(task.days)

            db.collection("actividades").add(activityToSave).addOnSuccessListener {
                Log.d("DOCUMENTOS", "SE AGREGO CON EXITO")
            }.addOnFailureListener { e ->
                    Log.w("DOCUMENTOS", "NO SE PUDO GUARDAR ${e.stackTrace}")
                }
        }

        fun getUidTasks(uid: String): ArrayList<Task> {
            val db = Firebase.firestore(Firebase.auth.app)

            fun transformData(query: QueryDocumentSnapshot): Task {

                val map: HashMap<String, String> = hashMapOf(
                    "mo" to "Monday",
                    "tu" to "Tuesday",
                    "we" to "Wednesday",
                    "th" to "Thursday",
                    "fr" to "Friday",
                    "sa" to "Saturday",
                    "su" to "Sunday"
                )

                val days: ArrayList<String> = ArrayList();

                for (day in map.keys) {
                    if (query.get(day) != null && query.get(day) == true) {
                        days.add( map.get(day) ?: "DIA NULO" )
                    }
                }

                return Task(
                    query.get("actividad").toString(),
                    days,
                    query.get("tiempo").toString(),
                    null
                )

            }

            val taskList: ArrayList<Task> = ArrayList()

            db.collection("actividades")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->

                    for (document in documents) {
                        val validDocument = transformData(document)
                        taskList.add(validDocument)
                    }

                    HomeFragment.tasks = taskList
                    HomeFragment.instance.notify(1)

                }.addOnFailureListener { e ->

                    Log.e("TRAER DOCUMENTOS", e.toString())

                }

            return taskList
        }

    }

}