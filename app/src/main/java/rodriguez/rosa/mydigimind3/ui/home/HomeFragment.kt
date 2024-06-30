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
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import rodriguez.rosa.mydigimind3.R
import rodriguez.rosa.mydigimind3.databinding.FragmentHomeBinding
import rodriguez.rosa.mydigimind3.ui.Task
import rodriguez.rosa.mydigimind3.util.TaskDAO

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var adaptador: AdaptadorTareas? = null

    companion object {
        var tasks = ArrayList<Task>()
        var first = true
        lateinit var instance: HomeFragment
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        instance = this

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (HomeFragment.first) {
            HomeFragment.first = false
            fillTask()
        }

        updateUI()

        return root
    }

    fun notify(Data: Any) {
        updateUI()
    }

    private fun updateUI() {

        adaptador = AdaptadorTareas(HomeFragment.tasks, this.requireContext())

        val gridView = binding.listaTareas as GridView

        gridView.adapter = adaptador

    }

    fun fillTask() {
        tasks = TaskDAO.getUidTasks(Firebase.auth.uid.toString())
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