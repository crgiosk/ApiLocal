package com.example.poryectoinicial.view.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.poryectoinicial.R
import com.example.poryectoinicial.view.Activities.MainActivity
import com.example.poryectoinicial.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProviders.of(activity!!).get(LoginViewModel(activity!!.application)::class.java)
        loginViewModel.getUsuid().observe(this, Observer {
            if (it != null)
                usuid = it
            else
                Toast.makeText(context, "hola", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btfloat = activity?.findViewById<View>(R.id.BtFloatAction)
        btfloat?.visibility = View.GONE
        BtIngresar.setOnClickListener {
            validalogin(it)
        }
    }

    private fun validalogin(view: View){
        val username= EdUsuarioLogin.text.toString()
        val pass = EdPasswordLogin.text.toString()
        if(username.isNotEmpty() && pass.isNotEmpty()){
            consultarusuid(username, pass)
            if(usuid != 0)
                navProyectos(view)
        }
        else
            Toast.makeText(context,"Debe llenar los campos", Toast.LENGTH_SHORT).show()
    }

    private fun consultarusuid(username: String, pass: String){
        loginViewModel.loginUsuario(username, pass)
    }

    private fun navProyectos(view: View){
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, TabLayoutFragment.newInstance(/*bundle*/), TabLayoutFragment.TAG)
        fragmentTransaction.addToBackStack(TAG)
        fragmentTransaction.commit()
    }

    companion object {
        var usuid: Int = 0
        const val TAG = "LoginFragment"
        fun newInstance(bundle: Bundle? = null): LoginFragment {
            val fragment = LoginFragment()
            if (bundle != null) {
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}
