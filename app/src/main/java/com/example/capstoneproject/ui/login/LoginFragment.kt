package com.example.capstoneproject.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.capstoneproject.ui.main.MainActivity
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentLoginBinding
import com.example.capstoneproject.ui.signup.SignUpFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
    }

    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var auth : FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupTextView.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.auth_fragmentContainer, SignUpFragment())
                .addToBackStack(null)
                .commit()


        }

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("626795924548-v5sq2b8pl44v4e8uj1i0meud8l4kcbbi.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        auth = Firebase.auth

        binding.btnGoogleLogin!!.setOnClickListener {
            signIn()
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.emailText.editText?.text.toString()
            val password = binding.passwordText.editText?.text.toString()

            /*
            * Checking whether the email field is empty or not?
            * If not empty then valid or not?
            * */
            if (TextUtils.isEmpty(email)) {
                binding.emailText.error = "Email is required."
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailText.error = "Enter a valid email address"
                return@setOnClickListener
            }

            /*
            * Checking whether the password field is empty or not?
            * If not empty then valid or not?
            * */
            if (TextUtils.isEmpty(password)) {
                binding.passwordText.error = "Password is required."
                return@setOnClickListener
            }

            binding.loginProgressBar.visibility = View.VISIBLE

            /*
            * Authentication for Firebase
            * */
            val auth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        startActivity(Intent(activity, MainActivity::class.java))
                    else {
                        Toast.makeText(
                            context,
                            "Something went wrong! Please try again.",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, task.exception.toString())
                    }
                }

        }

    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d(TAG, "firebaseAuthWithGoogle" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG,"Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) {task ->
                if (task.isSuccessful){
                    Log.d(TAG, "singInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "singInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    private fun updateUI(currentUser: FirebaseUser?){
        if (currentUser != null) {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            activity?.finish()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}