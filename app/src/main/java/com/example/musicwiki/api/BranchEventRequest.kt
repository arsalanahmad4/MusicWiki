package com.example.musicwiki.api

data class BranchEventRequest(
    var branch_key: String?,
    var custom_data: CustomData?,
    var customer_event_alias: String?,
    var debug: Boolean? = false,
    var event_data: EventData?,
    var name: String?,
    var user_data: UserData?,
    var metadata: MetaData?
)

data class CustomData(
    var CreatedLink: String?,
    var album: String?= null,
    var artist: String?
)

data class EventData(
    var description: String?,
    var search_query: String?
)

data class UserData(
    var android_id: String?,
    var local_ip: String?,
    var os: String? = "Android",
    var sdk: String? = "android"
)

data class MetaData(
    var android_id: String? = "139dcdee-8d58-40b4-8b2a-fb9a62573a61",
    var aaid : String? = "139dcdee-8d58-40b4-8b2a-fb9a62573a61"
)