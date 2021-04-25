package com.example.githubuser.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySettingBinding
import com.example.githubuser.fragment.TimePickerFragment
import com.example.githubuser.receiver.AlarmReceiver
import java.util.*

class SettingActivity : AppCompatActivity(), View.OnClickListener, TimePickerFragment.DialogTimeListener {
    private var binding : ActivitySettingBinding? = null
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val savedSwitch: Boolean = sharedPreferences.getBoolean("switch", false)
        binding?.switchAlarm?.isChecked = savedSwitch

        binding?.languange?.setOnClickListener(this)
        binding?.switchAlarm?.setOnClickListener(this)

        alarmReceiver = AlarmReceiver()

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.languange -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }

            R.id.switchAlarm -> {
                val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.apply {
                    binding?.switchAlarm?.isChecked?.let { putBoolean("switch", it) }
                }.apply()

                if (binding?.switchAlarm?.isChecked == true) {
                    val repeatMessage =  getString(R.string.notif_message)
                    alarmReceiver.setRepeatingAlarm(this, repeatMessage)
                } else {
                    alarmReceiver.cancelAlarm(this)
                }
            }

        }
    }


    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}