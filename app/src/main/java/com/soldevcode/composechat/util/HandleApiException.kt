package com.soldevcode.composechat.util

import com.soldevcode.composechat.util.Constants.API_KEY_ERROR
import com.soldevcode.composechat.util.Constants.UNKNOWN_ERROR

fun handleApiExceptions(e: Int): String {
    return when (e) {
        401 -> API_KEY_ERROR
        else -> UNKNOWN_ERROR
    }
}