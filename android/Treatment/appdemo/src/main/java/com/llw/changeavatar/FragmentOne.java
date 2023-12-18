package com.llw.changeavatar;

import android.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentOne extends Fragment {
    private int[] pic ={R.drawable.fa,R.drawable.fb,R.drawable.fc,R.drawable.fd,R.drawable.fa,};
    private String value[][] = {
            {"禅城第一靓仔","去老广喝早茶","上午 10:10"},
            {"顺德第一靓仔","去老广喝早茶","上午 10:15"},
            {"三水第一靓仔","去老广喝早茶","上午 10:20"},
            {"高明第一靓仔","去老广喝早茶","上午 10:25"},
            {"南海第一靓仔","去老广喝早茶","上午 10:30"},



    };
    //定义一个map集合存放数据
    private List<Map<String,String>> list = new ArrayList<>();
    private ListView datalist;
    private SimpleAdapter simpleAdapter;//适配器

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);
        datalist =view.findViewById(R.id.datalist);
        for(int i=0;i<pic.length;i++){
            Map<String,String> map = new HashMap<>();
            map.put("pic",String.valueOf(pic[i]));
            map.put("qmc",value[i][0]);
            map.put("content",value[i][1]);
            map.put("time",value[i][2]);
            list.add(map);//将map放到list集合中
        }
        simpleAdapter = new SimpleAdapter(getActivity(),
                this.list,
                R.layout.listveiw,
                new String[]{"pic","qmc","content","time"},
                new int[]{R.id.img1,R.id.qmc,R.id.content,R.id.time}
        );
        datalist.setAdapter(simpleAdapter);

        // Inflate the layout for this fragment
        return view;
    }
}