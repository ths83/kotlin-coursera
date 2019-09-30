package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)


class GameOfFifteen(private val initializer: GameOfFifteenInitializer = RandomGameInitializer()) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val values = initializer.initialPermutation.toMutableList<Int?>()
        values.add(null)

        board.getAllCells().forEachIndexed { index, cell ->
            board[cell] = values[index]
        }
    }

    override fun canMove(): Boolean {
        return true
    }

    override fun hasWon(): Boolean {
        val expectedBoard = listOf(1..15).flatten().toMutableList<Int?>()
        expectedBoard.add(null)
        return expectedBoard == board.getAllCells().map { get(it.i, it.j) }.toList()
    }

    override fun processMove(direction: Direction) = board.move(direction)

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    private fun GameBoard<Int?>.move(direction: Direction) {
        when (direction) {
            Direction.UP -> for (j in 0..this.width) {
                this.moveValuesInRowOrColumn(this.getAllCells().filter { it.j == j })
            }
            Direction.DOWN -> for (j in 0..this.width) {
                this.moveValuesInRowOrColumn(this.getAllCells().reversed().filter { it.j == j })
            }
            Direction.LEFT -> for (i in 0..this.width) {
                this.moveValuesInRowOrColumn(this.getAllCells().filter { it.i == i })
            }
            Direction.RIGHT -> for (i in 0..this.width) {
                this.moveValuesInRowOrColumn(this.getAllCells().reversed().filter { it.i == i })
            }
        }
    }

    private fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>) {
        val initialValues = rowOrColumn.map { this[it] }.toMutableList()
        val movedValues = mutableListOf<Int?>()
        movedValues.addAll(initialValues)

        val indexOfNull = movedValues.indexOf(null)

        if (indexOfNull != -1) {
            movedValues[indexOfNull] = movedValues[indexOfNull + 1]
            movedValues[indexOfNull + 1] = null
        }

        rowOrColumn.mapIndexed { index, cell -> this[cell] = movedValues[index] }
    }

}