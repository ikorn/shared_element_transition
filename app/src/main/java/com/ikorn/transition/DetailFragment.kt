package com.ikorn.transition


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.databinding.DataBindingUtil
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.ikorn.transition.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        postponeEnterTransition()
        sharedElementEnterTransition = getTransition()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("title")
        binding.title.text = title
        startPostponedEnterTransition()
    }

    private fun getTransition(): TransitionSet {
        val animationDuration = 500L
        val set = TransitionSet()
        set.addTransition(ChangeBounds().apply {
            duration = animationDuration
            interpolator = AccelerateOvershootInterpolator(0.5f, 1.2f)
            addListener(object : Transition.TransitionListener {
                override fun onTransitionResume(transition: Transition) = Unit
                override fun onTransitionPause(transition: Transition) = Unit
                override fun onTransitionCancel(transition: Transition) = Unit
                override fun onTransitionStart(transition: Transition) = Unit
                override fun onTransitionEnd(transition: Transition) {
                    removeListener(this)
                }
            })
        })
        set.addTransition(ChangeTransform().apply { duration = animationDuration })
        return set
    }
}

class AccelerateOvershootInterpolator(factor: Float, tension: Float) : Interpolator {
    private val accelerate: AccelerateInterpolator = AccelerateInterpolator(factor)
    private val overshoot: OvershootInterpolator = OvershootInterpolator(tension)

    override fun getInterpolation(input: Float): Float {
        return overshoot.getInterpolation(accelerate.getInterpolation(input))
    }

}