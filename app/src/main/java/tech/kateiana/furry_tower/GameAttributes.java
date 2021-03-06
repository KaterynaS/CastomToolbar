package tech.kateiana.furry_tower;

public class GameAttributes {

    int[] disksImgResourcesList;
    int[] targetImgResourceList;
    int[] bestPossibleResults;

    public GameAttributes() {
        disksImgResourcesList = new int[]{
                R.drawable.kawai_1, R.drawable.kawai_2,
                R.drawable.kawai_3, R.drawable.kawai_4,
                R.drawable.kawai_5, R.drawable.kawai_6,
                R.drawable.kawai_7};

        targetImgResourceList = new int[]{
                R.drawable.treat_cloud_donat,
                R.drawable.treat_cloud_icecream,
                R.drawable.treat_cloud_strawberry,
                R.drawable.treat_cloud_watermelon,
                R.drawable.treat_cloud_fish
        };

        bestPossibleResults = new int[]{
                0,
                1,
                3,
                7,
                15,
                31,
                63,
                127,
                255,
                511
        };

    }

    public int getMaxLevel()
    {
        return disksImgResourcesList.length;
    }
}
