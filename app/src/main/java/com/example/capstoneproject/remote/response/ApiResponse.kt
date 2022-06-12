package com.example.capstoneproject.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse(

	@field:SerializedName("item")
	val item: List<ItemItem?>? = null,

	@field:SerializedName("info")
	val info: Info? = null
)

data class Info(

	@field:SerializedName("schema")
	val schema: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("_exporter_id")
	val exporterId: String? = null,

	@field:SerializedName("_postman_id")
	val postmanId: String? = null
)

data class Url(

	@field:SerializedName("path")
	val path: List<String?>? = null,

	@field:SerializedName("protocol")
	val protocol: String? = null,

	@field:SerializedName("host")
	val host: List<String?>? = null,

	@field:SerializedName("raw")
	val raw: String? = null
)

data class Raw(

	@field:SerializedName("language")
	val language: String? = null
)

data class Body(

	@field:SerializedName("mode")
	val mode: String? = null,

	@field:SerializedName("options")
	val options: Options? = null,

	@field:SerializedName("raw")
	val raw: String? = null,

	@field:SerializedName("formdata")
	val formdata: List<FormdataItem?>? = null
)

data class ProtocolProfileBehavior(

	@field:SerializedName("disableBodyPruning")
	val disableBodyPruning: Boolean? = null
)

data class FormdataItem(

	@field:SerializedName("src")
	val src: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("key")
	val key: String? = null,

	@field:SerializedName("value")
	val value: String? = null
)

data class ItemItem(

	@field:SerializedName("request")
	val request: Request? = null,

	@field:SerializedName("response")
	val response: List<Any?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("protocolProfileBehavior")
	val protocolProfileBehavior: ProtocolProfileBehavior? = null
)

data class Request(

	@field:SerializedName("method")
	val method: String? = null,

	@field:SerializedName("header")
	val header: List<Any?>? = null,

	@field:SerializedName("body")
	val body: Body? = null,

	@field:SerializedName("url")
	val url: Url? = null
)

data class Options(

	@field:SerializedName("raw")
	val raw: Raw? = null
)
