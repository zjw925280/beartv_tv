package com.android.liba.ui.base.tab;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.liba.ui.base.BaseFragment;

import java.util.List;

public class FragmentList {
    private final String Tag=FragmentList.class.getSimpleName();
    private FragmentManager manager;
    private Fragment mCurrentFragment;
    private BaseFragment[] mBaseFragments;
    private OnSelectListener mOnSelectListener;
    private int selectItem=0;
    public FragmentList(FragmentManager manager) {
        this.manager = manager;
    }

    public void loadFragments(int containerId, Bundle savedInstanceState, BaseFragment[] baseFragments) {
        loadFragments(containerId, savedInstanceState, baseFragments.length, pos -> baseFragments[pos]);
    }

    public void loadFragments(int containerId, Bundle savedInstanceState, int size, OnGetFragmentListener onGetFragmentListener) {
        mBaseFragments = new BaseFragment[size];
        if (savedInstanceState == null || manager.getFragments() == null || manager.getFragments().isEmpty()) {
            try {
                FragmentTransaction transaction = manager.beginTransaction();
                for (int i = 0; i < size; i++) {
                    mBaseFragments[i] = onGetFragmentListener.getFragment(i);
                    transaction.add(containerId, mBaseFragments[i], Tag + i).hide(mBaseFragments[i]);
                }
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mOnSelectListener != null) {
                mOnSelectListener.onSelect(0);
            }
        } else {
            for (int i = 0; i < size; i++) {
                mBaseFragments[i] = findFragment(Tag + i);
                if (mBaseFragments[i] != null && !mBaseFragments[i].isHidden()) {
                    if (mOnSelectListener != null) {
                        mOnSelectListener.onSelect(i);
                    }
                }
            }
        }
    }

    public BaseFragment getFragment(int pos) {
        return mBaseFragments[pos];
    }

    private <T extends BaseFragment> T findFragment(String tag) {
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList == null) return null;
        for (Fragment fragment : fragmentList) {
            if (fragment.getTag().equals(tag)) {
                return (T) fragment;
            }
        }
        return null;
    }

    public void selectItem(int pos) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (mCurrentFragment == null) {
            mCurrentFragment = mBaseFragments[pos];
            transaction.show(mBaseFragments[pos]);
        } else if (mBaseFragments[pos] != mCurrentFragment) {
            transaction.hide(mCurrentFragment).show(mBaseFragments[pos]);
        }
        mCurrentFragment = mBaseFragments[pos];
        transaction.commitAllowingStateLoss();
    }

    public void previewSelectItem(int pos) {
        FragmentTransaction transaction = manager.beginTransaction();
        if (mCurrentFragment == null) {
            mCurrentFragment = mBaseFragments[pos];
            transaction.show(mBaseFragments[pos]);
        } else if (mBaseFragments[pos] != mCurrentFragment) {
            transaction.hide(mCurrentFragment).show(mBaseFragments[pos]);
        } else if (mBaseFragments[pos] == mCurrentFragment) {
            if (mCurrentFragment.isHidden()) {
                transaction.show(mCurrentFragment);
            } else {
                transaction.hide(mCurrentFragment);
            }
        }
        mCurrentFragment = mBaseFragments[pos];
        transaction.commitAllowingStateLoss();
    }

    public void release() {
        manager = null;
        mCurrentFragment = null;
        mBaseFragments = null;
        mOnSelectListener = null;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        void onSelect(int pos);
    }

    public interface OnGetFragmentListener {
        BaseFragment getFragment(int pos);
    }
}
