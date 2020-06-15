package io.polydev.public_transport.utils.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("signIn")
    suspend fun authorization(
        @Header("authorization") token: String
    ): Response<AuthorizationResponseBody>

    @POST("finish")
    suspend fun finish(
        @Header("authorization") token: String,
        @Body body: FinishRequestBody
    ): Response<FinishResponseBody>

    @POST("getHistory")
    suspend fun getHistory(
        @Header("authorization") token: String,
        @Body body: HistoryRequestBody
    ): Response<HistoryResponseBody>

    @POST("getNotifications")
    suspend fun getNotifications(
        @Header("authorization") token: String
    ): Response<NotificationsResponseBody>

    @POST("getMapEvents")
    suspend fun getMapEvents(
        @Header("authorization") token: String
    ): Response<MapEventsResponseBody>

    @POST("getRoute")
    suspend fun getRoute(
        @Header("authorization") token: String
    ): Response<RouteResponseBody>


    @POST("getCircles")
    suspend fun getCircles(
        @Header("authorization") token: String
    ): Response<CirclesResponseBody>


}


