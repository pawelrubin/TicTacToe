package am2019.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var size = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateSizeViews()
    }

    fun startOnePlayerGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("pvp", false)
            putExtra("size", size )
        }
        startActivity(intent)
    }

    fun startTwoPlayersGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("pvp", true)
            putExtra("size", size)
        }
        startActivity(intent)
    }

    fun sizeUp(view: View) {
        if (size < 8) {
            size++
            updateSizeViews()
        }
    }

    fun sizeDown(view: View) {
        if (size > 3) {
            size--
            updateSizeViews()
        }
    }

    private fun updateSizeViews() {
        leftSizeView.text = size.toString()
        rightSizeView.text = size.toString()
    }
}
