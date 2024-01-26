package com.example.pollycall.data

import com.example.pollycall.data.local.CallDao
import com.example.pollycall.data.remote.PollyCallRemoteDataSource
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

    override suspend fun getCallData(number: String): CallResponse<Call?> {
        // search call data in local database
        val localCallData = callDao.getCallCache(number).data

        // if there is no call data in local database, get call data from remote server
        if (localCallData != null) {
            return CallResponse.Success(localCallData)
        } else {
            try {

                val response = remoteDataSource.getCallData()

                response.data?.let { remoteCallData ->

                    if (response is CallResponse.Success) {
                        callDao.saveCallCache(remoteCallData)
                    } else if (response is CallResponse.Error) {
                        return CallResponse.Error(response.message ?: UNKNOWN_ERROR)
                    }
                }

            } catch (e: Exception) {
                return CallResponse.Error(e.message ?: UNKNOWN_ERROR)
            }

        }

        // all call data should be search in local database
        return callDao.getCallCache(number)
    }


    override suspend fun uploadCallData(call: Call): CallResponse<Call> {

        return try {
            val response = remoteDataSource.uploadCallData(call)

            if (response is CallResponse.Success) {

                // when upload data successfully, save the data to local database
                callDao.saveCallCache(call)
                CallResponse.Success(call)
            } else {
                val errorResponse = response as CallResponse.Error
                CallResponse.Error(errorResponse.message ?: UNKNOWN_ERROR)
            }

        } catch (e: Exception) {
            CallResponse.Error(e.message ?: UNKNOWN_ERROR)
        }


    }
}