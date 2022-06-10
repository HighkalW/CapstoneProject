package com.example.capstoneproject.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivitySignupBinding
import com.example.capstoneproject.ui.viewmodelfactory.UserVMF
import com.example.capstoneproject.data.Result
import com.example.capstoneproject.util.animateVisibility

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel
    val name = binding.edtSignupName.text.toString().trim()
    val email = binding.edtSignupEmail.text.toString().trim()
    val password = binding.edtPassword.text.toString().trim()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
        playAnimation()
    }
    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            signUp()
        }

        binding.tvLogin.setOnClickListener{
            finish()
        }
    }
    private fun setupViewModel() {
        val fac: UserVMF = UserVMF.getInstance(this)
        signupViewModel = ViewModelProvider(this, fac)[SignupViewModel::class.java]
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgSignupView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)
        val tvName = ObjectAnimator.ofFloat(binding.tvSignupName, View.ALPHA, 1f).setDuration(500)
        val edtName = ObjectAnimator.ofFloat(binding.edtSignupName, View.ALPHA, 1f).setDuration(500)
        val tvEmail = ObjectAnimator.ofFloat(binding.tvSignupEmail, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtSignupEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword = ObjectAnimator.ofFloat(binding.tvSignupPassword, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvKet, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,tvName,edtName, tvEmail, edtEmail, tvPassword, edtPassword, btnSignup, message, login)
            start()
        }
    }
    private fun signUp(){

        when {
            name.isEmpty() -> {
                binding.edtSignupName.error = resources.getString(R.string.message_validation, "name")
            }
            email.isEmpty() -> {
                binding.edtSignupEmail.error = resources.getString(R.string.message_validation, "email")
            }
            password.isEmpty() -> {
                binding.edtPassword.error = resources.getString(R.string.message_validation, "password")
            }
            else -> {
                signupViewModel.register(name, email, password).observe(this){ result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                val user = result.data
                                if (user.error){
                                    Toast.makeText(this@SignupActivity, user.message, Toast.LENGTH_SHORT).show()
                                }else{
                                    AlertDialog.Builder(this@SignupActivity).apply {
                                        setTitle(getString(R.string.tittle_alert))
                                        setMessage(getString(R.string.message_success_alert))
                                        setPositiveButton(getString(R.string.next)) { _, _ ->
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                                }
                            }
                            is Result.Error -> {
                                showLoading(false)
                                Toast.makeText(
                                    this,
                                    resources.getString(R.string.signup_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            edtSignupEmail.isEnabled = !isLoading
            edtPassword.isEnabled = !isLoading
            edtSignupName.isEnabled = !isLoading
            btnSignup.isEnabled = !isLoading

            if (isLoading) {
                viewProgressbar.animateVisibility(true)
            } else {
                viewProgressbar.animateVisibility(false)
            }
        }
    }


}