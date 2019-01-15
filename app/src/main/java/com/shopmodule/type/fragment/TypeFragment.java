package com.shopmodule.type.fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.shopmodule.R;
import com.shopmodule.base.BaseFragment;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

public class TypeFragment extends BaseFragment {

    private SegmentTabLayout segmentTabLayout;
    private ImageView iv_type_search;
    private FrameLayout fl_type;
    private List<BaseFragment> fragmentList;
    private Fragment tempFragment;

/*    public ListFragment listFragment;
    public TagFragment tagFragment;*/

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_type, null);
        segmentTabLayout = (SegmentTabLayout) view.findViewById(R.id.tl_1);
        iv_type_search = (ImageView) view.findViewById(R.id.iv_type_search);
        fl_type = (FrameLayout) view.findViewById(R.id.fl_type);
        return view;
    }

    /**
     * 初始化数据
     */
    public void initData() {
        super.initData();
        initFragment();
        String[] titles = {"分类", "标签"};
        segmentTabLayout.setTabData(titles);
        segmentTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
               switchFragment(tempFragment, fragmentList.get(position));
             //   Toast.makeText(getActivity(),titles[position],Toast.LENGTH_LONG).show();

            }
            @Override
            public void onTabReselect(int position) {
              //  Toast.makeText(getActivity(),"onTabReselect"+titles[position],Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        switchFragment(tempFragment, fragmentList.get(0));
    }

    public void switchFragment(Fragment fromFragment, BaseFragment nextFragment) {
        if (tempFragment != nextFragment) {
            tempFragment = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                //判断nextFragment是否添加
                if (!nextFragment.isAdded()) {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }

                    transaction.add(R.id.fl_type, nextFragment, "tagFragment").commit();
                } else {
                    //隐藏当前Fragment
                    if (fromFragment != null) {
                        transaction.hide(fromFragment);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ListFragment());
        fragmentList.add(new TagFragment());
        switchFragment(tempFragment, fragmentList.get(0));
    }


}
