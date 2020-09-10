package com.autonomad.ui.insurance.insurance_list

import androidx.lifecycle.MutableLiveData
import java.io.Serializable

class SerializableLiveData<T> : MutableLiveData<T>(), Serializable