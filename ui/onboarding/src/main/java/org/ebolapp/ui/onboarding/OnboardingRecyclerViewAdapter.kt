package org.ebolapp.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.ebolapp.presentation.onboarding.OnboardingViewModel
import org.ebolapp.presentation.onboarding.OnboardingViewModel.ScreenState
import org.ebolapp.ui.onboarding.OnboardingRecyclerViewAdapter.ViewHolder
import org.ebolapp.ui.onboarding.databinding.UiOnboardingScreen1Binding
import org.ebolapp.ui.onboarding.databinding.UiOnboardingScreen2Binding
import org.ebolapp.ui.onboarding.databinding.UiOnboardingScreen4Binding
import org.ebolapp.ui.webview.WebViewActivity
import org.ebolapp.features.staticPages.network.StaticPages
import org.ebolapp.ui.legend.LegendView
import org.ebolapp.ui.legend.databinding.UiLegendViewBinding

class OnboardingRecyclerViewAdapter(
    private val onboardindViewModel: OnboardingViewModel,
    private val staticPages: StaticPages,
) : ListAdapter<ScreenState, ViewHolder>(DiffCallback) {


    sealed class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        data class Screen1VH(val view: OnboardingView<UiOnboardingScreen1Binding>) : ViewHolder(view.binding.root)
        data class Screen2VH(val view: OnboardingView<UiOnboardingScreen2Binding>) : ViewHolder(view.binding.root)
        data class Screen3VH(val view: LegendView) : ViewHolder(view.binding.root)
        data class Screen4VH(val view: OnboardingView<UiOnboardingScreen4Binding>) : ViewHolder(view.binding.root)
    }

    object DiffCallback : DiffUtil.ItemCallback<ScreenState>() {
        @SuppressWarnings("DiffUtilEquals")
        override fun areItemsTheSame(
            oldItem: ScreenState,
            newItem: ScreenState
        ) = oldItem == newItem

        @SuppressWarnings("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: ScreenState,
            newItem: ScreenState
        ) = oldItem == newItem
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        ScreenState.Screen1 -> R.layout.ui_onboarding_screen1
        ScreenState.Screen2 -> R.layout.ui_onboarding_screen2
        ScreenState.Screen3 -> R.layout.ui_legend_view
        is ScreenState.Screen4 -> R.layout.ui_onboarding_screen4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.ui_onboarding_screen1 -> ViewHolder.Screen1VH(
                OnboardingView({
                    UiOnboardingScreen1Binding.inflate(inflater, parent, false)
                }, parent.context)
            )
            R.layout.ui_onboarding_screen2 -> ViewHolder.Screen2VH(
                OnboardingView({
                    UiOnboardingScreen2Binding.inflate(inflater, parent, false)
                }, parent.context)
            )
            R.layout.ui_legend_view -> ViewHolder.Screen3VH(
                LegendView(parent.context, attachToParent = false)
            )
            R.layout.ui_onboarding_screen4 -> ViewHolder.Screen4VH(
                OnboardingView({
                    UiOnboardingScreen4Binding.inflate(inflater, parent, false)
                }, parent.context)
            )
            else -> error("unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is ViewHolder.Screen1VH && item is ScreenState.Screen1 -> {
            }
            holder is ViewHolder.Screen2VH && item is ScreenState.Screen2 -> {
            }
            holder is ViewHolder.Screen3VH && item is ScreenState.Screen3 -> {
            }
            holder is ViewHolder.Screen4VH && item is ScreenState.Screen4 -> {
                holder.view.binding.locationPermissionButton.setOnClickListener {
                    onboardindViewModel.requestLocationPermission()
                }
                holder.view.binding.textViewPrivacyLink.setOnClickListener {
                    val context = holder.view.binding.root.context
                    WebViewActivity.start(
                        context = context,
                        dependencies = WebViewActivity.Dependencies(
                            url = staticPages.dataPrivacyURL,
                            title = context.resources.getString(R.string.settings_data_privacy),
                            isStatic = true
                        )
                    )
                }
                holder.view.binding.locationPermissionButton.isVisible = !item.hasPermission
                holder.view.binding.locationPermissionInfo.isVisible = item.hasPermission
            }
            else -> error("unknown holder to view item combination")
        }
    }
}