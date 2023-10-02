package com.nate.frequentask.data
import com.google.gson.annotations.SerializedName
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

data class Theme(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String = UUID.randomUUID().toString(),
    @SerializedName("active")val active: Boolean,
    @SerializedName("tasks")val tasks: List<Task>,
    @SerializedName("created")val created: Long = Date().time
) {
    data class Task(
        @SerializedName("name") val name: String,
        @SerializedName("id") val id: String = UUID.randomUUID().toString(),
        @SerializedName("description") val description: String,
        @SerializedName("note") val note: String,
        @SerializedName("frequency") val frequency: Long,
        @SerializedName("next_due_on") val nextDueOn: Long,
        @SerializedName("last_completed_on") val lastCompletedOn: Long? = null
    )
}
fun Theme.Task.updateFrequency(it: Number): Theme.Task {
    val freq = it.toLong()
    return copy(frequency = freq, nextDueOn = lastCompletedOn?.plus(TimeUnit.DAYS.toMillis(freq))?:nextDueOn)
}
fun Theme.Task.updateLastCompleted(it: Long): Theme.Task {
    return copy(lastCompletedOn = it, nextDueOn = it.plus(TimeUnit.DAYS.toMillis(frequency)))
}
fun Theme.Task.complete(note: String): Theme.Task {
    val now = Date().time
    return copy(lastCompletedOn = now, nextDueOn = now.plus(TimeUnit.DAYS.toMillis(frequency)), note = note)
}
