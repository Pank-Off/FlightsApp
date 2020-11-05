package ru.appintheair.flightreviewssubmit

class APIParameters {
    private var mListOfRating: HashMap<String, String?> = LinkedHashMap()
    fun setData(listOfRating: HashMap<String, String?>) {
        mListOfRating = listOfRating
    }

    override fun toString(): String {
        return "APIParameters($mListOfRating)"
    }

    fun getRating(): HashMap<String, Float> {
        val ratingMap = HashMap<String, Float>()
        mListOfRating.forEach { (key, _) ->
            if (key != "flight" && key != "text") {
                val value = mListOfRating[key]?.toFloat() ?: 0f
                ratingMap[key] = value
            }
        }
        return ratingMap
    }

    fun getAPIMap(): HashMap<String, String?> = mListOfRating
}

