package com.example.noteappktor.data.remote

import com.example.noteappktor.other.Constanst.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

/**
 * el intercepter es algo que cada request  que se haga la modificara
 *
 * Ya que utilizamos autentificacion para nuestros end poins
 * debemmos adjuntar el correo y contraseña cada que querramos acceder a nuestras
 * apis, lo cual a largo plazo es bolder place
 *
 */
class BasicAuthInterceptor: Interceptor {
    /**
     * en esta funcion se podra agregar los diferentes interceptors
     * en este caso necesitamos adjuntar correo y contraseña para casi todos los endpoins
     * excepto dos
     * esto se hace generando una lista de excepciones
     */
    val email : String? = null
    val password: String? = null
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.encodedPath in IGNORE_AUTH_URLS){
            return chain.proceed(request)
        }

        val authenticatedRequest = request.newBuilder()
            .header("Authorization",Credentials.basic(email?:"",password?:""))
            .build()


        return  chain.proceed(authenticatedRequest)
    }
}