package com.formula.flashcards

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews

class FormulaWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.formula_widget)
            views.setTextViewText(R.id.widget_title, "Formula Flash")
            views.setTextViewText(R.id.widget_text, "یادگیری فرمول‌ها")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
