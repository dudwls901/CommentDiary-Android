package com.movingmaker.commentdiary.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.movingmaker.commentdiary.databinding.FragmentMydiaryABinding

class Fragment3: Fragment() {

    companion object{
        const val TAG: String = "로그"

        fun newInstance() : Fragment3{
            return Fragment3()
        }
    }

    private lateinit var binding: FragmentMydiaryABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMydiaryABinding.inflate(layoutInflater)
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