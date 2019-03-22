package am2019.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.TableRow
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : AppCompatActivity() {

    private var currentTurn = Player.X
    private var moveCount = 0
    private var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        val pvp =  intent.getBooleanExtra("pvp", false)
        size = intent.getIntExtra("size", 3)
        createBoard(size)
        updateTurnView()
    }

    private fun createBoard(size: Int) {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val density = resources.displayMetrics.density
        val width = dpToPx((outMetrics.widthPixels / density /size).toInt())
        val params = TableRow.LayoutParams(width, width)
        params.setMargins(0,0,0,0)
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
            button.text = currentTurn.toString()
            checkEndConditions(button)
            currentTurn = when (currentTurn) {
                Player.X -> Player.O
                Player.O -> Player.X
            }
            updateTurnView()
            moveCount++
        }
    }

    private fun checkEndConditions(button: Button) {
        val row:TableRow = button.parent as TableRow
        val x:Int = board.indexOfChild(row)
        val y:Int = row.indexOfChild(button)

        //check col
        for (i in 0 until size) {
            if ((row.getChildAt(i) as Button).text !== currentTurn.toString())
                break
            if (i == size - 1) {
                freezeButtons()
                Toast.makeText(this, "$currentTurn won", Toast.LENGTH_SHORT).show()
            }
        }

        //check row
        for (i in 0 until size) {
            if (((board.getChildAt(i) as TableRow).getChildAt(y) as Button).text !== currentTurn.toString())
                break
            if (i == size - 1) {
                freezeButtons()
                Toast.makeText(this, "$currentTurn won", Toast.LENGTH_SHORT).show()
            }
        }

        //check diagonal
        if (x == y) {
            for (i in 0 until size) {
                if (((board.getChildAt(i) as TableRow).getChildAt(i) as Button).text !== currentTurn.toString())
                    break
                if (i == size - 1) {
                    freezeButtons()
                    Toast.makeText(this, "$currentTurn won", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //check anti diagonal
        if (x + y == size - 1) {
            for (i in 0 until size) {
                if (((board.getChildAt(i) as TableRow).getChildAt(size - 1 - i)
                            as Button).text !== currentTurn.toString())
                    break
                if (i == size - 1) {
                    freezeButtons()
                    Toast.makeText(this, "$currentTurn won", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //check draw
        if (moveCount == (Math.pow(size.toDouble(), 2.0) - 1).toInt()) {
            freezeButtons()
            Toast.makeText(this, "DRAW!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dpToPx(dp: Int): Int {
        val density = this.resources.displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    private fun updateTurnView() {
        turnView.text = "$currentTurn's turn"
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
