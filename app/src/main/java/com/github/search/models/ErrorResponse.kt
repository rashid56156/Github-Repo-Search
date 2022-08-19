package com.github.search.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


/**
 * API Error response model class
 */
@Parcelize
data class ErrorResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("documentation_url")
	val documentationUrl: String? = null,

	@field:SerializedName("errors")
	val errors: List<ErrorsItem?>? = null
) : Parcelable

@Parcelize
data class ErrorsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("field")
	val field: String? = null,

	@field:SerializedName("resource")
	val resource: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
