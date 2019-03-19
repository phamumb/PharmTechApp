package com.bhcc.app.pharmtech.view;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.view.favorites.FavoriteActivity;
import com.bhcc.app.pharmtech.view.filter.FilterActivity;
import com.bhcc.app.pharmtech.view.navigation.ReplaceFragmentCommand;
import com.bhcc.app.pharmtech.view.quiz.QuizActivity;
import com.bhcc.app.pharmtech.view.review.ReviewActivity;
import com.bhcc.app.pharmtech.view.study.CardFragment;
import com.bhcc.app.pharmtech.view.study.MedicineListActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    public static final String fileName  = "ReviewInfo.txt";
    private CardView mStudyCardView;
    private CardView mPracticeTest;
    private CardView mFavorites;
    private CardView mFilter;
    private CardView mReport;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mStudyCardView = (CardView)findViewById(R.id.card_view_index_cards);
        mPracticeTest = (CardView)findViewById(R.id.pratice_test_card_view);
        mFavorites = (CardView)findViewById(R.id.favorite_card_view);
        mFilter = (CardView)findViewById(R.id.filter_card_view);
        mReport = (CardView)findViewById(R.id.report_card_view);

        mStudyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(MedicineListActivity.newIntent(mContext));
            }
        });

        mPracticeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(QuizActivity.newIntent(mContext));
            }
        });

        mFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FavoriteActivity.newIntent(mContext));
            }
        });

        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FilterActivity.newIntent(mContext));
            }
        });

        mReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ReviewActivity.newItent(mContext));
            }
        });

        createReviewFile();
    }

    /**
     * If there's no fragment in container, display study fragment
     */

    public static Intent newIntent(Context context)
    {
        Intent i = new Intent(context,MainActivity.class);
        return i;
    }



    /**
     * To Create a file to hold review files information
     */
    private void createReviewFile() {
        File file = new File(getApplicationContext().getFilesDir(),fileName);

        if(!file.exists()) {
            try {
                PrintWriter printWriter = new PrintWriter(file);
                printWriter.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
