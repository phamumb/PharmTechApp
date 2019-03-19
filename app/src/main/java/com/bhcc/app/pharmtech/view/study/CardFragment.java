package com.bhcc.app.pharmtech.view.study;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.data.MedicineLab;
import com.bhcc.app.pharmtech.data.model.Medicine;
import com.wajahatkarim3.easyflipview.EasyFlipView;

/**
 * Created by Luat on 10/30/2017.
 */

public class CardFragment extends Fragment {
    private TextView mMedicineName;
    private TextView mBrandName;
    private TextView mSpecial;
    private TextView mCategory;
    private TextView mPurpose;
    private TextView mDeasch;
    private Medicine mMedicine;
    private ImageView mPlayFrontCard;
    private ImageView mPlayBackCard;
    private RelativeLayout mLayout;
    private int size;
    private EasyFlipView mEasyFlipView;
    private final static String ARG_GENERIC_NAME = "generic_name";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String genericName = (String)getArguments().getSerializable(ARG_GENERIC_NAME);
        mMedicine = MedicineLab.get(getActivity()).getMedicine(genericName);
    }

    public static CardFragment newInstance(String genericName) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_GENERIC_NAME, genericName);
        CardFragment fragment = new CardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card,container,false);
        setHasOptionsMenu(true);
        mEasyFlipView  = (EasyFlipView)v.findViewById(R.id.flip);
        mLayout = (RelativeLayout) v.findViewById(R.id.layout);
        mPlayFrontCard = (ImageView)v.findViewById(R.id.front_play_button);
        mPlayBackCard = (ImageView)v.findViewById(R.id.play_back_card);

        // Find audio file for front card

        try {
            String fileName = getFrontFileName();
            int resID = getResources().getIdentifier(fileName, "raw", getActivity().getPackageName());
            final MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), resID);
            if (myMediaPlayer.getDuration() == 0) {
                mPlayFrontCard.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ex) {
            mPlayFrontCard.setVisibility(View.INVISIBLE);
        }
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEasyFlipView.flipTheView();
            }
        });
        //----------------------------------------------------------------
        //                      SET FRONT CARD LISTENER
        mPlayFrontCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String fileName = getFrontFileName();
                    int resID = getResources().getIdentifier(fileName, "raw", getActivity().getPackageName());
                    final MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), resID);
                    myMediaPlayer.start();
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "No audio file for this medicine", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //------------------------------------------------------------------
        //                       SET BACK CARD LISTENER
        //-------------------------------------------------------------------

        try {
            String fileNameBack = getBackFileName();
            Log.i("Test", fileNameBack);
            int resID = getResources().getIdentifier(fileNameBack, "raw", getActivity().getPackageName());
            final MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), resID);
            if (myMediaPlayer.getDuration() == 0) {
                mPlayBackCard.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex) {
            mPlayBackCard.setVisibility(View.INVISIBLE);
        }

        mPlayBackCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   String fileName = getBackFileName();
                    int resID = getResources().getIdentifier(fileName, "raw", getActivity().getPackageName());
                    final MediaPlayer myMediaPlayer = MediaPlayer.create(getActivity(), resID);
                    myMediaPlayer.start();
                }
                catch (Exception ex) {
                    Toast.makeText(getActivity(), "No audio file for this medicine", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mMedicineName = (TextView)v.findViewById(R.id.medicine_name);
        mBrandName = (TextView)v.findViewById(R.id.brand_name);
        mSpecial =(TextView)v.findViewById(R.id.special);
        mCategory =(TextView)v.findViewById(R.id.category);
        mPurpose = (TextView)v.findViewById(R.id.purpose);
        mDeasch = (TextView)v.findViewById(R.id.deasch);
        size = MedicineLab.get(getActivity()).getMedicineSize();
        Log.i("CardFragment","Fragment has been created");
        loadView();
        return v;
    }

    private String getFrontFileName()
    {
        String fileName = mMedicine.getGenericName().toLowerCase();
        StringBuilder stringBuilder = new StringBuilder(fileName);
        Log.i("GenericName",fileName);
        if (fileName.contains("/")&&fileName.contains("-")) {
            Log.i("Contain","/-"+fileName);
            stringBuilder.deleteCharAt(fileName.indexOf('/'));
            stringBuilder.deleteCharAt(fileName.indexOf('-') - 1);

        }else if(fileName.contains("/"))
        {
            Log.i("Contain","/"+fileName);
            stringBuilder.deleteCharAt(fileName.indexOf('/'));
        }
        else if(fileName.contains(" "))
        {
            stringBuilder.deleteCharAt(fileName.indexOf(' '));
        }
        fileName = stringBuilder.toString();
        Log.i("FileName",fileName);
        return fileName;
    }

    private String getBackFileName()
    {
        String fileName = mMedicine.getBrandName();
        StringBuilder stringBuilder = new StringBuilder(fileName);
        fileName = fileName.substring(0, stringBuilder.indexOf("®"));
        fileName = fileName.toLowerCase();

        if (fileName.contains("/")) {
            stringBuilder.deleteCharAt(fileName.indexOf('/'));
            fileName = stringBuilder.toString();
        }
        Log.i("BackFileName", fileName);
        return fileName;
    }
    private void loadView() {
            mMedicineName.setText(mMedicine.getGenericName());
            mBrandName.setText("Brand Name: " + mMedicine.getBrandName());
            mSpecial.setText("Special:"+mMedicine.getSpecial());
            mCategory.setText("Category: " +mMedicine.getCategory());
            mPurpose.setText("Purpose: " + mMedicine.getPurpose());
            mDeasch.setText("DeaSch: " + mMedicine.getDeaSch());
    }
}
