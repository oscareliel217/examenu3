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

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.navigation.databinding.FragmentJuegoGanadoBinding


class JuegoGanadoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding: FragmentJuegoGanadoBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_juego_ganado, container, false)
        binding.siguientePartidaButton.setOnClickListener {vista: View ->
            vista.findNavController().navigate(R.id.action_juegoGanadoFragment_to_juegoFragment)
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.ganador_menu,menu)
        if (null == conseguirIntentCompartir().resolveActivity(activity!!.packageManager)){
            menu?.findItem(R.id.share).setVisible(false)
        }
    }

    private fun conseguirIntentCompartir(): Intent{
        val argumentos: JuegoGanadoFragmentArgs by navArgs()
        return ShareCompat.IntentBuilder.from(activity!!).setText(getString(R.string.share_success_text, argumentos.preguntasCorrectamente, argumentos.numeroPreguntas)).setType("text/plain").intent
    }

    private fun compartirExito(){
        startActivity(conseguirIntentCompartir())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item!!.itemId){
            R.id.share -> compartirExito()
        }

        return super.onOptionsItemSelected(item)
    }
}
