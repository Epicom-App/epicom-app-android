package org.ebolapp.ui.onboarding

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import org.ebolapp.presentation.onboarding.OnboardingViewModel
import org.ebolapp.ui.common.extensions.observe
import org.ebolapp.ui.onboarding.databinding.UiOnboardingFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.get


class OnboardingFragment : Fragment() {
    private var showDoneButton = false

    private var layoutmediator: TabLayoutMediator? = null
    private var layoutmediatorInverted: TabLayoutMediator? = null
    private val viewModel by viewModel<OnboardingViewModel>()

    private var binding: UiOnboardingFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return UiOnboardingFragmentBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doBinding(binding ?: return)
        viewModel.userSawOnboarding()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.doneEntry -> {
            viewModel.finish()
            true
        }
        R.id.nextEntry -> {
            viewModel.nextScreen()
            true
        }
        android.R.id.home -> {
            viewModel.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.ui_onboarding_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.doneEntry)?.isVisible = showDoneButton
        menu.findItem(R.id.nextEntry)?.isVisible = !showDoneButton
    }

    private fun doBinding(binding: UiOnboardingFragmentBinding) {
        binding.viewPager.adapter =
            OnboardingRecyclerViewAdapter(viewModel, get()).also { adapter ->
                observe(viewModel.availableScreens) {
                    adapter.submitList(it)
                }
            }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.selectPage(position)
            }
        })

        (requireActivity() as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(binding.toolbar)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            observe(viewModel.isOptional) {
                if (it) {
                    activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.toolbar.setNavigationIcon(R.drawable.ui_common_ic_close)
                } else {
                    activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
                }
            }
        }


        observe(viewModel.selectedPos) { selectedPos ->
            showDoneButton = selectedPos + 1 >= viewModel.availableScreens.value.size
            (activity as? AppCompatActivity)?.invalidateOptionsMenu()
            if (binding.viewPager.currentItem != selectedPos) {
                binding.viewPager.currentItem = selectedPos
            }

            binding.tabLayout.isVisible = selectedPos >= 2
            binding.tabLayoutInverted.isVisible = !binding.tabLayout.isVisible
        }

        layoutmediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { _, _ -> }.also { it.attach() }
        layoutmediatorInverted = TabLayoutMediator(
            binding.tabLayoutInverted,
            binding.viewPager
        ) { _, _ -> }.also { it.attach() }
    }

    override fun onDestroyView() {
        layoutmediator?.detach()
        layoutmediator = null
        layoutmediatorInverted?.detach()
        layoutmediatorInverted = null
        binding = null
        super.onDestroyView()
    }

}