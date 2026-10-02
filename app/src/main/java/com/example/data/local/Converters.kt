package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.local.model.PriceTier
import org.json.JSONArray
import org.json.JSONObject

/**
 * Convertisseurs de types Room pour sérialiser et désérialiser
 * les listes complexes telles que les paliers de prix B2B en local.
 */
class Converters {

    @TypeConverter
    fun fromPriceTierList(tiers: List<PriceTier>?): String {
        if (tiers.isNullOrEmpty()) return "[]"
        val array = JSONArray()
        for (tier in tiers) {
            val obj = JSONObject().apply {
                put("minQuantity", tier.minQuantity)
                if (tier.maxQuantity != null) {
                    put("maxQuantity", tier.maxQuantity)
                }
                put("unitPriceFcfa", tier.unitPriceFcfa)
                put("label", tier.label)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toPriceTierList(jsonString: String?): List<PriceTier> {
        if (jsonString.isNullOrBlank() || jsonString == "[]") return emptyList()
        val list = mutableListOf<PriceTier>()
        try {
            val array = JSONArray(jsonString)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val maxQty = if (obj.has("maxQuantity") && !obj.isNull("maxQuantity")) {
                    obj.getInt("maxQuantity")
                } else null

                list.add(
                    PriceTier(
                        minQuantity = obj.optInt("minQuantity", 1),
                        maxQuantity = maxQty,
                        unitPriceFcfa = obj.optDouble("unitPriceFcfa", 0.0),
                        label = obj.optString("label", "")
                    )
                )
            }
        } catch (e: Exception) {
            // En cas d'erreur de parsing, renvoyer une liste vide pour ne pas crasher
            return emptyList()
        }
        return list
    }
}
