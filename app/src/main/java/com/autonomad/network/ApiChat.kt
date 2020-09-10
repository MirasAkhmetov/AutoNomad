package com.autonomad.network

import com.autonomad.data.models.Page
import com.autonomad.data.models.chat.ChatParticipant
import com.autonomad.data.models.chat.Dialog
import com.autonomad.data.models.chat.Message
import com.autonomad.data.models.chat.PostMessage
import retrofit2.Response
import retrofit2.http.*

interface ApiChat {
    @GET("chat/v1/dialogues/")
    suspend fun getDialogs(@Query("offset") offset: Int? = null, @Query("limit") limit: Int? = null): Response<Page<Dialog>>

    @GET("chat/v1/dialogues/{id}/")
    suspend fun getDialog(@Path("id") dialogId: Int): Response<Dialog>

    @GET("chat/v1/dialogues/default/")
    suspend fun getDefault(): Response<Dialog>

    @GET("chat/v1/dialogues/{id}/messages/")
    suspend fun getMessages(
        @Path("id") dialogId: Int,
        @Query("from_message_id") fromMessage: String? = null,
        @Query("limit") limit: Int? = null
    ): Response<Page<Message>>

    @GET("chat/v1/dialogues/{id}/participants/")
    suspend fun getParticipants(@Path("id") dialogId: Int): Response<ChatParticipant>

    @POST("habars/messages/")
    suspend fun sendMessage(@Body message: PostMessage): Response<Message>

    @GET("auto-requests/me-client/dialogues/")
    suspend fun clientDialogs(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("auto_request_id") autoRequestId: Int? = null,
        @Query("auto_response_id") autoResponseId: Int? = null
    ): Response<Page<Dialog>>

    @GET("auto-requests/me-master/dialogues/")
    suspend fun masterDialogs(
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("auto_request_id") autoRequestId: Int? = null,
        @Query("auto_response_id") autoResponseId: Int? = null
    ): Response<Page<Dialog>>
}