package com.example.poryectoinicial.view.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.poryectoinicial.R
import com.example.poryectoinicial.model.Proyecto.Proyecto
import com.example.poryectoinicial.viewmodel.ProyectosViewModel
import kotlinx.android.synthetic.main.fragment_edit_proyectos.*

class EditProyectosFragment : Fragment() {

    private lateinit var proyectosViewModel: ProyectosViewModel
    private var proyecto: Proyecto = Proyecto()
    private var rta = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deshabilitarFloatActionB()
        proyectosViewModel = ViewModelProviders.of(activity!!).get(ProyectosViewModel(activity!!.application)::class.java)
        proyectosViewModel.geteditproyectos().observe(this, Observer {
            if(rta != 0)
                setData(it)
        })
        proyectosViewModel.getactualizarproyecto().observe( this, Observer {
            if(it != null)
                mostrarrespuesta(it)
        })
        proyectosViewModel.getinsertarproyecto().observe(this, Observer {
            if(it != null){
                mostrarrespuesta(it)

            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_proyectos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            rta = arguments?.getString("PROYECTOID")!!.toInt()
        }
        if(rta != 0)
            consultarDetalleProyecto()
        else
            limpiarcampos()
        BtGuardarProyecto.setOnClickListener {
            val objeto: Proyecto = construirobjeto()
            if(rta != 0)
                actualizarproyecto(objeto)
            else
                insertarproyecto(objeto)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        proyectosViewModel.limpiarObjetos()
    }

    private fun construirobjeto(): Proyecto{
        proyecto.proyectoid = rta.toString()
        proyecto.titulo = EdTituloProyecto.text.toString()
        proyecto.descripcion = EdDescripcionProyecto.text.toString()
        proyecto.fecestimada = EdFechaEst.text.toString()
        proyecto.fecentrega = EdFechaEnt.text.toString()
        proyecto.horas = EdHoras.text.toString()
        proyecto.usuid = LoginFragment.usuid.toString()

        return proyecto
    }

    private fun consultarDetalleProyecto(){
        proyectosViewModel.getdataProyectos(rta)
    }

    private fun actualizarproyecto(objeto: Proyecto){
        proyectosViewModel.actualizarProyecto(objeto)
    }

    private fun insertarproyecto(objeto: Proyecto){
        proyectosViewModel.insertarProyecto(objeto)
    }

    private fun mostrarrespuesta(respuesta: Proyecto){
        Toast.makeText(context,respuesta.mensaje, Toast.LENGTH_SHORT).show()
        regresaraGrid()
        //proyectosViewModel.getallProyectos(LoginFragment.usuid)
    }

    private fun setData(detalleproyecto: Proyecto){
        if(detalleproyecto.toString().isNotEmpty()){
            EdTituloProyecto.setText(detalleproyecto.titulo)
            EdDescripcionProyecto.setText(detalleproyecto.descripcion)
            EdFechaEnt.setText(detalleproyecto.fecentrega)
            EdFechaEst.setText(detalleproyecto.fecestimada)
            EdHoras.setText(detalleproyecto.horas)
        }
    }

    private fun regresaraGrid(){
        val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.container, TabLayoutFragment.newInstance(), TabLayoutFragment.TAG)
        fragmentTransaction.commit()
    }

    private fun limpiarcampos(){
        EdTituloProyecto.setText("")
        EdDescripcionProyecto.setText("")
        EdFechaEnt.setText("")
        EdFechaEst.setText("")
        EdHoras.setText("")
    }

    private fun deshabilitarFloatActionB(){
        val btfloat = activity?.findViewById<View>(R.id.BtFloatAction)
        btfloat?.visibility = View.GONE
        val tab = activity?.findViewById<View>(R.id.tabs_main)
        val vpager = activity?.findViewById<View>(R.id.viewpager_main)
        tab?.visibility = View.GONE
        vpager?.visibility = View.GONE
    }

    companion object {
        const val TAG = "EditProyectoFragment"
        fun newInstance(bundle: Bundle? = null): EditProyectosFragment {
            val fragment = EditProyectosFragment()
            if (bundle != null) {
                fragment.arguments = bundle
            }
            return fragment
        }
    }
}
