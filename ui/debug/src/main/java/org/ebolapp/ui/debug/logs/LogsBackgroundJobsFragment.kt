package org.ebolapp.ui.debug.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.ebolapp.logging.Logger
import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.logging.tags.Filter
import org.ebolapp.ui.debug.R
import org.ebolapp.ui.debug.databinding.UiDebugLogsFragmentBinding

class LogsBackgroundJobsFragment: Fragment() {

    private var binding: UiDebugLogsFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UiDebugLogsFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.title = "App Jobs Logs"
        setupLogRecyclerView()
        setupMenu()
    }

    private fun setupLogRecyclerView() {
        val adapter = LogRecyclerAdapter(loadLogEntries())
        binding?.logRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.logRecyclerView?.adapter = adapter
    }

    private fun loadLogEntries(): List<LogEntry> {
        return Logger
            .loadCurrentLog()
            .sortedByDescending { it.timestamp }
            .filter { it.tag in listOf(Filter.BACKGROUND_JOB_LOG) }
    }

    private fun setupMenu() {
        binding?.toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    Logger.deleteCurrentLog()
                    setupLogRecyclerView()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}