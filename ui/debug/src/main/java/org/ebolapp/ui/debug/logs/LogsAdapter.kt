package org.ebolapp.ui.debug.logs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ebolapp.logging.entities.LogEntry
import org.ebolapp.ui.debug.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LogRecyclerAdapter(
    private val entries: List<LogEntry>
): RecyclerView.Adapter<LogRecyclerAdapter.LogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        return LogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ui_debug_log_row_item, parent, false))
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        holder.update(entries[position])
    }

    class LogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val text = itemView.findViewById<TextView>(R.id.log_text)

        @SuppressLint("SetTextI18n")
        internal fun update(logEntry: LogEntry) {
            text.text = "${logEntry.timestamp.formatMillisToDate()} - ${logEntry.tag} - ${logEntry.message}"
        }

        private fun Long.formatMillisToDate(): String {
            return Instant
                .ofEpochMilli(this)
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss:SSS"))
        }
    }
}