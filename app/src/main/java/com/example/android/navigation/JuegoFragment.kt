/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentJuegoBinding

class JuegoFragment : Fragment() {
    data class Pregunta(
            val texto: String,
            val respuestas: List<String>)

    // Lista con todas las preguntas
    private val preguntas: MutableList<Pregunta> = mutableListOf(
            Pregunta(texto = "¿Cual es el nombre de los integrantes?",
                    respuestas = listOf("Guy-Manuel de Homen-Christo y THomas Bangalter", "Guy Ritchie y Tomas Lugero", "Pierre Alexandre y Jean Poilette")),
            Pregunta(texto = "¿Por que bandas fueron influenciados para crear Daft Punk?",
                    respuestas = listOf("The Beach Boys y The Rollings Stones", "Metallica y Led Zepellin", "The Carpenters y Earth Wind & Fire")),
            Pregunta(texto = "Pais de origen del duo",
                    respuestas = listOf("Francia", "Inglaterra", "Suecia")),
            Pregunta(texto = "¿Por que surgio el nombre de Daft Punk?",
                    respuestas = listOf("Una critica", "les gusta el punk", "Creian que el punk es musica tonta")),
            Pregunta(texto = "¿Que anio ganaron un grammy a mejor album?",
                    respuestas = listOf("2014", "2007", "1997")),
            Pregunta(texto = "¿De que album es la cancion One More Time?",
                    respuestas = listOf("Discovery", "RAM", "Human After All")),
            Pregunta(texto = "¿Cual es su disco mas reciente?",
                    respuestas = listOf("RAM", "Human After All", "Homework")),
            Pregunta(texto = "¿En que pelicula participaron como banda sonora?",
                    respuestas = listOf("Tron: Legacy", "Gravedad", "Interestelar")),
            Pregunta(texto = "¿Cuantos albunes de estudio tiene Daft Punk?",
                    respuestas = listOf("4", "5", "3")),
            Pregunta(texto = "Nombre del evento donde hicieron el Alive del 2007",
                    respuestas = listOf("Festival Coachella", "Tomorroland", "Music City"))
    )

    lateinit var preguntaActual: Pregunta
    lateinit var respuestas: MutableList<String>
    private var preguntasPosicion = 0
    private val numeroPreguntas = Math.min((preguntas.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Infla la layout de este fragment
        val binding = DataBindingUtil.inflate<FragmentJuegoBinding>(inflater, R.layout.fragment_juego, container, false)

        // Crea aleatoriamente las preguntas
        crearPreguntasAleatorias()

        // Bind this fragment class to the layout
        binding.juego = this


        binding.responderButton.setOnClickListener { vista: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.
                if (respuestas[answerIndex] == preguntaActual.respuestas[0]) {
                    preguntasPosicion++
                    // Advance to the next question
                    if (preguntasPosicion < numeroPreguntas) {
                        preguntaActual = preguntas[preguntasPosicion]
                        establecerPregunta()
                        binding.invalidateAll()
                    } else {
                        // Ganamos!  Navigate al JuegoGanadoFragment.
                        vista.findNavController().navigate(JuegoFragmentDirections.actionJuegoFragmentToJuegoGanadoFragment(
                                preguntasPosicion,numeroPreguntas))
                    }
                } else {
                    // Perdiste! Navigate al JuegoPerdidoFragment.
                    vista.findNavController().navigate(JuegoFragmentDirections.actionJuegoFragmentToJuegoPerdidoFragment())

                }
            }
        }
        return binding.root
    }

    // Establece las preguntas aleatorias y establece la primer pregunta.
    private fun crearPreguntasAleatorias() {
        preguntas.shuffle()
        preguntasPosicion = 0
        establecerPregunta()
    }

    // Establece las preguntas y respuestas aleatoriamente..
    // Al llamar invalidateAll en el  FragmentJuegoBinding actualiza la informacion.
    private fun establecerPregunta() {

        preguntaActual = preguntas[preguntasPosicion]
        // randomize the answers into a copy of the array
        respuestas = preguntaActual.respuestas.toMutableList()
        // and shuffle them
        respuestas.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.titulo_android_trivia_question, preguntasPosicion + 1, numeroPreguntas)
    }
}
