package com.lsm.supermemories.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lsm.supermemories.data.Repository
import com.lsm.supermemories.data.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
@HiltViewModel
class SentenceViewModel@Inject constructor(
        private val repository: Repository,


) : BaseViewModel() {
    var k = 0
    var contents: MutableLiveData<String> = MutableLiveData("")
    var source: MutableLiveData<String> = MutableLiveData("")


    var toRandom: ArrayList<Pair<String, String>> = ArrayList()
    fun getSentence() {

        viewModelScope.async {
            repository.sentenceDao.getAllSentences().collect() {

                addToArray(it)
            }
        }
    }

    fun addToArray(sentenceList: List<Sentence>) {

        sentenceList.forEach(){sentencePair->
            if (sentencePair.randomDate == get_date()){
                contents.value = sentencePair.contents
                source.value =sentencePair.source



                k=1

            }
        }
        if (k==0) {

            var uid:Long = 3
            sentenceList.forEach() { sentencePair ->
                toRandom.add(Pair(sentencePair.contents, sentencePair.source))
                uid = sentencePair.uid
            }
            val RandomElement = toRandom.random()
            contents.value = RandomElement.first
            source.value = RandomElement.second
            var updateRecord = Sentence(uid = uid,contents =RandomElement.first ,source = RandomElement.second,randomDate= get_date())
            GlobalScope.async {
                repository.sentenceDao.update(updateRecord)
            }

        }
    }

    fun get_date(): String {
        val currentDateTime =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
        var current_date = currentDateTime.format(DateTimeFormatter.ISO_DATE)
        var valid_date = current_date.toString()

        return valid_date
    }
}








