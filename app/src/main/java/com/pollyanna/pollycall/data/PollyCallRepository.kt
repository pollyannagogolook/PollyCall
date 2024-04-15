package com.pollyanna.pollycall.data

import android.util.Log
import com.pollyanna.pollycall.data.dataclass.Call
import com.pollyanna.pollycall.data.dataclass.CallResponse
import com.pollyanna.pollycall.data.local.PollyCallLocalDataSource
import com.pollyanna.pollycall.data.remote.PollyCallRemoteDataSource
import com.pollyanna.pollycall.utils.Constants.Companion.DETECT_CALL_TAG
import com.pollyanna.pollycall.utils.Constants.Companion.ERROR_UNKNOWN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
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
class PollyCallRepository @Inject constructor(
    private val remoteDataSource: PollyCallRemoteDataSource,
    private val localDataSource: PollyCallLocalDataSource
) {
    private val _phoneSearchResponse = MutableStateFlow<CallResponse>(CallResponse.Loading)
    val phoneSearchResponse: StateFlow<CallResponse> = _phoneSearchResponse

    suspend fun searchScreenCall(incomingNumber: String): CallResponse =
        withContext(Dispatchers.IO) {

            var numberInfo: CallResponse = CallResponse.Loading

            // search call data in local database
            localDataSource.getCallCache(incomingNumber).first()?.let { localCache ->
                numberInfo = CallResponse.Success(localCache)
            } ?: run {
                numberInfo = remoteDataSource.getCallData(incomingNumber)

                if (numberInfo is CallResponse.Success) {
                    // when get data successfully, save the data to local database
                    (numberInfo as CallResponse.Success).data?.let { remoteData ->
                        localDataSource.saveCallCache(remoteData)
                    }
                }
            }

            _phoneSearchResponse.value = numberInfo
            numberInfo
        }

    suspend fun uploadCallData(call: Call): CallResponse = withContext(Dispatchers.IO) {
        try {
            val response = remoteDataSource.uploadCallData(call)
            if (response is CallResponse.Success) {
                // when upload data successfully, save the data to local database
                localDataSource.saveCallCache(call)
            }
            if (call.isScam) {
                localDataSource.saveScamCall(call)
            }
            response
        } catch (e: Exception) {
            CallResponse.Error(e.message ?: ERROR_UNKNOWN)
        }
    }

}



