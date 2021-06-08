package org.dxworks.buildinspector

class Build(
    var name: String?,
    var id: Number,
    var duration: Int?,
    var timestamp: String?,
    result: String?,
    event: String?,
    var commitId: String?,
    var branch: String?,
    var parentName: String?
) {
    var result : String? = result?.toUpperCase()
    var event : String? = event?.toUpperCase()
}
