package com.pollyanna.pollycall.data

import android.telecom.CallScreeningService
import android.util.Log
import com.pollyanna.pollycall.data.dataclass.Call
import com.pollyanna.pollycall.data.dataclass.CallResponse
import com.pollyanna.pollycall.data.local.CallDao
import com.pollyanna.pollycall.data.remote.PollyCallRemoteDataSource
import com.pollyanna.pollycall.utils.Constants.Companion.DETECT_CALL_TAG
import com.pollyanna.pollycall.utils.Constants.Companion.UNKNOWN_ERROR
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
class PollyCallRepositoryImpl @Inject constructor(
    private val remoteDataSource: PollyCallRemoteDataSource,
    private val callDao: CallDao

) : PollyCallRepository {


    override suspend fun searchScreenCall(inComingNumber: String) = flow {


        var numberInfo: CallResponse<Call?> = CallResponse.Loading()

            // search call data in local database
            callDao.getCallCache(inComingNumber).first()?.let { localCache ->
                emit(CallResponse.Success(localCache))

            } ?: run {
                numberInfo = fetchDataFromRemote(inComingNumber)
                emit(numberInfo)

                if (numberInfo is CallResponse.Success) {

                    // when get data successfully, save the data to local database
                    CoroutineScope(Dispatchers.IO).launch {
                        numberInfo.data?.let { remoteData ->
                            callDao.saveCallCache(remoteData)
                        }
                    }

                }



        }
    }.flowOn(Dispatchers.IO)

    private suspend fun fetchDataFromRemote(inComingNumber: String): CallResponse<Call?> {
        var numberInfo: CallResponse<Call?> = CallResponse.Loading()
        try {
            val response = remoteDataSource.getCallData(inComingNumber)
            response.data?.let { remoteCallData ->

                when (response) {
                    is CallResponse.Success -> {
                        Log.i(DETECT_CALL_TAG, "searchScreenCall: success: $remoteCallData")
                        numberInfo = CallResponse.Success(remoteCallData)
                    }

                    is CallResponse.Error -> {
                        Log.i(DETECT_CALL_TAG, "searchScreenCall: error: ${response.message}")
                        numberInfo = CallResponse.Error(
                            response.message ?: UNKNOWN_ERROR,
                            data = null
                        )
                    }

                    is CallResponse.Loading -> {
                        Log.i(DETECT_CALL_TAG, "searchScreenCall: loading")
                        numberInfo = CallResponse.Loading()
                    }
                }
            } ?: run {
                numberInfo = CallResponse.Error("查無此電話資訊", data = null)
            }
        } catch (e: Exception) {
            numberInfo = CallResponse.Error(e.message ?: UNKNOWN_ERROR, data = null)
        }

        return numberInfo
    }


    // get phone number from flow, pass to viewModel
    override suspend fun getSearchResponse(): StateFlow<CallResponse<Call?>> {
        return flow {
            emit(_phoneSearchResponse)
        }.stateIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun uploadCallData(call: Call): CallResponse<Call?> {
        var uploadResponse: CallResponse<Call?> = CallResponse.Loading()
        withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.uploadCallData(call)

                if (response is CallResponse.Success) {
                    // when upload data successfully, save the data to local database
                    callDao.saveCallCache(call)
                    uploadResponse = CallResponse.Success(response.data)
                    return@withContext
                } else {
                    val errorResponse = response as CallResponse.Error
                    uploadResponse = CallResponse.Error(errorResponse.message ?: UNKNOWN_ERROR)
                    return@withContext
                }

            } catch (e: Exception) {
                uploadResponse = CallResponse.Error(e.message ?: UNKNOWN_ERROR)
                return@withContext
            }

        }
        return uploadResponse
    }


    override fun shouldBlockCall(number: String): Boolean {
        return false
    }
}



