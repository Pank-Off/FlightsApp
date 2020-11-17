package ru.appintheair.flightreviewssubmit


sealed class ViewHolderType(val viewType: ViewTypeEnum) {
    data class Ordinary(val text: String, val cell: MyCell) : ViewHolderType(ViewTypeEnum.ORDINARY)
    data class Crowd(val text: String, val cell: MyCell) : ViewHolderType(ViewTypeEnum.CROWD)
}