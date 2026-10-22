package com.example.android_ud05cp01

import android.app.DatePickerDialog
import android.icu.util.Calendar

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Fecha Actual
        val fechaActual = LocalDate.now()
        val formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val fechaTexto = fechaActual.format(formateador)
        val mensajeFinal = getString(R.string.hoy_es, fechaTexto)
        val textView = findViewById<TextView>(R.id.tvHoy)
        textView.text = mensajeFinal


        //Seleccionar Fecha


        val btnFechaNac = findViewById<Button>(R.id.btnFechaNac)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)

        btnFechaNac.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this,
                {_,selectedYear, selectedMonth, selectedDay ->
                    val fechaFormateada = String.format(Locale.getDefault(),"%02d/%02d/%04d",selectedDay,selectedMonth +1, selectedYear)
                   tvFecha.text ="Naciste el $fechaFormateada"
                    val calculo = calculaDias(month,selectedMonth,day,selectedDay,year,selectedYear)

                    val anhos = calculo[0]
                    val dias = calculo[1]
                    tvMensaje.text = "Tienes $anhos años y $dias dias"

                },
                year, month,day
            ).show()




        }




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    private fun calculaDias(
        mesActual: Int,
        mesNacimiento: Int,
        diaActual: Int,
        diaNacimiento: Int,
        anhoActual: Int,
        anhoNacimiento: Int
    ): IntArray {
        var dias = 0
        val calculo = IntArray(2)
        var anhos = anhoActual - anhoNacimiento
        if (mesActual >= mesNacimiento) {
            for (i in mesNacimiento + 1..mesActual) dias += numeroDeDias(i, anhoActual)
            if (diaActual < diaNacimiento && mesActual == mesNacimiento) {
                anhos--
                dias = 365 + diaActual - diaNacimiento
            } else {
                dias += diaActual - diaNacimiento
            }
        } else {
            anhos--
            dias = diasRestantes(diaNacimiento, mesNacimiento, anhoActual - 1)
            val veces = 12 + mesActual - mesNacimiento - 1
            var mesVer = mesNacimiento + 1
            var anhoVer = anhoActual - 1
            for (i in 1..veces) {
                if (mesVer > 12) {
                    mesVer = 1
                    anhoVer++
                }
                dias += numeroDeDias(mesVer, anhoVer)
                mesVer++
            }
            dias += diaActual
        }
        calculo[0] = anhos
        calculo[1] = dias
        return calculo
    }

    private fun esBisiesto(anho: Int): Boolean {
        var bisiesto = false
        if (anho > 1582) {
            if (anho % 400 == 0 || anho % 4 == 0 && anho % 100 != 0) {
                bisiesto = true
            }
        } else {
            bisiesto = false
        }
        return bisiesto
    }

    private fun numeroDeDias(m: Int, a: Int): Int {
        return when (m) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            else -> if (esBisiesto(a)) {
                29
            } else {
                28
            }
        }
    }

    private fun diasRestantes(dia: Int, mes: Int, anho: Int): Int {
        return numeroDeDias(mes, anho) - dia
    }
}