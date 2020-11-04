package ru.appintheair.flightreviewssubmit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubmitScreenViewModel : ViewModel() {
    private val mRating: MutableLiveData<APIParameters> = MutableLiveData()
    private val apiParameters: APIParameters = APIParameters()

    fun getRating(): LiveData<APIParameters> = mRating

    fun setRating(listOfRating: HashMap<String, String?>) {
        if (listOfRating.containsKey("text")) {
            apiParameters.setTextComment(listOfRating["text"])
        }
        if (listOfRating.containsKey("flight")) {
            apiParameters.setFlightRating(listOfRating["flight"])
        }
        if (listOfRating.containsKey("food")) {
            apiParameters.setFoodRating(listOfRating["food"])
        }

        mRating.value = apiParameters
    }
}