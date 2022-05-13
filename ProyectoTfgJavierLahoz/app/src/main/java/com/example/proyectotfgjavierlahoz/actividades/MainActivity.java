package com.example.proyectotfgjavierlahoz.actividades;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectotfgjavierlahoz.R;
import com.example.proyectotfgjavierlahoz.actividades.registro.LoginActivity;
import com.example.proyectotfgjavierlahoz.databinding.ActivityMainBinding;
import com.example.proyectotfgjavierlahoz.modelos.Empleado;
import com.example.proyectotfgjavierlahoz.sql.DatabaseHelper;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private String dni;

    TextView txvNombre;
    TextView txvCorreo;
    Button btnCerrarSesion;
    Button btnBorrarCuenta;
    ImageView imagenUsuario;

    Empleado empleado;
    DatabaseHelper databaseHelper;
    Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.toolbar.setBackgroundColor(getResources().getColor(R.color.gris));


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View view = navigationView.getHeaderView(0);




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_empleados, R.id.nav_departamentos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        inicializarVistas(view);
        inicializarObjetos();

        establecerDatosUsuario();
        escuchadorBotones();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void inicializarVistas(View view) {
        txvNombre = (TextView) view.findViewById(R.id.txvNombre);
        txvCorreo = (TextView) view.findViewById(R.id.txvCorreo);
        btnCerrarSesion = (Button) findViewById(R.id.btnCerrarSesion);
        imagenUsuario = (ImageView) view.findViewById(R.id.imagenUsuario);
        btnBorrarCuenta = (Button) findViewById(R.id.btnBorrarCuenta);
    }

    private void inicializarObjetos(){
        databaseHelper = new DatabaseHelper(this);
        empleado = new Empleado();
        datos = getIntent().getExtras();
    }

    private void establecerDatosUsuario(){
        //dni = datos.getString("dni");
        dni = LoginActivity.dni;
        empleado = databaseHelper.datosUsuario(dni);

        txvNombre.setText(empleado.getNombre() + " " + empleado.getApellidos());
        txvCorreo.setText(empleado.getCorreo());

        Bitmap imagen = databaseHelper.obtenerImagen(dni);
        if(imagen != null){
            imagenUsuario.setImageBitmap(imagen);
        } else {
            imagenUsuario.setImageResource(R.drawable.user_logo);
        }
    }

    private void escuchadorBotones(){
        btnCerrarSesion.setOnClickListener(this);
        imagenUsuario.setOnClickListener(this);
        btnBorrarCuenta.setOnClickListener(this);
    }

    private void dialogoCerrarSesion(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Cerrar sesion");
        alertDialogBuilder.setMessage("¿Seguro que desea cerrar sesion?");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(loginActivity);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }

    private void dialogoBorrarCuenta(){
        AlertDialog.Builder dialogBorrarCuenta = new AlertDialog.Builder(this);
        dialogBorrarCuenta.setTitle("Borrar cuenta")
                .setMessage("¿Seguro que desea borrar la cuenta?")
                .setCancelable(false)
                .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseHelper.eliminarUsuario(dni);
                        Toast.makeText(MainActivity.this,getString(R.string.menu_cuenta_borrada), Toast.LENGTH_LONG).show();
                        Intent inicioSesion = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(inicioSesion);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCerrarSesion:
                dialogoCerrarSesion();
                break;
            case R.id.btnBorrarCuenta:
                dialogoBorrarCuenta();
                break;
        }
    }

}