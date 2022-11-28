package com.movingmaker.commentdiary.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment<VB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.i("onViewCreated")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView")
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Timber.i("onViewStateRestored: ")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause ")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Timber.i("onSaveInstanceState: ")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
        Timber.i("onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
    }

}