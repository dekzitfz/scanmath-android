package id.adiandrea.scanmath.feature

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import id.adiandrea.scanmath.base.BaseViewModel
import id.adiandrea.scanmath.data.DataManager
import id.adiandrea.scanmath.data.local.history.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class CalculatorViewModel
@Inject constructor(private val dataManager: DataManager) : BaseViewModel() {

    internal var latestCalculation: History? = null

    fun saveResultToLocalDatabase(history: History) = CoroutineScope(Dispatchers.IO).launch {
        dataManager.saveHistoryToLocal(history)
    }

    fun calculateFromString(text: String): Double {
        Timber.i("data to proccess: $text")
        var arg1 = 0
        var arg2 = 0
        var result = 0.0

        //remove whitespace
        //define delimiter to split
        val symbol = defineSymbol(text)
        //separate symbol & number
        if(symbol != ""){
            val listArguments = text.filter { !it.isWhitespace() }.split(symbol)
            if(listArguments.size == 2){
                listArguments.forEachIndexed { index, s ->
                    if(index == 0) { arg1 = s.toInt() }
                    else { arg2 = s.toInt() }
                }
                Timber.i("arg1 is $arg1\narg2 is $arg2")

                //calculate
                when(symbol){
                    "+" -> { result = arg1.toDouble() + arg2.toDouble() }
                    "-" -> { result = arg1.toDouble() - arg2.toDouble() }
                    "*" -> { result = arg1.toDouble() * arg2.toDouble() }
                    "/" -> { result = arg1.toDouble() / arg2.toDouble() }
                }
                latestCalculation = History(
                    arg1 = arg1,
                    arg2 = arg2,
                    symbol = symbol,
                    result = result
                )
            }else{
                Timber.w("it has more than 2 arguments!")
            }
        }

        Timber.i("calculate result is $result")
        return result
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

}