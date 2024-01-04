package utils

import platform.Foundation.NSUUID

actual fun generateRandomId() = NSUUID().UUIDString
