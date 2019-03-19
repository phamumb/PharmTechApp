package com.bhcc.app.pharmtech.view.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.MedicineSchema;
import com.bhcc.app.pharmtech.data.model.Medicine;
import com.bhcc.app.pharmtech.data.model.Question;
import com.bhcc.app.pharmtech.view.MainActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Luat on 11/14/2017.
 */

public class QuizFillFragment extends Fragment {

    // Widgets
    private TextView mDoneQuestionText;
    private ProgressBar mProgressBar;
    private TextView mQuestionText;
    private Button mNextButton;
    private LinearLayout mQuestionLayout;
    private EditText mEditText;

    // Data List
    private List<Question> mQuestionList;
    private List<Medicine> mMedicines;

    // Lists
    private String[] topicList;
    private String[] fieldList;
    private static int numQuiz;

    // variables
    private int index;
    private int correct;
    private int randomCorrect;

    // Filename
    String fileName;

    // Bundle argument id
    private static final String EXTRA_TOPIC_LIST = "extra: topic list";
    private static final String EXTRA_FIELD_LIST = "extra: field list";
    private static final String EXTRA_NUM_QUIZ = "extra: num quiz";

    public static QuizFillFragment newInstance(String[] topicList, String[] fieldList,
                                               int numQuiz) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(EXTRA_TOPIC_LIST, topicList);
        bundle.putStringArray(EXTRA_FIELD_LIST, fieldList);
        bundle.putInt(EXTRA_NUM_QUIZ, numQuiz);
        QuizFillFragment quizFragment = new QuizFillFragment();
        quizFragment.setArguments(bundle);
        return quizFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File reviewInfo = new File(getActivity().getFilesDir(), MainActivity.fileName);
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(reviewInfo, true)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyy_HHmmss");
            fileName = dateFormat.format(new Date()).toString();
            printWriter.append(fileName + "\n");
            printWriter.close();
        } catch (Exception ex) {
        }

        topicList = getArguments().getStringArray(EXTRA_TOPIC_LIST);
        fieldList = getArguments().getStringArray(EXTRA_FIELD_LIST);
        numQuiz = getArguments().getInt(EXTRA_NUM_QUIZ, 0);
        mMedicines = findMedicinesQuiz(topicList);
        mQuestionList = new ArrayList<>();
        index =0; correct =0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveToFile();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        mDoneQuestionText = (TextView) v.findViewById(R.id.done_question_text);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        mQuestionText = (TextView) v.findViewById(R.id.question_text);
        mNextButton = (Button) v.findViewById(R.id.next_button);
        mQuestionLayout = (LinearLayout) v.findViewById(R.id.question_layout);

        mEditText = new EditText(getActivity());
        mQuestionLayout.addView(mEditText);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mNextButton.getText().toString().equals("Submit")) {
                        String userAnwer = mEditText.getText().toString().toLowerCase();
                        String correctAnswer = mQuestionList.get(index).getCorrectAnswer().toLowerCase();
                        Log.i("TestString","User: " + userAnwer+"Correct: " + correctAnswer);
                        if (userAnwer.equals(correctAnswer)){
                            mEditText.setTextColor(getResources().getColor(R.color.correct_color));
                            correct++;
                        }
                        else
                            mEditText.setTextColor(getResources().getColor(R.color.incorrect_color));
                        mQuestionList.get(index).setUserAnswer(userAnwer);
                        //mNextButton.setBackground(getResources().getDrawable(R.drawable.corner_radius_enable));
                        mNextButton.setText("Next");
                        setEnable(false);
                    } else {
                        if(index<numQuiz-1) {
                            setEnable(true);
                            index++;
                            updateUI();
                        }else{
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_container, SummaryFragment.createFragment(correct,numQuiz), "NewFragmentTag");
                            ft.commit();
                        }
                    }

            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.equals("")) {
                    mNextButton.setBackground(getResources().getDrawable(R.drawable.corner_radius_enable));
                    mNextButton.setEnabled(true);
                }
                else
                    mNextButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        updateUI();
        return v;
    }

    private void updateUI() {

        double percent = ((index + 1) / (double) numQuiz) * 100;
        mDoneQuestionText.setText("Question " + (index + 1) + "/" + numQuiz + "(" + (int) percent + "%)");
        mProgressBar.setProgress((int) percent);
        mEditText.setText("");
        mEditText.setHint("Enter your answer here");
        mEditText.setTextColor(Color.BLACK);
        mNextButton.setText("Submit");

        // Shuffle the List.
        Collections.shuffle(mMedicines);

        // Disable button and reset the view
        mNextButton.setBackground(getResources().getDrawable(R.drawable.corner_radius_disable));
        mNextButton.setEnabled(false);

        // Insert emtpy question object to List<Question>
        Question mQuestion = new Question();
        mQuestionList.add(mQuestion);

        // Random field list.
        int randomField = generateRandom(fieldList.length);

        String question = "What is the " + fieldList[randomField] + " of " +
                mMedicines.get(index).getGenericName() + "/" + mMedicines.get(index).getBrandName();

        mQuestionList.get(index).setQuestion(question);
        mQuestionText.setText(question);

        // Set correct answer for specific question list.
        String tempCorrect = randomIndex(fieldList[randomField]);

        // Set correct answer for Question object.
        mQuestionList.get(index).setCorrectAnswer(tempCorrect);
        Log.i("Correct", tempCorrect);
    }

    private int generateRandom(int length) {
        Random random = new Random();
        int randomNumber = Math.abs(random.nextInt() % length);
        Log.i("Random Number", String.valueOf(randomNumber));
        return randomNumber;
    }

    private String randomIndex(String list)
    {
        String temp ="";
        Medicine medicine = mMedicines.get(index);
        switch (list) {
            case MedicineSchema.MedicineTable.Cols.PURPOSE:
                temp = medicine.getPurpose();
                break;
            case MedicineSchema.MedicineTable.Cols.DEASCH:
                temp = medicine.getDeaSch();
                break;
            case MedicineSchema.MedicineTable.Cols.SPECIAL:
                temp = medicine.getSpecial();
                break;
            case MedicineSchema.MedicineTable.Cols.CATEGORY:
                temp = medicine.getCategory();
                break;
        }
        if (temp.equals("") || temp.equals("-")) {
            temp = "N/A";
        }
        return temp;
    }

    private void setEnable(boolean flag) {
        for (int i = 0; i < mQuestionLayout.getChildCount(); i++) {
            View child = mQuestionLayout.getChildAt(i);
            child.setEnabled(flag);
        }
    }

    private void saveToFile() {
        File file = new File(getActivity().getFilesDir(), fileName);
        try {
            PrintWriter printWriter = new PrintWriter(file);

            for (Question q : mQuestionList) {
                printWriter.write(q.getQuestion() + " ?\n");
                /*printWriter.write("Your Answer: " +
                        ((RadioButton) getView().findViewById(rgChoices[i].getCheckedRadioButtonId())).getText() + "\n");
                        */
                Log.i("WriteToFile",q.getQuestion());
                printWriter.write(q.getUserAnswer() + "\n");
                printWriter.write((q.getCorrectAnswer() + "\n"));
            }

            printWriter.close();
        } catch (Exception ex) {
        }
    }
    private List<Medicine> findMedicinesQuiz(String[] topicList) {

        String whereArgs = "(";
        for (int i = 0; i < topicList.length; i++) {
            whereArgs += "?";
            if (i != topicList.length - 1)
                whereArgs += ",";
        }
        whereArgs += ")";

        List<Medicine> medicinesQuiz = MedicineLab.get(getActivity())
                .getSpecificMedicine("StudyTopic IN " + whereArgs, topicList, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);
        return medicinesQuiz;
    }
}
