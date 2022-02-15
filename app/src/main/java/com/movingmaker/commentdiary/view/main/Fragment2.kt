package com.movingmaker.commentdiary.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.movingmaker.commentdiary.databinding.FragmentMyABindingImpl
import com.movingmaker.commentdiary.databinding.FragmentReceiveddiaryABinding

class Fragment2: Fragment() {

    companion object{
        const val TAG: String = "로그"

        fun newInstance() : Fragment2{
            return Fragment2()
        }
    }

    private lateinit var binding: FragmentReceiveddiaryABinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentReceiveddiaryABinding.inflate(layoutInflater)
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