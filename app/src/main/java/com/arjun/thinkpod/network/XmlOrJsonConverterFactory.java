package com.arjun.thinkpod.network;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class XmlOrJsonConverterFactory extends Converter.Factory {

    private final Converter.Factory json;
    private final Converter.Factory xml;

    public XmlOrJsonConverterFactory() {
        json = GsonConverterFactory.create();
        xml = SimpleXmlConverterFactory.createNonStrict(new Persister(new AnnotationStrategy()));
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {

            if (annotation.annotationType() == Xml.class) {
                return xml.responseBodyConverter(type, annotations, retrofit);
            }

            if (annotation.annotationType() == Json.class) {
                return json.responseBodyConverter(type, annotations, retrofit);
            }
        }
        return null;
    }
}
