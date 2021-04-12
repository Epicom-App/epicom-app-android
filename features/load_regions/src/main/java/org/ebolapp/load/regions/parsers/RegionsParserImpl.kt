package org.ebolapp.load.regions.parsers

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import okio.buffer
import okio.source
import org.ebolapp.load.regions.entities.DistrictRegion
import org.ebolapp.load.regions.entities.StateRegion
import java.io.InputStream

interface RegionsParser {
    fun parseStates(): Sequence<StateRegion>
    fun parseDistricts(): Sequence<DistrictRegion>
}


class RegionsParserImpl(
    private val moshiParser: Moshi,
    private val statesInputStream: InputStream,
    private val districtsInputStream: InputStream
) : RegionsParser {

    override fun parseStates(): Sequence<StateRegion> {
        return StatesStreamingParser(moshiParser).parse(JsonReader.of(statesInputStream.source().buffer()))
    }

    override fun parseDistricts(): Sequence<DistrictRegion> {
        return DistrictsStreamingParser(moshiParser).parse(JsonReader.of(districtsInputStream.source().buffer()))
    }

    private class StatesStreamingParser(moshi: Moshi) {

        private val statesAdapter: JsonAdapter<StateRegion> =
            moshi.adapter(StateRegion::class.java)

        fun parse(reader: JsonReader): Sequence<StateRegion> {
            return sequence {
                reader.beginArray()
                while (reader.hasNext()) {
                    statesAdapter.fromJson(reader)?.let { yield(it) }
                }
                reader.endArray()
            }
        }
    }

    private class DistrictsStreamingParser(moshi: Moshi) {

        private val districtsAdapter: JsonAdapter<DistrictRegion> =
            moshi.adapter(DistrictRegion::class.java)

        fun parse(reader: JsonReader): Sequence<DistrictRegion> {
            return sequence {
                reader.beginArray()
                while (reader.hasNext()) {
                    districtsAdapter.fromJson(reader)?.let { yield(it) }
                }
                reader.endArray()
            }
        }
    }
}
