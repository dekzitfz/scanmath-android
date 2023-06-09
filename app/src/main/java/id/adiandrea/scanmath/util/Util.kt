package id.adiandrea.scanmath.util

import id.adiandrea.scanmath.model.History
import timber.log.Timber

fun calculateFromString(text: String): History? {
    Timber.i("data to proccess: $text")
    var latestCalculation: History? = null
    var arg1: Int? = 0
    var arg2: Int? = 0
    var result = 0.0

    //remove whitespace
    //define delimiter to split
    val symbol = defineSymbol(text)
    //separate symbol & number
    if(symbol != ""){
        val listArguments = text.filter { !it.isWhitespace() }.split(symbol)
        if(listArguments.size == 2){
            listArguments.forEachIndexed { index, s ->
                if(index == 0) { s.toIntOrNull()?.let { number -> arg1 = number } }
                else { s.toIntOrNull()?.let { number -> arg2 = number } }
            }
            Timber.i("arg1 is $arg1\narg2 is $arg2")

            if(arg1 != null && arg2 != null){
                //calculate
                when(symbol){
                    "+" -> { result = arg1!!.toDouble() + arg2!!.toDouble() }
                    "-" -> { result = arg1!!.toDouble() - arg2!!.toDouble() }
                    "*" -> { result = arg1!!.toDouble() * arg2!!.toDouble() }
                    "/" -> { result = arg1!!.toDouble() / arg2!!.toDouble() }
                }
                latestCalculation = History(
                    arg1 = arg1!!,
                    arg2 = arg2!!,
                    symbol = symbol,
                    result = result
                )
            }else{
                Timber.w("one of arguments is invalid!")
            }
        }else{
            Timber.w("it has more than 2 arguments!")
        }
    }

    Timber.i("calculate result is $result")
    return latestCalculation
}

private fun defineSymbol(source: String): String {
    return if(source.contains("+")){
        "+"
    }else if(source.contains("-")){
        "-"
    }else if(source.contains("x") || source.contains("*")){
        "*"
    }else if(source.contains("/") || source.contains(":")){
        "/"
    }else{
        ""
    }
}