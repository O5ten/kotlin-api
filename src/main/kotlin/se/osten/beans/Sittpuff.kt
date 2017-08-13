package se.osten.beans

import com.google.gson.annotations.SerializedName
import se.osten.utils.createGuid
import java.io.Serializable

data class Sittpuff(
        val id: String,
        @SerializedName("name") val name: String = "",
        @SerializedName("price") val price :String = "",
        @SerializedName("imageLink") val imageLink: String = "",
        @SerializedName("targetLink") val targetLink: String = "",
        @SerializedName("tags") val tags: List<String> = emptyList()
) : Serializable