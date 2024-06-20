package rodriguez.rosa.mydigimind3.ui.dashboard

import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import rodriguez.rosa.mydigimind3.R
import rodriguez.rosa.mydigimind3.databinding.FragmentDashboardBinding
import rodriguez.rosa.mydigimind3.ui.Task
import rodriguez.rosa.mydigimind3.ui.home.HomeFragment
import java.text.SimpleDateFormat
import java.util.logging.SimpleFormatter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val deadlineButton: TextView = root.findViewById(binding.deadline.id)
        deadlineButton.setOnClickListener {

            val calendar = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->

                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                deadlineButton.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }

            TimePickerDialog(root.context, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show()

        }

//        variables

        val buttonSave: Button = root.findViewById<Button>(R.id.doneButton)
        val et_titulo: EditText = root.findViewById<EditText>(R.id.name)
        val checkBoxMonday: CheckBox = root.findViewById(R.id.monday)
        val checkBoxTuesday: CheckBox = root.findViewById(R.id.tuesday)
        val checkBoxWednesday: CheckBox = root.findViewById(R.id.wednesday)
        val checkBoxThursday: CheckBox = root.findViewById(R.id.thursday)
        val checkBoxFriday: CheckBox = root.findViewById(R.id.friday)
        val checkBoxSaturday: CheckBox = root.findViewById(R.id.saturday)
        val checkBoxSunday: CheckBox = root.findViewById(R.id.sunday)

        buttonSave.setOnClickListener {

            var days = ArrayList<String>()

            if (checkBoxMonday.isChecked) {
                days.add("Monday")
            }
            if (checkBoxTuesday.isChecked) {
                days.add("Tuesday")
            }
            if (checkBoxWednesday.isChecked) {
                days.add("Wednesday")
            }
            if (checkBoxThursday.isChecked) {
                days.add("Thursday")
            }
            if (checkBoxFriday.isChecked) {
                days.add("Friday")
            }
            if (checkBoxSaturday.isChecked) {
                days.add("Saturday")
            }
            if (checkBoxSunday.isChecked) {
                days.add("Sunday")
            }

            val hour = deadlineButton.text.toString()
            val title = et_titulo.text.toString()

            var tarea = Task(title, days, hour)

            HomeFragment.tasks.add(tarea)
            Toast.makeText(root.context, "New task added", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}