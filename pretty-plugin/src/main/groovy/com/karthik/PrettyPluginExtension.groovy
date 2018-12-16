package com.karthik
/**
 * Extension to enable for disable pretty print.
 */
class PrettyPluginExtension{
    def enabled = false

    def setEnabled(boolean enabled) {
        this.enabled = enabled
    }

    def getEnabled() {
        return enabled
    }
}