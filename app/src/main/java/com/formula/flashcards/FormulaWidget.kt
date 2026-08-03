package com.formula.flashcards

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

class FormulaWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_NEXT = "com.formula.flashcards.NEXT_FORMULA"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_NEXT) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val thisWidget = ComponentName(context, FormulaWidget::class.java)
            val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
            for (widgetId in allWidgetIds) {
                updateWidget(context, appWidgetManager, widgetId)
            }
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.formula_widget)

        val prefs = context.getSharedPreferences("formulas_pref", Context.MODE_PRIVATE)
        val savedSet = prefs.getStringSet("formulas_set", emptySet())
        val formulaList = savedSet?.toList() ?: emptyList()

        val textToShow = if (formulaList.isNotEmpty()) {
            formulaList.random()
        } else {
            "No formulas! Open app to add."
        }

        views.setTextViewText(R.id.txt_formula, textToShow)

        val intent = Intent(context, FormulaWidget::class.java).apply {
            action = ACTION_NEXT
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
