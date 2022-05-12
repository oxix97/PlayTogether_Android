package com.playtogether_android.app.presentation.ui.thunder

import android.net.Uri
import android.os.Bundle
import com.bumptech.glide.Glide
import com.playtogether_android.app.R
import com.playtogether_android.app.databinding.ActivityApplyThunderDetailBinding
import com.playtogether_android.app.presentation.base.BaseActivity
import com.playtogether_android.app.presentation.ui.thunder.viewmodel.ThunderDetailViewModel
import com.playtogether_android.app.util.CustomDialog
import com.playtogether_android.app.util.shortToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.http.Url
import java.net.URL

class ApplyThunderDetailActivity :
    BaseActivity<ActivityApplyThunderDetailBinding>(R.layout.activity_apply_thunder_detail) {

    private val thunderDetailViewModel: ThunderDetailViewModel by viewModel()
    private lateinit var applicantListAdapter: ApplicantListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thunderId = intent.getIntExtra("thunderId", -1)
        initData(thunderId)
//        testData()
        initAdapter()
        binding.tvCancelApplication.setOnClickListener {
            showCancelDialog(thunderId)
        }
        setClickListener()
    }

    private fun initData(thunderId: Int) {
        binding.viewModel = thunderDetailViewModel
        thunderDetailViewModel.thunderDetail(thunderId)
        thunderDetailViewModel.thunderDetailMember(thunderId)
        thunderDetailViewModel.thunderDetailOrganizer(thunderId)

        thunderDetailViewModel.detailItemList.observe(this) {
            binding.detailData = it
            Glide
                .with(this)
                .load(it.image)
                .into(binding.ivApplythunderdetailImage)
        }

        thunderDetailViewModel.organizerInfo.observe(this) {
            binding.organizer = it
        }
    }

    private fun setClickListener() {
        binding.clThunderOpenerMessage.setOnClickListener {
//           쪽지 보내기로 이동
        }
    }

    private fun showCancelDialog(thunderId: Int) {
        val title = "신청을 취소할까요?"
        val dialog = CustomDialog(this, title)
        dialog.showChoiceDialog(R.layout.dialog_yes_no)
        dialog.setOnClickedListener(object : CustomDialog.ButtonClickListener {
            override fun onClicked(num: Int) {
                if (num == 1) {
                    thunderDetailViewModel.joinAndCancel(thunderId)
                    thunderDetailViewModel.isConfirm.observe(this@ApplyThunderDetailActivity) { success ->
                        if (success) {
                            showConfirmDialog()
                        } else {
                            shortToast("실패")
                        }
                    }
                }
            }
        })
    }

    private fun showConfirmDialog() {
        val title = "신청 취소되었습니다."
        val dialog = CustomDialog(this@ApplyThunderDetailActivity, title)
        dialog.showConfirmDialog(R.layout.dialog_check)
    }


    private fun testData() {
        with(binding) {
            tvApplythunderdetailOpenerName.text = "문수제비"
            tvApplythunderdetailTitle.text = "우리집에서 피자 먹기"
            tvApplythunderdetailDateContent.text = "2022.04.15"
            tvApplythunderdetailTimeContent.text = "18:00 ~"
            tvApplythunderdetailPlaceContent.text = "우리집"
            tvApplythunderdetailCategoryContent.text = "음식"
            tvApplythunderdetailDescription.text = "스크롤뷰 적용 후 스티링 더 길게 테스트 예정임다"
            tvCurrentApplicant.text = "2"
            tvMaxApplicant.text = "6"
        }
    }

    private fun initAdapter() {
        applicantListAdapter = ApplicantListAdapter()
        binding.rvThunderApplicantList.adapter = applicantListAdapter

        thunderDetailViewModel.memberList.observe(this) {
            applicantListAdapter.applicantList.addAll(it)
            applicantListAdapter.notifyDataSetChanged()
        }
//        applicantListAdapter.applicantList = listOf(
//            TempApplicantData.UserList("김세후니", 25, "ENFJ"),
//            TempApplicantData.UserList("권용민 바보", 26, "ESFJ"),
//            TempApplicantData.UserList("김세후니", 25, "ENFJ"),
//            TempApplicantData.UserList("권용민 바보", 26, "ESFJ"),
//            TempApplicantData.UserList("김세후니", 25, "ENFJ"),
//            TempApplicantData.UserList("권용민 바보", 26, "ESFJ"),
//            TempApplicantData.UserList("권용민 바보", 26, "ESFJ")
//        )

    }


}