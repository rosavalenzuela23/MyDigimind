package rodriguez.rosa.mydigimind3.ui.home

import android.content.Context
import android.icu.text.ListFormatter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rodriguez.rosa.mydigimind3.R
import rodriguez.rosa.mydigimind3.databinding.FragmentHomeBinding
import rodriguez.rosa.mydigimind3.ui.Task

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null


    private var adaptador: AdaptadorTareas? = null

    companion object {
        var tasks = ArrayList<Task>()
        var first = true
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (HomeFragment.first) {
            HomeFragment.first = false
            fillTask()
        }

        adaptador = AdaptadorTareas(HomeFragment.tasks, root.context)

        val gridView = binding.listaTareas as GridView

        gridView.adapter = adaptador

        return root
    }

    fun fillTask() {

        tasks.add(Task("Hello world", arrayListOf("Monday", "Sunday"), "13:00"))
        tasks.add(Task("Jugar persona 5", arrayListOf("Friday", "Sunday"), "1:00"))
        tasks.add(Task("Terminar tarea moviles", arrayListOf("Monday", "Tuesday", "Wednesday"), "4:00"))
        tasks.add(Task("Practicar deporte", arrayListOf("Monday", "Tuesday"), "15:00"))
        tasks.add(Task("Comer ensalada", arrayListOf("Wednesday", "Thursday"), "17:00"))
        tasks.add(Task("Jugar Persona 5 otra vez", arrayListOf("Monday", "Saturday"), "22:00"))
        tasks.add(Task("Hello world2", arrayListOf("Tuesday"), "10:00"))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class AdaptadorTareas: BaseAdapter {
        var tasks = ArrayList<Task>()
        var contexto: Context? = null

        constructor(tasks: ArrayList<Task>, contexto: Context) {
            this.tasks = tasks
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): Any {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val task = tasks[position]
            val inflator = LayoutInflater.from(contexto)
            val vista = inflator.inflate(R.layout.task_view, null)

            val textViewTitle: TextView = vista.findViewById(R.id.tv_title)
            val textViewDays: TextView = vista.findViewById(R.id.tv_days)
            val textViewTime: TextView = vista.findViewById(R.id.tv_time)

            textViewTitle.text = task.title
            textViewDays.text = task.days.toString()
            textViewTime.text = task.time

            return vista
        }

    }

}