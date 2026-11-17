package com.h.game

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.random.Random

class MainActivity : Activity() {

    private var score = 0
    private lateinit var scoreText: TextView
    private lateinit var target: TextView
    private lateinit var root: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        root = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }

        // Header: small "H" and the score
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, dp(30), 0, 0)
        }
        header.addView(label("H", 28f))
        scoreText = label("0", 48f)
        header.addView(scoreText)
        root.addView(
            header,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        // The big tappable "H"
        target = label("H", 100f).apply {
            typeface = Typeface.create(Typeface.DEFAULT, 900, false)
            setOnClickListener { onTapped() }
        }
        root.addView(
            target,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(root)

        // Start centered once layout is known
        root.post { moveTo(centerX(), centerY(), animate = false) }
    }

    private fun onTapped() {
        score++
        scoreText.text = score.toString()

        val w = root.width.toFloat()
        val h = root.height.toFloat()
        val halfW = target.width / 2f
        val halfH = target.height / 2f

        val minX = maxOf(dp(70).toFloat(), halfW)
        val maxX = maxOf(minX + 1f, w - dp(70))
        val minY = dp(150).toFloat()
        val maxY = maxOf(minY + 1f, h - dp(100))

        moveTo(Random.nextFloat() * (maxX - minX) + minX,
               Random.nextFloat() * (maxY - minY) + minY,
               animate = true)
    }

    /** Moves the target so its centre lands on (cx, cy). */
    private fun moveTo(cx: Float, cy: Float, animate: Boolean) {
        val tx = cx - target.width / 2f
        val ty = cy - target.height / 2f
        if (!animate) {
            target.x = tx
            target.y = ty
            return
        }
        ObjectAnimator.ofPropertyValuesHolder(
            target,
            PropertyValuesHolder.ofFloat(View.X, tx),
            PropertyValuesHolder.ofFloat(View.Y, ty)
        ).apply {
            duration = 400
            interpolator = OvershootInterpolator(2f) // spring-like feel
            start()
        }
    }

    private fun centerX() = root.width / 2f
    private fun centerY() = root.height / 2f

    private fun label(text: String, sizeSp: Float) = TextView(this).apply {
        this.text = text
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp)
        setTypeface(typeface, Typeface.BOLD)
        includeFontPadding = false
        gravity = Gravity.CENTER
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()
}
