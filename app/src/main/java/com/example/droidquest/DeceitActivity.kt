package com.example.droidquest

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DeceitActivity : AppCompatActivity() {
    private lateinit var mAnswerTextView: TextView
    private lateinit var mShowAnswer: Button
    private var mAnswerShown = false

    companion object {
        private const val EXTRA_ANSWER_IS_TRUE = "com.example.droidquest.answer_is_true"
        private const val EXTRA_ANSWER_SHOWN = "com.example.droidquest.answer_show"
        private val KEY_SHOWN = "shown"

        fun newIntent(packageContext: Context?, answerIsTrue: Boolean): Intent? {
            val intent = Intent(packageContext, DeceitActivity::class.java)
            return intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
        }

        fun wasAnswerShown(result: Intent) = result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deceit)
        if (savedInstanceState != null){
            mAnswerShown = savedInstanceState.getBoolean(KEY_SHOWN, false)
        }
        val mAnswerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        mAnswerTextView = findViewById(R.id.answerTextView)
        mShowAnswer = findViewById(R.id.showAnswerButton)
        if (mAnswerShown){   //проверка: если пользователь посмотрел ответ, то
            setText(mAnswerIsTrue)
        }
        mShowAnswer.setOnClickListener {
            setText(mAnswerIsTrue)
        }
    }

    private fun setText(mAnswerIsTrue: Boolean){ //вывод правильного ответа и передача информации о том что пользовотель подсмотрел ответ
        mAnswerTextView.setText(
            if (mAnswerIsTrue) R.string.true_button
            else R.string.false_button)
        setAnswerShownResult(true)
    }

    private fun setAnswerShownResult(isAnswerShow: Boolean){
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN , isAnswerShow)
        setResult(Activity.RESULT_OK, data)
        if (isAnswerShow) mAnswerShown = true
    }

    override fun onSaveInstanceState(outState: Bundle) { //сохраняем данные о том, что пользователь посмотрел ответ
        super.onSaveInstanceState(outState)
        if (mAnswerShown)
            outState!!.putBoolean(KEY_SHOWN, mAnswerShown)
    }
}
