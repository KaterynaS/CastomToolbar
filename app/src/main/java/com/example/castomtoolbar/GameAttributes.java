package com.example.castomtoolbar;

public class GameAttributes {

    int[] disksImgResourcesList;
    int[] discsForMiniScreens;
    int[] discsForScreens683to1000;
    int[] discsForScreens1000to1366;
    int[] discsForScreens1367to2050;
    int[] discsForScreens2050andMore;


    public GameAttributes() {
        disksImgResourcesList = new int[]{
                R.drawable.kawai_1, R.drawable.kawai_2,
                R.drawable.kawai_3, R.drawable.kawai_4,
                R.drawable.kawai_5, R.drawable.kawai_6,
                R.drawable.kawai_7};

        //screen W below 683
        discsForMiniScreens = new int[]{
                R.drawable.rec_one_mini, R.drawable.rec_two_mini,
                R.drawable.rec_three_mini, R.drawable.rec_four_mini,
                R.drawable.rec_five_mini, R.drawable.rec_six_mini,
                R.drawable.rec_seven_mini
        };

        //screen W from 683 to 1000
        discsForScreens683to1000 = new int[]{
                R.drawable.rec_one_683_to_thousand, R.drawable.rec_two_683_to_thousand,
                R.drawable.rec_three_683_to_thousand, R.drawable.rec_four_683_to_thousandxml,
                R.drawable.rec_five_683_to_thousand, R.drawable.rec_six_683_to_thousand,
                R.drawable.rec_seven_683_to_thousand
        };

        discsForScreens2050andMore = new int[]{
                R.drawable.rec_one_2050_and_more, R.drawable.rec_two_2050_and_more,
                R.drawable.rec_three_2050_and_more, R.drawable.rec_four_2050_and_more,
                R.drawable.rec_five_2050_and_more, R.drawable.rec_six_2050_and_more,
                R.drawable.rec_seven_2050_and_more};

        discsForScreens1367to2050 = new int[]{
                R.drawable.rec_one_1367_to_2050, R.drawable.rec_two_1367_to_2050,
                R.drawable.rec_three_1367_to_2050, R.drawable.rec_four_1367_to_2050,
                R.drawable.rec_five_1367_to_2050, R.drawable.rec_six_1367_to_2050,
                R.drawable.rec_seven_1367_to_2050};

        discsForScreens1000to1366 = new int[]{
                R.drawable.rec_one_1000_to_1366, R.drawable.rec_two_1000_to_1366,
                R.drawable.rec_three_1000_to_1366, R.drawable.rec_four_1000_to_1366,
                R.drawable.rec_five_1000_to_1366, R.drawable.rec_six_1000_to_1366,
                R.drawable.rec_seven_1000_to_1366};

    }


}
