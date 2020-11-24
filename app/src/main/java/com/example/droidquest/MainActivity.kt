package com.example.droidquest

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "QuestActivity"
    private val KEY_INDEX = "index" //ключ для метода который сохраняет текущий вопрос
    private val REQUEST_CODE_DECEIT = 0 //ключ для startActivityForResult
    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mDeceitButton: Button
    private lateinit var mNextButton: com.google.android.material.button.MaterialButton
    private lateinit var mBackButton: com.google.android.material.button.MaterialButton
    private lateinit var mQuestionTextView: TextView
    private val mQuestionBank = listOf(   //список вопросов
        Question(R.string.question_android, true),
        Question(R.string.question_linear, false),
        Question(R.string.question_service, false),
        Question(R.string.question_res, true),
        Question(R.string.question_manifest, true),
        Question(R.string.question_oop, false),
        Question(R.string.question_destroy, false),
        Question(R.string.question_textView, true),
        Question(R.string.question_button, true),
        Question(R.string.question_listAdapter, true)
    )
    private var mCurrentIndex = 0  //индекс текущего вопроса
    private var isDeceiter = false  //переменная показывающая подсмотрел ли пользователь ответ

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate вызван")
        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
        }

        mTrueButton = findViewById(R.id.trueButton)
        mFalseButton = findViewById(R.id.falseButton)
        mNextButton = findViewById(R.id.next_button)
        mBackButton = findViewById(R.id.back_button)
        mDeceitButton = findViewById(R.id.deceit_button)
        mQuestionTextView = findViewById(R.id.question_text_view)
        mTrueButton.setOnClickListener {
           checkAnswer(true)
        }
        mFalseButton.setOnClickListener {
           checkAnswer(false)
        }
        //переход к след вопросу при нажатии на TextView
        mQuestionTextView.setOnClickListener{
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        }
        mNextButton.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
            isDeceiter = false
        }
        mDeceitButton.setOnClickListener {
            val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue
            val intent = DeceitActivity.newIntent(this, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_DECEIT) //если пользователь нажимает на кнопку "Обмануть", чтобы подсмотреть ответ, происходит переход в другую активити и ожидается возврат result, т.е подсмотрел ли пользователь ответ
        }
        mBackButton.setOnClickListener{
            if (mCurrentIndex > 0)
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.size
            updateQuestion()
            isDeceiter = false
        }
        updateQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState!!.putInt(KEY_INDEX, mCurrentIndex)
    }

    private fun updateQuestion(){
        val question = mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) //проверка, вернулся ли соответствующий result code
            return
        if (requestCode == REQUEST_CODE_DECEIT){ //проверка, от нужной ли активити вернулся результат
            if (data == null)
                return
            isDeceiter = DeceitActivity.wasAnswerShown(data)
        }
    }

    private fun checkAnswer(userPressedTrue: Boolean){  //проверка ответа пользователя
        val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue  //записываем в переменную правильный ответ на текущий вопрос
        val messageResId = if (isDeceiter) R.string.judgment_toast //если пользователь подсмотрел ответ, выводим этот текст
           else if (userPressedTrue == answerIsTrue)  // иначе если ответ совпадает с правильным выводим ВЕРНО, иначе НЕВЕРНО
            R.string.correct_toast
        else
            R.string.incorrect_toast
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}