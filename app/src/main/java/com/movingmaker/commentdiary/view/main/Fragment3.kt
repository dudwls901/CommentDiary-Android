package com.movingmaker.commentdiary.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentCollectionABinding

class Fragment3: BaseFragment() {
    override val TAG: String = Fragment3::class.java.simpleName
    companion object{
        fun newInstance() : Fragment3{
            return Fragment3()
        }
    }

    private lateinit var binding: FragmentCollectionABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCollectionABinding.inflate(layoutInflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root

    }
}