package ru.appintheair.flightreviewssubmit

class APIParameters {
    private var mListOfRating: HashMap<String, String?>? = null
    fun setData(listOfRating: HashMap<String, String?>) {
        mListOfRating = listOfRating
    }

    override fun toString(): String {
        return "APIParameters($mListOfRating)"
    }

}