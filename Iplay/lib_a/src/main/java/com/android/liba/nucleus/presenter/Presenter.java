package com.android.liba.nucleus.presenter;


import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This is a base class for all presenters. Subclasses can override
 * {@link #onCreate}, {@link #onDestroy}, {@link #onSave},
 * {@link #onTakeView}, {@link #onDropView}.
 * <p/>
 * {@link Presenter.OnDestroyListener} can also be used by external classes
 * to be notified about the need of freeing resources.
 *
 * @param <View> a type of view to return with {@link #getView()}.
 */
public class Presenter<View> extends ViewModel {

    @Nullable
    private View view;
    private CopyOnWriteArrayList<Presenter.OnDestroyListener> onDestroyListeners = new CopyOnWriteArrayList<>();

    protected void onCreate(@Nullable Bundle savedState) {
    }

    protected void onDestroy() {
    }
    protected void onSave(Bundle state) {
    }

    protected void onTakeView(View view) {
    }


    protected void onDropView() {
    }


    public interface OnDestroyListener {

        void onDestroy();
    }

    public void addOnDestroyListener(Presenter.OnDestroyListener listener) {
        onDestroyListeners.add(listener);
    }


    public void removeOnDestroyListener(Presenter.OnDestroyListener listener) {
        onDestroyListeners.remove(listener);
    }

    @Nullable
    public View getView() {
        return view;
    }

    /**
     * Initializes the presenter.
     */
    public void create(Bundle bundle) {
        onCreate(bundle);
    }

    /**
     * Destroys the presenter, calling all {@link Presenter.OnDestroyListener} callbacks.
     */
    public void destroy() {
        for (Presenter.OnDestroyListener listener : onDestroyListeners)
            listener.onDestroy();
        onDestroy();
    }

    /**
     * Saves the presenter.
     */
    public void save(Bundle state) {
        onSave(state);
    }

    /**
     * Attaches a view to the presenter.
     *
     * @param view a view to attach.
     */
    public void takeView(View view) {
        this.view = view;
        onTakeView(view);
    }

    /**
     * Detaches the presenter from a view.
     */
    public void dropView() {
        onDropView();
        this.view = null;
    }
}

