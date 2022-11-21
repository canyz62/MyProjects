package com.example.mapwithmarker;

import com.google.gson.annotations.SerializedName;

/**
 * ModuleType enum
 *
 * @author Jörg Quick, Tilo Steinmetzer
 * @version 1.0
 */

public enum ModuleType {
    @SerializedName("Normalladeeinrichtung") STANDARD,
    @SerializedName("Schnellladeeinrichtung") FAST_CHARGING
}
