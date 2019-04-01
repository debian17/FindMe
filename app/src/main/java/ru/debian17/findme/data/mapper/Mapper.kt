package ru.debian17.findme.data.mapper

interface Mapper<in T, out V> {

    fun map(obj: T): V

}