package ru.appintheair.flightreviewssubmit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubmitScreenViewModel : ViewModel() {
    private val mRating: MutableLiveData<APIParameters> = MutableLiveData()
    private val apiParameters: APIParameters = APIParameters()

    init {
        mRating.value = apiParameters
    }

    fun getRating(): LiveData<APIParameters> = mRating

    fun setRating(listOfRating: HashMap<String, String?>) {
        apiParameters.setData(listOfRating)
        mRating.value = apiParameters
    }
}