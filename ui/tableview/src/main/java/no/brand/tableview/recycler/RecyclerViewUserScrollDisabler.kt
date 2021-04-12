package no.brand.tableview.recycler

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewUserScrollDisabler : RecyclerView.OnItemTouchListener {

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}