package board

open class SquareBoardImpl(override val width: Int) : SquareBoard {

    var cells = createSquare(width)

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return getAllCells().filter { it == Cell(i, j) }.getOrNull(0)
    }

    override fun getCell(i: Int, j: Int): Cell {
        require(i <= cells.size && j <= cells.size)
        return getAllCells().filter { it == Cell(i, j) }[0]
    }

    override fun getAllCells(): Collection<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val row = cells[i - 1].filter { it.j in jRange }
        if (jRange.first > jRange.last) return row.asReversed()
        return row
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val column = getAllCells().filter { it.j == j && it.i in iRange }
        if (iRange.first > iRange.last) return column.asReversed()
        return column
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.LEFT -> getCellOrNull(i, j - 1)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
        }
    }

}

fun createSquareBoard(width: Int): SquareBoard {
    return SquareBoardImpl(width)
}

private fun createSquare(width: Int): Array<Array<Cell>> {
    var board = arrayOf<Array<Cell>>()
    for (i in 1..width) {
        var column = arrayOf<Cell>()
        for (j in 1..width) {
            column += Cell(i, j)
        }
        board += column
    }

    return board
}

class GameBoardImpl<T>(override val width: Int) : SquareBoardImpl(width), GameBoard<T> {

    var cellsValues = mutableMapOf<Cell, T?>()

    override fun get(cell: Cell): T? {
        return cellsValues[cell]
    }

    override fun set(cell: Cell, value: T?) {
        return cellsValues.set(cell, value)
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return cellsValues.filter { predicate.invoke(it.value) }.keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return cellsValues.values.any { predicate.invoke(it) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return cellsValues.values.all { predicate.invoke(it) }
    }

}

fun <T> createGameBoard(width: Int): GameBoard<T> {
    val gameBoard = GameBoardImpl<T>(width)
    gameBoard.cells = createSquare(width)
    gameBoard.cellsValues = gameBoard.getAllCells().associateWith { null }.toMutableMap()
    return gameBoard
}

