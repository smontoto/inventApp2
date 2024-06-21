package com.montoto.inventapp.util

import com.montoto.inventapp.BuildConfig

class Constants {
    companion object Inventory {
        object Url{
            const val FULL_URL = BuildConfig.BASE_URL + "test_app/inventapp/"
            const val FULL_URL_TO_SAVE_FILES = FULL_URL + "save_article_photos.php"
        }

        object States {
            const val ITEMS_ENTERED = "1"
            const val ITEMS_DELIVERED = "2"
        }

        object ResponseStates{
            const val SUCCESS = "success"
            const val ERROR = "error"
        }

        object Files{
            const val REQUEST_PARAM_SERVER = "archivo[]"
            const val REQUEST_METHOD_SERVER = "POST"
            const val ID_PARAM = "id"
        }
    }
}