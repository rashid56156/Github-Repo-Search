package com.github.search.util

import android.app.Activity
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.github.search.GithubApplication
import com.github.search.R
import com.tapadoo.alerter.Alerter

object AlertView {
    fun showErrorMsg(activity: Activity, messageText: CharSequence) {
        Alerter.create(activity).setText(messageText).setContentGravity(Gravity.START).setIcon(ContextCompat.getDrawable(GithubApplication.getInstance(), R.drawable.ic_error_alert)!!).setBackgroundColorRes(R.color.red).show()
    }

    fun showWarningMsg(activity: Activity, messageText: CharSequence) {
        Alerter.create(activity).setText(messageText).setContentGravity(Gravity.START).setIcon(ContextCompat.getDrawable(GithubApplication.getInstance(), R.drawable.ic_warning_alert)!!).setBackgroundColorRes(R.color.orange).show()
    }

    fun showNoticeMsg(activity: Activity, noticeText: CharSequence) {
        Alerter.create(activity).setText(noticeText).setContentGravity(Gravity.START).setBackgroundColorRes(R.color.green).show()
    }
}