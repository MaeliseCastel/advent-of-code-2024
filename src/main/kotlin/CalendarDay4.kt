package main.kotlin

fun calendarDay4PartOne() {
    val wordSearchMatrix = {}.javaClass.getResource("/calendar_day_4.txt")!!.readText()
    val xMasLeftToRight = Regex("XMAS")
    val xMasRightToLeft = Regex("SAMX")
    val xMasesLeftToRight = xMasLeftToRight.findAll(wordSearchMatrix).toList().count()
    val xMasesRightToLeft = xMasRightToLeft.findAll(wordSearchMatrix).toList().count()

    val wordSearchMatrixAsLines = wordSearchMatrix.lines()

    val nbColumns = wordSearchMatrixAsLines.first().length
    val nbLines = wordSearchMatrixAsLines.size
    var indexLine = 0
    val wordSearchMatrixAsColumns = mutableMapOf<Int, String>()
    val wordSearchMatrixAs16SizeMatrix = mutableListOf<List<String>>()
    while (indexLine < nbLines) {
        var indexRow = 0
        while (indexRow < nbColumns) {
            wordSearchMatrixAsColumns[indexRow] =
                wordSearchMatrixAsColumns.getOrPut(indexRow) { "" } + wordSearchMatrixAsLines[indexLine][indexRow]
            indexRow++
        }
        indexLine++
    }

    val xMasesColumns = wordSearchMatrixAsColumns
        .map { (_, column) -> (xMasLeftToRight.findAll(column) + xMasRightToLeft.findAll(column)).toList().size }.sum()

    var allXmases = xMasesLeftToRight + xMasesRightToLeft + xMasesColumns

    indexLine = 0
    while (indexLine <= nbLines - 4) {
        var indexRow = 0
        while (indexRow <= nbColumns - 4) {
            wordSearchMatrixAs16SizeMatrix.add(
                listOf(
                    wordSearchMatrixAsLines[indexLine].substring(indexRow..indexRow + 3),
                    wordSearchMatrixAsLines[indexLine + 1].substring(indexRow..indexRow + 3),
                    wordSearchMatrixAsLines[indexLine + 2].substring(indexRow..indexRow + 3),
                    wordSearchMatrixAsLines[indexLine + 3].substring(indexRow..indexRow + 3),
                ),
            )
            indexRow++
        }
        indexLine++
    }

    wordSearchMatrixAs16SizeMatrix.forEach { matrix ->
        matrix.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { letterIndex, letter ->
                if (letter == 'X') {
                    allXmases += lookForDiagonalXMases(matrix, lineIndex, letterIndex)
                }
            }
        }
    }

    println(allXmases)
}

private fun lookForDiagonalXMases(matrix: List<String>, lineIndex: Int, letterIndex: Int): Int {
    var nbXmasesFromX = 0
    if (isCorner(lineIndex, letterIndex) && checkDiagonal(matrix, lineIndex, letterIndex)) {
        nbXmasesFromX++
    }
    return nbXmasesFromX
}

private fun isCorner(lineIndex: Int, letterIndex: Int): Boolean = lineIndex % 3 == 0 && letterIndex % 3 == 0

private fun checkDiagonal(matrix: List<String>, lineIndex: Int, letterIndex: Int): Boolean {
    val (m, a, s) = if (lineIndex == 0 && letterIndex == 0) {
        Triple(matrix[1][1] == 'M', matrix[2][2] == 'A', matrix[3][3] == 'S')
    } else if (lineIndex == 0 && letterIndex == 3) {
        Triple(matrix[1][2] == 'M', matrix[2][1] == 'A', matrix[3][0] == 'S')
    } else if (lineIndex == 3 && letterIndex == 0) {
        Triple(matrix[2][1] == 'M', matrix[1][2] == 'A', matrix[0][3] == 'S')
    } else {
        Triple(matrix[2][2] == 'M', matrix[1][1] == 'A', matrix[0][0] == 'S')
    }
    return m && a && s
}

fun calendarDay4PartTwo() {
    val wordSearchMatrix = {}.javaClass.getResource("/calendar_day_4.txt")!!.readText().lines()

    val nbColumns = wordSearchMatrix.first().length
    val nbLines = wordSearchMatrix.size

    val mas9SizeMatrix = mutableListOf<List<String>>()
    var indexLine = 0
    while (indexLine <= nbLines - 3) {
        var indexRow = 0
        while (indexRow <= nbColumns - 3) {
            mas9SizeMatrix.add(
                listOf(
                    wordSearchMatrix[indexLine].substring(indexRow..indexRow + 2),
                    wordSearchMatrix[indexLine + 1].substring(indexRow..indexRow + 2),
                    wordSearchMatrix[indexLine + 2].substring(indexRow..indexRow + 2),
                ),
            )
            indexRow++
        }
        indexLine++
    }

    var allXmases = 0
    mas9SizeMatrix.forEach { matrix ->
        matrix.forEachIndexed { lineIndex, line ->
            line.forEachIndexed { letterIndex, letter ->
                if (letter == 'A') {
                    allXmases += lookForDiagonalACentered(matrix, lineIndex, letterIndex)
                }
            }
        }
    }

    println(allXmases)
}

private fun lookForDiagonalACentered(matrix: List<String>, lineIndex: Int, letterIndex: Int): Int {
    var centeredMasDiagonals = 0
    if (isMiddle(lineIndex, letterIndex)) {
        val checkDiagonalTopLeftBottomRight =
            (matrix[lineIndex - 1][letterIndex - 1] == 'M' && matrix[lineIndex + 1][letterIndex + 1] == 'S') || (matrix[lineIndex - 1][letterIndex - 1] == 'S' && matrix[lineIndex + 1][letterIndex + 1] == 'M')
        val checkDiagonalBottomLeftTopRight =
            (matrix[lineIndex + 1][letterIndex - 1] == 'M' && matrix[lineIndex - 1][letterIndex + 1] == 'S') || (matrix[lineIndex + 1][letterIndex - 1] == 'S' && matrix[lineIndex - 1][letterIndex + 1] == 'M')

        if (checkDiagonalTopLeftBottomRight && checkDiagonalBottomLeftTopRight) centeredMasDiagonals++
    }
    return centeredMasDiagonals

}

private fun isMiddle(lineIndex: Int, letterIndex: Int) = lineIndex == 1 && letterIndex == 1
