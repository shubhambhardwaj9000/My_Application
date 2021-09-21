/*
 * Copyright (C) 2021 The Android Open Source Project.
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
package com.example.lemonade

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**

     */
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has be drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === END IF STATEMENT ===

        /**
         * Put view of ImageView into lemonImage.
         * */

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            // TODO: call the method that handles the state when the image is clicked
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            // TODO: replace 'false' with a call to the function that shows the squeeze count
            showSnackbar()
        }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action, and replace TextView and ImageView respectively.
     */
    private fun clickLemonImage() {

        // Conditional statement 'when' to track the lemonadeState

        /**
         * When the image is clicked in the SELECT state, the state should become SQUEEZE
           - The lemonSize variable needs to be set using the 'pick()' method in the LemonTree class
           - The squeezeCount should be 0 since lemon is not squeezed yet.
         */
        when(lemonadeState){
           SELECT-> {
               lemonadeState = SQUEEZE
               lemonSize = lemonTree.pick()
               squeezeCount = 0
               text_action.setText(R.string.lemon_squeeze)
               image_lemon_state.setImageResource(R.drawable.lemon_squeeze)
           }
            /**
             * When the image is clicked in the SQUEEZE state the squeezeCount needs to be
             * INCREASED by 1 and lemonSize needs to be DECREASED by 1.
             * - If the lemonSize has reached 0, it has been juiced and the state should become DRINK
             * - Additionally, lemonSize is no longer relevant and should be set to -1
             */
            SQUEEZE->{
                squeezeCount+=1
                lemonSize-=1

                if (lemonSize==0){
                    lemonadeState=DRINK
                    text_action.setText(R.string.lemon_drink)
                    image_lemon_state.setImageResource(R.drawable.lemon_drink)
                    lemonSize=-1
                }
            }
            /**
             * When the image is clicked in the DRINK state the state should become RESTART
             * */
            DRINK->{
                lemonadeState=RESTART
                text_action.setText(R.string.lemon_empty_glass)
                image_lemon_state.setImageResource(R.drawable.lemon_restart)

            }
            /**
             * When the image is clicked in the RESTART state the state should become SELECT
             * */
            RESTART->{
                lemonadeState=SELECT
                text_action.setText(R.string.lemon_select)
                image_lemon_state.setImageResource(R.drawable.lemon_tree)
            }
        }
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)

        if(lemonadeState==SELECT){
            textAction.setText(R.string.lemon_select)
            lemonImage!!.setImageResource(R.drawable.lemon_tree)
        }else if(lemonadeState==SQUEEZE){
            textAction.setText(R.string.lemon_squeeze)
            lemonImage!!.setImageResource(R.drawable.lemon_squeeze)
        }else if(lemonadeState==DRINK){
            textAction.setText(R.string.lemon_drink)
            lemonImage!!.setImageResource(R.drawable.lemon_drink)
        }else{
            textAction.setText(R.string.lemon_empty_glass)
            lemonImage!!.setImageResource(R.drawable.lemon_restart)
        }
    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}



/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
