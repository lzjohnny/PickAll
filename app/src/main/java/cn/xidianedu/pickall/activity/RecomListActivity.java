package cn.xidianedu.pickall.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import Jama.Matrix;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.xidianedu.pickall.R;
import cn.xidianedu.pickall.adapter.DividerItemDecoration;
import cn.xidianedu.pickall.adapter.PickListActivityAdapter;
import cn.xidianedu.pickall.bean.IndexEntry;
import cn.xidianedu.pickall.bean.PickParkBean;

/**
 * Created by ShiningForever on 2017/5/10.
 */

public class RecomListActivity extends AppCompatActivity {
    // 只支持这7种水果
    private final static String[] fruits = {"葡萄", "草莓", "西瓜", "樱桃", "苹果", "香蕉", "鸭梨"};
    List<String> fruitsList = Arrays.asList(fruits);

    List<IndexEntry> indexList = new ArrayList<>();
    PickListActivityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_list);
//        Intent intent = getIntent();
//        String title = intent.getStringExtra("title");

        Toolbar toolbar = (Toolbar) findViewById(R.id.pick_list_toolbar);
        toolbar.setTitle("个性化推荐");
        toolbar.setNavigationIcon(R.drawable.ic_arraw_back_white);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pick_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(divider);
        adapter = new PickListActivityAdapter(this);
        recyclerView.setAdapter(adapter);

        getOidFromSP();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    public void getOidFromSP() {

        SharedPreferences pref = getSharedPreferences("RATING_RECORD", MODE_PRIVATE);
        Map<String, Float> map = (Map<String, Float>) pref.getAll();

        // 用户评价采摘园数目
        int size = map.size();

        if (size == 0) {
            Toast.makeText(this, "未对采摘园进行过评价，无法推荐", Toast.LENGTH_LONG).show();
            return;
        }
        final double[] scoreArray = new double[size];
        final double[][] itemArray = new double[size][fruits.length];
        final String[] parkArray = new String[size];

        int i = 0;
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            parkArray[i] = entry.getKey();
            scoreArray[i] = entry.getValue();
            i++;

            Log.d("map--", entry.getKey());
            Log.d("map--", entry.getValue() + "");
        }

        //包装类，便于查oid对应的下标
        final List<String> parkList = Arrays.asList(parkArray);

        BmobQuery<PickParkBean> query = new BmobQuery<PickParkBean>();
        query.addWhereContainedIn("objectId", Arrays.asList(parkArray));
        query.findObjects(new FindListener<PickParkBean>() {
            @Override
            public void done(List<PickParkBean> list, BmobException e) {
                if (e == null) {
                    for (PickParkBean bean : list) {
                        int row = parkList.indexOf(bean.getObjectId());
                        int col = fruitsList.indexOf(bean.getCategory());

                        Log.d("---row-col---", row + ":" + col);
                        itemArray[row][col] = 1;
                    }
//                    Log.d("矩阵", Arrays.toString(itemArray[0]));
//                    Log.d("矩阵", Arrays.toString(itemArray[1]));

                    runCBRecommender(scoreArray, itemArray);
                } else {
                    Toast.makeText(RecomListActivity.this, "查询失败1", Toast.LENGTH_SHORT).show();
                    Log.d("查询失败", e.getMessage());
                }
            }
        });


        Log.d("采摘园", Arrays.toString(parkArray));
        Log.d("用户评分", Arrays.toString(scoreArray));

    }

    private void runCBRecommender(double[] scoreArray, double[][] itemArray) {
        double[][] itemArrayT = new Matrix(itemArray).transpose().getArray();
        double avg = calcAvgFromArrayValue(scoreArray);
        double[] userProfiles = calcUserProfiles(itemArrayT, scoreArray, avg);

        getAllPark(userProfiles);
    }

    private double calcAvgFromArrayValue(double[] scoreArray) {
        double sum = 0.0;
        for (double d : scoreArray) {
            sum += d;
        }
        return (sum / scoreArray.length);
    }

    private double[] calcUserProfiles(double[][] itemArrayT, double[] scoreArray, double avg) {
        int size = fruits.length;
        double[] userArray = new double[size];

        // i为水果种类
        for (int i = 0; i < size; i++) {
            int cnt = 0;
            double sum = 0.0;
            for (int j = 0; j < itemArrayT[i].length; j++) {
                if (itemArrayT[i][j] != 0) {
                    cnt++;
                    // j对应的采摘园，使用评分计算公式
                    sum += (scoreArray[j] - avg);
                }
            }
            if (cnt != 0) {
                double score = sum / cnt;
                userArray[i] = score;
            }
        }
        return userArray;
    }

    public void getAllPark(final double[] userProfiles) {
        BmobQuery<PickParkBean> query2 = new BmobQuery<>();
        query2.addWhereEqualTo("usage", "main_list");
        query2.findObjects(new FindListener<PickParkBean>() {
            @Override
            public void done(List<PickParkBean> list, BmobException e) {
                if (e == null) {
//                    Toast.makeText(RecomListActivity.this, list.size() + "", Toast.LENGTH_SHORT).show();
                    int waitForRecomRow = list.size();
                    double[][] waitForRecomItemArray = new double[waitForRecomRow][fruits.length];
                    String[] waitForRecomOid = new String[waitForRecomRow];

                    Log.d("row", waitForRecomRow + "");

                    for (int i = 0; i < waitForRecomRow; i++) {
                        waitForRecomOid[i] = list.get(i).getObjectId();

                        int col = fruitsList.indexOf(list.get(i).getCategory());

                        Log.d("---col", list.get(i).getCategory());
                        Log.d("---col", col + "");
                        waitForRecomItemArray[i][col] = 1;

                    }

                    double[] cosIndex = calcCosIndex(waitForRecomItemArray, userProfiles);

                    /*********************推荐指数和对应的objectId************************/
                    Log.d("cosindex", Arrays.toString(cosIndex));
                    Log.d("waitForRecom---", Arrays.toString(waitForRecomOid));
                    if (cosIndex.length == waitForRecomOid.length) {
                        for (int i = 0; i < cosIndex.length; i++) {
//                            indexMap.put(waitForRecomOid[i], cosIndex[i]);
                            indexList.add(new IndexEntry(waitForRecomOid[i], cosIndex[i]));
                        }

                        Collections.sort(indexList);
                        String[] sortedOid = new String[waitForRecomOid.length];
                        for (int k = 0; k < indexList.size(); k++) {
//                            Log.d("---last_result--", entry.oid);
//                            Log.d("---last_result--", entry.index + "");
                            sortedOid[k] = indexList.get(k).oid;
                        }
                        queryForRecomPickList(sortedOid);

                    } else {
                        Toast.makeText(RecomListActivity.this, "个性化推荐失败，展示默认采摘园", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(RecomListActivity.this, "查询失败2", Toast.LENGTH_SHORT).show();
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void queryForRecomPickList(String[] sortedOid) {
        Log.d("sortedOid", Arrays.toString(sortedOid));
        BmobQuery<PickParkBean> query = new BmobQuery<>();
        query.addWhereContainedIn("objectId", Arrays.asList(sortedOid));
        query.findObjects(new FindListener<PickParkBean>() {
            @Override
            public void done(List<PickParkBean> list, BmobException e) {
                if (e == null) {
                    adapter.addSrcAndNotify(list);
                } else {
                    Toast.makeText(RecomListActivity.this, "查询失败3", Toast.LENGTH_SHORT).show();
                    Log.d("查询失败3", e.getMessage());
                }
            }
        });
    }

    private double[] calcCosIndex(double[][] waitForRecomItemArray, double[] userProfiles) {
        double[] cosIndex = new double[waitForRecomItemArray.length];

        for (int i = 0; i < waitForRecomItemArray.length; i++) {
            cosIndex[i] = realCalcCosindex(userProfiles, waitForRecomItemArray[i]);
        }

        return cosIndex;
    }

    private double realCalcCosindex(double[] arr1, double[] arr2) {
        if (arr1.length == arr2.length) {
            double up = 0.0;
            double down1 = 0.0;
            double down2 = 0.0;
            for (int i = 0; i < arr1.length; i++) {
                up += (arr1[i] * arr2[i]);
                down1 += (arr1[i] * arr1[i]);
                down2 += (arr2[i] * arr2[i]);
            }
            double result = up / (Math.sqrt(down1) * Math.sqrt(down2));
            return result;
        }

        return -316.316;
    }
}
