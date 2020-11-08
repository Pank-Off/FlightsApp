package ru.appintheair.flightreviewssubmit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubmitScreenViewModel : ViewModel() {
    private val mRating: MutableLiveData<RatingClass> = MutableLiveData()
    private val ratingClass: RatingClass = RatingClass()
    private val mAPIParameters: MutableLiveData<APIParameters> = MutableLiveData()

    init {
        mRating.value = ratingClass
    }

    fun getRating(): LiveData<RatingClass> = mRating

    fun getData(): LiveData<APIParameters> = mAPIParameters

    fun setRating(listOfRating: HashMap<String, String?>) {
        val apiParameters = APIParameters(null, 1, 1, 1, 1, 1, 1)
        listOfRating.forEach { (key, _) ->
            when (key) {
                "aircraft" -> apiParameters.aircraft =
                    listOfRating[key]?.toInt()?.plus(1)
                "flight" -> apiParameters.flight =
                    listOfRating[key]?.toInt()?.plus(1)
                "food" -> apiParameters.food = listOfRating[key]?.toInt()?.plus(1)
                "people" -> apiParameters.people =
                    listOfRating[key]?.toInt()?.plus(1)
                "text" -> apiParameters.text = listOfRating[key]
                "crew" -> apiParameters.crew = listOfRating[key]?.toInt()?.plus(1)
                "seat" -> apiParameters.seat = listOfRating[key]?.toInt()?.plus(1)
            }
        }
        mAPIParameters.value = apiParameters
    }
}