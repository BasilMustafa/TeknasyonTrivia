package com.basil.teknasiyontrivia.ui.results;

import com.basil.teknasiyontrivia.model.Standing;
import com.basil.teknasiyontrivia.utils.mvp.IBaseView;

import java.util.List;

/**
 * Created by Basil on 7/31/2018.
 */

public interface IResultsView extends IBaseView {
    void publishStandings(List<Standing> standings);

}
