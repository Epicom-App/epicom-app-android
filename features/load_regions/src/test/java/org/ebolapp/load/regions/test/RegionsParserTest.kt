package org.ebolapp.load.regions.test

import org.ebolapp.load.regions.parsers.RegionsParser
import org.ebolapp.load.regions.parsers.RegionsParserImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.io.File
import java.net.URL

class RegionsParserTest {

    @Test
    fun testStatesParsing() {

        val moshi : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val parser : RegionsParser = RegionsParserImpl(
            moshi,
            getFileFromPath(this, "json/states.json").inputStream(),
            getFileFromPath(this, "json/districts.json").inputStream()
        )

        val states = parser.parseStates()

        val statesList  = states.toList()

        assertThat("Parsed 16 states", statesList.size == 16)
    }

    @Test
    fun testDistrictParsing() {

        val moshi : Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val parser : RegionsParser = RegionsParserImpl(
            moshi,
            getFileFromPath(this, "json/states.json").inputStream(),
            getFileFromPath(this, "json/districts.json").inputStream()
        )

        val districtsSequence = parser.parseDistricts()

        val districtsList  = districtsSequence.toList()

        assertThat("Parsed 412 districts", districtsList.size == 412)
    }

    @Test
    @Throws(Exception::class)
    fun fileObjectShouldNotBeNull() {
        val file = getFileFromPath(this, "json/states.json")
        assertThat(file, notNullValue())
    }

    private fun getFileFromPath(obj: Any, fileName: String): File {
        val classLoader = obj.javaClass.classLoader
        val resource: URL = classLoader.getResource(fileName)
        return File(resource.path)
    }
}