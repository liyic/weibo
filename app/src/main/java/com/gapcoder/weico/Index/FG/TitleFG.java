package com.gapcoder.weico.Index.FG;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gapcoder.weico.General.SysMsg;
import com.gapcoder.weico.General.URLService;
import com.gapcoder.weico.Index.Adapter.TitleAdapter;
import com.gapcoder.weico.Index.Model.TitleModel;
import com.gapcoder.weico.R;
import com.gapcoder.weico.Utils.Pool;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;

public class TitleFG extends BaseFG {

    List<TitleModel.inner> data = new LinkedList<>();
    List<TitleModel.inner> tmp = new LinkedList<>();
    TitleAdapter adapter;
    @BindView(R.id.timeline)
    RecyclerView tl;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout rf;


    public TitleFG() {

    }

    View init(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_fg, container, false);
    }

    @Override
    public void CreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState, View v) {
        adapter = new TitleAdapter(data, getActivity());
        tl.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        tl.setAdapter(adapter);
        rf.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Refresh();
            }
        });
        rf.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                rf.finishLoadmore();
            }
        });
        rf.autoRefresh();
    }

    public void Refresh() {

        Pool.run(new Runnable() {
            @Override
            public void run() {

                final SysMsg m = URLService.get("title.php", TitleModel.class);
                if (!check(m, rf)) {
                    return;
                }
                tmp = ((TitleModel) m).getInner();
                if (tmp.size() > 0)
                    data.clear();

                for (int i = 0; i < tmp.size(); i++) {
                    data.add(tmp.get(i));
                }

                UI(new Runnable() {
                    @Override
                    public void run() {
                        SmartRefresh(rf);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
