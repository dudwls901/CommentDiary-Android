package com.movingmaker.commentdiary.view.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.movingmaker.commentdiary.databinding.VpPageOnboardingIntroBinding
import com.movingmaker.commentdiary.viewmodel.onboarding.IntroViewModel

class OnboardingIntroAdapter(private val pageList: List<String>) : RecyclerView.Adapter<OnboardingIntroAdapter.OnBoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardViewHolder =
        OnBoardViewHolder(
            VpPageOnboardingIntroBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        )


    override fun onBindViewHolder(holder: OnBoardViewHolder, position: Int) {
        holder.bind(pageList[position])
    }

    class OnBoardViewHolder(private val binding: VpPageOnboardingIntroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(page: String){
            binding.onboardingTextView.text = page
        }
    }

    override fun getItemCount() = pageList.size
}