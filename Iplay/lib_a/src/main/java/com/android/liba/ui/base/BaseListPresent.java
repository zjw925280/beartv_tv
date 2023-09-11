package com.android.liba.ui.base;


import android.os.Bundle;

import com.android.liba.exception.EmptyError;
import com.android.liba.exception.Error;
import com.android.liba.network.protocol.BaseListInfo;
import com.android.liba.nucleus.factory.Factory;
import com.android.liba.ui.base.listgroup.holder.listProvider;
import com.android.liba.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;


public abstract class BaseListPresent<D, V extends listProvider> extends BasePresent<V> {
    private static final int REQUEST_REFRESH = 0x111111;
    private int mlastId = 0;
    private List<D> mDataList = new ArrayList<>();
    private ArrayList<HandlerNetWorkData> process = new ArrayList<>();

    public interface HandlerNetWorkData {
        boolean canHanlder(Object data);

        Object process(Object data);
    }

    public BaseListPresent() {
        process.add(new HandlerNetWorkData() {

            @Override
            public boolean canHanlder(Object data) {
                return true;
            }

            @Override
            public Object process(Object data) {
                if (data instanceof BaseListInfo) {
                    mlastId = ((BaseListInfo) data).getLastid();
                    return ((BaseListInfo) data).getItems();
                }
                return data;
            }
        });
    }

    public void addDataHandler(HandlerNetWorkData handler) {
        if (handler != null) {
            process.add(0, handler);
        }
    }

    private void proccessData(Object object, V view) {
        for (HandlerNetWorkData handler : process) {
            if (handler.canHanlder(object)) {
                Object res = handler.process(object);
                if (res instanceof List) {
                    inserted(mDataList.size(), (List) res, view);
                    UIHelper.showLog("proccessData", "mDataList 1 " + mDataList.size());
                } else if (res != null) {
                    insertItem((D) res, view);
                    UIHelper.showLog("proccessData", "mDataList " + mDataList.size());
                }
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        onStart(savedState);
        restartable(REQUEST_REFRESH, getDisposable(process.size()));
    }

    public <T> Factory<Disposable> getDisposable(int size) {
        return super.getDisposableFactory(size, () ->
                        getListData(mlastId)
                                .flatMap(response -> {
                                    if (response == null) {
                                        return Observable.error(new UnknownError(null));
                                    } else if (response instanceof BaseListInfo && ((BaseListInfo) response).getItems().size() == 0) {
                                        return Observable.error(new EmptyError());
                                    }
                                    return Observable.just(response);
                                })
                , (view, response) -> {
                    UIHelper.showLog("typeinfo", "typeinfo---" + response);
                    proccessData(response, view);
                    if (isFirstPage()) {
                        view.onReady();
                    }
                    view.getRecycleViewManager().showListItems(isFirstPage(), mDataList);
                }, (view, errorThrowable) -> {
                    loadDataCallBack(view, null);
                    if (isFirstPage() && containsHeadData()) {
                        view.onReady();
                    }
                    if (isFirstPage() && !containsHeadData()) {
                        onError(view, errorThrowable);
                    } else {
                        view.getRecycleViewManager().showLoadMoreState(errorThrowable, mlastId);
                    }
                });
    }

    public boolean isFirstPage() {
        return mlastId == 0;
    }

    public void requestData(boolean isRefresh) {
        if (isRefresh) {
            this.mlastId = 0;
            notifyItemAllRemoved(false);
        }
        start(REQUEST_REFRESH);
    }

    public void onError(V view, Error response) {
        if (response instanceof EmptyError) {
            view.getRecycleViewManager().showLoadingEmpty();
        } else {
            view.getRecycleViewManager().showLoadingRetry(response);
        }
    }

    public List<D> getListData() {
        return mDataList;
    }

    public boolean isListDataEmpty() {
        return mDataList.isEmpty();
    }

    public void notifyItemAllRemoved(boolean showEmptyView) {
        notifyItemRangeRemoved(showEmptyView, 0, mDataList.size());
    }

    public void notifyItemRangeRemoved(boolean showEmptyView, int posIncludeHeaderSize, int count) {
        runMethod((v, o) -> {
            int innerPosition = posIncludeHeaderSize;
            if (mDataList.size() == 0 || count == 0 || innerPosition > mDataList.size() - count) {
                return;
            }
            int lastListCount = mDataList.size();
            if (count == mDataList.size()) {
                mDataList.clear();
            } else {
                for (int i = 0; i < count; i++) {
                    mDataList.remove(innerPosition);
                }
            }
            v.getRecycleViewManager().notifyItemRangeRemoved(showEmptyView && mDataList.size() == 0, posIncludeHeaderSize, count, lastListCount);
        });
    }

    public void notifyItemRangeChanged(int posIncludeHeaderSize, int count) {
        runMethod((v, o) -> {
            int pos = posIncludeHeaderSize;
            if (pos < 0 || count == 0 || pos + count > mDataList.size()) {
                return;
            }
            v.getRecycleViewManager().notifyItemRangeChanged(posIncludeHeaderSize, count);
        });
    }

    public void notifyItemRangeInserted(int posOut, List<D> dataList) {
        runMethod((v, o) -> inserted(posOut, dataList, v));
    }

    public void notifyItemInserted(int innerPosition, D data) {
        runMethod((v, o) -> {
            int pos = innerPosition;

            if (innerPosition < 0 || innerPosition > mDataList.size())
                return;

            int lastSize = mDataList.size();
            mDataList.add(innerPosition, data);
            v.getRecycleViewManager().notifyItemRangeInserted(pos, 1, lastSize);
        });
    }

    private void inserted(int innerPosition, List<D> dataList, V view) {
        if (dataList == null || dataList.isEmpty())
            return;

        int pos = innerPosition;
        if (innerPosition < 0 || innerPosition > mDataList.size())
            return;

        int lastSize = mDataList.size();
        mDataList.addAll(innerPosition, dataList);
        view.getRecycleViewManager().notifyItemRangeInserted(pos, dataList.size(), lastSize);
    }

    public void insertItem(D data, V view) {
        if (data == null)
            return;
        int pos = mDataList.size();
        mDataList.add(data);
        view.getRecycleViewManager().notifyItemRangeChanged(pos, 1);
    }

    public void notifyItemInsertedEnd(D data) {
        notifyItemInserted(mDataList.size(), data);
    }

    public void notifyItemMoved(int fromPosition, int toPosition) {
        runMethod((v, o) -> {
            int fromPos = fromPosition;
            int toPos = toPosition;
            if (mDataList.size() == 0 || fromPos > mDataList.size() - 1 || toPos > mDataList.size() - 1) {
                return;
            }
            D fromData = mDataList.get(fromPos);
            D toData = mDataList.get(toPos);
            mDataList.remove(fromPos);
            mDataList.add(fromPos, toData);
            mDataList.remove(toPos);
            mDataList.add(toPos, fromData);
            v.getRecycleViewManager().notifyItemMoved(fromPosition, toPosition);
        });
    }

    private void runMethod(final BiConsumer<V, Object> onNext) {
        add(Observable.just(new Object()).compose(deliverFirst()).subscribe(vObjectDelivery -> vObjectDelivery.split(onNext, (v, throwable) -> {
                })
                , Throwable::printStackTrace));
    }

    protected void loadDataCallBack(V view, BaseListInfo<D> data) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDataList != null) {
            mDataList.clear();
            mDataList = null;
        }
    }

    public boolean containsHeadData() {
        return false;
    }

    public abstract Observable<D> getListData(int mlastId);

    public void onStart(Bundle savedState) {

    }
}
