package com.movingmaker.commentdiary.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.movingmaker.commentdiary.base.BaseFragment
import com.movingmaker.commentdiary.databinding.FragmentMyABinding

class Fragment4: BaseFragment() {
    override val TAG: String = Fragment4::class.java.simpleName
    companion object{
        fun newInstance() : Fragment4{
            return Fragment4()
        }
    }

    private lateinit var binding: FragmentMyABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyABinding.inflate(layoutInflater)
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