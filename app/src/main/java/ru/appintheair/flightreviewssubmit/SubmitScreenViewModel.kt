package ru.appintheair.flightreviewssubmit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.roundToInt

class SubmitScreenViewModel : ViewModel() {
    private val mRating: MutableLiveData<EnumMap<MyCell, String?>> = MutableLiveData()
    private val mAPIParameters: MutableLiveData<APIParameters> = MutableLiveData()
    private val mProgressBarState: MutableLiveData<ProgressBarState> = MutableLiveData()

    init {
        mRating.value = EnumMap(MyCell::class.java)
        mProgressBarState.value = ProgressBarState.INVISIBLE
    }

    enum class ProgressBarState {
        None, VISIBLE, INVISIBLE
    }

    fun refreshMap(key: MyCell, value: String?) {
        mRating.value?.put(key, value)
    }

    fun getProgressBarState(): LiveData<ProgressBarState> = mProgressBarState

    fun getRating(): LiveData<EnumMap<MyCell, String?>> = mRating

    fun getData(): LiveData<APIParameters> = mAPIParameters
    fun setRating() {
        mProgressBarState.postValue(ProgressBarState.VISIBLE)
        val apiParameters = APIParameters("", 1, 1, 1, 1, 1, 1)
        runBlocking {
            Log.d(javaClass.simpleName, "Imitate long operation")
            delay(3000)
        }
        mRating.value?.forEach { (key, _) ->
            when (key) {
                MyCell.AIRCRAFT -> apiParameters.aircraft =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1) ?: 1
                MyCell.FLIGHT -> apiParameters.flight =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1) ?: 1
                MyCell.FOOD -> apiParameters.food =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1)
                MyCell.CROWD -> apiParameters.people =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1) ?: 1
                MyCell.TEXT -> apiParameters.text = mRating.value?.get(key) ?: ""
                MyCell.CREW -> apiParameters.crew =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1) ?: 1
                MyCell.SEATS -> apiParameters.seat =
                    mRating.value?.get(key)?.toFloat()?.roundToInt()?.plus(1) ?: 1
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            mAPIParameters.value = apiParameters
            mProgressBarState.postValue(ProgressBarState.INVISIBLE)
        }
    }
}