package utils

import java.util.UUID

actual fun generateRandomId() = UUID.randomUUID().toString()
