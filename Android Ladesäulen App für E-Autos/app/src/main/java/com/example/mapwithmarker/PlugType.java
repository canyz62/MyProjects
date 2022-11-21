package com.example.mapwithmarker;

import com.google.gson.annotations.SerializedName;

/**
 * PlugType enum
 *
 * @author Jörg Quick, Tilo Steinmetzer
 * @version 1.0
 */

public enum PlugType {
    @SerializedName("AC Steckdose Typ 2") AC_PLUG_TYPE_2,
    @SerializedName("AC Kupplung Typ 2") AC_CLUTCH_TYPE_2,
    @SerializedName("AC Schuko") AC_SCHUKO,
    @SerializedName("DC Kupplung Combo") DC_CLUTCH_COMBO,
    @SerializedName("DC CHAdeMO") DC_CHADEMO
}
