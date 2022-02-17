package com.example.gamegal_app.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gamegal_app.AccountSettingsActivity
import com.example.gamegal_app.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.edit_account_settings_btn.setOnClickListener {
            startActivity(Intent(context,AccountSettingsActivity::class.java))
        }
        return view
    }

}