package net.azarquiel.blackjack

import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import net.azarquiel.blackjack.model.Carta
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var titulo: CharSequence = ""
    private lateinit var random: Random
    private lateinit var llcartas: LinearLayout
    val mazo = Array(40) { i -> Carta() }
    var imazo = 0
    val palos = arrayOf ("clubs","diamonds", "hearts", "spades")
    var jugador = 0
    var puntos = Array(2) {0}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titulo = title.toString()

        random = Random(System.currentTimeMillis())

        llcartas = findViewById<LinearLayout>(R.id.llcartas)
        var ivmazo = findViewById<ImageView>(R.id.ivmazo)
        var btnstop = findViewById<Button>(R.id.btnstop)

        ivmazo.setOnClickListener { ivmazoOnclick() }
        btnstop.setOnClickListener { btnstopOnclick() }

        newGame()
        makeMazo()
    }

    private fun makeMazo() {
        var c = 0
        for(palo in 0 until 4) {
            for (num in 1..10){
                mazo[c] = Carta(num, palo)
                c++
            }
        }
    }

    private fun newGame() {
        imazo = 0
        jugador = 0
        puntos = Array(2){0}
        mazo.shuffle(random)
        sacaCarta()
        sacaCarta()

        /*for (carta in mazo) {carta.palo
            Log.d("paco", "${carta.numero} de ${palos[carta.palo]}")
        }*/

    }

    private fun btnstopOnclick() {
        jugador++
        if (jugador >= puntos.size) {
            mostrarResultado()
        } else {
            llcartas.removeAllViews()
            sacaCarta()
        }

    }
    private fun mostrarResultado() {

        if (jugador == 0){
            nextPlayer()
        }else{
            gameOver()
        }
    }

    private fun gameOver() {
        var mensaje = ""
        if (puntos[0]>21 && puntos[1]>21){
            mensaje ="Empate"
        }else if(puntos[0]>21){
            mensaje = "Gana jugador 2"
        }else if (puntos[1]>21){
            mensaje = "Gana jugador 1"
        }else if (21-puntos[0] < 21 - puntos[1]){
            mensaje = "Gana jugador 1"
        }else if (21-puntos[0] > 21 - puntos[1]){
            mensaje = "Gana jugador 2"
        }else{
            mensaje ="Empate"
        }
        AlertDialog.Builder(this)

            .setTitle("Game Over - $mensaje")
            .setMessage("Jugador 1: ${puntos[0]} puntos. \n \n Jugador 2: ${puntos[1]} puntos.")
            .setPositiveButton("Nuevo Juego") { dialog, which ->
                newGame() // Inicia un nuevo juego si se presiona "Nuevo Juego"
            }
            .setNegativeButton("Salir") { dialog, which ->
                finish() // Sale de la aplicación si se presiona "Salir"
            }
            .show()
    }

    private fun nextPlayer() {
        jugador = 1
        llcartas.removeAllViews()
        sacaCarta()
        sacaCarta()
    }


    private fun ivmazoOnclick() {
        sacaCarta()
    }

    private fun sacaCarta() {
        val cartaJuego:Carta = mazo[imazo]
        imazo++

        //Creamos la ImageView de la carta para posteriormente pintarla en la pantalla
        val ivCarta = ImageView(this)

        //Buscamos la id de la imagen a traves de su nombre
        val idimage = resources.getIdentifier("${palos[cartaJuego.palo]}${cartaJuego.numero}", "drawable", packageName)
        ivCarta.setImageResource(idimage)

        //Le ponemos atributos de ancho, alto y margenes a traves de un LayoutParams
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 0, 8, 0)
        ivCarta.layoutParams = lp

        //Engancgamos la ImageView al linear para que se pinte en la pantalla
        llcartas.addView(ivCarta,0)

        //Miramos los puntos de la carta y se lo acumulamos al jugador
        val punt = if (cartaJuego.numero>7) 10 else cartaJuego.numero
        puntos[jugador] += punt
        title = "$titulo - ${puntos[jugador]} puntos "
        if (puntos[jugador] > 21 && jugador == 0){
            tostada("Te pasaste Jugador 1")
           nextPlayer()
        }else if (jugador == 1 && puntos [jugador] > 21) {
            tostada("Te pasaste Jugador 2")
            gameOver()
        }
    }

    private fun tostada(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT)
            .show()
    }

}