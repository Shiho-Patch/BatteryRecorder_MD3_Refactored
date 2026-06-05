package yangfentuozi.batteryrecorder.ui.navigation

/**
 * 应用内导航路由定义。
 *
 * 统一集中在这里，避免字符串散落在各 Screen / NavHost 调用点。
 */
sealed class NavRoute(val route: String) {
    object Home : NavRoute("home")
    object History : NavRoute("history")
    object Settings : NavRoute("settings")

    // 应用预测详情页无入参，直接复用固定 route。
    object PredictionDetail : NavRoute("prediction")
    object HistoryList : NavRoute("history/list/{type}") {
        fun createRoute(type: String): String = "history/list/$type"
    }

    object RecordDetail : NavRoute("record/{type}/{name}") {
        fun createRoute(type: String, name: String): String = "record/$type/$name"
    }
}
