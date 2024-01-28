package com.example.pollycall.data

import android.util.Log
import androidx.compose.ui.layout.LookaheadLayout
import com.example.pollycall.data.local.CallDao
import com.example.pollycall.data.remote.PollyCallRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Pollyanna Wu on 1/26/24.
 *
 * Repository for getting call data from remote server and local database with SSOT principle
 * this class call api in remoteRemoteDataSource only when local DB has no data.
 *
 * When upload data successfully, save the data to local database
 */
@Singleton
class PollyCallRepositoryImpl @Inject constructor(
    private val remoteDataSource: PollyCallRemoteDataSource,
    private val callDao: CallDao

) : PollyCallRepository {

    companion object {
        private const val TAG = "PollyCallRepositoryImpl"
        private const val UNKNOWN_ERROR = "Unknown error"
    }

    private val phoneNumberFlow = MutableStateFlow<String>("")

    override suspend fun searchScreenCall(number: String): Flow<CallResponse<Call?>> = flow {

        // save number to flow
        phoneNumberFlow.value = number

        // search call data in local database
        val localCallData = callDao.getCallCache(number).first()

        // if there is no call data in local database, get call data from remote server
        if (localCallData != null) {
            emit(CallResponse.Success(localCallData))
        } else {
            try {
                val response = remoteDataSource.getCallData(number)
                response.data?.let { remoteCallData ->
                    when (response) {
                        is CallResponse.Success -> {
                            callDao.saveCallCache(remoteCallData)
                            Log.i(TAG, "searchScreenCall: ${remoteCallData.owner}")
                        }
                        is CallResponse.Error -> {
                            emit(CallResponse.Error(response.message ?: UNKNOWN_ERROR))
                            Log.e(TAG, "searchScreenCall: ${response.message}")
                        }
                        is CallResponse.Loading -> {
                            emit(CallResponse.Loading())
                            Log.i(TAG, "searchScreenCall: loading")
                        }
                    }

                }
            } catch (e: Exception) {
                    emit(CallResponse.Error(e.message ?: UNKNOWN_ERROR))
                }

                // all call data should be search in local database
                val callData = callDao.getCallCache(number).first()
                emit(CallResponse.Success(callData))
            }
        }.flowOn(Dispatchers.IO)

        // get phone number from flow, pass to viewModel
        override fun getScreenCall(): StateFlow<String> = phoneNumberFlow

        override suspend fun uploadCallData(call: Call): Flow<CallResponse<Call>> = flow {

            try {
                val response = remoteDataSource.uploadCallData(call)

                if (response is CallResponse.Success) {

                    // when upload data successfully, save the data to local database
                    callDao.saveCallCache(call)
                    emit(CallResponse.Success(call))
                } else {
                    val errorResponse = response as CallResponse.Error
                    emit(CallResponse.Error(errorResponse.message ?: UNKNOWN_ERROR))
                }

            } catch (e: Exception) {
                emit(CallResponse.Error(e.message ?: UNKNOWN_ERROR))
            }


        }.flowOn(Dispatchers.IO)


        override fun shouldBlockCall(number: String): Boolean {
            return false
        }
    }
