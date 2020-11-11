package ru.appintheair.flightreviewssubmit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.aircraft
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.crew
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.flight
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.food
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.people
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.seat
import ru.appintheair.flightreviewssubmit.SubmitScreen.Companion.text
import kotlin.math.roundToInt

class SubmitScreenViewModel : ViewModel() {
    private val mRating: MutableLiveData<HashMap<String, String?>> = MutableLiveData()
    private val ratingMap: HashMap<String, String?> = LinkedHashMap()
    private val mAPIParameters: MutableLiveData<APIParameters> = MutableLiveData()

    init {
        mRating.value = ratingMap
    }

    fun getRating(): LiveData<HashMap<String, String?>> = mRating

    fun getData(): LiveData<APIParameters> = mAPIParameters

    fun setRating(listOfRating: HashMap<String, String?>) {
        val apiParameters = APIParameters(null, 1, 1, 1, 1, 1, 1)
        runBlocking {
            Log.d(javaClass.simpleName, "Imitate long operation")
            delay(3000)
        }
        listOfRating.forEach { (key, _) ->
            when (key) {
                aircraft -> apiParameters.aircraft =
                    listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
                flight -> apiParameters.flight =
                    listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
                food -> apiParameters.food = listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
                people -> apiParameters.people =
                    listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
                text -> apiParameters.text = listOfRating[key]
                crew -> apiParameters.crew = listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
                seat -> apiParameters.seat = listOfRating[key]?.toFloat()?.roundToInt()?.plus(1)
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            mAPIParameters.value = apiParameters
        }
    }
}