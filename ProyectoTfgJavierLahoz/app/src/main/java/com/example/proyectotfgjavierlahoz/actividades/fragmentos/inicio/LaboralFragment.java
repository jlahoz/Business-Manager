package com.example.proyectotfgjavierlahoz.actividades.fragmentos.inicio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.FragmentLaboralBinding;
import com.example.proyectotfgjavierlahoz.databinding.FragmentPersonalBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;


public class LaboralFragment extends Fragment {

    private FragmentLaboralBinding binding;

    private String dni;

    private DatabaseHelper bd;
    private Empleado empleado;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLaboralBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarObjetos();
        establecerDatos();

        return root;
    }

    private void inicializarObjetos(){
        bd = new DatabaseHelper(getContext());
        empleado = new Empleado();
    }

    private void establecerDatos(){

        if(getArguments() != null){
            dni = getArguments().getString("dni");
        } else {
            dni = LoginActivity.dni;
        }

        empleado = bd.datosUsuario(dni);
        binding.txvPuesto2.setText(empleado.getPuesto());

        if(empleado.getDepartamento() != null){
            String nombreDep = bd.obtenerDepCod(empleado.getDepartamento());
            binding.txvDepartamento2.setText(nombreDep);
        }

        if(LoginActivity.administrador == true){
            binding.swcAdministrador2.setChecked(true);
        } else {
            binding.swcAdministrador2.setChecked(false);
        }

    }
}