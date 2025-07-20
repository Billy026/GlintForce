package com.billy.glintforce.common

// Rounds an integer up
fun roundUp(int: Int, divisor: Int): Int {
    return if (int.toDouble() % divisor == 0.0) {
        int / divisor
    } else {
        (int.toDouble() / divisor).toInt() + 1
    }
}

// Rounds an integer down
fun roundDown(int: Int, divisor: Int): Int {
    return if (int.toDouble() % divisor == 0.0) {
        int / divisor
    } else {
        (int.toDouble() / divisor).toInt()
    }
}

// Rounds a double into the nearest tenths.
fun roundedToNearestTens(double: Double): Int {
    var str = double.toInt().toString()
    val lastDigits = str.filterIndexed { index, _ -> index > str.length - 2 }
    str = str.filterIndexed { index, _ -> index < str.length - 1 }

    return if (str == "") {
        10
    } else if (lastDigits == "00") {
        str.toInt() * 10
    } else {
        (str.toInt() + 1) * 10
    }
}

// Rounds a double to the nearest hundredths
fun roundedToNearestHundred(double: Double): Int {
    var str = double.toInt().toString()
    val lastDigits = str.filterIndexed { index, _ -> index > str.length - 3 }
    str = str.filterIndexed { index, _ -> index < str.length - 2 }

    return if (str == "") {
        100
    } else if (lastDigits == "00") {
        str.toInt() * 100
    } else {
        (str.toInt() + 1) * 100
    }
}

// Validates inputted text to a double value
fun getValidated(text: String): String {
    // Start by filtering out unwanted characters like commas and multiple decimals
    val filteredChars = text.filterIndexed { index, c ->
        // Take all digits
        c in "0123456789" ||
                // Take only the first decimal
                (c == '.' && text.indexOf('.') == index)
    }

    // Remove extra digits from the input
    return if (filteredChars.contains('.')) {
        val beforeDecimal = filteredChars.substringBefore('.')
        val afterDecimal = filteredChars.substringAfter('.')
        if (afterDecimal.length == 1) {
            beforeDecimal + "." + afterDecimal + "0"
        } else {
            // If decimal is present, take first 3 digits before decimal and first 2 digits after decimal
            beforeDecimal + "." + afterDecimal.take(2)
        }
    } else {
        // If there is no decimal, just take the first 3 digits
        filteredChars
    }
}

// Gets the month from a fully-formatted date
fun getMonth(fullDate: String): String {
    val len = fullDate.length
    return fullDate.filterIndexed { index, _ ->
        index > 2 && index < len - 5
    }
}