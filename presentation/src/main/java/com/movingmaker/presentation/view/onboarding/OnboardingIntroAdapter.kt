package com.movingmaker.presentation.view.onboarding

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.presentation.R
import com.movingmaker.presentation.databinding.VpPageOnboardingIntroBinding

class OnboardingIntroAdapter(private val pageList: List<String>, private val context: Context) :
    RecyclerView.Adapter<OnboardingIntroAdapter.OnBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder =
        OnBoardViewHolder(
            VpPageOnboardingIntroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        val imgList = listOf(
            R.drawable.img_onboarding_1,
            R.drawable.img_onboarding_2,
            R.drawable.img_onboarding_3
        )
        holder.bind(pageList[position], imgList[position], context)
    }

    class OnBoardViewHolder(private val binding: VpPageOnboardingIntroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(page: String, imageId: Int, context: Context) {
            binding.onboardingTextView.text = page
            binding.root.background = context.getDrawable(imageId)
        }
    }

    override fun getItemCount() = pageList.size
}