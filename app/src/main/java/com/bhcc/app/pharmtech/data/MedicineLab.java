package com.bhcc.app.pharmtech.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bhcc.app.pharmtech.data.model.Medicine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Singleton + Database
public class MedicineLab {
    private List<Medicine> medicines;  // to hold all medicines
    private static MedicineLab medicineLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * To get the medicineLab
     * @param context
     * @return MedicineLab
     */
    public static MedicineLab get(Context context) {
        if (medicineLab == null) {
            medicineLab = new MedicineLab(context);
        }
        return medicineLab;
    }

    /**
     * Constructor
     * @param context
     */
    private MedicineLab(Context context) {
        mContext = context.getApplicationContext();

        // get a database helper
        MedicineBaseHelper myDbHelper = new MedicineBaseHelper(mContext);

        // try to create a database
        try {
            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        // get a database helper
        mDatabase = myDbHelper.getWritableDatabase();

        medicines = new ArrayList<>();
        MedicineCursorWrapper cursor = queryMedicines(null, null, MedicineSchema.MedicineTable.Cols.GENERIC_NAME);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                medicines.add(cursor.getMedicine());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

    }

    /**
     * To get all medicines
     * @return Medicines List
     */
    public List<Medicine> getMedicines() {
        return medicines;
    }

    public List<Medicine> getFavortieMedicines()
    {
        List<Medicine> listFavoriteMedicine = new ArrayList<>();
        for(int i =0;i<medicines.size();i++)
        {
            if(medicines.get(i).isFavorite()==true)
                listFavoriteMedicine.add(medicines.get(i));
        }
        return listFavoriteMedicine;
    }

    // get one medicine

    /**
     * To get the medicine w/ specific generic name
     * @param genericName
     * @return Medicine
     */
    public Medicine getMedicine(String genericName) {

        MedicineCursorWrapper cursor = queryMedicines(
                MedicineSchema.MedicineTable.Cols.GENERIC_NAME + " = ?",
                new String[] { genericName },
                MedicineSchema.MedicineTable.Cols.GENERIC_NAME
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMedicine();
        } finally {
            cursor.close();
        }
    }

    public int getMedicineSize(){return medicines.size();}

    /**
     * To get medicines w/ specific arguments
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     * @return List of Medicines
     */
    public List<Medicine> getSpecificMedicine(String whereClause, String[] whereArgs, String orderBy) {
        MedicineCursorWrapper cursor = queryMedicines(whereClause, whereArgs, orderBy);
        List<Medicine> list = new ArrayList<>();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getMedicine());
                cursor.moveToNext();
            }

            return list;
        } finally {
            cursor.close();
        }
    }

    /**
     * To get medicines w/ the raw SQL Command
     * @param rawSql
     * @param whereArgs
     * @return List of Medicines
     */
    public List<Medicine> getMedicinesWithRawSql(String rawSql, String[] whereArgs) {
        MedicineCursorWrapper cursor = rawQueryCrimes(rawSql, whereArgs);
        List<Medicine> list = new ArrayList<>();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getMedicine());
                cursor.moveToNext();
            }

            return list;
        } finally {
            cursor.close();
        }
    }

    /**
     * To get study topics
     * @return List of Study Topics
     */
    public List<String> getStudyTopics() {
        MedicineCursorWrapper cursor = rawQueryCrimes(
                "SELECT DISTINCT " + MedicineSchema.MedicineTable.Cols.STUDY_TOPIC + " FROM " +
                MedicineSchema.MedicineTable.NAME, null);
        List<String> list = new ArrayList<>();

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Log.i("test", cursor.getString(cursor.getColumnIndex(MedicineSchema.MedicineTable.Cols.STUDY_TOPIC)));
                list.add(cursor.getString(cursor.getColumnIndex(MedicineSchema.MedicineTable.Cols.STUDY_TOPIC)));
                cursor.moveToNext();
            }

            return list;
        } finally {
            cursor.close();
        }
    }

    /**
     * To get a cursor wrapper w/ arguments
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     * @return MedicineCursorWrapper
     */
    private MedicineCursorWrapper queryMedicines(String whereClause, String[] whereArgs, String orderBy) {
        Cursor cursor = mDatabase.query(
                MedicineSchema.MedicineTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                orderBy  // orderBy
        );

        return new MedicineCursorWrapper(cursor);
    }

    /**
     * To get a cursor wrapper w/ a raw SQL Command
     * @param rawSql
     * @param whereArgs
     * @return MedicinCursorWrapper
     */
    private MedicineCursorWrapper rawQueryCrimes(String rawSql, String[] whereArgs) {
        Cursor cursor = mDatabase.rawQuery(rawSql, whereArgs);

        return new MedicineCursorWrapper(cursor);
    }

    /**
     * To update medicineLab w/ specific arguments
     * @param whereClause
     * @param whereArgs
     * @param orderBy
     */
    public void updateMedicineLab(String whereClause, String[] whereArgs, String orderBy) {
        MedicineCursorWrapper cursor = queryMedicines(whereClause, whereArgs, orderBy);

        try {
            if (cursor.getCount() == 0) {
                return;
            }

            medicines.clear();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                medicines.add(cursor.getMedicine());
                cursor.moveToNext();
            }

            return;
        } finally {
            cursor.close();
        }
    }

    /**
     * To sort the medicineLab Ascending
     */
    public void sortAscending() {
        Collections.sort(medicines, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o1.getGenericName().compareTo(o2.getGenericName());
            }
        });
    }

    /**
     * To sort the medicineLab Descending
     */
    public void sortDescending() {
        Collections.sort(medicines, Collections.<Medicine>reverseOrder(new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o1.getGenericName().compareTo(o2.getGenericName());
            }
        }));
    }

    private static ContentValues getContentValues(Medicine medicine) {
        ContentValues values = new ContentValues();
        values.put(MedicineSchema.MedicineTable.Cols.GENERIC_NAME, medicine.getGenericName());
        values.put(MedicineSchema.MedicineTable.Cols.BRAND_NAME, medicine.getBrandName());
        values.put(MedicineSchema.MedicineTable.Cols.CATEGORY, medicine.getCategory());
        values.put(MedicineSchema.MedicineTable.Cols.FAVORITE, medicine.isFavorite() ? 1 : 0);
        values.put(MedicineSchema.MedicineTable.Cols.DEASCH, medicine.getDeaSch());
        values.put(MedicineSchema.MedicineTable.Cols.SPECIAL,medicine.getSpecial());
        values.put(MedicineSchema.MedicineTable.Cols.PURPOSE,medicine.getPurpose());
        values.put(MedicineSchema.MedicineTable.Cols.STUDY_TOPIC,medicine.getStudyTopic());
        return values;
    }

    public void updateCrime(Medicine medicine) {
        String genericName = medicine.getGenericName();
        ContentValues values = getContentValues(medicine);
        mDatabase.update(MedicineSchema.MedicineTable.NAME, values,
                MedicineSchema.MedicineTable.Cols.GENERIC_NAME + " = ?",
                new String[] { genericName });
        Log.i("DatabaseUpdate","Database has been updated");
    }
}
