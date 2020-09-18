package com.arjun.thinkpod.network

import com.arjun.thinkpod.network.ITunesApi.Json
import okhttp3.ResponseBody
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.lang.reflect.Type

/**
 * References: @see "https://speakerdeck.com/jakewharton/making-retrofit-work-for-you-ohio-devfest-2016"
 * @see "https://stackoverflow.com/questions/40824122/android-retrofit-2-multiple-converters-gson-simplexml-error"
 */
class XmlOrJsonConverterFactory : Converter.Factory() {
    private val json: Converter.Factory
    private val xml: Converter.Factory
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        for (annotation in annotations) {
            if (annotation == ITunesApi.Xml::class.java) {
                return xml.responseBodyConverter(type, annotations, retrofit)
            }
            if (annotation == Json::class.java) {
                return json.responseBodyConverter(type, annotations, retrofit)
            }
        }
        return null
    }

    init {
        json = GsonConverterFactory.create()
        xml = SimpleXmlConverterFactory.createNonStrict(Persister(AnnotationStrategy()))
    }
}