package ru.appintheair.flightreviewssubmit

class APIParameters {
    private var mText: String? = null
    private var mFood: String? = "food"
    private var mFlight: String? = null
    private var mCrew: String? = null
    private var mAircraft: String? = null
    private var mSeat: String? = null
    private var mPeople: String? = null

    fun setFlightRating(flight: String?) {
        mFlight = flight
    }

    fun setTextComment(text: String?) {
        mText = text
    }

    fun setFoodRating(food: String?) {
        mFood = food
    }

    override fun toString(): String {
        return "APIParameters(mText=$mText, mFood=$mFood, mFlight=$mFlight, mCrew=$mCrew, mAircraft=$mAircraft, mSeat=$mSeat, mPeople=$mPeople)"
    }
}