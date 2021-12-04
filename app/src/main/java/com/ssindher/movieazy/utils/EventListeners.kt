package com.ssindher.movieazy.utils

interface EventListeners {
    fun click(pos: Int)
}


interface DualEventListeners {
    fun click(pos: Int, flag: Int)
}