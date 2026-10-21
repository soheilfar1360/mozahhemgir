package art.navastudio.mozahhemgir

import android.telecom.Call
import android.telecom.CallScreeningService

class SpamCallScreeningService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        // Android only requires a response for incoming calls.
        if (callDetails.callDirection != Call.Details.DIRECTION_INCOMING) return

        // Fail-open: if anything unexpected happens, allow the call rather than
        // risk incorrectly blocking a legitimate caller.
        val response = try {
            val rawNumber = callDetails.handle?.schemeSpecificPart
            val shouldBlock = RuleStore.shouldBlock(this, rawNumber)

            if (shouldBlock) {
                RuleStore.incrementBlockedCount(this)
                CallResponse.Builder()
                    .setDisallowCall(true)
                    .setRejectCall(true)
                    .setSkipNotification(true)
                    .build()
            } else {
                allowResponse()
            }
        } catch (_: Throwable) {
            allowResponse()
        }

        respondToCall(callDetails, response)
    }

    private fun allowResponse(): CallResponse =
        CallResponse.Builder()
            .setDisallowCall(false)
            .setRejectCall(false)
            .setSkipNotification(false)
            .build()
}
