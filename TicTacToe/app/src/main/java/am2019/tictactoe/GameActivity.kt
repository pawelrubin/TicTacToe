package am2019.tictactoe

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.floor
import kotlin.random.Random


class GameActivity : AppCompatActivity() {

    private var currentPlayer = Player.X
    private var moveCount = 0
    private var size = 0
    private var pvp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        pvp =  intent.getBooleanExtra("pvp", true)
        size = intent.getIntExtra("size", 3)
        createBoard(size)
        updateTurnView()
    }

    private fun createBoard(size: Int) {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val pixels = when (this.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> outMetrics.widthPixels
            Configuration.ORIENTATION_LANDSCAPE -> floor(outMetrics.heightPixels * 0.7).toInt()
            else -> outMetrics.widthPixels
        }
        val width = dpToPx((pixels / density /(size)).toInt())
        val params = TableRow.LayoutParams(width, width)
        for (i in 0 until size) {
            val row = TableRow(this)
            for (j in 0 until size) {
                val button = Button(this).apply {
                    layoutParams = params
                    setOnClickListener { makeMove(this) }
                }
                row.addView(button)
            }
            board.addView(row)
        }
    }

    private fun makeMove(button: Button) {
        if (button.text.isEmpty()) {
            button.text = currentPlayer.toString()
            if (!checkEndConditions(button)) {
                moveCount++
                if (!pvp) {
                    currentPlayer = nextPlayer()
                    botMove()
                    currentPlayer = nextPlayer()
                } else {
                    currentPlayer = nextPlayer()
                    updateTurnView()
                }
            }
        }
    }

    private fun nextPlayer():Player{
        return when (currentPlayer) {
            Player.X -> Player.O
            Player.O -> Player.X
        }
    }

    private fun botMove() {
        while (true) {
            val row:TableRow = board.getChildAt(Random.nextInt(size)) as TableRow
            val button:Button = row.getChildAt(Random.nextInt(size)) as Button
            if (button.text.isEmpty()) {
                button.text = currentPlayer.toString()
                checkEndConditions(button)
                moveCount++
                break
            }
        }
    }

    private fun checkEndConditions(button: Button):Boolean {
        val row:TableRow = button.parent as TableRow
        val x:Int = board.indexOfChild(row)
        val y:Int = row.indexOfChild(button)

        //check col
        for (i in 0 until size) {
            if ((row.getChildAt(i) as Button).text !== currentPlayer.toString())
                break
            if (i == size - 1) {
                freezeButtons()
                Toast.makeText(this, "$currentPlayer won", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        //check row
        for (i in 0 until size) {
            if (((board.getChildAt(i) as TableRow).getChildAt(y) as Button).text !== currentPlayer.toString())
                break
            if (i == size - 1) {
                freezeButtons()
                Toast.makeText(this, "$currentPlayer won", Toast.LENGTH_SHORT).show()
                return true
            }
        }

        //check diagonal
        if (x == y) {
            for (i in 0 until size) {
                if (((board.getChildAt(i) as TableRow).getChildAt(i) as Button).text !== currentPlayer.toString())
                    break
                if (i == size - 1) {
                    freezeButtons()
                    Toast.makeText(this, "$currentPlayer won", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
        }

        //check anti diagonal
        if (x + y == size - 1) {
            for (i in 0 until size) {
                if (((board.getChildAt(i) as TableRow).getChildAt(size - 1 - i)
                            as Button).text !== currentPlayer.toString())
                    break
                if (i == size - 1) {
                    freezeButtons()
                    Toast.makeText(this, "$currentPlayer won", Toast.LENGTH_SHORT).show()
                    return true
                }
            }
        }

        //check draw
        if (moveCount == (Math.pow(size.toDouble(), 2.0) - 1).toInt()) {
            freezeButtons()
            Toast.makeText(this, "DRAW!!!", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }

    private fun dpToPx(dp: Int): Int {
        return Math.round(dp.toFloat() * this.resources.displayMetrics.density)
    }

    private fun updateTurnView() {
        turnView.text = "$currentPlayer's turn"
    }

    private fun freezeButtons() {
        for (i in 0 until board.childCount) {
            val row = board.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                val button = row.getChildAt(j) as Button
                button.isEnabled = false
            }
        }
    }

    enum class Player {
        X,
        O
    }
}
