package com.example.musicwiki.model

import com.google.gson.annotations.SerializedName

data class BranchResponseData(
    @SerializedName("+click_timestamp")var clickTimeStamp: Int?,
    @SerializedName("+clicked_branch_link")var branchLinkClicked: Boolean?,
    @SerializedName("+is_first_session")var isFirstSession: Boolean?,
    var genere: String?
)