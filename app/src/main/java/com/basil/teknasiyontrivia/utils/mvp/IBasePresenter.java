package com.basil.teknasiyontrivia.utils.mvp;

public interface IBasePresenter<V extends IBaseView> {

    void onAttach(V v);

    void onDetach();
}
